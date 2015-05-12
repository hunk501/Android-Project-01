package com.example.valhala;

import java.util.Calendar;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class AddEvents extends Activity {
	
	private TableLayout tl;
	private LayoutParams lp;
	private CheckBox chk;
	private EditText txttitle, txt_drinking_fact;
	
	private String event_id = null;
	private String event_date = null;
	private DBHelper helper;
	
	private Calendar cal = Calendar.getInstance();
	
	private Contacts contact;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_events);
		
		contact = new Contacts(this);
		
		// Table Layout
		tl = (TableLayout) findViewById(R.id.tablelayout01);
		// Table Layout Params
		lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		
		txttitle = (EditText) findViewById(R.id.editText1);
		txt_drinking_fact = (EditText) findViewById(R.id.editText2);
		
		addTableRow();
		
		helper = new DBHelper(this);
		
		Bundle b = getIntent().getExtras();
		event_id = randomGenerator();
		event_date = b.getString("_date");
		
		Log.e("[random]", event_id);
	}

	private void addTableRow(){
		//String[] names = {"dennis","john","sam","marian","hazel"};
		List<TodoContact> lists = contact.getContacts();
		if(lists != null){
			int i=0;
			for(TodoContact todo : lists){
				TableRow tr = new TableRow(this);
				tr.setLayoutParams(lp);
				// Add Check box
				chk = new CheckBox(this);
				chk.setText(todo.getName());
				chk.setId(i);
				chk.setOnClickListener(getCheckbox(chk));
				// add to Table row
				tr.addView(chk);
				// add table row to table layout
				tl.addView(tr);
				i++;
			}
		}
//		for(int i=0; i < names.length; i++){
//			TableRow tr = new TableRow(this);
//			tr.setLayoutParams(lp);
//			// Add Check box
//			chk = new CheckBox(this);
//			chk.setText(names[i]);
//			chk.setId(i);
//			chk.setOnClickListener(getCheckbox(chk));
//			// add to Table row
//			tr.addView(chk);
//			// add table row to table layout
//			tl.addView(tr);
//		}
	}
	
	View.OnClickListener getCheckbox(final CheckBox btn){
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(btn.isChecked()){
					// Insert
					helper.insert("tbl_buddy", new String[]{"_event_id","_buddy_name"}, new String[]{event_id, btn.getText().toString()});
				} else {
					// Delete
					helper.deleteRecords("tbl_buddy", "_event_id=? AND _buddy_name=?", new String[]{event_id,btn.getText().toString()});
				}
				helper.close();
			}
		};
	}
	
	/**
	 * Save Events
	 */
	private void menuSave(){
		String title = txttitle.getText().toString();
		String drinking_facts = txt_drinking_fact.getText().toString();
		if(title.length() > 0){
			//String _date = cal.get(Calendar.YEAR)+"-"+ cal.get(Calendar.MONTH)+"-"+ cal.get(Calendar.DAY_OF_MONTH);
			Log.e("_date", event_date);
			helper.insert("tbl_events", new String[]{"_event_id","_event_title","_event_date","_event_facts"}, new String[]{event_id,title,event_date,drinking_facts});
			
			finish();
		} else {
			Toast.makeText(getApplicationContext(), "Please enter Event Title", Toast.LENGTH_LONG).show();
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_events, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
			case R.id.action_cancel:
				Log.e("[MenuItem]", "Cancel");
				finish();
				break;
			case R.id.action_save:
				menuSave();
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	private String randomGenerator(){
		return 
				cal.get(Calendar.YEAR) +""+ 
				cal.get(Calendar.MONTH) +""+ 
				cal.get(Calendar.DAY_OF_WEEK) +""+
				cal.get(Calendar.HOUR_OF_DAY) +""+
				cal.get(Calendar.MINUTE) +""+
				cal.get(Calendar.SECOND);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		finish();
		super.onPause();
	}
}
