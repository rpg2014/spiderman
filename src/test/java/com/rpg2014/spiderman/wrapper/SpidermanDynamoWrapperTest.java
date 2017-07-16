package com.rpg2014.spiderman.wrapper;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;

import com.rpg2014.spiderman.types.Person;

public class SpidermanDynamoWrapperTest {
	
	
//	@Test
//	public void testGetEdges() {
//		SpidermanDynamoWrapper wrapper = SpidermanDynamoWrapper.getInstance();
//		System.out.println(wrapper.getEdges().toString());
//	}
	
	
	
	@Test
	public void testEdges() {
		UndirectedGraph<String,DefaultEdge> graph;
		graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
		Person parker = new Person("Parker G");
		
		Person katie = new Person("katie J");
		Person hannah = new Person("hannah B");
		graph.addVertex(parker.toString());
		
		graph.addVertex(katie.toString());
		graph.addVertex(hannah.toString());
		graph.addEdge(parker.toString(), katie.toString());
		graph.addEdge(parker.toString(), hannah.toString());
		List<DefaultEdge> edges = new ArrayList<>( graph.edgeSet());
		System.out.println(graph.toString());
		List<String> persons = new ArrayList<>(graph.vertexSet());
		UndirectedGraph<String,DefaultEdge> graph2;
		graph2 = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
		System.out.println(edges.get(0).toString());
		
		for (DefaultEdge e: edges) {
			String[] vertices = e.toString().replaceAll("\\(","").replaceAll("\\)","").toLowerCase().split(":");
			Person p0 = new Person(vertices[0]);
			Person p1 = new Person(vertices[1]);
			graph2.addVertex(p0.toString());
			graph2.addVertex(p1.toString());
			graph2.addEdge(p0.toString(), p1.toString());
			
			
			
		}
		System.out.println("graph2 = "+graph2.toString());
	}
	
	
	
//	@Test
//	public void testPutAndGet() {
//		UndirectedGraph<String,DefaultEdge> graph;
//		graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
//		Person parker = new Person("Parker G");
//		
//		Person katie = new Person("katie J");
//		Person hannah = new Person("hannah B");
//		graph.addVertex(parker.toString());
//		
//		graph.addVertex(katie.toString());
//		graph.addVertex(hannah.toString());
//		graph.addEdge(parker.toString(), katie.toString());
//		graph.addEdge(parker.toString(), hannah.toString());
//		String expected = graph.toString();
//		
//		SpidermanGraphWrapper g = SpidermanGraphWrapper.getInstance();
//		String actual = g.getGraph().toString();
//		System.out.println("expected\t=\t"+expected );
//		System.out.println("actual\t=\t"+actual );
//		assertEquals(expected,actual);
//		
//		
//	
//		
//		
//	}
}
