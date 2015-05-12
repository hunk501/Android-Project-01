package com.valhala.SMS;

import java.util.List;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.valhala.DBHelper;
import com.example.valhala.TodoEmergency;

public class SMSUtils {
	
	/**
	 * Send SMS from all Emergency Contacts
	 * @param context
	 * @param helper
	 */
	public static void sendEmergencyContact(Context context, DBHelper helper){
		List<TodoEmergency> lists = helper.showAllEmergency();
		StringBuilder number = new StringBuilder();
		if(lists != null){
			int lop = 0;
			for(TodoEmergency list : lists){
				String num = list.getPhoneNumber();
				// remove the first number which is 0 with +63 Country Code
				String new_contact = "+63"+num.substring(1);
				
				number.append(new_contact);
				if(lop > 1){
					number.append(";");
				}
				lop++;
				Log.e("[EmergencyContact]", new_contact);
			}
			
			String msg = "From: Drunk Dial Application, mga pre yung tropa niyo lasing na.!";
			SmsManager sms = SmsManager.getDefault();
			try {
				sms.sendTextMessage(number.toString(), null, msg, null, null);
				Log.e("[SMS]", "Message sent to all Emergeny Contacts.");
			} catch (Exception e) {
				Log.e("[SMS]", e.getMessage());
				Toast.makeText(context.getApplicationContext(), "Message sending failed.!", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(context.getApplicationContext(), "There's no Emergency Contact to be notify.!", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	
}
