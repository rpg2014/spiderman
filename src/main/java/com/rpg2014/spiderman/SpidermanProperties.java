package com.rpg2014.spiderman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class SpidermanProperties {
	
	private static int port;
	private static Properties prop;
	public static void getProperties() throws FileNotFoundException, IOException{
		try {
			prop = new Properties();
			String propFileName = "config.properties";
			InputStream inputStream = new FileInputStream("config.properties"); //SpidermanProperties.class.getClassLoader().getResourceAsStream(propFileName);
			
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			
			port = Integer.valueOf(prop.getProperty("PORT"));
			
		}catch(IOException e) {
			throw e;
		}
		
	}
	
	
	
	public static String getBotID() {
		try {
			Scanner scan = new Scanner(new File(".botId"));
			String str= scan.nextLine();
			scan.close();
			return str;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	
	}
	public static String getBotID(String envName) {
		return prop.getProperty(envName);
	}
	
	
	public static String getDadBotID() {
		try {
			Scanner scan = new Scanner(new File(".dadbotId"));
			String str = scan.nextLine();
			scan.close();
			return str;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	public static int getPort() {
		return port;
	}
}
