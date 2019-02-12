package com.rpg2014.spiderman.types;

public enum EC2CommandType {
    START("start"),
    STOP("stop"),
    REBOOT("reboot"),
    NOT_COMMAND("");

    String command;


    EC2CommandType(String str){
        command = str;
    }

    public String toString(){
        return command;
    }
}
