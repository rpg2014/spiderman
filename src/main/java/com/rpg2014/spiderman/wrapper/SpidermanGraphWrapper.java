package com.rpg2014.spiderman.wrapper;

import com.rpg2014.spiderman.logger.SpidermanLogger;
import com.rpg2014.spiderman.types.Person;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Collection;
import java.util.Collections;


import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import org.jgrapht.GraphPath;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.ext.JGraphXAdapter;
/**
 * @author rpg2014 make singleton make it so it downloads all the people from
 *         dynamo once at the beginning and store
 */
public class SpidermanGraphWrapper {
	private UndirectedGraph<String, DefaultEdge> graph;
	private SpidermanDynamoWrapper dynamoWrapper;
	private static  SpidermanLogger logger = SpidermanLogger.getInstance();
	private static final int NUM_PEOPLE_TO_RETURN_FOR_SIZE = 3;

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
	
	public String path(final Person startPerson, final Person stopPerson) {
		GraphPath<String,DefaultEdge> path = DijkstraShortestPath.findPathBetween(graph, startPerson.toString(), stopPerson.toString());
		path.getVertexList();
		StringBuilder builder = new StringBuilder();
		for(String p : path.getVertexList()) {
			builder.append(p + " -> ");
		}
		String pathString = builder.toString().trim().substring(0, builder.toString().length()-3).trim();
		return "Shortest path = [" + pathString +"]";
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
	
	public static
	<T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
  		List<T> list = new ArrayList<T>(c);
  		Collections.sort(list);
  		return list;
	}
	
	public String listAll() {
		Set<String> allNames = graph.vertexSet();
		List<String> sortedNames = asSortedList(allNames);
		StringBuilder builder = new StringBuilder();
		builder.append("All names in database:\n");
		for (String s: sortedNames) {
			builder.append(s+", ");
		}
		return builder.toString().trim().substring(0, builder.toString().trim().length()-1); 
	}
	
	public String list(final Person personToList) {
		Set<DefaultEdge> edgeSet = graph.edgesOf(personToList.toString());
		StringBuilder builder = new StringBuilder();
		builder.append(personToList.toString()+" is connected to: ");
		for(DefaultEdge e :edgeSet) {
			String[] vertices = e.toString().replaceAll("\\(","").replaceAll("\\)","").split(":");
			for (String s : vertices) {
				
				if(!s.trim().equalsIgnoreCase(personToList.toString())) {
					builder.append(s.trim()+", ");
				}
			}
		}
		return builder.toString().trim().substring(0, builder.toString().trim().length()-1);
	}
	
	public String getTop3Connections() {
		List<String> vertexSet = new ArrayList<>(graph.vertexSet());
		List<String> top3 = new ArrayList<>();
		//find the top 3 people with the largest number of connections.
		for(int i=1;i<=NUM_PEOPLE_TO_RETURN_FOR_SIZE;i++) {
			String largest = "Morgan T";
			for (String s: vertexSet) {
				if(graph.degreeOf(s)>graph.degreeOf(largest)) {
					logger.logDebug(s+" > "+largest, this.getClass().getSimpleName());
					largest = s;
					
				}
			}
			top3.add(largest);
			vertexSet.remove(largest);
			
		}
		StringBuilder response = new StringBuilder();
		int i = 1;
		for(String s: top3) {
			response.append(i+":    "+s+"  |  "+graph.degreeOf(s)+"\n");
			i++;
		}
		return response.toString().trim();
		
	}
	
	public String sizeOf(final List<Person> peopleToGetSizeOf) {
		StringBuilder response = new StringBuilder();
		int i = 0;
		for(Person s: peopleToGetSizeOf) {
			response.append(s.toString()+" is connected to "+graph.degreeOf(s.toString())+" people\n");
		}
		return response.toString().trim();
	}
	
	protected BufferedImage generateImage(UndirectedGraph<String,DefaultEdge> graphToView) {
		mxGraph graphMx = new JGraphXAdapter<String,DefaultEdge>(graphToView);
		graphMx.setAlternateEdgeStyle(mxConstants.EDGESTYLE_ELBOW);
//		graphMx.setCellStyles(key, value);
		graphMx.setLabelsVisible(true);
//		graphMx.setStylesheet(value);
		
		Map<String, Object> edgeStyle = new HashMap<String, Object>();
		//edgeStyle.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ORTHOGONAL);
		edgeStyle.put(mxConstants.STYLE_SHAPE,    mxConstants.SHAPE_CONNECTOR);
		edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_BLOCK);
		edgeStyle.put(mxConstants.STYLE_STARTARROW, mxConstants.ARROW_BLOCK);
		edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "gray");
		edgeStyle.put(mxConstants.STYLE_FONTCOLOR, "black");
		edgeStyle.put(mxConstants.STYLE_OVERFLOW, "hidden"); // change to fill next
		edgeStyle.put(mxConstants.STYLE_FONTCOLOR, "gray");
		
		mxStylesheet stylesheet = new mxStylesheet();
		stylesheet.setDefaultEdgeStyle(edgeStyle);
		graphMx.setStylesheet(stylesheet);
//		graphMx.setCellStyle("edgeStyle=entityRelationEdgeStyle");
		mxIGraphLayout layout = new mxOrganicLayout(graphMx);
		
		
		layout.execute(graphMx.getDefaultParent());

		 return  mxCellRenderer.createBufferedImage(graphMx, null, 1, Color.WHITE, true, null);
	}
	
	public boolean sync() {
		//upload new node list and edgelist to dynamo;
		
//		dynamoWrapper.putPeople(graph.vertexSet());
		return dynamoWrapper.putEdges(graph.edgeSet());
	}
	public String getRandomPerson() {
		List<String> vertexList = new ArrayList<>(graph.vertexSet());
		Random rand = new Random();
		return vertexList.get(rand.nextInt(vertexList.size()));
	}
	public UndirectedGraph<String,DefaultEdge> getGraph(){
		return graph;
	}
}
