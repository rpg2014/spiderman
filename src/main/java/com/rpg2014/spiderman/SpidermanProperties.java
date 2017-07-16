package com.rpg2014.spiderman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class SpidermanProperties {
	private static String botID;
	private static int port;
	
	public static void getProperties() throws FileNotFoundException, IOException{
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
			InputStream inputStream = SpidermanProperties.class.getClassLoader().getResourceAsStream(propFileName);
			
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			botID = prop.getProperty("BOT_ID");
			port = Integer.valueOf(prop.getProperty("PORT"));
			
		}catch(IOException e) {
			throw new IOException();
		}
		
	}
	
	
	
	public static String getBotID() {
		try {
			Scanner scan = new Scanner(new File(".botId"));
			return scan.nextLine();
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
