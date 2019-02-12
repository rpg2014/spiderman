package com.rpg2014.spiderman.types;

public class EC2Command {



    EC2CommandType commandType;
    boolean isCommand;

    EC2Command(EC2CommandType commandType, boolean isCommand){
        commandType = commandType;
        this.isCommand = isCommand;
    }

    public static EC2Command parse(String text) {
        String[] commandArray = text.split("//s+");
        if(commandArray[0].equals("!server") || commandArray[0].equals("!s")){

            EC2CommandType commandType = EC2CommandType.valueOf(commandArray[1]);
            return new EC2Command(commandType,true);
        }else{
            return new EC2Command(EC2CommandType.NOT_COMMAND,false);
        }

    }

    public boolean isCommand(){
        return isCommand;
    }


    public EC2CommandType getCommand(){
        return commandType;
    }
}
