package com.rpg2014.spiderman;


import java.io.IOException;
import java.net.InetSocketAddress;

import com.rpg2014.spiderman.handlers.IronSpiderHandler;
import com.rpg2014.spiderman.handlers.SpidermanHandler;
import com.rpg2014.spiderman.handlers.SpidermanHelpPageHandler;
import com.rpg2014.spiderman.handlers.SpidermanQuotePageHandler;
import com.rpg2014.spiderman.logger.SpidermanLogger;
import com.sun.net.httpserver.HttpServer;




public class Spiderman{

    private static final String className = Spiderman.class.getSimpleName();
    private static HttpServer server;
    private static SpidermanLogger logger = SpidermanLogger.getInstance();

   

    public static void run(){
        try{

            boolean onHeroku = Boolean.valueOf(System.getenv("ON_HEROKU"));
            
            int portNum;
            if(onHeroku){
                portNum = Integer.valueOf(System.getenv("PORT"));
            }else {
            	//SpidermanProperties.getProperties();
                portNum = 5000;

            }

            logger.logInfo("Starting Http server on port "+portNum,className);
            server = HttpServer.create(new InetSocketAddress(portNum), 0);
            server.createContext("/requests", new SpidermanHandler());
            server.createContext("/quotes",new SpidermanQuotePageHandler());
            server.createContext("/help", new SpidermanHelpPageHandler());
            //server.createContext("/discord",new IronSpiderHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
            }catch (IOException e ){
                logger.logError("IOException when creating server: "+e.getMessage(), className);
            }
    
        
    }


   


}
