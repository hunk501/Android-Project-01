package com.example.valhala;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class TabContacts extends Activity {

	private ListView list;
	private ListAdapter adapter;  // Customize Adapter
	
	private ArrayList<TodoContact> alist = new ArrayList<TodoContact>();
	private boolean isEmpty = true;
	private DBHelper helper;
	private Contacts contacts;
	
	private String password;
	private boolean isUnlock = false; // Action
	private String selected_contact;
	private int selected_position;
	
	private boolean isBlockAll = false;
	private String[][] mode = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_contacts);
		
		//insertView();
		load();
	}
	
	
	private void load(){
		MyLoader m = new MyLoader(this);
		m.execute();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		helper.close();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUp();
		Log.e("[onResume]", "on resume");
	}
	
	
	/**
	 * Show Dialog
	 * @param title
	 */
	private void displayDialog(String title){
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setIcon(R.drawable.ic_launcher);
		dialog.setTitle(title);
		
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.contacts_dialog, null);
		
		final EditText txtpass = (EditText) view.findViewById(R.id.txtPassBlock);
		
		dialog.setView(view);
		dialog.setCancelable(false);
		dialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String pass = txtpass.getText().toString();
				if(pass.length() > 0){
					validate(pass);
				} else {
					Toast.makeText(getApplicationContext(), "Password is required.!", Toast.LENGTH_LONG).show();
				}
			}
		});
		dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		AlertDialog alert = dialog.create();
		alert.show();
	}
	
	
	/**
	 * This will be fires when the dialog click positive or negative
	 * @param pass
	 */
	private void validate(String pass){
		password = pass;
		// Check Action
		if(isUnlock){
			/**
			 * Unlock Block Contacts
			 */
			int check = helper.getCount("tbl_password", "_phone_number='"+ selected_contact +"' AND _password='"+password+"' ");
			if(check > 0){
				// delete from database
				delete();
				Toast.makeText(getApplicationContext(), "Contact has been Unblock.!", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "Invalid Password.!", Toast.LENGTH_LONG).show();
			}
		} 
		else {
			if(pass.equalsIgnoreCase("canceled")){
				//Toast.makeText(getApplicationContext(), "Password was updated.!", Toast.LENGTH_LONG).show();
			} else {
				/**
				 * Set Contact Password
				 */
				int check = helper.getCount("tbl_password", "_phone_number='"+ selected_contact +"'");
				if(check > 0){
					helper.updatePassword(pass, selected_contact);
					Toast.makeText(getApplicationContext(), "Password was updated.!", Toast.LENGTH_LONG).show();
				} else {
					helper.insertPassword(selected_contact, pass);
					Toast.makeText(getApplicationContext(), "Password was set.!", Toast.LENGTH_LONG).show();
				}
			}
		}
	}
	
	
	/**
	 * Delete from database
	 */
	private void delete(){
		helper.deleteRecords("tbl_block", "_phone_number=?", new String[]{selected_contact});
		/**
		 * Update List
		 */
		alist.clear();
		update();
	}
	
	/**
	 * Update List
	 */
	private void update(){
		insertView();
		//finish();
		//startActivity(getIntent());  // Refresh itself
	}
	
	/**
	 * Insert a View
	 */
	private void insertView(){
		// Instantiate Contacts
		contacts = new Contacts(this);

		list = (ListView) findViewById(R.id.contactList);

		List<TodoContact> lists = contacts.getContacts();
		
		alist.addAll(contacts.getContacts());
		adapter = new ListAdapter(this, alist);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				if(isBlockAll){
					ShowDialog d = new ShowDialog(TabContacts.this, "Message", "All Contacts is Currently Blocked this will be automatically removed by the System.!");
					d.showDialog();
				} 
				else {
					// check the Blocking mode settings
					if(mode[0][0].equalsIgnoreCase("all")){
						ShowDialog d = new ShowDialog(TabContacts.this, "Message", "Contacts Blocking Mode is Set to All, you cannot set a password individually.!, try to change the blocking mode settings.!");
						d.showDialog();
					} else {
						TodoContact c = alist.get(pos);
						if (c.isEnabled()) { // Contact was not in the blocked list
							isUnlock = true; // Set the action to Unlock Contact
							selected_contact = c.getContact();
							selected_position = pos;
							displayDialog("Unblock Contact");
						} else { // Contact was in the blocked List
							isUnlock = false; // Set the action to Set Password
							selected_contact = c.getContact();
							selected_position = pos;
							displayDialog("Set Password");
							adapter.notifyDataSetChanged();
						}
					}
				}
			}
		});

		// Open database helper
		helper = new DBHelper(this);
		setUp();
	}
	
	
	private void setUp(){
		try {
			int c = helper.getCount("tbl_block_mode", "_block_mode='all'");
			isBlockAll = (c > 0) ? true : false;
			mode = helper.getRecords("Select _blocking_mode from tbl_setting where _id=1");
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Loader
	 * @author Dennis
	 *
	 */
	private class MyLoader extends AsyncTask<String, String, String>{
		
		private Context context;
		private ProgressDialog pDialog;
		
		public MyLoader(Context con){
			this.context = con;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			pDialog = new ProgressDialog(context);
			pDialog.setTitle("Contacts");
			pDialog.setMessage("loading please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			Log.d("[Loader]", "Initializing");
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			runOnUiThread(new Runnable(){
				@Override
				public void run() {
					insertView();
				}				
			});
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
		}
		
	}
	
}












