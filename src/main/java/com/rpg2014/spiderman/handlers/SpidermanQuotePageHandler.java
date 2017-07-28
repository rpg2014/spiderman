package com.rpg2014.spiderman.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.rpg2014.spiderman.WebpageBuilder;
import com.rpg2014.spiderman.logger.SpidermanLogger;
import com.rpg2014.spiderman.types.FailedAuthException;
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
				Map<String,String> keyValuePairs = parseQuery(httpExchange.getRequestURI().getQuery());
				if(isAuthenticated(Integer.parseInt(keyValuePairs.get("kee"))) ){
					logger.logInfo("Recived authenticated Request", className);
					String response = WebpageBuilder.getWebpage();
					int responseCode = 200;
					httpExchange.sendResponseHeaders(responseCode, response.getBytes().length);
					OutputStream os = httpExchange.getResponseBody();
					os.write(response.getBytes());
					os.close();
					
				}else {
					logger.logWarn("Received Unauthenticted request", className);
					throw new FailedAuthException();
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
		}catch (FailedAuthException e) { 
			String response = Webpage.getError();
			int responseCode = 403;
			httpExchange.sendResponseHeaders(responseCode, response.getBytes().length);
			OutputStream os = httpExchange.getResponseBody();
			os.write(response.getBytes());
			os.close();
		
		}catch (Exception e) {
			String response = "Server error";
			httpExchange.sendResponseHeaders(500, response.getBytes().length);
			OutputStream os = httpExchange.getResponseBody();

			os.write(response.getBytes());
			os.close();

			logger.logFatal("Expection: " + e.getMessage(), className);
		}

	}
	
	
	private boolean isAuthenticated(Integer authCode) {
		if(quoteWrapper == null) {
			quoteWrapper = SpidermanQuoteWrapper.getInstance();
		}
		return authCode.equals(quoteWrapper.getQuoteMap().toString().hashCode());
	}


	protected Map<String,String> parseQuery(final String query) {
		Map<String, String> result = new HashMap<String, String>();
		for (String param : query.split("&")) {
	        String pair[] = param.split("=");
	        if (pair.length>1) {
	            result.put(pair[0], pair[1]);
	        }else{
	            result.put(pair[0], "");
	        }
	    }
	    return result;
	}

}
