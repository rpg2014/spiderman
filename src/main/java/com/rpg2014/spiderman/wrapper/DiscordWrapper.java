package com.rpg2014.spiderman.wrapper;

import com.rpg2014.spiderman.GroupMe.GroupMeResponse;
import com.rpg2014.spiderman.logger.SpidermanLogger;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DiscordWrapper {
    private static final String DISCORD_URL = System.getenv("DISCORD_WEBHOOK_URL");
    private static String className = DiscordWrapper.class.getSimpleName();
    private static SpidermanLogger logger = SpidermanLogger.getInstance();

    public static void sendToDiscord(final String message) {
        //TODO
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost(DISCORD_URL);
        post.setHeader("Content-Type", "application/json; charset=UTF-8");
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();


        logger.logDebug("response text: " + message, className);
        nameValuePair.add(new BasicNameValuePair("content", message));
        //logger.logDebug("response bod_id=" + response.getBotID(), className);


        CloseableHttpResponse httpResponse = null;
        try {

            post.setEntity(new UrlEncodedFormEntity(nameValuePair));
            httpResponse = httpclient.execute(post);

            logger.logInfo("Discord request: " + httpResponse.getStatusLine(), className);

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
            try {
                httpResponse.close();
            }catch (IOException e){
                logger.logError("IO Exception when trying to close response: "+e.getMessage(),className);
            }
        }
    }
}
