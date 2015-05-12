package com.example.valhala;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

public class OutgoingCallReceiver extends WakefulBroadcastReceiver {

	private DBHelper helper;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		/**
		 * When the User try to Call,
		 * Intercepts the NEW_OUTGOING_CALL intent, so that we can put our new task containing a password
		 */
		try {
			
			String phone_number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			helper = new DBHelper(context);
			
			/**
			 * Get the Blocking Hours
			 */
			String[][] blocking_hours = helper.getRecords("Select _blocking_mode, _blocking_hours from tbl_setting Where _id=1");
			int hrs = Integer.parseInt(blocking_hours[0][1]);
			
			/**
			 * Check the Blocking Mode
			 */			
			int count2 = helper.getCount("tbl_block_mode", "_block_mode='all'");
			if(count2 > 0){
				// It Seems that ALl Contact is Block
				setResultData(null);
				Toast.makeText(context, "All Contacts is Blocked for "+ hrs +" hours.!", Toast.LENGTH_LONG).show();
			} 
			else {
				int count = helper.getCount("tbl_block", "_phone_number='"+ phone_number +"'");
				/**
				 * Check the phone number if exists on the table block
				 */
				if(count > 0){
					setResultData(null);
					Toast.makeText(context, "This Contact is currently Blocked for "+ hrs +" hours.!", Toast.LENGTH_LONG).show();
				} 
				else {
					
					/**
					 * Check the Blocking Mode
					 */
					String[][] mode = helper.getRecords("Select _blocking_mode from tbl_setting Where _id=1");
					if(mode[0][0].equalsIgnoreCase("all")){
						/**
						 * Check if this Contact has an password set
						 */
						int check = helper.getCount("tbl_password", "_phone_number='"+ phone_number +"' ");
						if(check > 0 ){ // Exists
							setResultData(null);
							startCustomService(context, intent);
						} else {
							setResultData(null);
							startCustomService(context, intent);
						}
					} else {
						/**
						 * Check if this Contact has an password set
						 */
						int check = helper.getCount("tbl_password", "_phone_number='"+ phone_number +"' ");
						if(check > 0 ){ // Exists
							setResultData(null);
							
							startCustomService(context, intent);
						}
					}
				}
			}
			
			
		} catch (Exception e) {
			Toast.makeText(context, "OutgoingCallReceiver: "+ e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	
	private void startCustomService(Context context, Intent intent){
		/**
		 * Start our Service to handle for Running a new Task with GUI
		 */
		ComponentName comp = new ComponentName(context.getPackageName(), OutgoingCallService.class.getName());
		
		// start the service
		startWakefulService(context, (intent.setComponent(comp)));
	}
	
	
	/**
	 * This will Enabled the OutgoingReceiver
	 * @param context
	 */
	public void setEnabledReceiver(Context context){
		
		ComponentName comp = new ComponentName(context, OutgoingCallReceiver.class);
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(comp, 
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 
				PackageManager.DONT_KILL_APP);
		
		//Toast.makeText(context, "Receiver was Enabled", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * This will Disabled the OutgoingReceiver
	 * @param context
	 */
	public void setDisabledReceiver(Context context){
		
		ComponentName comp = new ComponentName(context, OutgoingCallReceiver.class);
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(comp, 
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 
				PackageManager.DONT_KILL_APP);
		
		//Toast.makeText(context, "Receiver was Disabled", Toast.LENGTH_SHORT).show();
	}
	
	
	/**
	 * Set Enabled Service
	 * @param context
	 */
	public void setEnabledService(Context context){
		
		ComponentName comp = new ComponentName(context, OutgoingCallService.class);
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(comp, 
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 
				PackageManager.DONT_KILL_APP);
		
		//Toast.makeText(context, "Service was Enabled", Toast.LENGTH_SHORT).show();
		
	}
	
	
	/**
	 * This will Disabled the Service
	 * @param context
	 */
	public void setDisabledService(Context context){
		
		ComponentName comp = new ComponentName(context, OutgoingCallService.class);
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(comp, 
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 
				PackageManager.DONT_KILL_APP);
		
		//Toast.makeText(context, "Service was Disabled", Toast.LENGTH_SHORT).show();
	}
	
	
}







