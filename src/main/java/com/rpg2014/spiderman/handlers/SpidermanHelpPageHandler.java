package com.rpg2014.spiderman.handlers;

import java.io.IOException;
import java.io.OutputStream;

import com.rpg2014.spiderman.logger.SpidermanLogger;
import com.rpg2014.spiderman.types.Webpage;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SpidermanHelpPageHandler implements HttpHandler{
	private static final SpidermanLogger logger = SpidermanLogger.getInstance();
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		try {
			if (httpExchange.getRequestMethod().equalsIgnoreCase("get")) {
				String response = Webpage.getHelp();
				int responseCode = 200;
				httpExchange.sendResponseHeaders(responseCode, response.getBytes().length);
				OutputStream os = httpExchange.getResponseBody();
				os.write(response.getBytes());
				os.close();

				
			}else {
				throw new Exception("Not a get");
			}
		}catch (Exception e) {
			String response = "Server error";
			httpExchange.sendResponseHeaders(500, response.getBytes().length);
			OutputStream os = httpExchange.getResponseBody();

			os.write(response.getBytes());
			os.close();

			logger.logFatal("Expection: " + e.getMessage(), this.getClass().getSimpleName());
		}
		
	}
	
	
	

}
