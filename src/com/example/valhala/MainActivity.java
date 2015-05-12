package com.example.valhala;


import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements OnTabChangeListener {

	TabHost tabhost;
	
	private OutgoingCallReceiver receiver = new OutgoingCallReceiver();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Resources res = getResources();
		tabhost = getTabHost();
		tabhost.setOnTabChangedListener(this);
		
		
		// ---------------- Tab Home ---------------------- //
		Intent intent_home = new Intent().setClass(this, TabHome.class);
		TabSpec home_spec = tabhost.newTabSpec("tabhome")
				.setIndicator("", res.getDrawable(R.drawable.settings))
				.setContent(intent_home);
		
		// ---------------- Tab Contacts ---------------------- //
		Intent intent_contact = new Intent().setClass(this, TabContacts.class);
		TabSpec contact_spec = tabhost
				.newTabSpec("tabcontact")
				.setIndicator("", res.getDrawable(R.drawable.contact2))
				.setContent(intent_contact);
		
		// ---------------- Tab Alarm ---------------------- //
		Intent intent_alarm = new Intent().setClass(this, TabAlarm.class);
		TabSpec alarm_spec = tabhost
				.newTabSpec("tabalarm")
				.setIndicator("", res.getDrawable(R.drawable.alarm))
				.setContent(intent_alarm);
		
		// ---------------- Tab Emergency ---------------------- //
		Intent intent_emergency = new Intent().setClass(this, TabEmergency.class);
		TabSpec emergency_spec = tabhost
				.newTabSpec("tabemergency")
				.setIndicator("", res.getDrawable(R.drawable.emergency))
				.setContent(intent_emergency);
		
		// ---------------- Tab Drinking Buddy ---------------------- //
		Intent intent_drinking_buddy = new Intent().setClass(this, TabDrinkingBuddy.class);
		TabSpec drinking_buddy_spec = tabhost.newTabSpec("tabdrinkingbuddy")
				.setIndicator("", res.getDrawable(R.drawable.calendar))
				.setContent(intent_drinking_buddy);
				
		// Add all Tab
		tabhost.addTab(home_spec);
		tabhost.addTab(contact_spec);
		tabhost.addTab(drinking_buddy_spec);
		tabhost.addTab(alarm_spec);
		tabhost.addTab(emergency_spec);
		
		// Set default tab
		tabhost.setCurrentTab(2);
		
		
		// Set Enabled Receiver
		receiver.setEnabledReceiver(getApplicationContext());
		receiver.setEnabledService(getApplicationContext());
	}
	
	/**
	 * Display Help
	 */
	private void gotoHelp(){
		Intent i = new Intent(this, Settings.class);
		i.putExtra("_filename", "manual.html");
		i.putExtra("_title", "Help");
		// start activity
		startActivity(i);
	}
	
	/**
	 * Goto Hospital Lists
	 */
	private void gotoHospital(){
		Intent i = new Intent(this, Settings.class);
		i.putExtra("_filename", "hospital.html");
		i.putExtra("_title", "Hospital Lists");
		// start activity
		startActivity(i);
	}

	@Override
	public void onTabChanged(String tabId) {
		Log.d("[Tab]", "TabName: " +tabhost.getCurrentTab() +" TabTag: "+ tabhost.getCurrentTabTag());
		//Toast.makeText(getApplicationContext(), "TabName: " +tabhost.getCurrentTab() +" TabTag: "+ tabhost.getCurrentTabTag(), Toast.LENGTH_LONG).show();
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("[Main]", "onPause()");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e("[Main]", "onDestroy()");
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		int id = item.getItemId();
		switch(id){
		
			case R.id.menu_help:
				gotoHelp();
				break;
				
			case R.id.menu_hospital:
				gotoHospital();
				break;
			
			case R.id.menu_close:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}



















