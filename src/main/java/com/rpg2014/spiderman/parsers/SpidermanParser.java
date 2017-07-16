package com.rpg2014.spiderman.parsers;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.AbstractMap;

import com.rpg2014.spiderman.Spiderman;
import com.rpg2014.spiderman.GroupMe.GroupMeCallback;
import com.rpg2014.spiderman.logger.SpidermanLogger;
import com.rpg2014.spiderman.types.SpidermanCommand;

public class SpidermanParser{

    private static SpidermanLogger logger = SpidermanLogger.getInstance();

    public static SpidermanCommand parseCommand(final String commandString){
        
        SpidermanCommand retCommand = null;
        
        retCommand = SpidermanCommand.valueOf(commandString);
        logger.logInfo("Found command: "+ retCommand.name(), SpidermanParser.class.getSimpleName());
        
        return retCommand;
    }


    public static Map.Entry<SpidermanCommand,List<String>> getCommandEntry(final GroupMeCallback callback){
        String[] commandArr = callback.getText().replace("@spiderman", "").trim().split(":");//);
        
        List<String> argList = new ArrayList<>();
        //removes the : + replaces spaces with underscore
        System.out.println("commandArray=" + commandArr[0].toUpperCase().replace(":", "").trim().replaceAll(" ", "_"));
        SpidermanCommand command = parseCommand(commandArr[0].toUpperCase().replace(":", "").trim().replaceAll(" ", "_"));
        String[] args = commandArr[1].split(",");
        logger.logInfo("args = " + commandArr[1], SpidermanParser.class.getSimpleName());

        for(int i = 0;i<args.length;i++){
            argList.add(args[i].trim());
//            logger.logDebug("Found argument: " +args[i], SpidermanParser.class.getSimpleName());
        }
        
        
        Map.Entry<SpidermanCommand,List<String>> commandEntry = new AbstractMap.SimpleEntry<SpidermanCommand,List<String>>(command,argList);
        
        
        
        return commandEntry;
        
    }
}