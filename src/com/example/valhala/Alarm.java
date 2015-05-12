package com.example.valhala;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Alarm {

	
	/**
	 * Set an Alarm
	 * @param context
	 * @param mode
	 * @param phone
	 * @param block_hrs
	 * @param alarm_code
	 */
	public static void setAlarm(Context context, String mode, String phone, int block_hrs, int alarm_code){
		
		Intent i = new Intent(context, BlockContactReceiver.class);
		if(phone != null){
			i.putExtra("_phone_number", phone);
		}
		i.putExtra("_mode", mode);
		
		PendingIntent pending = PendingIntent.getActivity(context.getApplicationContext(), alarm_code, i, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
		
		// Set Time
		Calendar cal = Calendar.getInstance();
		int h = cal.get(Calendar.HOUR_OF_DAY) + block_hrs;
		cal.set(Calendar.HOUR_OF_DAY, h);
		cal.set(Calendar.SECOND, 0);
		
//		int h = cal.get(Calendar.MINUTE) + block_hrs;
//		cal.set(Calendar.MINUTE, h);
//		cal.set(Calendar.SECOND, 0);
		
		
		
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pending);
		
	}
	
	
	
}
