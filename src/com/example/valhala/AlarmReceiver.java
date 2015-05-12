package com.example.valhala;

import java.util.List;

import com.valhala.SMS.SMSUtils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AlarmReceiver extends Activity {
	
	private MediaPlayer player;
	private Button btnStop;
	private EditText txtpass;
	
	private DBHelper helper;
	private boolean isFirstTime = true;
	
	private String mode = null;
	private int block_hrs = 0;
	private NotificationManager nManager;
	private WakeLock wakelock;
	
	private ScheduledAlarm sched_alarm = new ScheduledAlarm();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_receiver);
		
		txtpass = (EditText) findViewById(R.id.editText1);
		
		btnStop = (Button) findViewById(R.id.button1);
		btnStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/**
				 * Check the password if match
				 */
				String pass = txtpass.getText().toString();
				int count = helper.getCount("tbl_alarm", "_password='"+ pass +"'");
				if(count > 0){
					helper.deleteRecords("tbl_alarm", "_password=?", new String[]{ pass });
					player.stop(); // Stop Alarm Sound					
					cancelAlarm(); // Cancel repeating alarm
					finish();
					Toast.makeText(getApplicationContext(), "Alarm is Off", Toast.LENGTH_LONG).show();
				} else {
					showMyDialog("Error", "Invalid Password, please try again.!", v);
				}
			}
		});
		
		// Setup NotificationManager
		nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		// Set PendingIntent
		Intent i = new Intent(this, MainActivity.class);
		PendingIntent pending = PendingIntent.getActivity(this, 12345, i,
						PendingIntent.FLAG_UPDATE_CURRENT);
		
		// Set notification message
		Notification notification = new Notification.Builder(this)
			.setContentTitle("Drunk Dial")
			.setContentText("Lasing ka na TAMA na yan.!")
			.setContentIntent(pending)
			.setSmallIcon(R.drawable.ic_launcher)
			.build();
		
		// Notify
		nManager.notify(12345, notification);
		
		// instantiate Database
		helper = new DBHelper(this);
		// Check blocking Mode
		String[][] m = helper.getRecords("Select _blocking_mode, _blocking_hours from tbl_setting Where _id=1");
		mode = m[0][0];
		block_hrs = Integer.parseInt(m[0][1]);
		
		// Play sound automatically
		playSound(this, getAlarmUri());
		
		KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("mykeyguardlock");
		kl.disableKeyguard();
		
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		
		wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "mywakelock");
		wakelock.acquire(1000*30);
		
		Log.e("[AlarmReceiver]", "Closed.");
	}
	
	/**
	 * Cancel Alarm
	 */
	private void cancelAlarm(){
		Intent i = new Intent(this, ScheduledAlarm.class);
		PendingIntent pending = PendingIntent.getBroadcast(this, 12345, i, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
		// Cancel Alarm
		alarm.cancel(pending);
		
		// Enabled Receiver
	    sched_alarm.setDisabledThis(this);
	}
	
	/**
	 * Play Alarm Sounds
	 * @param context
	 * @param alert
	 */
	private void playSound(Context context, Uri alert){
		player = new MediaPlayer();
		try {
			player.setDataSource(context, alert);
			
			final AudioManager audiomanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			if(audiomanager.getStreamVolume(AudioManager.STREAM_ALARM) !=0){
				player.setAudioStreamType(AudioManager.STREAM_ALARM);
				player.prepare();
				player.start();
			} else {
				Toast.makeText(getApplicationContext(), "Volume Error", Toast.LENGTH_LONG).show();
				player.setAudioStreamType(AudioManager.STREAM_ALARM);
				player.prepare();
				player.start();
			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Error: "+ e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	
	/**
	 * Get Alarm Resource
	 * @return
	 */
	private Uri getAlarmUri(){
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		if(alert == null){
			alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			if(alert == null){
				alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			}
		}
		return alert;
	}
	
	/**
	 * Show Dialog Message
	 * @param title
	 * @param msg
	 * @param v
	 */
	private void showMyDialog(String title, String msg, View v){
		AlertDialog.Builder dialog = new AlertDialog.Builder(AlarmReceiver.this);
		dialog.setTitle(title);
		dialog.setMessage(msg);
		dialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendSMS(); // send SMS
				if(isFirstTime){
					/**
					 * Insert record to table block mode
					 */
					helper.insert("tbl_block_mode", new String[]{"_block_mode"}, new String[]{"all"});
					/**
					 * Set an Alarm to Unblock
					 * Request Code for Alarm Block: 502
					 */
					Alarm.setAlarm(AlarmReceiver.this, mode, null, block_hrs, 502);
				}
				isFirstTime = false;
				dialog.cancel();
			}
		});
		dialog.setCancelable(false);
		AlertDialog alert = dialog.create();
		alert.show();
	}
	
	/**
	 * Send SMS to all Emergency Contacts
	 */
	private void sendSMS(){
		
		SMSUtils.sendEmergencyContact(this, helper);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//player.stop(); // Stop Alarm Sound
		//finish();
		Log.e("[AlarmReceiver]", "Closed.");
	}
}














