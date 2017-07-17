/**
 * 
 */
package com.rpg2014.spiderman.GroupMe;

import java.awt.image.BufferedImage;
import org.json.JSONArray;
import org.json.JSONObject;

import com.rpg2014.spiderman.SpidermanProperties;
import com.rpg2014.spiderman.logger.SpidermanLogger;

/**
 * @author rpg2014
 *
 */
public class GroupMeResponse {
	private static SpidermanLogger logger = SpidermanLogger.getInstance();
	boolean imageResonse;
	String text;
	String bot_id;
	// image only things
	BufferedImage image;

	JSONArray attachments;
	JSONObject imageObject;
	String type;
	String url;

	public GroupMeResponse(final String text) {
		this.text = text;
		if (Boolean.valueOf(System.getenv("ON_HEROKU"))){
			this.bot_id = System.getenv("BOT_ID");
			
		}else {
			this.bot_id = SpidermanProperties.getBotID(); 
		}
		this.imageResonse = false;
		
	}
	public BufferedImage getBufferedImage() {
		return image;
	}
	
	public GroupMeResponse(final String text,final BufferedImage img) {
		this.text = text;
		if (Boolean.valueOf(System.getenv("ON_HEROKU"))){
			this.bot_id = System.getenv("BOT_ID");
			
		}else {
			this.bot_id = SpidermanProperties.getBotID(); 
		}
		this.imageResonse = true;
		this.image = img;
		this.type = "image";
		this.attachments = new JSONArray();
		
		
		
	}
	public void setText(final String newText) {
		this.text =  newText;
	}
	public void appendURLToText(final String url) {
		text += url;
	}
	
	public String getText() {
		return text;
	}
	public String getBotID() {
		return bot_id;
	}
	public JSONArray getAttachments() {
		return this.attachments;
	}

	public boolean isImageResonse() {
		return imageResonse;
	}

	public void setUrl(String url) {
		imageObject = new JSONObject();
		imageObject.put("type",type);
		
		imageObject.put("url", url);
		logger.logDebug("ImageObject= "+imageObject.toString(), this.getClass().getSimpleName());
		attachments.put(imageObject);
		logger.logDebug("attachments= "+attachments.toString(), this.getClass().getSimpleName());
		this.url = url;
	}



}
