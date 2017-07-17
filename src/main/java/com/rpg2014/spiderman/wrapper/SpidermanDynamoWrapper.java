package com.rpg2014.spiderman.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

public class SpidermanDynamoWrapper {
	
	private static SpidermanDynamoWrapper ourInstance = new SpidermanDynamoWrapper();
	
	private AmazonDynamoDB client;
	private DynamoDB dynamo;
	private Table table;
	private Item edges;
	private Item people;
	
	public static SpidermanDynamoWrapper getInstance() {
		
		return ourInstance;
	}
	
	
	
	public List<String> getEdges(){
		return edges.getList("edgeList");
	}
	public boolean putEdges(final Set<DefaultEdge> edgeSet) {
		List<DefaultEdge> edgeList = new ArrayList<>(edgeSet);
		List<String> strList = new ArrayList<>();
		for (DefaultEdge e : edgeList) {
			strList.add(e.toString());
		}
		edges.withList("edgeList", strList);
		PutItemOutcome outcome = table.putItem(edges);
		
		boolean success = false;
		if(outcome.getPutItemResult().getSdkHttpMetadata().getHttpStatusCode()-200<100) {
			success =true;
		}
		return success;
		
	}
	
	private SpidermanDynamoWrapper(){
		if(Boolean.valueOf(System.getenv("ON_HEROKU")))
			client = AmazonDynamoDBClientBuilder.standard().withCredentials(getCredentials()).withRegion(Regions.US_EAST_1).build();
		else
			client =AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

		dynamo = new DynamoDB(client);
		table = dynamo.getTable("spiderman-web");
		
		edges = table.getItem("name","edges");
	}

	private AWSCredentialsProvider getCredentials() {
		// TODO Auto-generated method stub
		AWSCredentials cred = new BasicAWSCredentials(System.getenv("AWS_ACCESS_KEY"),System.getenv("AWS_SECRET_KEY"));
		AWSCredentialsProvider provider = new AWSStaticCredentialsProvider(cred);
		return provider;
	}

}
