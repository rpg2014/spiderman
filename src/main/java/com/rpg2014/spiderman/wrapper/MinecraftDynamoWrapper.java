package com.rpg2014.spiderman.wrapper;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

public class MinecraftDynamoWrapper {
    private static MinecraftDynamoWrapper ourInstance = new MinecraftDynamoWrapper();

    public static MinecraftDynamoWrapper getInstance() {
        return ourInstance;
    }

    private static final String ITEM_ID = "itemId";
    private static final String AMI_ID = "amiId";
    private static final String INSTANCE_ID = "instanceId";
    private static final String SERVER_RUNNING = "serverRunning";
    private static final String SNAPSHOT_ID = "snapshotId";
    private static final String VALUE = "value";
    private Table table;

    AmazonDynamoDB client;

    private MinecraftDynamoWrapper() {
        if(Boolean.valueOf(System.getenv("ON_HEROKU"))) {
            client = AmazonDynamoDBClientBuilder.standard().withCredentials(getCredentials()).withRegion(Regions.US_EAST_1).build();
        } else
            client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

        table = new DynamoDB(client).getTable("minecraftServerDetails");

    }

    public boolean isServerRunning() {
        return table.getItem(ITEM_ID, SERVER_RUNNING).getBoolean(VALUE);
    }

    public void setServerRunning() {
        table.putItem(new Item().with(SERVER_RUNNING, true));
    }

    public void setServerStopped() {
        table.putItem(new Item().with(SERVER_RUNNING, false));
    }

    public String getSnapshotId() {
        return table.getItem(ITEM_ID, SNAPSHOT_ID).getString(VALUE);
    }

    public void setSnapshotId(final String snapshotId){
        table.putItem(new Item().with(SNAPSHOT_ID, snapshotId));
    }


    public String getInstanceId() {
        return table.getItem(ITEM_ID, INSTANCE_ID).getString(VALUE);
    }

    public void setInstanceId(final String instanceId) {
        table.putItem(new Item().with(INSTANCE_ID, instanceId));
    }

    public String getAmiID() {
        return table.getItem(ITEM_ID, AMI_ID).getString(VALUE);
    }

    public void setAmiId(final String amiId) {
        table.putItem(new Item().with(AMI_ID, amiId));
    }


    private AWSCredentialsProvider getCredentials() {
        // TODO Auto-generated method stub
        AWSCredentials cred = new BasicAWSCredentials(System.getenv("AWS_ACCESS_KEY"),System.getenv("AWS_SECRET_KEY"));
        AWSCredentialsProvider provider = new AWSStaticCredentialsProvider(cred);
        return provider;
    }
}
