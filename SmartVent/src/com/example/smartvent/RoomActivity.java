package com.example.smartvent;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RoomActivity extends Activity implements OnClickListener {

	private Integer targetTemp = 70;
	private String room = "Kitchen";
	private TextView targetTempText;
	
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
    
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_room, menu);
        return true;
    }

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
		
		new SetTempTask(this).execute(this.room, this.targetTemp.toString());
		
	}
    
}
