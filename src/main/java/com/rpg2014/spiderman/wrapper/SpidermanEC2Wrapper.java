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

public class SpidermanEC2Wrapper {
    private static SpidermanEC2Wrapper ourInstance = new SpidermanEC2Wrapper();

    public static SpidermanEC2Wrapper getInstance() {
        return ourInstance;
    }
    private SpidermanLogger logger = SpidermanLogger.getInstance();
    private static AmazonEC2 ec2Client;
    private SpidermanEC2Wrapper() {
        if(Boolean.valueOf(System.getenv("ON_HEROKU")))
            this.ec2Client = AmazonEC2ClientBuilder.standard().withCredentials(getCredentials()).withRegion(Regions.US_EAST_1).build();
        else
            this.ec2Client =AmazonEC2ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
    }

    public boolean startInstance(final String instanceId){
        StartInstancesRequest request = new StartInstancesRequest()
                .withInstanceIds(instanceId);

        StartInstancesResult result = ec2Client.startInstances(request);
        InstanceState state = result.getStartingInstances().get(0).getCurrentState();
        if (state.getCode() < 32){
            DiscordWebhook.sendToDiscord("Starting Server");
            return true;
        }else {
            DiscordWebhook.sendToDiscord("Unable to Start Server");
            return false;
        }
    }

    public boolean stopInstance(final String instanceId){
        StopInstancesRequest request = new StopInstancesRequest().withInstanceIds(instanceId);
        StopInstancesResult result = ec2Client.stopInstances(request);
        InstanceState state = result.getStoppingInstances().get(0).getCurrentState();
        if(state.getCode() >= 32) {
            DiscordWebhook.sendToDiscord("Stopping Server");
            return true;
        }else {
            DiscordWebhook.sendToDiscord("Unable To Stop Server");
            return false;
        }
    }

    public void rebootInstance(final String instanceId){
        RebootInstancesRequest request = new RebootInstancesRequest().withInstanceIds(instanceId);
        RebootInstancesResult result = ec2Client.rebootInstances(request);
        logger.logInfo(result.toString(),this.getClass().getSimpleName());
        DiscordWebhook.sendToDiscord("Rebooting instance: "+instanceId);
    }


    private AWSCredentialsProvider getCredentials() {
        // TODO Auto-generated method stub
        AWSCredentials cred = new BasicAWSCredentials(System.getenv("AWS_ACCESS_KEY"),System.getenv("AWS_SECRET_KEY"));
        AWSCredentialsProvider provider = new AWSStaticCredentialsProvider(cred);
        return provider;
    }
}