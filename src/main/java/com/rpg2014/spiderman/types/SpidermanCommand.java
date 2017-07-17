package com.rpg2014.spiderman.types;

public enum SpidermanCommand{
    VIEW(1,"view"),
    ADD(2,"add"),
    REMOVE(2,"remove"),
    REMOVE_PERSON(1,"removePersons"),
    DAD_JOKE(1,"dadJoke"),
    JOKE(2,"nameJoke"),
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