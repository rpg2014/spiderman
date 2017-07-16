package com.rpg2014.spiderman;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.rpg2014.spiderman.types.*;

import com.rpg2014.spiderman.wrapper.SpidermanGraphWrapper;
import com.rpg2014.spiderman.GroupMe.GroupMeResponse;
import com.rpg2014.spiderman.logger.SpidermanLogger;

public class SpidermanCommandRunner {
	private static final String className = SpidermanCommandRunner.class.getSimpleName();
	private static SpidermanLogger logger = SpidermanLogger.getInstance();
	private static SpidermanGraphWrapper graphWrapper = SpidermanGraphWrapper.getInstance();

	public static void setDynamoWrapper(SpidermanGraphWrapper newWrapper) {
		graphWrapper = newWrapper;
	}

	public static GroupMeResponse runCommand(final Map.Entry<SpidermanCommand, List<String>> commandEntry) {
		GroupMeResponse response = null;
		switch (commandEntry.getKey()) {
		case VIEW:
			 response = SpidermanCommandRunner.view(commandEntry.getValue());
			break;
		case ADD:
			List<String> connectionsToAdd = commandEntry.getValue();
			String personToAddTo = commandEntry.getValue().get(0);
			connectionsToAdd.remove(0);
			response = SpidermanCommandRunner.add(personToAddTo, connectionsToAdd);
			break;
		case CREATE:
			response = SpidermanCommandRunner.create(commandEntry.getValue());
			break;
		case REMOVE:
			List<String> connectionToRemove = commandEntry.getValue();
			String personToRemoveFrom = commandEntry.getValue().get(0);
			connectionToRemove.remove(0);
			response = SpidermanCommandRunner.add(personToRemoveFrom, connectionToRemove);
			response = SpidermanCommandRunner.remove(personToRemoveFrom, connectionToRemove);
			break;
		case REMOVE_PERSON:
			// this puts the first name as the person , then the rest are new connections
			// for that person.
			response = SpidermanCommandRunner.removePersons(commandEntry.getValue());
			break;
		case DAD_JOKE:
			response = getDadJoke();
			break;
		default:
			response = getDefaultResponse();
			break;
		}

		return response;
	}
	
	
	public static GroupMeResponse getDadJoke() {
		HttpGet get = new HttpGet("https://icanhazdadjoke.com/");
		get.addHeader("Accept", "text/plain");
		get.addHeader("User-Agent","GroupMeBot Dad joke getter");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse= null;
		GroupMeResponse response = new GroupMeResponse("Unable to download dad joke.");
		try {
			httpResponse = httpclient.execute(get);
			HttpEntity entity = httpResponse.getEntity();
			OutputStream os = new ByteArrayOutputStream();
			entity.writeTo(os);
			response = new GroupMeResponse(os.toString());
			EntityUtils.consume(entity);
			httpclient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.logError("unable to fetch dad joke: "+e.getMessage(), className);
		}
		
		return response;
	}
	
	
	public static GroupMeResponse getDefaultResponse() {
		return new GroupMeResponse("Not a valid command");
	}

	public static GroupMeResponse create(final List<String> namesToCreate) {

		String message = "";
		for (String person : namesToCreate) {
			if (graphWrapper.createPerson(new Person(person))) {
				message += (person + " successfully created; ");
			} else {
				message += (person + " not created; ");
			}
		}
		// might wanna throw a if around to see if it acutally uploads
		graphWrapper.sync();

		return new GroupMeResponse(message);

	}

	protected static GroupMeResponse removePersons(final List<String> namesToRemove) {

		String responseMessage = "";
		for (String person : namesToRemove) {
			if (graphWrapper.removePerson(new Person(person))) {
				responseMessage += person + " successfully removed; ";
			} else {
				responseMessage += person + " not removed; ";
			}
		}
		// might wanna throw a if around to see if it acutally uploads
		graphWrapper.sync();

		return new GroupMeResponse(responseMessage);
	}

	protected static GroupMeResponse remove(final String person, final List<String> connectionsToRemove) {
		String responseMessage = "";
		Person person1 = new Person(person);
		for (String person2 : connectionsToRemove) {
			if (graphWrapper.removeConnection(person1, new Person(person2))) {
				responseMessage += "Removed: " + "[" + person + "<->" + person2 + "]\n";
			} else {
				responseMessage += "Not removed: " + "[" + person + "<->" + person2 + "]\n";
			}
		}
		graphWrapper.sync();
		return new GroupMeResponse(responseMessage);

	}

	protected static GroupMeResponse add(final String person, final List<String> newConnections) {
		String responseMessage = "";
		logger.logDebug("person to add connections to " + person, className);
		for (String newConnectionToPerson : newConnections) {
			if (graphWrapper.addConnection(new Person(person), new Person(newConnectionToPerson))) {
				responseMessage += "Created: [" + person + "<->" + newConnectionToPerson + "]\n";
				logger.logDebug("add connecton to local graph = success", className);
			} else {
				responseMessage += "Not Created: [" + person + "<->" + newConnectionToPerson + "]\n";
			}
		}
		// might wanna throw a if around to see if it acutally uploads
		graphWrapper.sync();

		return new GroupMeResponse(responseMessage);
	}

	 protected static GroupMeResponse view(List<String> namesToView) {
		 GroupMeResponse response;
		 if(namesToView.size()==1 && namesToView.get(0).equalsIgnoreCase("all")) {
			 BufferedImage img = graphWrapper.viewAll();
			 response = new GroupMeResponse("",img);
		 }
		 else {
			 List<Person> peopleToView = new ArrayList<>();
			 for (String s : namesToView) {
				 peopleToView.add(new Person(s));
			 }
			 BufferedImage img = graphWrapper.view(peopleToView);
			 response = new GroupMeResponse("",img);
		 }
		
		 return response;
	 }
}
