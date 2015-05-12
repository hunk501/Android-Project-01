package com.example.valhala;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class TabEmergency extends Activity {

	private ListEmergency adapter;
	private Button btnAdd;
	private ListView listview;
	private ArrayList<TodoEmergency> alist = new ArrayList<TodoEmergency>();
	private DBHelper helper;
	
	private void insertView(){
		
		// Instantiate Database
		helper = new DBHelper(this);

		listview = (ListView) findViewById(R.id.listView1);
		btnAdd = (Button) findViewById(R.id.button1);
		btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), AddEmergency.class);
				startActivity(intent);
			}
		});
		
		alist = helper.showAllEmergency();
		adapter = new ListEmergency(this, alist);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				TodoEmergency todo = alist.get(position);
				Log.e("[Emergency]", todo.getId());
				Log.e("[Emergency]", todo.getName());
				Log.e("[Emergency]", todo.getPhoneNumber());
				// show dialog
				displayDialog(new String[]{todo.getId(),todo.getName()});
			}
		});
	}
	
	/**
	 * Show Dialog
	 */
	private void displayDialog(final String[] data){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Emergency Contact");
		dialog.setMessage("Are you wish to delete this '"+ data[1] +"' Emergency Contacts. Continue.?");
		dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				helper.deleteRecords("tbl_emergency", "_id=?", new String[]{data[0]});
				// update view
				insertView();
			}
		});
		dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		dialog.setCancelable(false);
		AlertDialog alert = dialog.create();
		alert.show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_emergency);
		
		insertView();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		insertView();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		helper.close();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		helper.close();
	}
}
