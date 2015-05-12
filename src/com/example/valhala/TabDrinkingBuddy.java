package com.example.valhala;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class TabDrinkingBuddy extends Activity {

	private ImageButton btn_prev, btn_next;
	private Button btnprev, btnnext;
	private TextView txtmonth;
	
	int day = 0, month = 0, year = 0, current_day = 0;
	public int firstDay = Calendar.SUNDAY;
	private TextView btn;
	private Boolean[] isEvent = new Boolean[32];
	private int[] resDaysSun = { R.string.sunday, R.string.monday,
			R.string.tuesday, R.string.wednesday, R.string.thursday,
			R.string.friday, R.string.saturday };
	private int[] resDaysMon = { R.string.monday, R.string.tuesday,
			R.string.wednesday, R.string.thursday, R.string.friday,
			R.string.saturday, R.string.sunday };
	private String[] days = new String[7];

	private int[] monthIds = { R.string.january, R.string.february,
			R.string.march, R.string.april, R.string.may, R.string.june,
			R.string.july, R.string.august, R.string.september,
			R.string.october, R.string.november, R.string.december };

	private String[] months = new String[12];

	Calendar cal, prevCal, today; // today will be used for setting a box around
									// today's date
	private TextView tv;
	int selected_day = 0;
	
	private TableLayout tl;

	private LinearLayout customlayout;
	private DBHelper helper;
	
	private TableLayout tl1;
	private TableLayout tl2;
	private LayoutParams lp;
	private EditText txt_drinking_facts;
	private View selected_current_date = null;
	private View prev_date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_drinking_buddy);
		
		insertView();
	}
	
	private void insertView(){
//		btn_prev = (ImageButton) findViewById(R.id.imageButton2);
//        btn_prev.setTag("<");
//        btn_prev.setBackgroundResource(R.drawable.button_design);
//        btn_prev.setOnClickListener(changeMonth);
        
        btnprev = (Button) findViewById(R.id.imageButton2);
        btnprev.setTag("<");
        btnprev.setOnClickListener(changeMonth);
        
        btnnext = (Button) findViewById(R.id.imageButton1);
        btnnext.setTag(">");
        btnnext.setOnClickListener(changeMonth);
        
//        btn_next = (ImageButton) findViewById(R.id.imageButton1);
//        btn_next.setTag(">");
//        btn_next.setOnClickListener(changeMonth);
        
        txtmonth = (TextView) findViewById(R.id.txtMonth);
        txtmonth.setTextColor(Color.WHITE);
        
        tl = (TableLayout) findViewById(R.id.tableLayout1);
        tl.setStretchAllColumns(true);
        
        customlayout = (LinearLayout) findViewById(R.id.customLayout); // Custom Layout
        
        today = Calendar.getInstance();// get current date and time's instance
		today.clear(Calendar.HOUR);// remove the hour,minute,second and
									// millisecond from the today variable
		today.clear(Calendar.MINUTE);
		today.clear(Calendar.SECOND);
		today.clear(Calendar.MILLISECOND);
		if (firstDay == Calendar.MONDAY)
			today.setFirstDayOfWeek(Calendar.MONDAY);
		cal = (Calendar) today.clone();// create exact copy as today for dates
										// display purpose.
		Resources r = getResources();
		for(int i=0; i < 12; i++){
			months[i] = r.getString(monthIds[i]);
		}
		
		// DBHelper
		helper = new DBHelper(this);
		
		// Display Months
		displayMonths();
	}
	
	/**
	 * Display Calenda Months
	 */
	public void displayMonths(){

    	Resources r = getResources();
    	String tempDay;
		for (int i = 0; i < 7; i++) {
			if (firstDay == Calendar.MONDAY)
				tempDay = r.getString(resDaysMon[i]);
			else
				tempDay = r.getString(resDaysSun[i]);
			days[i] = tempDay.substring(0, 3);
		}

		int firstDayOfWeek, prevMonthDay, nextMonthDay, week;
		cal.set(Calendar.DAY_OF_MONTH, 1); // Set date = 1st of current month so
											// that we can know in next step
											// which day is the first day of the
											// week.
		firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1; // get which day is
															// on the first date
															// of the month
		if (firstDay == Calendar.MONDAY) {
			firstDayOfWeek--;
			if (firstDayOfWeek == -1)
				firstDayOfWeek = 6;
		}
		week = cal.get(Calendar.WEEK_OF_YEAR) - 1; // get which week is the
													// current week.
		if (firstDayOfWeek == 0 && cal.get(Calendar.MONTH) == Calendar.JANUARY)
			week = 1;
		if (week == 0)
			week = 52;

		prevCal = (Calendar) cal.clone(); // create a calendar item for the
											// previous month by subtracting
		prevCal.add(Calendar.MONTH, -1); // 1 from the current month

		// get the number of days in the previous month to display last few days
		// of previous month
		prevMonthDay = prevCal.getActualMaximum(Calendar.DAY_OF_MONTH) - firstDayOfWeek + 1;
		nextMonthDay = 1; // set the next month counter to date 1
		
		/**
		 * -----  Add Headers ------------------
		 */
		TableRow tr = new TableRow(this);
    	tr.setWeightSum(0.7f);
    	for(int i=0; i < 7; i++){
    		String tempday;
    		if(firstDay == Calendar.MONDAY){
    			tempday = r.getString(resDaysMon[i]);
    		} else {
    			tempday = r.getString(resDaysSun[i]);
    		}
    		days[i] = tempday.substring(0, 3);
    		
    		TextView tview = new TextView(this);
    		tview.setBackgroundResource(R.drawable.calheader);
    		tview.setPadding(10, 3, 10, 3);
    		tview.setTextColor(Color.parseColor("#9C9A9D"));
    		tview.setText(days[i]);
    		tview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
    		tview.setGravity(Gravity.CENTER);
    		// Add to Row
    		tr.addView(tview);
    	}
    	tl.addView(tr);
    	
    	txtmonth.setText(months[cal.get(Calendar.MONTH)]+" "+cal.get(Calendar.YEAR));
    	
		/**
		 * ----- Add Days Label -------------------------
		 */
		int day = 1;
		for (int i = 0; i < 6; i++) {
			if (day > cal.getActualMaximum(Calendar.DAY_OF_MONTH))
				break;
			// this loop is used to fill out the days in the i-th row in the
			// calendar
			TableRow tr2 = new TableRow(this);
			tr2.setWeightSum(0.7f);
			for (int j = 0; j < 7; j++) {				
				btn = new TextView(this);
				// btn.setLayoutParams(lp);
				btn.setBackgroundResource(R.drawable.rectgrad);
				btn.setGravity(Gravity.CENTER);
				btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
				btn.setTextColor(Color.GRAY);
				if (j < firstDayOfWeek && day == 1) 
					btn.setText(Html.fromHtml(String.valueOf("<b>"
							+ prevMonthDay++ + "</b>")));
				else if (day > cal.getActualMaximum(Calendar.DAY_OF_MONTH))
				{
					btn.setText(Html.fromHtml("<b>" + nextMonthDay++ + "</b>"));
				} 
				else // day counter is in the current month
				{
					try {
						// ----------------------- Check if has an events  -----------------------
						boolean b = CheckEvent.hasEvent(cal.get(Calendar.YEAR)+"-"+ cal.get(Calendar.MONTH) +"-"+ day, helper);
						if(b){
							btn.setBackgroundResource(R.drawable.dayinmonth);
						} else {
							btn.setBackgroundResource(R.drawable.rectgrad);
						}
					} catch (Exception ex) {
						btn.setBackgroundResource(R.drawable.rectgrad);
					}
					
					cal.set(Calendar.DAY_OF_MONTH, day);

					btn.setTag(day);
					
					// Set OnClick Listener
					btn.setOnClickListener(selectDate);
					if (cal.equals(today)) {
						tv = btn;
						btn.setBackgroundResource(R.drawable.current_day);
					} else if (selected_day == day) {
						tv = btn;
						btn.setBackgroundResource(R.drawable.selectedgrad);
						btn.setTextColor(Color.WHITE);
					} else {
						btn.setTextColor(Color.WHITE);
					}

					// set the text of the day
					btn.setText(Html.fromHtml("<b>" + String.valueOf(day++) + "</b>"));
					//Log.e("[Days]", String.valueOf(day));
					
					if (j == 0)
						btn.setTextColor(Color.parseColor("#D73C10"));
					else if (j == 6)
						btn.setTextColor(Color.parseColor("#009EF7"));

					if ((day == this.day + 1)
							&& (this.month == cal.get(Calendar.MONTH) + 1)
							&& (this.year == cal.get(Calendar.YEAR)))
						btn.setBackgroundColor(Color.GRAY);
				}
				btn.setPadding(8, 8, 8, 8); // maintains proper distance between two adjacent days
				tr2.addView(btn);
			}
			// Add to Table Layout
			tl.addView(tr2);
		}
		
		customlayout.removeAllViews();
    }
	
	
	 /**
     * Change Month 
     */
    private OnClickListener changeMonth = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			tl.removeAllViews();
			
			if(v.getTag().equals("<")){
				cal.add(Calendar.MONTH, -1);
			} else {
				cal.add(Calendar.MONTH, 1);
			}
			
			selected_day = 0;
			// Display Months
			displayMonths();
		}
	};
	
	
	/**
	 * Selected Date
	 */
	private OnClickListener selectDate = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (tv != null) {
				try {
					if (isEvent[day]) {
						tv.setBackgroundResource(R.drawable.dayinmonth);
					} else
						tv.setBackgroundResource(R.drawable.rectgrad);

				} catch (Exception ex) {
					tv.setBackgroundResource(R.drawable.rectgrad);
				}
				tv.setPadding(8, 8, 8, 8);
			}
			if (tv.getText().toString().trim()
					.equals(String.valueOf(today.get(Calendar.DATE)))) {
				tv.setBackgroundResource(R.drawable.selectedgrad);
				//Log.e("[Date]", "Current Date: "+ tv.getText().toString());
			}
			day = Integer.parseInt(v.getTag().toString());
			selected_day = day;
			tv = (TextView) v;
			tv.setBackgroundResource(R.drawable.selectedgrad);
			
			String c_date = cal.get(Calendar.YEAR)+ "-"+ cal.get(Calendar.MONTH) +"-"+ day;
			
			boolean b = CheckEvent.hasEvent(c_date, helper);
			if(b){
				displayButton();
			} else {
				addButton();
			}
			
			/**
			 * Check the previous date if there is an events, then set customized background on it,
			 * else set default
			 */
			if(prev_date == null){
				prev_date = v;
				selected_current_date = prev_date;
			} else {
				String get_prev_date = cal.get(Calendar.YEAR)+ "-"+ cal.get(Calendar.MONTH) +"-"+ prev_date.getTag().toString();
				if(CheckEvent.hasEvent(get_prev_date, helper)){
					TextView t = (TextView) prev_date;
					t.setHeight(44);
					t.setWidth(45);
					t.setGravity(Gravity.CENTER);
					t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
					t.setBackgroundResource(R.drawable.dayinmonth);
				}
				prev_date = v;
				selected_current_date = prev_date;
			}
			
		}
	};
	
	private void displayButton(){
		customlayout.removeAllViews(); // clear previous views
		
		Button addbtn = new Button(this);
		addbtn.setText("(Tap to Display Events)");
		addbtn.setTag("displaybtn");
		addbtn.setHeight(80);
		addbtn.setOnClickListener(displaybuttonListener);
		addbtn.setBackgroundResource(R.drawable.button_design);
		addbtn.setTextColor(Color.WHITE);
		
		// add to layout
		customlayout.addView(addbtn);
	}
	
	private OnClickListener displaybuttonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String s = ""+ cal.get(Calendar.YEAR)+"-"+ cal.get(Calendar.MONTH)+"-"+ day;
			Log.d("[Display Events]", s);
			displayEvents(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, day);
		}
	};
	
	
	
	private void addButton(){
		customlayout.removeAllViews(); // clear previous views
		
		Button addbtn = new Button(this);
		addbtn.setText("(Tap to Create Events)");
		addbtn.setTag("addbtn");
		addbtn.setHeight(80);
		addbtn.setOnClickListener(addbuttonListener);
		addbtn.setBackgroundResource(R.drawable.button_design);
		addbtn.setTextColor(Color.WHITE);
		
		// add to layout
		customlayout.addView(addbtn);
	}
	
	private OnClickListener addbuttonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String s = ""+ cal.get(Calendar.YEAR)+"-"+ cal.get(Calendar.MONTH)+"-"+ day;
			Log.d("[Create Events]", s);
			
			Intent i = new Intent(getApplicationContext(), AddEvents.class);
			i.putExtra("_date", s);
			startActivity(i);
		}
	};

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		tl.removeAllViews();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		tl.removeAllViews();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		tl.removeAllViews();
		helper.close();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		tl.removeAllViews();
		customlayout.removeAllViews(); // clear previous views
		insertView();
		
		if(selected_current_date != null){
			String get_prev_date = cal.get(Calendar.YEAR)+ "-"+ cal.get(Calendar.MONTH) +"-"+ selected_current_date.getTag().toString();
			if(CheckEvent.hasEvent(get_prev_date, helper)){
				TextView t = (TextView) prev_date;
				t.setHeight(44);
				t.setWidth(45);
				t.setGravity(Gravity.CENTER);
				t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
				t.setBackgroundResource(R.drawable.dayinmonth);
			}
		}
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		tl.removeAllViews();
		customlayout.removeAllViews(); // clear previous views
		insertView();
	}
	
	
	
	/**
	 * Display Events Dialog
	 */
	private void displayEvents(int year, int month, int day){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		
		String d = ""+ year +"-"+ month +"-"+ day;
		dialog.setTitle(d);
		dialog.setIcon(R.drawable.note);
		
		LayoutInflater inflater = this.getLayoutInflater();
		View view = inflater.inflate(R.layout.display_events, null);
		
		tl1 = (TableLayout) view.findViewById(R.id.tablelayout01);
		tl2 = (TableLayout) view.findViewById(R.id.tablelayout02);
		TextView txt_title = (TextView) view.findViewById(R.id.textview3);
		txt_drinking_facts = (EditText) view.findViewById(R.id.edittext1);
		
		lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		String date = ""+ year +"-"+ (month - 1) +"-"+ day;
		final String[] getevent = helper.getEventId(date);
		
		txt_title.setText("Title: "+ getevent[1]);
		txt_drinking_facts.setText(getevent[2]);
		
		ArrayList<TodoBuddy> present_buddy = helper.showEvents("Select * from tbl_buddy where _event_id='"+ getevent[0] +"'");
		ArrayList<TodoBuddy> absent_buddy = helper.showEvents("Select * from tbl_buddy_2 where _event_id='"+ getevent[0] +"'");
		
		if(present_buddy != null){
			Iterator<TodoBuddy> itr = present_buddy.iterator();
			while(itr.hasNext()){
				TodoBuddy todo = itr.next();
				
				TableRow tr = new TableRow(this);
				tr.setLayoutParams(lp);
				CheckBox checkbox = new CheckBox(this);
				checkbox.setText(todo.getName()); // name
				checkbox.setId(todo.getId()); // id
				checkbox.setTag(todo.getEventId()); // event id
				checkbox.setChecked(true);
				checkbox.setOnClickListener(getCheckBox(checkbox));
				tr.addView(checkbox);
				tl1.addView(tr);
			}
		}
		
		if(absent_buddy != null){
			Iterator<TodoBuddy> itr = absent_buddy.iterator();
			while(itr.hasNext()){
				TodoBuddy todo = itr.next();
				
				TableRow tr = new TableRow(this);
				tr.setLayoutParams(lp);
				CheckBox checkbox = new CheckBox(this);
				checkbox.setText(todo.getName()); // name
				checkbox.setId(todo.getId()); // id
				checkbox.setTag(todo.getEventId()); // event id
				checkbox.setChecked(true);
				checkbox.setEnabled(false);
				tr.addView(checkbox);
				tl2.addView(tr);
			}
		}
		
		dialog.setView(view);
		dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Delete Events and all buddy
				helper.deleteRecords("tbl_events", "_event_id=?", new String[]{getevent[0]});
				helper.deleteRecords("tbl_buddy", "_event_id=?", new String[]{getevent[0]});
				helper.deleteRecords("tbl_buddy_2", "_event_id=?", new String[]{getevent[0]});
				customlayout.removeAllViews();
				Toast.makeText(getApplicationContext(), "Events has been Deleted.!", Toast.LENGTH_LONG).show();
			}
		});
		dialog.setNegativeButton("Update", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String s = txt_drinking_facts.getText().toString();
				helper.update("Update tbl_events Set _event_facts='"+s+"' Where _event_id='"+getevent[0]+"'");
				Toast.makeText(TabDrinkingBuddy.this, "Event has beeen Updated.!", Toast.LENGTH_LONG).show();
				customlayout.removeAllViews();
			}
		});
		
		AlertDialog alert = dialog.create();
		alert.show();
	}
	
	
	private View.OnClickListener getCheckBox(final CheckBox chk){
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( !chk.isChecked()){
					chk.setEnabled(false);
					String event_id = chk.getTag().toString();
					String name = chk.getText().toString();
					String id = String.valueOf(chk.getId());
					helper.insert("tbl_buddy_2", new String[]{"_event_id","_buddy_name"}, new String[]{event_id,name});
					helper.deleteRecords("tbl_buddy", "_id=?", new String[]{id});
					// Append to Absent table row
					addTableRow(new String[]{name, event_id, id}, tl2, false);
				}
			}
		};
	}
	
	/**
	 * Add Table Row
	 * @param values ex. Text, Tag, Id
	 * @param tlx
	 * @param isEnabled
	 */
	private void addTableRow(String[] values, TableLayout tlx, boolean isEnabled){
		TableRow tr = new TableRow(this);
		tr.setLayoutParams(lp);
		// check box
		CheckBox chk = new CheckBox(this);
		chk.setText(values[0]); // Text
		chk.setTag(values[1]); // tag
		chk.setId(Integer.parseInt(values[2])); // id
		chk.setEnabled(isEnabled);
		chk.setChecked(true);
		// add to table row
		tr.addView(chk);
		// add to Table layout
		tlx.addView(tr);
	}

}











