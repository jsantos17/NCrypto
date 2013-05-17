package org.sec.ncrypto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.IntentService;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;


public class MessageCheckService extends IntentService {

	final private String url = "http://juanpablo.me/checkQueue.php";
	
	public MessageCheckService(String name) {
		super("MessageCheckService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		HttpResponse response = null;
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			String destinationKey = PreferenceManager
	    			.getDefaultSharedPreferences(getBaseContext())
	    			.getString("KEY_FINGERPRINT", "Key not found");
			String originKey = intent.getStringExtra("origin");
			nameValuePairs.add(new BasicNameValuePair("origin",originKey));
			nameValuePairs.add(new BasicNameValuePair("destination",destinationKey));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = httpClient.execute(httpPost);
		}
		catch(ClientProtocolException e){
			Log.d("org.sec.ncrypto", "Client protocol exception");
		}
		catch(IOException e) {
			Log.d("org.sec.ncrypto", "I/O Exception");
		}
		try {
			String result = response.getEntity().getContent().toString();
			JSONParser parser = new JSONParser();
			JSONArray array = (JSONArray) parser.parse(result);
			List<String[]> messages = new ArrayList<String[]>();
			for(Object obj : array) {
				JSONObject item = (JSONObject)obj;
				String message = (String) item.get("message");
				String key = (String) item.get("key");
				String iv = (String) item.get("vector");
				String[] agMessage = {message, key, iv};
				messages.add(agMessage);
			}
			
		} 
		catch (IllegalStateException e) {
			Log.d("org.sec.ncrypto","IllegalStateException extracting entity");
		} 
		catch (IOException e) {
			Log.d("org.sec.ncrypto","IOException extracting entity");
		} 
		catch (ParseException e) {
			Log.d("org.sec.ncrypto","Parser exception!");
		}
		
		
		
	}

}
