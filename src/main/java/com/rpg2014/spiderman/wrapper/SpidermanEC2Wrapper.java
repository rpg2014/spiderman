package com.rpg2014.spiderman.wrapper;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.*;
import com.rpg2014.spiderman.logger.SpidermanLogger;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpidermanEC2Wrapper {
    private static final String AMI_NAME = "Minecraft_Server";
    private static final String SECURITY_GROUP_ID = "sg-0bcf97234db49f1d4";
    private static final String AWS_ACCOUNT_ID = System.getenv("AWS_ACCOUNT_ID");
    private static final String USER_DATA =
        "KGNyb250YWIgLWwgMj4vZGV2L251bGw7IGVjaG8gIiovNSAqICAgKiAgICogICAqICAgd2dldCAtcSAtTyAtICJodHRwczovL2lyb24tc3BpZGVyLmhlcm9rdWFwcC5jb20iID4vZGV2L251bGwgMj4mMSIpIHwgY3JvbnRhYiAtCnNoIG1pbmVjcmFmdC9ydW5fc2VydmVyLnNo";
    //"(crontab -l 2>/dev/null; echo \"*/5 *   *   *   *   wget -q -O - \"url.com\" >/dev/null 2>&1\") | crontab -\nsh minecraft/run_server.sh";
    private static SpidermanEC2Wrapper ourInstance = new SpidermanEC2Wrapper();
    private SpidermanLogger logger = SpidermanLogger.getInstance();
    private AmazonEC2 ec2Client;
    private MinecraftDynamoWrapper serverDetails;
    private String oldAMIid;
    private String oldSnapshotId;
    private static final String CLASS_NAME = SpidermanEC2Wrapper.class.getSimpleName();

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


    public static SpidermanEC2Wrapper getInstance() {
        return ourInstance;
    }

    public boolean startInstance() {
        if (!serverDetails.isServerRunning() || !isInstanceUp()) {
            String amiId = serverDetails.getAmiID();

            RunInstancesRequest runInstancesRequest = new RunInstancesRequest()
                .withImageId(amiId)
                .withMaxCount(1)
                .withMinCount(1)
                .withUserData(USER_DATA)
                .withInstanceType("c5.large")
                .withSecurityGroupIds(SECURITY_GROUP_ID)
                //.withCreditSpecification(new CreditSpecificationRequest().withCpuCredits("standard"))
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
                logger.logInfo("Started server",CLASS_NAME);
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
        if (serverDetails.isServerRunning() || isInstanceUp()) {
            String instanceId = serverDetails.getInstanceId();

            StopInstancesRequest request = new StopInstancesRequest().withInstanceIds(instanceId);
            StopInstancesResult result = ec2Client.stopInstances(request);

            waitforServerStop(instanceId);
            serverDetails.setServerStopped();
            String amiId = makeAMI(instanceId);

            serverDetails.setAmiId(amiId);
            serverDetails.setSnapshotId(getCurrentSnapshot());


            logger.logInfo("Server Stopped",CLASS_NAME);
            if (!serverDetails.getAmiID().equals(oldAMIid) && !serverDetails.getSnapshotId().equals(oldSnapshotId)) {
                deleteOldAmi(oldAMIid, oldSnapshotId);

            }


            TerminateInstancesRequest terminateInstancesRequest =
                new TerminateInstancesRequest().withInstanceIds(instanceId);
            TerminateInstancesResult terminateInstancesResult = ec2Client.terminateInstances(terminateInstancesRequest);
            boolean success =
                terminateInstancesResult.getTerminatingInstances().get(0).getCurrentState().getCode() > 32;
            if (success)
                logger.logInfo("Terminated Server", CLASS_NAME);
                serverDetails.setServerStopped();
            return success;
        } else {
            return false;
        }

    }

    private void waitforServerStop(String instanceId) {
        logger.logInfo("Waiting for instance "+ instanceId+ "to stop", CLASS_NAME);
        do{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while(!isInstanceStopped(instanceId));
    }

    private String getCurrentSnapshot() {
        DescribeSnapshotsRequest request = new DescribeSnapshotsRequest().withOwnerIds(AWS_ACCOUNT_ID.replaceAll("-",""));
        DescribeSnapshotsResult result = ec2Client.describeSnapshots(request);
        if(result != null && result.getSnapshots().size() == 1) {
            for (Snapshot snapshot : result.getSnapshots()) {
                if (!oldSnapshotId.equals(snapshot.getSnapshotId())) {
                    logger.logInfo("Current Snapshot is " + snapshot.getSnapshotId();, CLASS_NAME);
                    return snapshot.getSnapshotId();
                }
            }
        }else {
            Snapshot newestSnap= new Snapshot().withStartTime(new Date(Long.MIN_VALUE));
            for(Snapshot snapshot: result.getSnapshots()){
                if(newestSnap.getStartTime().before(snapshot.getStartTime())) {
                    newestSnap = snapshot;
                }
            }
            logger.logInfo("Current Snapshot is " + snapshot.getSnapshotId();, CLASS_NAME);
            return newestSnap.getSnapshotId();
        }
        logger.logInfo("Describe Snapshot did not return anything", CLASS_NAME);
        logger.logInfo("Current Snapshot is " + snapshot.getSnapshotId();, CLASS_NAME);
        return oldSnapshotId;
    }

    private void deleteOldAmi(String oldAMIid, final String oldSnapshotId) {
        DeregisterImageRequest deregisterImageRequest = new DeregisterImageRequest().withImageId(oldAMIid);
        ec2Client.deregisterImage(deregisterImageRequest);
        DeleteSnapshotRequest deleteSnapshotRequest = new DeleteSnapshotRequest().withSnapshotId(oldSnapshotId);
        ec2Client.deleteSnapshot(deleteSnapshotRequest);
    }

    private String makeAMI(String instanceId) {
//        String volumeId = getVolumeId(instanceId);
//        CreateSnapshotRequest request =
//            new CreateSnapshotRequest().withVolumeId(volumeId).withDescription("MinecraftServer");
//        CreateSnapshotResult result = ec2Client.createSnapshot(request);
//        String snapshotId = result.getSnapshot().getSnapshotId();
//        serverDetails.setSnapshotId(snapshotId);

        CreateImageRequest createImageRequest = new CreateImageRequest().withInstanceId(instanceId).withName(AMI_NAME+ "-" + Instant
            .now().hashCode());
        CreateImageResult createImageResult = ec2Client.createImage(createImageRequest);
        String amiId = createImageResult.getImageId();

        logger.logInfo("Created AMI, image id: "+ amiId, CLASS_NAME);
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

    public String getInstanceDomainName() {
        String instanceId = serverDetails.getInstanceId();
        DescribeInstancesRequest request = new DescribeInstancesRequest().withInstanceIds(instanceId);
        DescribeInstancesResult result = ec2Client.describeInstances(request);
        return result.getReservations().get(0).getInstances().get(0).getPublicDnsName();
    }

    public String getInstanceIp() {
        String instanceId = serverDetails.getInstanceId();
        DescribeInstancesRequest request = new DescribeInstancesRequest().withInstanceIds(instanceId);
        DescribeInstancesResult result = ec2Client.describeInstances(request);
        return result.getReservations().get(0).getInstances().get(0).getPublicIpAddress();
    }

    public boolean isInstanceUp() {
        String instanceId = serverDetails.getInstanceId();
        DescribeInstancesRequest request = new DescribeInstancesRequest().withInstanceIds(instanceId);
        DescribeInstancesResult result = ec2Client.describeInstances(request);
        if(result.getReservations().size() == 0 || result.getReservations().get(0).getInstances().size() ==0){
            return false;
        }
        boolean isUp = result.getReservations().get(0).getInstances().get(0).getState().getCode() == 16;
        if(isUp){
            logger.logInfo("Server instance "+ instanceId +" is up", CLASS_NAME);
        }else {
            logger.logInfo("Server instance "+ instanceId +" is down", CLASS_NAME);
        }
        return isUp;

    }

    private boolean isInstanceStopped(String instanceId){
        DescribeInstancesRequest request = new DescribeInstancesRequest().withInstanceIds(instanceId);
        DescribeInstancesResult result = ec2Client.describeInstances(request);
        if(result.getReservations().size() == 0 || result.getReservations().get(0).getInstances().size() ==0){
            return true;
        }
        boolean isDown = result.getReservations().get(0).getInstances().get(0).getState().getCode() == 80;
        if(isDown)
            logger.logInfo("Server Instance "+ instanceId + "is down", CLASS_NAME);
        return isDown;
    }

    private AWSCredentialsProvider getCredentials() {
        // TODO Auto-generated method stub
        AWSCredentials cred = new BasicAWSCredentials(System.getenv("AWS_ACCESS_KEY"), System.getenv("AWS_SECRET_KEY"));
        AWSCredentialsProvider provider = new AWSStaticCredentialsProvider(cred);
        return provider;
    }
}
