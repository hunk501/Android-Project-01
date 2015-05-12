package com.example.valhala;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class BuddyCalendar extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buddy_calendar);
		
		Bundle bundle = getIntent().getExtras();
		Log.d("[Extras]", bundle.getString("_date"));
	}
}
