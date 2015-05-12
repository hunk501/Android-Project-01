package com.example.valhala;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.valhala.SMS.SMSUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class TabAlarm extends Activity implements OnClickListener {
	
	private TimePicker timepicker;
	private Button btn;
	private EditText txtpass;
	
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
	
	private DBHelper helper = null;
	private String alarm_id = null;
	private String alarm_hour = null;
	private String alarm_minute = null;
	private String alarm_time = null;
	private AlarmManager alarm = null;
	private int attempt = 2;
	private boolean firstTime = true;
	private RelativeLayout relativeLayout;
	
	private String mode = null;
	private int block_hrs = 0;
	
	private ScheduledAlarm sched_alarm = new ScheduledAlarm();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_alarm);
		
		insertView();
		
	}
	
	/**
	 * Insert View
	 */
	private void insertView(){
		/**
		 * Instantiate database then check if there is an existing alarm
		 * then use that as an default time, so that the user can reset that.
		 */
		helper = new DBHelper(this);
		List<TodoAlarm> lists = helper.countRecords("tbl_alarm");
		
		timepicker = (TimePicker) findViewById(R.id.timePicker1);
		btn = (Button) findViewById(R.id.button1);
		txtpass = (EditText) findViewById(R.id.editText1);
		TextView txt_time = (TextView) findViewById(R.id.textView2);
		
		// Instantiate Calendar
		Calendar calendar = Calendar.getInstance();
			
		// there is an Alarm exists
		if (lists != null) {
			timepicker.setVisibility(View.GONE);
			txt_time.setVisibility(View.VISIBLE);
			for (TodoAlarm list : lists) {
				alarm_id = list.getId();
				alarm_hour = list.getHour();
				alarm_minute = list.getMinute();
				alarm_time = list.getTime();
				txt_time.setText(alarm_time);
				Log.d("[Alarm Time]", list.getTime());
			}
			btn.setText(R.string.lbl_cancel_alarm);
			btn.setTag("cancel");
			// set Time
			//setTime(Integer.parseInt(alarm_hour), Integer.parseInt(alarm_minute));
		} 
		// no alarm exist
		else {
			timepicker.setVisibility(View.VISIBLE);
			txt_time.setVisibility(View.GONE);
			
			btn.setText(R.string.lbl_set_alarm);
			btn.setTag("set");
			// Set Time
			int h = calendar.get(Calendar.HOUR_OF_DAY);
			int m = calendar.get(Calendar.MINUTE);
			setTime(h, m);
		}
	
		btn.setOnClickListener(this);
		
		/**
		 * Instantiate an AlarmManager
		 */
		alarm = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
		
		/**
		 * Check the Blocking Mode
		 */
		String[][] m = helper.getRecords("Select _blocking_mode, _blocking_hours from tbl_setting Where _id=1");
		mode = m[0][0];
		block_hrs = Integer.parseInt(m[0][1]);
		
	}
	
	
	/**
	 * Set the default time in the TimePicker view
	 * @param hour
	 * @param minute
	 */
	private void setTime(int hour, int minute){
		// update time picker
		timepicker.setCurrentHour(hour);
		timepicker.setCurrentMinute(minute);
	}


	@Override
	public void onClick(View v) {
		String get_pass = txtpass.getText().toString();
		/**
		 * Set Alarm
		 */
		if(v.getTag().equals("set")){
			if(get_pass.length() > 0){
				
				// update calendar
				int hour = timepicker.getCurrentHour();
				int minute = timepicker.getCurrentMinute();
						
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY, hour);
				calendar.set(Calendar.MINUTE, minute);
				calendar.set(Calendar.SECOND, 0);
								
				String[] columns = {"_hour","_minute","_password","_year","_month","_day","_time"};
				String[] values = {
						String.valueOf(hour), 
						String.valueOf(minute),
						get_pass,
						String.valueOf(calendar.get(Calendar.YEAR)),
						String.valueOf(calendar.get(Calendar.MONTH)),
						String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),
						sdf.format(calendar.getTimeInMillis())};
				
				helper.insert("tbl_alarm", columns, values);
				
				// Create a PendingIntent and then add it to Alarm manager
			    Intent intent = new Intent(this, ScheduledAlarm.class);
			    
			    // Tells Alarm Manager that if theres's already pending Intent with 12345 id cancel it
			    PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), 12345, intent, 
			    		PendingIntent.FLAG_CANCEL_CURRENT);
			    
			    // Set Single Alarm
			    //alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
			    
			    // Set Repeating Alarm every 1 minute
			    alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*1, pending);
				
			    // Enabled Receiver
			    sched_alarm.setEnabledThis(this);
			    
			    Toast.makeText(getApplicationContext(), "Alarm was Set "+ sdf.format(calendar.getTimeInMillis())  , Toast.LENGTH_LONG).show();
			    calendar = null;
				refresh();
			} else {
				//int hh = timepicker.
				Toast.makeText(getApplicationContext(), "Please enter a password.!" , Toast.LENGTH_LONG).show();
			}
		} 
		/**
		 * Cancel Alarm
		 */
		else if(v.getTag().equals("cancel")){
			int check = helper.getCount("tbl_alarm", "_id="+ alarm_id+" AND _password='"+get_pass+"' ");
			if(check > 0){
				helper.deleteRecords("tbl_alarm", "_id=?", new String[]{ alarm_id });
				
				try {
					Intent i = new Intent(this, ScheduledAlarm.class);
					PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), 12345, i, PendingIntent.FLAG_CANCEL_CURRENT);
					// Cancel Alarm
					alarm.cancel(pending);
					// Enabled Receiver
				    sched_alarm.setDisabledThis(this);
					firstTime = true;
					attempt = 2;
					Toast.makeText(getApplicationContext(), "Alarm was Canceled.!", Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				}
				
				refresh();
			} else {
				// Check the user attempts
				attempt-= 1;
				if(attempt <= 0){
					if(firstTime){
						/**
						 * Insert record to table block mode
						 */
						helper.insert("tbl_block_mode", new String[]{"_block_mode"}, new String[]{"all"});
						/**
						 * Set an Alarm to Unblock
						 * Request Code for Alarm Block: 502
						 */
						Alarm.setAlarm(this, mode, null, block_hrs, 502);
						
						// Send SMS from Emergency Contact
						SMSUtils.sendEmergencyContact(this, helper);
					}
					firstTime = false;
					Toast.makeText(this, "Opps you reached the limit, Contacts is now block for "+block_hrs+" hours.!" , Toast.LENGTH_LONG).show();
				} else {
					
					Toast.makeText(this, "Invalid Password, you have "+ attempt +" more attempt to retry.!" , Toast.LENGTH_LONG).show();
				}
				Log.e("[Attempt]", ""+ attempt);
			}
		}
	}
	
	/**
	 * Refresh
	 */
	private void refresh(){
		/**
		 * Refresh Activity
		 */
		txtpass.setText("");
		insertView();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		helper.close();
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		insertView();
	}
	
}












