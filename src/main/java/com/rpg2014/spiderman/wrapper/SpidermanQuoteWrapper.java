package com.rpg2014.spiderman.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.rpg2014.spiderman.types.Person;

public class SpidermanQuoteWrapper {

	private static SpidermanQuoteWrapper ourInstance = new SpidermanQuoteWrapper();
	Random rand;
	private SpidermanDynamoWrapper dynamoWrapper = SpidermanDynamoWrapper.getInstance();
	private Map<String, List<String>> quoteMap;

	public static SpidermanQuoteWrapper getInstance() {
		return ourInstance;
	}

	private SpidermanQuoteWrapper() {
		quoteMap = dynamoWrapper.getQuotes();
		rand = new Random();
	}

	public String getRandomQuote(final Person person) {
		List<String> quoteList = quoteMap.get(person.toString());
		String retQuote = quoteList.get(rand.nextInt(quoteList.size()));
		return retQuote + "\n    -" + person.toString();
	}

	public String getQuote(final Person person, final String searchStr) {
		List<String> quoteList = quoteMap.get(person.toString());
		if (quoteList == null) {
			return "Unable to find a quote that contains \"" + searchStr + "\" by " + person.toString();
		}

		String retQuote = searchQuotes(quoteList, searchStr);
		

		return retQuote + "\n    -" + person.toString();

	}

	public String addQuote(final Person person, final String quote) {
		List<String> quoteList = quoteMap.get(person.toString());
		if (quoteList == null) {
			quoteList = new ArrayList<>();
		}
		quoteList.add(quote);
		quoteMap.put(person.toString(), quoteList);
		if (this.sync()) {
			return "Quote was added successfully";
		} else {
			return "Unable to add quote";
		}
	}

	public boolean sync() {
		return dynamoWrapper.putQuotes(quoteMap);
	}

	protected String searchQuotes(final List<String> quoteList, final String searchStr) {
		String retQuote = null;
		for (String quote : quoteList) {
			if (quote.toLowerCase().contains(searchStr.toLowerCase().trim())) {
				retQuote = quote;
				break;
			}
		}
		return retQuote;
	}

}