package com.rpg2014.spiderman;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.rpg2014.spiderman.wrapper.SpidermanGraphWrapper;
import com.rpg2014.spiderman.wrapper.SpidermanQuoteWrapper;
import com.rpg2014.spiderman.GroupMe.GroupMeResponse;
import com.rpg2014.spiderman.logger.SpidermanLogger;
import com.rpg2014.spiderman.types.Person;
import com.rpg2014.spiderman.types.SpidermanCommand;

public class SpidermanCommandRunner {
	private static final String className = SpidermanCommandRunner.class.getSimpleName();
	private static SpidermanLogger logger = SpidermanLogger.getInstance();
	private static SpidermanGraphWrapper graphWrapper ;
	private static SpidermanQuoteWrapper quoteWrapper;

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
			response = SpidermanCommandRunner.remove(personToRemoveFrom, connectionToRemove);
			break;
		case REMOVE_PERSON:
			// this puts the first name as the person , then the rest are new connections
			// for that person.
			response = SpidermanCommandRunner.removePersons(commandEntry.getValue());
			break;
		case LIST:
			if(commandEntry.getValue().size()==0) {
				response = SpidermanCommandRunner.listAll();
			}else {
				response = SpidermanCommandRunner.list(commandEntry.getValue());
			}
			break;
		case PATH:

			if (commandEntry.getValue().size() != 2) {
				response = new GroupMeResponse("Path requires 2 names");
			} else {
				String startName = commandEntry.getValue().get(0);
				String stopName = commandEntry.getValue().get(1);
				response = SpidermanCommandRunner.path(startName, stopName);
			}
			break;
			
		case SIZE:
			
			if(commandEntry.getValue().size()==0) {
				response = sizeTop();
			}else {
				response = size(commandEntry.getValue());
			}
			
