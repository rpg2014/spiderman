package com.rpg2014.spiderman.handlers;


import com.rpg2014.spiderman.GroupMe.GroupMeCallback;
import com.rpg2014.spiderman.GroupMe.GroupMeResponse;
import com.rpg2014.spiderman.logger.SpidermanLogger;
import com.rpg2014.spiderman.types.SpidermanCommand;
import com.rpg2014.spiderman.parsers.SpidermanParser;
import com.rpg2014.spiderman.SpidermanCommandRunner;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class SpidermanHandler implements HttpHandler {
	private SpidermanLogger logger = SpidermanLogger.getInstance();
	private String className = SpidermanHandler.class.getSimpleName();
	CloseableHttpClient httpclient = HttpClients.createDefault();

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		GroupMeCallback callback = null;
		try {
			if (httpExchange.getRequestMethod().equalsIgnoreCase("post")) {
				callback = new GroupMeCallback(httpExchange.getRequestBody());
				// respond to server that we got the request.
				String response;
				int responseCode;
				if (callback.isCommand()) {
					response = "OK";
					responseCode = 200;
				} else {
					response = "OK, not a command";
					responseCode = 201;
				}

				httpExchange.sendResponseHeaders(responseCode, response.getBytes().length);
				OutputStream os = httpExchange.getResponseBody();

				os.write(response.getBytes());
				os.close();
				logger.logInfo("Received request " + response, className);
				// this will hand
				if (callback.isCommand()) {
					this.handleCallBack(callback);
				}
			} else {
				String response = "Bad Request";
				int responseCode = 400;
				httpExchange.sendResponseHeaders(responseCode, response.getBytes().length);
				OutputStream os = httpExchange.getResponseBody();

				os.write(response.getBytes());
				os.close();
				logger.logInfo(response + " not a post", className);
			}

		} catch (Exception e) {

			String response = "Server error";
			httpExchange.sendResponseHeaders(500, response.getBytes().length);
			OutputStream os = httpExchange.getResponseBody();

			os.write(response.getBytes());
			os.close();
			GroupMeResponse errorResponse = new GroupMeResponse("Something Broke: "+e.getMessage());
			errorResponse.setGroupIdToSendTo(callback);
			this.doPost(errorResponse);
			
			logger.logFatal("Expection: " + e.getMessage(), className);
			e.printStackTrace();
		}

	}

	private void handleCallBack(final GroupMeCallback callback) {
		// get first word of text check for @spiderman. if true then check second word
		// to see if valid command (enum parse?)
		// then pass the command in a map entry <SpidermanCommand,String[] args> then

		Map.Entry<SpidermanCommand, List<String>> commandEntry = SpidermanParser.getCommandEntry(callback);
		// logger.logInfo(MessageFormat.format("Found command {0} + args {1}",
		// commandEntry.getKey().toString(),
		// commandEntry.getValue().toString()), className);
		GroupMeResponse gmResponse = SpidermanCommandRunner.runCommand(commandEntry);
		gmResponse.setGroupIdToSendTo(callback);
		// if(gmResponse.isImageResponse()){
		// do stuff for uploading an img
		// }
		try {
			this.doPost(gmResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //

	}

	// make a wrapper around httpclient that sents posts. takes a GroupMeRequest

	private void doPost(final GroupMeResponse response) throws IOException {
		HttpPost post = new HttpPost("https://api.groupme.com/v3/bots/post");
		post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		
		
		if(response.isImageResonse()) {

			response.appendURLToText(this.doImgPost(response.getBufferedImage()));

		}
		logger.logDebug("response text: " + response.getText(), className);
		nameValuePair.add(new BasicNameValuePair("text", response.getText()));
		//logger.logDebug("response bod_id=" + response.getBotID(), className);
		nameValuePair.add(new BasicNameValuePair("bot_id", response.getBotID()));
		
		CloseableHttpResponse httpResponse = null;
		try {

			post.setEntity(new UrlEncodedFormEntity(nameValuePair));
			httpResponse = httpclient.execute(post);

			logger.logInfo("GM request: " + httpResponse.getStatusLine(), className);
			
			HttpEntity entity2 = httpResponse.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			EntityUtils.consume(entity2);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.logError("Unsupported Encodeing exception thrown when trying to post: " + e.getMessage(), className);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			logger.logError("Client protocal exception when trying to post:" + e.getMessage(), className);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.logError("IOException when trying to post: " + e.getMessage(), className);
		} finally {
			httpResponse.close();
		}
	}
	
	private String doImgPost(final BufferedImage img) throws IOException {
		ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
		ImageIO.write(img,"jpg",imgOutputStream);
		//if local write to file so i can see image;
		if(!Boolean.valueOf(System.getenv("ON_HEROKU"))) {
			FileOutputStream fileos = new FileOutputStream(new File("img.jpg"));
			fileos.write(imgOutputStream.toByteArray());
			fileos.flush();
			fileos.close();
		}
		
		HttpPost imgPost = new HttpPost("https://image.groupme.com/pictures");
		String accessToken = this.getAccessToken();
		
		
		imgPost.setEntity(new ByteArrayEntity(imgOutputStream.toByteArray()));
		
		imgPost.addHeader("X-Access-Token", accessToken);
		imgPost.addHeader("Content-Type", "image/jpeg");
		
		
		
		CloseableHttpResponse httpResponse = httpclient.execute(imgPost);
		imgPost.completed();
		logger.logInfo("Img upload response: " +httpResponse.getStatusLine(), className);
		OutputStream os = new ByteArrayOutputStream();
		HttpEntity entity = httpResponse.getEntity();
		entity.writeTo(os);
		
		//getting url from response;
		JSONObject obj = new JSONObject(os.toString());;
		
		JSONObject payload = obj.getJSONObject("payload");
		String url = payload.getString("picture_url");
		
		
		logger.logInfo("Uploaded img to url: "+ url , className);
		

		EntityUtils.consume(entity);
		return url;
	}
	
	private String getAccessToken() throws FileNotFoundException {
		String token = "";
		if(Boolean.valueOf(System.getenv("ON_HEROKU"))){
			token = System.getenv("ACCESS_TOKEN");
		}else {
			Scanner scan = new Scanner(new File(".access")); 
			token = scan.nextLine().trim();
		}
		return token;
	}


}
