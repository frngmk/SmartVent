package com.example.smartvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RoomActivity extends Activity implements OnClickListener {

	private Integer targetTemp = 70;
	private String currentTemp;
	private String room = "Kitchen";
	private TextView targetTempText;
	private ScheduledExecutorService scheduleTaskExecutor;
	private TextView currentTempText;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        
        Button upButton = (Button) findViewById(R.id.set_temp_up);
        upButton.setOnClickListener(this);

        Button downButton = (Button) findViewById(R.id.set_temp_down);
        downButton.setOnClickListener(this);
        
        targetTempText = (TextView) findViewById(R.id.set_temp_val);
        targetTempText.setText(targetTemp.toString());
        
        currentTempText = (TextView) findViewById(R.id.current_temp_val);
        
        
        
        scheduleTaskExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
        	public void run() {
			    // Create a new HttpClient and Post Header
			    HttpClient httpclient = new DefaultHttpClient();
			    HttpPost httppost = new HttpPost("http://www.obycode.com/smartventure/query.php");
		        
			    try {
			    	// Add data
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			        nameValuePairs.add(new BasicNameValuePair("room", room));
			        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			    	
			    	// Execute HTTP Post Request
			        HttpResponse response = httpclient.execute(httppost);
			        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			        String jsonStr = reader.readLine();
			        JSONObject jsonObj = new JSONObject(jsonStr);

			        currentTemp = (String) jsonObj.get("temp");
			        
			    } catch (ClientProtocolException e) {
			        Toast.makeText(getBaseContext(), "ClientProtocol Error", Toast.LENGTH_SHORT).show();
			        Log.e(getString(R.string.app_name), "ClientProtocol Error");
			    } catch (IOException e) {
			    	Toast.makeText(getBaseContext(), "IO HTTP Error", Toast.LENGTH_SHORT).show();
			    	Log.e(getString(R.string.app_name), "IO HTTP Error");
			    } catch (JSONException e) {
			    	Toast.makeText(getBaseContext(), "JSON Error", Toast.LENGTH_SHORT).show();
			    	Log.e(getString(R.string.app_name), "JSON Error");
				}

        		runOnUiThread(new Runnable() {
        			public void run() {
        				currentTempText.setText(currentTemp);
        			}
        		});
        	}
        }, 0, 15, TimeUnit.SECONDS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_room, menu);
        return true;
    }
    
    @Override
    protected void onDestroy() {
    	// kill scheduled Task Executor
    	scheduleTaskExecutor.shutdown();
    	
    	super.onDestroy();
    };

	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
		case R.id.set_temp_up:
			this.targetTemp++;
			break;
		case R.id.set_temp_down:
			this.targetTemp--;
			break;
		default:
			Toast.makeText(this, "Error. Invalid Button", Toast.LENGTH_SHORT).show();
		}
		
		// update target temp being displayed
		targetTempText.setText(this.targetTemp.toString());
		
		// tell server new temp
		// new SetTempTask(this).execute(this.room, this.targetTemp.toString());
		new AsyncTask<String, Void, Void>() {
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
			        Toast.makeText(getBaseContext(), "ClientProtocol Error", Toast.LENGTH_SHORT).show();
			    } catch (IOException e) {
			    	Toast.makeText(getBaseContext(), "IO HTTP Error", Toast.LENGTH_SHORT).show();
			    }
				return null;
			}
		}.execute(this.room, this.targetTemp.toString());
	}
}
