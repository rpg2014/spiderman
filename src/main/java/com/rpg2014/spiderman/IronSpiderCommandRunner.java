package com.rpg2014.spiderman;

import com.rpg2014.spiderman.logger.SpidermanLogger;
import com.rpg2014.spiderman.types.EC2Command;
import com.rpg2014.spiderman.types.EC2CommandType;
import com.rpg2014.spiderman.wrapper.DiscordWrapper;
import com.rpg2014.spiderman.wrapper.SpidermanEC2Wrapper;

public class IronSpiderCommandRunner {
    private static SpidermanEC2Wrapper ec2;
    private static SpidermanLogger logger  = SpidermanLogger.getInstance();
    public static void execute(final EC2CommandType command)throws InterruptedException{
        switch (command){
            case START:
                startServer();
                break;
            case STOP:
                stopServer();
                break;
            case REBOOT:
                rebootServer();
                break;
            case STATUS:
                serverStatus();
                break;
        }
    }

    private static void startServer() throws InterruptedException{
        lazyLoadEC2();
        boolean success = ec2.startInstance();
        if (success) {
            DiscordWrapper.sendToDiscord("Starting server");
            Thread.sleep(30000);
            if(ec2.isInstanceUp()){
                DiscordWrapper.sendToDiscord("Server is up at: "+ec2.getInstanceDomainName() + ":25565.  Give it a few minutes to launch minecraft");
            }
        }else {
            DiscordWrapper.sendToDiscord("Unable to start server");
        }
    }

    private static void stopServer() {
        lazyLoadEC2();
        DiscordWrapper.sendToDiscord("Stopping server");
        boolean success = ec2.stopInstance();
        if (success) {
            DiscordWrapper.sendToDiscord("Stopped server");
        }else {
            DiscordWrapper.sendToDiscord("Unable to stop server");
        }
    }

    private static void rebootServer(){
        lazyLoadEC2();
        ec2.rebootInstance();
        DiscordWrapper.sendToDiscord("Rebooting server");
    }

    private static void serverStatus(){
        lazyLoadEC2();
        logger.logInfo("Getting status",IronSpiderCommandRunner.class.getSimpleName());
        boolean isUp = ec2.isInstanceUp();
        if (isUp){
            String dnsName = ec2.getInstanceDomainName();
            String ip = ec2.getInstanceIp();
            DiscordWrapper.sendToDiscord("Server is up at address: "+dnsName +"; and ip: "+ip);
        }else {
            DiscordWrapper.sendToDiscord("Server is down");
        }
    }



    private static void lazyLoadEC2(){
        if (ec2 == null){
            ec2 = SpidermanEC2Wrapper.getInstance();
        }
    }

}
