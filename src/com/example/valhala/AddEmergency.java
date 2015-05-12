package com.example.valhala;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEmergency extends Activity {

	private Button btnadd;
	private EditText txtphone;
	private EditText txtname;
	
	private DBHelper helper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_emergency);
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		txtphone = (EditText) findViewById(R.id.editText1);
		txtname = (EditText) findViewById(R.id.editText2);
		btnadd = (Button) findViewById(R.id.button1);
		btnadd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phone = txtphone.getText().toString();
				String name = txtname.getText().toString();
				if(name.length() > 0 && phone.length() > 0){
					int check = helper.getCount("tbl_emergency", "_phone_name='"+ name +"'");
					if(check <= 0){
						helper.insert("tbl_emergency", 
								new String[]{"_phone_number","_phone_name"}, 
								new String[]{phone, name});
						txtphone.setText("");
						txtname.setText("");
						Toast.makeText(getApplicationContext(), "Emergency Contact was Added.!", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getApplicationContext(), "Contact Name is already exists.!", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "All fields is required.!", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		// instantiate database
		helper = new DBHelper(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("[onPause]", "Done");
		helper.close();
		finish();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e("[onDestroy]", "Done");
		helper.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		if(id == android.R.id.home){
			finish();
		} else if(id == R.id.menu_back){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
