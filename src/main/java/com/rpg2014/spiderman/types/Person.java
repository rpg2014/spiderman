package com.rpg2014.spiderman.types;

import java.util.ArrayList;

import org.apache.commons.lang3.text.WordUtils;





@SuppressWarnings("deprecation")
public class Person{

	String name;
	ArrayList<Person> connections;

	public Person(String person) {
		// TODO Auto-generated constructor stub
		name = person.toLowerCase().trim();
		connections = new ArrayList<>();
	} 
	
	
	
	@SuppressWarnings("deprecation")
	public String toString() {

		return WordUtils.capitalize(name).trim();
		

	}
	
	public boolean equals(Person other) {
		return this.toString().equals(other.toString());
	}
	public boolean equals(String other) {
		return this.toString().equalsIgnoreCase(other.trim());
	}
}


