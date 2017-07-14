package com.rpg2014.spiderman.logger;

public class SpidermanLogger{


    private static final SpidermanLogger ourInstance = new SpidermanLogger();


    private SpidermanLogger(){

    }

    public static SpidermanLogger getInstance(){
        return ourInstance;
    }

    public void logFatal(final String message, final String className){
        this.log("FATAL", className, message);
    }

    public void logError(final String message, final String className){
        this.log("ERROR", className, message);
    }

    public void logWarn(final String message, final String className){
        this.log("WARN", className, message);
    }
    public void logDebug(final String message,final String className){
        this.log("DEBUG", className, message);
    }
    public void logInfo(final String message, final String className){
        this.log("INFO", className, message);
    }







    private void log(final String level, final String className, final String message){
        String logMessage = String.format("%s:\t[Spiderman: %s]\t%s",level,className,message);
        
        System.out.println(logMessage);
    }

}