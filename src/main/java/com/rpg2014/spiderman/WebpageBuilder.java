package com.rpg2014.spiderman;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.rpg2014.spiderman.types.Webpage;
import com.rpg2014.spiderman.wrapper.SpidermanQuoteWrapper;

public class WebpageBuilder {
	private static String htmlHead = Webpage.getHead();
	private static String tableRow = Webpage.getTableRow();
	private static String body1 = Webpage.getBody1();
	private static String body2 = Webpage.getBody2();
	private static SpidermanQuoteWrapper quoteWrapper;
	private static String webpage = null;

	public static String getWebpage() {
		if (webpage == null) {
			webpage = buildWebpage();
		}
		return webpage;
	}

	protected static String buildWebpage() {
		StringBuilder builder = new StringBuilder();
		builder.append(htmlHead);
		builder.append(body1);
		builder.append(buildTableRows());
		builder.append(body2);
		
		return builder.toString();
	}

	protected static String buildTableRows() {
		quoteWrapper = SpidermanQuoteWrapper.getInstance();
		Set<Map.Entry<String,List<String>>> entrySet = quoteWrapper.getQuoteMap().entrySet();
		StringBuilder builder = new StringBuilder();
		for(Map.Entry<String, List<String>> entry : entrySet) {
			for(String s : entry.getValue()) {
				builder.append(MessageFormat.format(tableRow, entry.getKey(),s.replaceAll("\n", "<br/>")));
			}
		}
		return builder.toString();
	}
	public static void resetWebpage() {
		webpage = null;
	}
}
