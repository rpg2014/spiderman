package com.rpg2014.spiderman.wrapper;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.*;
import com.rpg2014.spiderman.logger.SpidermanLogger;
import com.rpg2014.spiderman.wrapper.DiscordWrapper;

import java.util.ArrayList;
import java.util.List;

public class SpidermanEC2Wrapper {
    private static SpidermanEC2Wrapper ourInstance = new SpidermanEC2Wrapper();

    public static SpidermanEC2Wrapper getInstance() {
        return ourInstance;
    }

    private SpidermanLogger logger = SpidermanLogger.getInstance();
    private AmazonEC2 ec2Client;
    private MinecraftDynamoWrapper serverDetails;
    private String oldAMIid;
    private String oldSnapshotId;

    private static final String AMI_NAME = "Minecraft_Server";
    private static final String SECURITY_GROUP_ID = " sg-0bcf97234db49f1d4";
    private static final String USER_DATA = "KGNyb250YWIgLWwgMj4vZGV2L251bGw7IGVjaG8gIiovNSAqICAgKiAgICogICAqICAgd2dldCAtcSAtTyAtICJodHRwczovL2lyb24tc3BpZGVyLmhlcm9rdWFwcC5jb20iID4vZGV2L251bGwgMj4mMSIpIHwgY3JvbnRhYiAtCnNoIG1pbmVjcmFmdC9ydW5fc2VydmVyLnNo";
    //"(crontab -l 2>/dev/null; echo \"*/5 *   *   *   *   wget -q -O - \"https://iron-spider.herokuapp.com\" >/dev/null 2>&1\") | crontab -\nsh minecraft/run_server.sh";

    private SpidermanEC2Wrapper() {
        if (Boolean.valueOf(System.getenv("ON_HEROKU")))
            this.ec2Client =
                AmazonEC2ClientBuilder.standard().withCredentials(getCredentials()).withRegion(Regions.US_EAST_1)
                    .build();
        else
            this.ec2Client = AmazonEC2ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

        serverDetails = MinecraftDynamoWrapper.getInstance();
        oldAMIid = serverDetails.getAmiID();
        oldSnapshotId = serverDetails.getSnapshotId();
    }

    public boolean startInstance() {
        if (!serverDetails.isServerRunning()) {
            String amiId = serverDetails.getAmiID();

            RunInstancesRequest runInstancesRequest = new RunInstancesRequest()
                .withImageId(amiId)
                .withMaxCount(1)
                .withMinCount(1)
                .withUserData(USER_DATA)
                .withInstanceType("t3.small")
                .withSecurityGroupIds(SECURITY_GROUP_ID)
                .withKeyName("Minecraft Server");

            RunInstancesResult runInstancesResult = ec2Client.runInstances(runInstancesRequest);
            String instanceId = getInstanceId(runInstancesResult);
            serverDetails.setInstanceId(instanceId);

            StartInstancesRequest request = new StartInstancesRequest()
                .withInstanceIds(instanceId);

            StartInstancesResult result = ec2Client.startInstances(request);
            InstanceState state = result.getStartingInstances().get(0).getCurrentState();
            boolean success = state.getCode() < 32;
            if (success) {
                serverDetails.setServerRunning();
            }
            return success;
        } else
            return false;
    }

    private String getInstanceId(RunInstancesResult runInstancesResult) {
        List<Instance> instanceList = runInstancesResult.getReservation().getInstances();
        List<String> idList = new ArrayList<>();
        for (Instance instance : instanceList) {
            idList.add(instance.getInstanceId());
        }
        if (idList.size() == 1) {
            return idList.get(0);
        } else
            return null;
    }

    public boolean stopInstance() {
        if(serverDetails.isServerRunning()) {
            String instanceId = serverDetails.getInstanceId();

            StopInstancesRequest request = new StopInstancesRequest().withInstanceIds(instanceId);
            StopInstancesResult result = ec2Client.stopInstances(request);
            String amiId = makeAMI(instanceId);
            serverDetails.setAmiId(amiId);

            serverDetails.setServerStopped();
            if (!serverDetails.getAmiID().equals(oldAMIid) && !serverDetails.getSnapshotId().equals(oldSnapshotId))
                deleteOldAmi(oldAMIid, oldSnapshotId);

            TerminateInstancesRequest terminateInstancesRequest =
                new TerminateInstancesRequest().withInstanceIds(instanceId);
            TerminateInstancesResult terminateInstancesResult = ec2Client.terminateInstances(terminateInstancesRequest);
            boolean success =
                terminateInstancesResult.getTerminatingInstances().get(0).getCurrentState().getCode() > 32;
            if (success)
                serverDetails.setServerStopped();
            return success;
        }else
            return false;
    }

