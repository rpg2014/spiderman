package com.rpg2014.spiderman.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.lang.Math;
import com.rpg2014.spiderman.WebpageBuilder;
import com.rpg2014.spiderman.logger.SpidermanLogger;
import com.rpg2014.spiderman.types.FailedAuthException;
import com.rpg2014.spiderman.types.Person;
import com.rpg2014.spiderman.types.Webpage;
import com.rpg2014.spiderman.wrapper.SpidermanQuoteWrapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SpidermanQuotePageHandler implements HttpHandler {
	private final String className = this.getClass().getSimpleName();
	private static SpidermanLogger logger = SpidermanLogger.getInstance();
	private SpidermanQuoteWrapper quoteWrapper;

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		try {
			if (httpExchange.getRequestMethod().equalsIgnoreCase("get")) {
				Map<String, String> keyValuePairs = parseQuery(httpExchange.getRequestURI().getQuery());
				if (isAuthenticated(Integer.parseInt(keyValuePairs.get("kee")))) {
					logger.logInfo("Recived authenticated Request", className);
					String response = WebpageBuilder.getWebpage();
					int responseCode = 200;
					httpExchange.sendResponseHeaders(responseCode, response.getBytes().length);
					OutputStream os = httpExchange.getResponseBody();
					os.write(response.getBytes());
					os.close();

				} else {
					logger.logWarn("Received Unauthenticted request", className);
					throw new FailedAuthException();
				}
			} else if (httpExchange.getRequestMethod().equalsIgnoreCase("post")) { // HANDLE QUOTE SUBMISSIONS POST

				String query = URLDecoder.decode(convertInputStreamToString(httpExchange.getRequestBody()), "UTF-8");
				Map<String, String> params = parseQuery(query);
				if (params.containsKey("quote")) {
					handleQuoteAdd(params);

					String response = "OK";
					int responseCode = 303;
					Headers responseHeaders = httpExchange.getResponseHeaders();
					responseHeaders.add("Location", quoteWrapper.getQuoteLink());
					httpExchange.sendResponseHeaders(responseCode, response.getBytes().length);
					OutputStream os = httpExchange.getResponseBody();
					os.write(response.getBytes());
					os.close();
				} else if (params.containsKey("pss") && params.containsKey("quoteToRemove")
						&& params.get("pss").equalsIgnoreCase("supersecretpassword")) {
					handleQuoteRemove(params);
					String response = "OK";
					int responseCode = 303;
					Headers responseHeaders = httpExchange.getResponseHeaders();
					responseHeaders.add("Location", quoteWrapper.getQuoteLink());
					httpExchange.sendResponseHeaders(responseCode, response.getBytes().length);
					OutputStream os = httpExchange.getResponseBody();
					os.write(response.getBytes());
					os.close();
				} else {
					throw new Exception("Request doesn't contain the right headers or pass is wrong");
				}

			} else {
				String response = "Bad Request";
				int responseCode = 400;
				httpExchange.sendResponseHeaders(responseCode, response.getBytes().length);
				OutputStream os = httpExchange.getResponseBody();

				os.write(response.getBytes());
				os.close();
				logger.logInfo(response + " not a get", className);
			}
		} catch (FailedAuthException e) {
			String response = Webpage.getError();
			int responseCode = 403;
			httpExchange.sendResponseHeaders(responseCode, response.getBytes().length);
			OutputStream os = httpExchange.getResponseBody();
			os.write(response.getBytes());
			os.close();

		} catch (Exception e) {
			String response = "Server error";
			httpExchange.sendResponseHeaders(500, response.getBytes().length);
			OutputStream os = httpExchange.getResponseBody();

			os.write(response.getBytes());
			os.close();

			logger.logFatal("Expection: " + e.getMessage(), className);
		}

	}

	protected void handleQuoteRemove(final Map<String, String> params) {
		if (quoteWrapper == null) {
			quoteWrapper = SpidermanQuoteWrapper.getInstance();
		}
		logger.logInfo("Removeing quote: "+params.get("quoteToRemove").substring(0, Math.min(20,params.get("quote").length()-1))+"... by: "+params.get("name"), className);
		quoteWrapper.removeQuote(new Person(params.get("name")), params.get("quoteToRemove"));
		WebpageBuilder.resetWebpage();
	}

	protected void handleQuoteAdd(final Map<String, String> params) {
		if (quoteWrapper == null) {
			quoteWrapper = SpidermanQuoteWrapper.getInstance();
		}
		logger.logInfo("Adding quote: "+params.get("quote").substring(0, Math.min(20,params.get("quote").length()-1))+"... by: "+params.get("name"), className);
		quoteWrapper.addQuote(new Person(params.get("name")), params.get("quote").trim());
		
	}

	private boolean isAuthenticated(Integer authCode) {
		if (quoteWrapper == null) {
			quoteWrapper = SpidermanQuoteWrapper.getInstance();
		}

		return authCode.equals(quoteWrapper.getQuoteMap().toString().hashCode());
	}

	protected Map<String, String> parseQuery(final String query) {
		Map<String, String> result = new HashMap<String, String>();
		for (String param : query.split("&")) {
			String pair[] = param.split("=");
			if (pair.length > 1) {
				result.put(pair[0], pair[1]);
			} else {
				result.put(pair[0], "");
			}
		}
		return result;
	}

	protected String convertInputStreamToString(final InputStream in) {
		Scanner scan = new Scanner(in).useDelimiter("\\A");

		return scan.hasNext() ? scan.next() : "";
	}
}
