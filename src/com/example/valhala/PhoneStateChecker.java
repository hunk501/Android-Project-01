package com.example.valhala;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class PhoneStateChecker extends BroadcastReceiver{

	private OutgoingCallReceiver receiver = new OutgoingCallReceiver();
	
	@Override
	public void onReceive(Context context, Intent arg1) {
		
		CheckPhoneState checker = new CheckPhoneState(context);
		
		TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		// Listen
		tel.listen(checker, PhoneStateListener.LISTEN_CALL_STATE);
	}

	
	
	private class CheckPhoneState extends PhoneStateListener{
		
		private Context context;
		
		public CheckPhoneState(Context con){
			this.context = con;
		}
		
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			
			switch(state){
				case TelephonyManager.CALL_STATE_IDLE:
					// Enabled Receiver and Service
					receiver.setEnabledReceiver(context);
					receiver.setEnabledService(context);
					
					//Toast.makeText(context, "CALL_STATE_IDLE", Toast.LENGTH_SHORT).show();
					break;
					
				case TelephonyManager.CALL_STATE_OFFHOOK:
					//Toast.makeText(context, "CALL_STATE_HOOK", Toast.LENGTH_SHORT).show();
					break;
					
				case TelephonyManager.CALL_STATE_RINGING:
					//Toast.makeText(context, "CALL_STATE_RINGING", Toast.LENGTH_SHORT).show();
					break;
			}
		}
	}
	
}