    private void deleteOldAmi(String oldAMIid, final String oldSnapshotId) {
        DeleteSnapshotRequest deleteSnapshotRequest = new DeleteSnapshotRequest().withSnapshotId(oldSnapshotId);
        ec2Client.deleteSnapshot(deleteSnapshotRequest);
        DeregisterImageRequest deregisterImageRequest = new DeregisterImageRequest().withImageId(oldAMIid);
        ec2Client.deregisterImage(deregisterImageRequest);
    }

    private String makeAMI(String instanceId) {
        String volumeId = getVolumeId(instanceId);
        CreateSnapshotRequest request =
            new CreateSnapshotRequest().withVolumeId(volumeId).withDescription("MinecraftServer");
        CreateSnapshotResult result = ec2Client.createSnapshot(request);
        String snapshotId = result.getSnapshot().getSnapshotId();
        serverDetails.setSnapshotId(snapshotId);
        ImportImageRequest importImageRequest =
            new ImportImageRequest().withDiskContainers(new ImageDiskContainer().withSnapshotId(snapshotId));
        ImportImageResult importImageResult = ec2Client.importImage(importImageRequest);

        String amiId = null;
        do {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {

            }
            DescribeImportImageTasksRequest importRequest =
                new DescribeImportImageTasksRequest().withImportTaskIds(importImageResult.getImportTaskId());
            DescribeImportImageTasksResult importResult = ec2Client.describeImportImageTasks(importRequest);
            amiId = importResult.getImportImageTasks().get(0).getImageId();

        } while (!amiId.isEmpty());

        return amiId;
    }

    private String getVolumeId(String instanceId) {
        DescribeInstancesRequest request = new DescribeInstancesRequest().withInstanceIds(instanceId);
        DescribeInstancesResult result = ec2Client.describeInstances(request);

        String volumeId = result.getReservations().get(0).getInstances().get(0).getBlockDeviceMappings().get(0).getEbs()
            .getVolumeId();
        return volumeId;
    }

    public void rebootInstance(final String instanceId) {
        RebootInstancesRequest request = new RebootInstancesRequest().withInstanceIds(instanceId);
        RebootInstancesResult result = ec2Client.rebootInstances(request);
        logger.logInfo(result.toString(), this.getClass().getSimpleName());

    }

    public String getInstanceDomainName(final String instanceId) {
        DescribeInstancesRequest request = new DescribeInstancesRequest().withInstanceIds(instanceId);
        DescribeInstancesResult result = ec2Client.describeInstances(request);
        return result.getReservations().get(0).getInstances().get(0).getPublicDnsName();
    }

    public String getInstanceIp(final String instanceId) {
        DescribeInstancesRequest request = new DescribeInstancesRequest().withInstanceIds(instanceId);
        DescribeInstancesResult result = ec2Client.describeInstances(request);
        return result.getReservations().get(0).getInstances().get(0).getPublicIpAddress();
    }

    public boolean isInstanceUp() {
        if(serverDetails.isServerRunning()) {
            String instanceId = serverDetails.getInstanceId();
            DescribeInstancesRequest request = new DescribeInstancesRequest().withInstanceIds(instanceId);
            DescribeInstancesResult result = ec2Client.describeInstances(request);
            return result.getReservations().get(0).getInstances().get(0).getState().getCode() == 16;
        }
    }

    private AWSCredentialsProvider getCredentials() {
        // TODO Auto-generated method stub
        AWSCredentials cred = new BasicAWSCredentials(System.getenv("AWS_ACCESS_KEY"), System.getenv("AWS_SECRET_KEY"));
        AWSCredentialsProvider provider = new AWSStaticCredentialsProvider(cred);
        return provider;
    }
}
