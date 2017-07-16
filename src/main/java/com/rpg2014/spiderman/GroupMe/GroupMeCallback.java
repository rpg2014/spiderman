// "attachments": [],
// "avatar_url": "https://i.groupme.com/123456789",
// "created_at": 1302623328,
// "group_id": "1234567890",
// "id": "1234567890",
// "name": "John",
// "sender_id": "12345",
// "sender_type": "user",
// "source_guid": "GUID",
// "system": false,
// "text": "Hello world ☃☃",
// "user_id": "1234567890"
package com.rpg2014.spiderman.GroupMe;

import java.io.InputStream;
import java.util.Scanner;

import org.json.JSONObject;



import com.rpg2014.spiderman.logger.SpidermanLogger;

import java.time.LocalDate;

public class GroupMeCallback{
    private SpidermanLogger logger = SpidermanLogger.getInstance();
    private static final String className = GroupMeCallback.class.getSimpleName();

    private int senderID;
    private String senderType;
    //the important part
    private String text;
    private boolean isCommand;
    
    
    
    
    public GroupMeCallback(final InputStream inputStream){
        String callback = convertInputStreamToString(inputStream);
        JSONObject obj = new JSONObject(callback);
        
        text = obj.getString("text");
        senderID = obj.getInt("sender_id");
        senderType = obj.getString("sender_type");
        
        if(text.startsWith("@spiderman")){
            isCommand = true;
        }else{
            isCommand = false;
        }
       
    }

    public String getText(){
        return text;
    }
    public boolean isCommand(){
        return isCommand;
    }


    protected String parseCallbackForText(final String callbackJson){
        JSONObject json = new JSONObject(callbackJson);
        String text = String.valueOf(json.get("text"));
        return text;
    }

    protected String convertInputStreamToString(final InputStream in){
        Scanner scan = new Scanner(in).useDelimiter("\\A");
        return scan.hasNext() ? scan.next() : "";
    }
}