package com.example.valhala;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class TabHome extends Activity {

	private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
	
	private RadioButton single_btn, all_btn;
	private EditText txtpass, txthours;
	private Button btn_save;
	private boolean setting = false;
	private DBHelper helper = new DBHelper(this);
	
	private String b_mode = null;
	private String b_password = null;
	private int b_hrs = 0;
	private boolean isBlockAll = false;
	private Handler handler = new Handler();
	
	
	private void setUp(){
		try {
			// Check if block all present
			int c = helper.getCount("tbl_block_mode", "_block_mode='all'");
			isBlockAll = (c > 0) ? true : false;
			Log.e("[isBlockAll]", ""+c);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_home);
		
		btn_save = (Button) findViewById(R.id.button1);
		single_btn = (RadioButton) findViewById(R.id.radiobutton1);
		all_btn = (RadioButton) findViewById(R.id.radiobutton2);
		txtpass = (EditText) findViewById(R.id.edittext1);
		txthours = (EditText) findViewById(R.id.edittext2);
		
		setUp();
		
		single_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					txtpass.setEnabled(false);
				}
			}
		});
		all_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					txtpass.setEnabled(true);
					txtpass.setFocusable(true);
				}
			}
		});
		btn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isBlockAll){
					ShowDialog d = new ShowDialog(TabHome.this, "Message", "Unble to change Settings at this time, All Contacts is Currently Blocked this will be automatically removed by the System.!");
					d.showDialog();
				} else {
					String pass = txtpass.getText().toString();
					String txth = (txthours.getText().toString().length() > 0) ? txthours.getText().toString() : "0";			
					int h = Integer.parseInt(txth);
					if(single_btn.isChecked()){
						if(isHourOk(h)){
							/**
							 * Save Setting
							 */
							saveSetting("single", pass, h);
						} else{
							Toast.makeText(getApplicationContext(), "Hours is invalid, must be 1-24 only.!", Toast.LENGTH_LONG).show();
						}
					}
					else if(all_btn.isChecked()){
						if(pass.length() > 0){
							if(isHourOk(h)){
								/**
								 * Save Setting
								 */
								saveSetting("all", pass, h);
							} else{
								Toast.makeText(getApplicationContext(), "Hours is invalid, must be 1-24 only.!", Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(getApplicationContext(), "Password is Required.!", Toast.LENGTH_LONG).show();
						}
					}
				}
			}
		});
		
		checkSetting();
		handler.post(new UpdateGUI());
	}
	
	
	private void checkSetting(){
		int count = helper.getCount("tbl_setting", "_id=1");
		if(count > 0){
			String[][] result = helper.getRecords("Select * from tbl_setting");
			for(int x=0; x < result.length; x++){
				b_mode = result[x][1];
				b_password = result[x][2];
				b_hrs = Integer.parseInt(result[x][3]);
				Log.e("[res]", b_mode);
				Log.e("[res]", b_password);
				Log.e("[res]", ""+b_hrs);
			}
			setting = true;
		}
		Log.e("[onresume]", "onresume");
	}
	
	/**
	 * Save Settings
	 * @param blocking_mode
	 * @param password
	 * @param hours
	 */
	private void saveSetting(String blocking_mode, String password, int hours){
		int count = helper.getCount("tbl_setting", "_id=1");
		if(count > 0){
			Log.e("[Setting]", "Existing Count: "+ count);
			String sql = "UPDATE tbl_setting SET _blocking_mode='"+blocking_mode+"',_blocking_password='"+password+"',_blocking_hours='"+hours+"' where _id=1";
			helper.update(sql);
		} 
		else {
			Log.e("[Setting]", "New");
			helper.insert("tbl_setting", 
					new String[]{"_blocking_mode","_blocking_password","_blocking_hours"}, 
					new String[]{blocking_mode, password, String.valueOf(hours)});
		}
		helper.close();
		Toast.makeText(getApplicationContext(), "Settings has been Updated.!", Toast.LENGTH_LONG).show();
	}
	
	
	/**
	 * Check the Time if it is valid
	 * @param h
	 * @return
	 */
	private boolean isHourOk(int h){
		return (h >= 1 && h <= 24) ? true : false;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		helper.close();
	}
	
	
	/**
	 * Update GUI
	 * @author Dennis
	 *
	 */
	class UpdateGUI implements Runnable{

		@Override
		public void run() {
			/**
			 * If Setting is already set Update GUI
			 */
			if(setting){
				if(b_mode.equalsIgnoreCase("single")){
					single_btn.setChecked(true);
				} else {
					all_btn.setChecked(true);
					txtpass.setText(b_password);
				}
				txthours.setText(String.valueOf(b_hrs));
				//Toast.makeText(getApplicationContext(), "OK GUI", Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
}
