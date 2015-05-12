package com.example.valhala;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class BlockContactReceiver extends Activity	 {

	private TextView txt1;
	private DBHelper helper;
	
	private NotificationManager nManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.block_contac_receiver);
		
		Bundle bundle = getIntent().getExtras();
		String phone = bundle.getString("_phone_number");
		String mode = bundle.getString("_mode");
		
		helper = new DBHelper(this);
		// Delete from Table Block
		helper.deleteRecords("tbl_block", "_phone_number=?", new String[]{phone});
		// Delete from Table Block Mode
		helper.deleteRecords("tbl_block_mode", "_block_mode=?", new String[]{"all"});
		
		txt1 = (TextView) findViewById(R.id.textView1);
		//txt1.setText(phone);
		
		// Setup NotificationManager
		nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		// Set PendingIntent
		Intent i = new Intent(this, MainActivity.class);
		PendingIntent pending = PendingIntent.getActivity(this, 502, i,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// Set notification message
		Notification notif = new Notification.Builder(this)
			.setContentTitle("Drunk Dial")
			.setContentText("Lasing ka na TAMA na yan.!")
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentIntent(pending)
			.build();
		
		// Notify
		nManager.notify(502, notif);
		
		displayDialog(phone, mode);
	}
	
	
	private void displayDialog(String phone, String mode){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setIcon(R.drawable.info);
		dialog.setTitle("Message");
		if(mode.equalsIgnoreCase("single")){
			dialog.setMessage("This Contact Number '"+ phone +"' is now Unblock.!");
		} else {
			dialog.setMessage("All Contacts is now Unblock.!");
		}
		dialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				finish();
			}
		});
		
		dialog.setCancelable(false);
		
		AlertDialog alert = dialog.create();
		alert.show();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		finish();
	}
}
