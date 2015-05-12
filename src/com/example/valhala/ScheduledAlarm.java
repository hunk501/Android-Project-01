package com.example.valhala;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

public class ScheduledAlarm extends WakefulBroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Intent i = new Intent(context, AlarmReceiver.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		context.startActivity(i);
	}
	
	/**
	 * Enabled Receiver
	 * @param context
	 */
	public void setEnabledThis(Context context){
		ComponentName comp = new ComponentName(context, ScheduledAlarm.class);
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(comp, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
	}
	
	/**
	 * Disabled Receiver
	 * @param context
	 */
	public void setDisabledThis(Context context){
		ComponentName comp = new ComponentName(context, ScheduledAlarm.class);
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(comp, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
	}
}
