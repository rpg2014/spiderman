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

            for (EC2CommandType type:
                 EC2CommandType.values()) {
                if (type.toString().equalsIgnoreCase(commandArray[1])){
                    return new EC2Command(type,true);
                }
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
