package com.rpg2014.spiderman;

import com.rpg2014.spiderman.logger.SpidermanLogger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class SpidermanHandler implements HttpHandler{
    private SpidermanLogger logger = SpidermanLogger.getInstance();
    private String className = SpidermanHandler.class.getSimpleName();

    public void handle(HttpExchange httpExchange)throws IOException{
        Scanner scan = new Scanner(new InputStreamReader(httpExchange.getRequestBody()));
        String response = "";
        while(scan.hasNext()){
            response += scan.nextLine();
        }
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        
        os.write(response.getBytes());
        os.close();
        logger.logInfo(response, className);

    }

}