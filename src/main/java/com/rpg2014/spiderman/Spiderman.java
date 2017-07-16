package com.rpg2014.spiderman;


import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.rpg2014.spiderman.logger.SpidermanLogger;
import com.sun.net.httpserver.HttpServer;




public class Spiderman{

    private static final String className = Spiderman.class.getSimpleName();
    private static Spiderman ourInstance;
    private static HttpServer server;
    private static SpidermanLogger logger = SpidermanLogger.getInstance();

   

    public static void run(){
        try{

            boolean onHeroku = Boolean.valueOf(System.getenv("ON_HEROKU"));

            int portNum;
            if(onHeroku){
                portNum = Integer.valueOf(System.getenv("PORT"));
            }else {
            	
                portNum = 5000;

            }

            logger.logInfo("Starting Http server on port "+portNum,className);
            server = HttpServer.create(new InetSocketAddress(portNum), 0);
            server.createContext("/requests", new SpidermanHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
            }catch (IOException e ){
                logger.logError("IOException when creating server: "+e.getMessage(), className);
            }
    
        
    }


   


}