			break;
		case QUOTE:
				if(commandEntry.getValue().size()==0) {
					response = getRandomQuote();
				}else if (commandEntry.getValue().size() == 1) {
					response = getRandomQuote(commandEntry.getValue().get(0));
				}else if (commandEntry.getValue().size()==2) {
					response = getQuote(commandEntry.getValue().get(0),commandEntry.getValue().get(1));
				}else {
					response = new GroupMeResponse("Invalid args, try \"quote: name , keyword\"");
				}
			break;
		case ADD_QUOTE:
			if(commandEntry.getValue().size()!=2) {
				response = new GroupMeResponse("Invalid args, do \"add quote: name, quote");
			}else {
				response = addQuote(commandEntry.getValue().get(0),commandEntry.getValue().get(1));
			}
			break;
		case DAD_JOKE:
			response = getDadJoke();
			break;
		case JOKE:
			if (commandEntry.getValue().size() > 2) {
				response = new GroupMeResponse("Joke only takes 2 args");
			} else if (commandEntry.getValue().size() == 2) {
				response = getChuckJoke(commandEntry.getValue().get(0), commandEntry.getValue().get(1));
			} else if (commandEntry.getValue().size() == 1) {
				response = getChuckJoke(commandEntry.getValue().get(0), null);
			} else if (commandEntry.getValue().isEmpty()) {
				response = getChuckJoke();
			}
			break;
		default:
			response = getDefaultResponse();
			break;
		}

		return response;
	}

	public static GroupMeResponse getChuckJoke() {
		return getChuckJoke(null, null);
	}

	public static GroupMeResponse getChuckJoke(final String firstName, final String lastName) {
		String url = "http://api.icndb.com/jokes/random?escape=javascript";
		if (firstName != null) {
			url += "&firstName=" + firstName;
			if (lastName != null) {
				url += "&lastName=" + lastName;
			}
		}
		System.out.println(url);
		HttpGet get = new HttpGet(url);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse = null;
		GroupMeResponse response = new GroupMeResponse("Unable to download joke.");
		try {
			httpResponse = httpclient.execute(get);
			HttpEntity entity = httpResponse.getEntity();

			OutputStream os = new ByteArrayOutputStream();
			entity.writeTo(os);

			JSONObject json = new JSONObject(os.toString());
			if (json.getString("type").equalsIgnoreCase("success")) {
				response = new GroupMeResponse(json.getJSONObject("value").getString("joke"));
			}
			EntityUtils.consume(entity);
			httpclient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.logError("unable to fetch dad joke: " + e.getMessage(), className);
		}

		return response;
	}

	public static GroupMeResponse getDadJoke() {
		HttpGet get = new HttpGet("https://icanhazdadjoke.com/");
		get.addHeader("Accept", "text/plain");
		get.addHeader("User-Agent", "GroupMeBot Dad joke getter");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse = null;
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
			logger.logError("unable to fetch dad joke: " + e.getMessage(), className);
		}

		return response;
	}

	public static GroupMeResponse getDefaultResponse() {
		return new GroupMeResponse(
				"Not a valid command.\nValid commands are: view, create, add, remove, remove person, path, size and list.");
	}
	
	protected static GroupMeResponse getRandomQuote() {
		
		lazyQuoteWrapper();
		
		return new GroupMeResponse(quoteWrapper.getRandomQuote());
	}
	protected static GroupMeResponse getRandomQuote(final String person) {
		lazyQuoteWrapper();
		return new GroupMeResponse(quoteWrapper.getRandomQuote(new Person(person)));
	}
	protected static GroupMeResponse getQuote(final String person,final String strForSearch) {
		lazyQuoteWrapper();
		return new GroupMeResponse(quoteWrapper.getQuote(new Person(person),strForSearch));
	}
	
	protected static GroupMeResponse addQuote(final String person, final String quote) {
		lazyQuoteWrapper();
		return new GroupMeResponse(quoteWrapper.addQuote(new Person(person),quote));
	}

	protected static GroupMeResponse path(final String startName, final String stopName) {
		lazyGraphWrapper();
		return new GroupMeResponse(graphWrapper.path(new Person(startName), new Person(stopName)));
	}

	public static GroupMeResponse create(final List<String> namesToCreate) {
		lazyGraphWrapper();
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
		lazyGraphWrapper();
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
		lazyGraphWrapper();
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
		lazyGraphWrapper();
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

		if (responseMessage.isEmpty()) {
			responseMessage = "Person not found in database, check your commas and spellings";
		}
		return new GroupMeResponse(responseMessage);
	}

	protected static GroupMeResponse view(List<String> namesToView) {
		lazyGraphWrapper();
		GroupMeResponse response;
		if ((namesToView.size() == 1 && namesToView.get(0).equalsIgnoreCase("all")) || namesToView.size()==0) {
			BufferedImage img = graphWrapper.viewAll();
			response = new GroupMeResponse("", img);
		} else {
			List<Person> peopleToView = new ArrayList<>();
			for (String s : namesToView) {
				peopleToView.add(new Person(s));
			}
			BufferedImage img = graphWrapper.view(peopleToView);
			response = new GroupMeResponse("", img);
		}

		return response;
	}
	
	protected static GroupMeResponse listAll() {
		lazyGraphWrapper();
		return new GroupMeResponse(graphWrapper.listAll());
	}
	protected static GroupMeResponse list(final List<String> namesToList) {
		lazyGraphWrapper();
		if(namesToList.get(0).equalsIgnoreCase("all")) {
			return listAll();
		}
		StringBuilder builder = new StringBuilder();
		for (String s : namesToList) {
			builder.append(graphWrapper.list(new Person(s))+"\n");
		}
		return new GroupMeResponse(builder.toString().trim());
	}
	protected static void lazyGraphWrapper() {
		if(graphWrapper == null) {
			graphWrapper = SpidermanGraphWrapper.getInstance();
		}
	}
	protected static void lazyQuoteWrapper() {
		if(quoteWrapper == null) {
			quoteWrapper = SpidermanQuoteWrapper.getInstance();
		}
	}
	protected static GroupMeResponse sizeTop() {
		lazyGraphWrapper();
		return new GroupMeResponse(graphWrapper.getTop3Connections());
	}
	
	protected static GroupMeResponse size(final List<String> namesToGetSizeOf) {
		lazyGraphWrapper();
		List<Person> listOfPeople = new ArrayList<>();
		for (String s: namesToGetSizeOf) {
			listOfPeople.add(new Person(s));
		}
		return new GroupMeResponse(graphWrapper.sizeOf(listOfPeople));
	}
}
