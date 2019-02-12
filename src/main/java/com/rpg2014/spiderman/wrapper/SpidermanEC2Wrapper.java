package com.rpg2014.spiderman.wrapper;

public class SpidermanEC2Wrapper {
    private static SpidermanEC2Wrapper ourInstance = new SpidermanEC2Wrapper();

    public static SpidermanEC2Wrapper getInstance() {
        return ourInstance;
    }

    private SpidermanEC2Wrapper() {
    }
}
