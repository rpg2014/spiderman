package com.rpg2014.spiderman.handlers;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * IronSpiderHandler
 */
public class IronSpiderHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange ) {
        try {
            if(httpExchange.getRequestMethod().equalsIgnoreCase("POST")){
                
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    
}