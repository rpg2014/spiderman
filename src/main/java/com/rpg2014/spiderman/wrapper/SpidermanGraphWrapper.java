package com.rpg2014.spiderman.wrapper;

import com.rpg2014.spiderman.logger.SpidermanLogger;
import com.rpg2014.spiderman.types.Person;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import org.jgrapht.ext.JGraphXAdapter;
/**
 * @author rpg2014 make singleton make it so it downloads all the people from
 *         dynamo once at the beginning and store
 */
public class SpidermanGraphWrapper {
	private UndirectedGraph<String, DefaultEdge> graph;
	private SpidermanDynamoWrapper dynamoWrapper;
	private static  SpidermanLogger logger = SpidermanLogger.getInstance();

	private static class MyWrapper {
		static SpidermanGraphWrapper INSTANCE = new SpidermanGraphWrapper();
	}

	private SpidermanGraphWrapper() {
		// TODO put aws dynamo client stuff in here
		dynamoWrapper = SpidermanDynamoWrapper.getInstance();
		graph = getGraphFromDynamo();
		
	}

	private UndirectedGraph<String, DefaultEdge> getGraphFromDynamo() {
		// TODO Auto-generated method stub
//		List<Person> vertexList = dynamoWrapper.getPeopleFromDynamo();
//		List<DefaultEdge> edgeList = dynamoWrapper.getEdgesFromDynamo();
		graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
		List<String> edges = dynamoWrapper.getEdges();
		
		return buildGraph(edges);
	}
	
	public UndirectedGraph<String,DefaultEdge> buildGraph(List<String> edgesStrings) {
		UndirectedGraph<String,DefaultEdge> graph2;
		graph2 = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
		for (String s: edgesStrings) {
			String[] vertices = s.replaceAll("\\(","").replaceAll("\\)","").toLowerCase().split(":");
			Person p0 = new Person(vertices[0]);
			Person p1 = new Person(vertices[1]);
			graph2.addVertex(p0.toString());
			graph2.addVertex(p1.toString());
			graph2.addEdge(p0.toString(), p1.toString());
			
		}
		return graph2;
	}

	public static SpidermanGraphWrapper getInstance() {
		return MyWrapper.INSTANCE;
	}
	
	
	//TODO
	//will want to make all the names lower case? or Camel Case?
	//these methods just work with the local graph. sync will be called after each command
	public boolean createPerson(final Person personToCreate) {
		return graph.addVertex(personToCreate.toString());
	}
	
	public boolean removePerson(final Person personToRemove) {
		return graph.removeVertex(personToRemove.toString());
	}
	
	public boolean addConnection(final Person person1, final Person person2) {
		logger.logDebug("Adding connection between "+person1+" and " +person2,this.getClass().getSimpleName());
		boolean success = false; 
		try {
			success = graph.addEdge(person1.toString(), person2.toString())!= null; 
		}catch (IllegalArgumentException e) {
			logger.logInfo(e.getMessage(), this.getClass().getSimpleName());
		}
		return success;
	}
	public boolean removeConnection(final Person person1, final Person person2) {
		return graph.removeEdge(person1.toString(), person2.toString()) != null;
	}
	
	public BufferedImage viewAll() {
		
		
		return generateImage(this.graph);
	}
	
	
	public BufferedImage view (final List<Person> namesToView){
		UndirectedGraph<String,DefaultEdge> tempGraph = new SimpleGraph<String,DefaultEdge>(DefaultEdge.class);
		Set<String> edgeSet = new HashSet<String>();
		for(Person name : namesToView) {
			Set<DefaultEdge> edges = graph.edgesOf(name.toString());
			for (DefaultEdge e: edges) {
				edgeSet.add(e.toString());
			}
		}
		tempGraph = buildGraph(new ArrayList<String>(edgeSet));

		
		return generateImage(tempGraph);
	}
	
	protected BufferedImage generateImage(UndirectedGraph<String,DefaultEdge> graphToView) {
		mxGraph graphMx = new JGraphXAdapter<String,DefaultEdge>(graphToView);
		graphMx.setAlternateEdgeStyle(mxConstants.EDGESTYLE_ELBOW);
//		graphMx.setCellStyles(key, value);
		graphMx.setLabelsVisible(true);
//		graphMx.setStylesheet(value);
		
		
		mxIGraphLayout layout = new mxFastOrganicLayout(graphMx);
		
		layout.execute(graphMx.getDefaultParent());

		 return  mxCellRenderer.createBufferedImage(graphMx, null, 1, Color.WHITE, true, null);
	}
	
	public boolean sync() {
		//upload new node list and edgelist to dynamo;
		
//		dynamoWrapper.putPeople(graph.vertexSet());
		return dynamoWrapper.putEdges(graph.edgeSet());
	}
	public UndirectedGraph<String,DefaultEdge> getGraph(){
		return graph;
	}
}