package com.rpg2014.spiderman.types;

public enum SpidermanCommand{
    VIEW(1,"view"),
    ADD(2,"add"),
    REMOVE(2,"remove"),
    REMOVE_PERSONS(1,"removePersons"),
    CREATE(1,"create");



    private int numArgs;
    private String commandTxt;
    private SpidermanCommand(final int args,final String commandText){
        this.numArgs = args;
        this.commandTxt = commandText;
    }


    public int getNumArgs(){
        return numArgs;
    }

    



}