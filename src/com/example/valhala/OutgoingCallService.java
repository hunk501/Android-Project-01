package com.example.valhala;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class OutgoingCallService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		/**
		 * Get the Extras from the Intent
		 */
		try {
			
			String phone_number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			
			/**
			 * Start an Activity to handle GUI for Password
			 */
			Intent i = new Intent(this.getApplicationContext(), BlockOutgoingCalls.class);
			i.putExtra("_phone_number", phone_number); // Put extra String containing Phone Number
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Set to a New task to bring it to front
			startActivity(i);
			
		} catch (Exception e) {
			//Toast.makeText(getApplicationContext(), "OutgoingCallService: "+ e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
}
