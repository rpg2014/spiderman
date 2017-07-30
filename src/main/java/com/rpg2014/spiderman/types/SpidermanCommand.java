package com.rpg2014.spiderman.types;

public enum SpidermanCommand{
    VIEW(),
    ADD(),
    REMOVE(),
    REMOVE_PERSON(),
    DAD_JOKE(),
    JOKE(),
    PATH(),
    LIST(),
    SIZE(),
    HELP(),
    CREATE(),
    ADD_QUOTE(),
    PORN(),
    QUOTE_LINK(),
    QUOTE();



    private int numArgs;
    private String commandTxt;
    private SpidermanCommand(){
        
    }


    public int getNumArgs(){
        return numArgs;
    }

    



}