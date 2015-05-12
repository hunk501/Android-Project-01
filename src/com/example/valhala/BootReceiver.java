package com.example.valhala;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This will be fired by the System when the System has completely Reboot
 * @author Dennis
 *
 */
public class BootReceiver extends BroadcastReceiver {

	private OutgoingCallReceiver receiver = new OutgoingCallReceiver();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		
		/**
		 * Enabled Broadcast Receiver
		 */
		
		receiver.setEnabledReceiver(context); // Broadcast receiver
		receiver.setEnabledService(context);  // Services
		
	}
	
}
