package com.example.smartvent;

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

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class SetTempTask extends AsyncTask<String, Void, Void> {

	private Context context;
    
	public SetTempTask (Context context){
         this.context = context;
    }

	@Override
	protected Void doInBackground(String... params) {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://www.obycode.com/smartventure/set.php");

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("room", params[0]));
	        nameValuePairs.add(new BasicNameValuePair("setpoint", params[1]));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        
	    } catch (ClientProtocolException e) {
	        Toast.makeText(context, "ClientProtocol Error", Toast.LENGTH_SHORT).show();
	    } catch (IOException e) {
	    	Toast.makeText(context, "IO HTTP Error", Toast.LENGTH_SHORT).show();
	    }
		return null;
	}

	
	
}
