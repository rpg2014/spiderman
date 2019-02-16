package com.rpg2014.spiderman.handlers;


import com.rpg2014.spiderman.IronSpiderCommandRunner;
import com.rpg2014.spiderman.logger.SpidermanLogger;
import com.rpg2014.spiderman.types.EC2Command;
import com.rpg2014.spiderman.types.Webpage;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

/**
 * IronSpiderHandler
 */
public class IronSpiderHandler implements HttpHandler {
    private SpidermanLogger logger = SpidermanLogger.getInstance();
    private final String CLASS_NAME = this.getClass().getSimpleName();
    @Override
    public void handle(HttpExchange httpExchange ) throws IOException {
            try {
                if (httpExchange.getRequestMethod().equalsIgnoreCase("POST")) {
                    String json = convertInputStreamToString(httpExchange.getRequestBody());
                    logger.logInfo("json is: "+json,CLASS_NAME);
                    JSONObject obj = new JSONObject(json);
                    final String text = obj.getString("text");

                    final EC2Command command = EC2Command.parse(text);
                    if (command.isCommand()) {
                        Thread responseThread = new Thread(() -> {
                            EC2Command com = command;
                            try {
                                logger.logInfo("Running Command",CLASS_NAME);
                                IronSpiderCommandRunner.execute(com.getCommand());
                            }catch (InterruptedException e){
                                logger.logError("Thread Interrupted: "+ e.getMessage(), CLASS_NAME);
                            }

                        });
                        responseThread.start();
                        logger.logInfo("Received Command: " + command.getCommand().toString(), CLASS_NAME);
                        //return response
                        String response = "Received";
                        int responseCode = 200;
                        sendResponse(httpExchange, response, responseCode);
                        responseThread.join();
                    } else {
                        String response = "Received";
                        int responseCode = 202;
                        sendResponse(httpExchange, response, responseCode);
                    }
                } else {
                    String response = "451 Forbidden";
                    int responseCode = 451;
                    sendResponse(httpExchange, response, responseCode);
                }
            }catch (IOException e){
                String response = "451 Forbidden";
                int responseCode = 451;
                sendResponse(httpExchange,response,responseCode);
            }catch(Exception e){
                e.printStackTrace();
            }


    }

    public void sendResponse(HttpExchange httpExchange, String response, int responseCode) throws IOException {
        httpExchange.sendResponseHeaders(responseCode, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    protected String convertInputStreamToString(final InputStream in){
        Scanner scan = new Scanner(in).useDelimiter("\\A");
        return scan.hasNext() ? scan.next() : "";
    }

    
}