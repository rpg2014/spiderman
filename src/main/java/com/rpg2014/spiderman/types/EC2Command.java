package com.rpg2014.spiderman.types;

import java.util.Arrays;

public class EC2Command {



    EC2CommandType commandType;
    boolean isCommand;

    EC2Command(EC2CommandType commandType, boolean isCommand){
        this.commandType = commandType;
        this.isCommand = isCommand;
    }

    public static EC2Command parse(String text) {
        String[] commandArray = text.split("\\s+");
        if(commandArray[0].equals("!server") || commandArray[0].equals("!s")){

            EC2CommandType commandType = EC2CommandType.valueOf(commandArray[1].toUpperCase());
            if (commandType!=null) {
                return new EC2Command(commandType, true);
            }
        }
        return new EC2Command(EC2CommandType.NOT_COMMAND,false);


    }

    public boolean isCommand(){
        return isCommand;
    }


    public EC2CommandType getCommand(){
        return commandType;
    }
}
