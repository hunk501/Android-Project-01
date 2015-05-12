package com.example.valhala;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * When the System fires an Call Intent, this application well be forced to Start
 * @author Dennis
 *
 */
public class BlockOutgoingCalls extends Activity implements OnClickListener {

	private EditText txtpass;
	private String phone_number;
	private Button btnunlock;
	
	private OutgoingCallReceiver receiver = new OutgoingCallReceiver();
	
	private int level = 2; // Set Error Level
	private boolean isBlock = false;
	private String mode = null;
	private int block_hrs = 0;
	private DBHelper helper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.block_outgoing_calls);
		
		txtpass = (EditText) findViewById(R.id.editText1);
		
		btnunlock = (Button) findViewById(R.id.button1);
		btnunlock.setOnClickListener(this);
		
		/**
		 * Get Extras from the Intent
		 */
		Bundle bundle = getIntent().getExtras();
		phone_number = bundle.getString("_phone_number");
		
		// Instantiate Database Helper
		helper = new DBHelper(this);
		// Check blocking Mode
		String[][] m = helper.getRecords("Select _blocking_mode, _blocking_hours from tbl_setting Where _id=1");
		mode = m[0][0];
		block_hrs = Integer.parseInt(m[0][1]);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.button1){
			if(isBlock){
				Toast.makeText(getApplicationContext(), "Contact is now Block for 2 hours.!", Toast.LENGTH_LONG).show();
			} else {
				String pass = txtpass.getText().toString();
				/**
				 * Check the Blocking Mode
				 */
				if(mode.equalsIgnoreCase("all")){ // All Block
					boolean isMatched = helper.checkPassword2(pass);
					if(isMatched){
						startCalling();
					} else {
						decrementLevel("all");
					}
				} 
				else { // Single Block
					/**
					 * Get Password
					 */
					boolean isMatched = helper.checkPassword(phone_number, pass);
					if(isMatched){
						startCalling();
					}
					else {
						decrementLevel("single");
					}
				}
			}
			// Clear Text
			txtpass.setText("");
		}
	}
	
	/**
	 *  Decrement
	 */
	private void decrementLevel(String mode){
		level--; // Decrement Level
		if(level <= 0){
			if(mode.equalsIgnoreCase("single")){
				helper.insertData("tbl_block", phone_number);
			} else {
				helper.insert("tbl_block_mode", new String[]{"_block_mode"}, new String[]{"all"});
			}
			
			showDialog("Invalid Password you have reached the Limit, Contacts is now Blocked for "+ block_hrs +" hours.!", mode);
		} else {
			showDialog("Invalid Password you have "+ level +" more Attempt to Retry your password.!", mode);
		}
	}
	
	/**
	 * Start A Call
	 */
	private void startCalling(){
		// Disabled Receiver and Service
		receiver.setDisabledReceiver(getApplicationContext());
		receiver.setDisabledService(getApplicationContext());
		// start phone call
		
		// Start a Phone Call Intent
		Intent i = new Intent(Intent.ACTION_CALL);
		i.setData(Uri.parse("tel:"+ phone_number));
		startActivity(i);
		
		finish(); // close activity
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		finish();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		helper.close();
		finish();
	}
	
	/**
	 * Show Dialog
	 */
	private void showDialog(String msg, final String mode){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(msg);
		dialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(level <= 0){
					isBlock = true;
					setAlarm(phone_number, mode);
					Toast.makeText(getApplicationContext(), "Contact is now Block for "+ block_hrs +" hours.!", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		dialog.setCancelable(false);
		
		AlertDialog alert = dialog.create();
		alert.setTitle("Message");
		alert.show();
	}
	
	/**
	 * This will set an Alarm for Block contacts
	 * Request Code for Alarm Block: 501
	 * @param phone
	 */
	private void setAlarm(String phone, String mode){
		
		Alarm.setAlarm(BlockOutgoingCalls.this, mode, phone, block_hrs, 501);
		
	}
	
}
















