package com.example.valhala;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBHelper {
	
	private final String tag = "[DBHelper]";
	private Context context;
	
	private static SQLiteHelper helper = null;
	
	public DBHelper(Context con){
		this.context = con;
		helper = new SQLiteHelper(context);
		Log.d(tag, "Instatiate Database helper");
	}
	
	
	/**
	 *    ------------------ CRUD --------------------- //
	 */
	private static synchronized SQLiteDatabase connect(){
		Log.d("[DB]", "Opening Database connection.");
		return helper.getWritableDatabase();
		
	}
	
	public void close(){
		try {
			helper.close();
			Log.d("[Close]", "Database Helper is Closed.!");
		} catch (Exception e) {
			Log.e("[Close]", e.getMessage());
		}
	}
	
	// Insert Data
	public void insertData(String table, String data){
		final SQLiteDatabase db = connect();
		ContentValues values = new ContentValues();
		values.put("_phone_number", data);
		// Insert
		try {
			Log.d(tag, "Inserting Data to "+ table);
			db.insert(table, null, values);
			db.close();
			Log.d(tag, "Done Inserting data");
		} catch (Exception e) {
			displayMessage("[InsertData]"+e.getMessage());
		}
	}
	
	public void insertPassword(String phone, String password){
		final SQLiteDatabase db = connect();
		ContentValues values = new ContentValues();
		values.put("_phone_number", phone);
		values.put("_password", password);
		try {
			db.insert("tbl_password", null, values);
			db.close();
		} catch (Exception e) {
			displayMessage("[InsertData]"+e.getMessage());
		}
	}
	
	public void updatePassword(String pass, String phone){
		final SQLiteDatabase db = connect();
		try {
			db.rawQuery("Update tbl_password Set _password='"+ pass +"' Where _phone_number='"+ phone +"'", null);
			db.close();
		} catch (Exception e) {
			displayMessage("[UpdateData]"+e.getMessage());
		}
	}
	
	public boolean checkPassword(String phone, String password){
		final SQLiteDatabase db = connect();
		
		try {
			Cursor cursor = db.rawQuery("Select * from tbl_password Where _phone_number='"+phone+"' AND _password='"+password+"'", null);
			if(cursor.moveToFirst()){
				cursor.close();
				return true;
			}
		} catch (Exception e) {
			displayMessage("[UpdateData]"+e.getMessage());
		}
		return false;
	}
	
	// Check The password from table Setting
	public boolean checkPassword2(String password){
		final SQLiteDatabase db = connect();
		try {
			Cursor cursor = db.rawQuery("Select * from tbl_setting Where _blocking_password='"+password+"'", null);
			if(cursor.moveToFirst()){
				cursor.close();
				return true;
			}
		} catch (Exception e) {
			displayMessage("[UpdateData]"+e.getMessage());
		}
		return false;
	}
	
	// Show All
	public List<TodoContact> showAll(String table){
		final SQLiteDatabase db = connect();
		
		List<TodoContact> lists = new ArrayList<TodoContact>();
		Cursor cur = db.rawQuery("SELECT * FROM "+ table, null);
		Log.d(tag, "Showing All Records..");
		if(cur.getCount() > 0){
			cur.moveToFirst(); // Move Cursor to First records
			Log.d(tag, "Total Counts: "+ cur.getCount());
			do{
				TodoContact list = new TodoContact("[Name]", false, cur.getString(1));
				lists.add(0, list);
				Log.d("[Row]", cur.getString(1));
			} while(cur.moveToNext());
			cur.close();
			db.close();
			return lists;
		} else {
			Log.d(tag, "No Records was found..");
			db.close();
			return null;
		}
	}
	
	public ArrayList<TodoEmergency> showAllEmergency(){
		ArrayList<TodoEmergency> lists = new ArrayList<TodoEmergency>();
		final SQLiteDatabase db = connect();
		Cursor cursor = db.rawQuery("Select * from tbl_emergency", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				TodoEmergency todo = new TodoEmergency(cursor.getString(0), cursor.getString(2), cursor.getString(1));
				lists.add(todo);
			} while(cursor.moveToNext());
		} else {
			Log.e("[Emergency]", "No records was found.!");
		}
		return lists;
	}
	
	public String[] getEventId(String date){
		final SQLiteDatabase db = connect();
		Cursor cursor = db.rawQuery("Select * from tbl_events where _event_date='"+ date +"'", null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			String[] result = new String[3];
			result[0] = cursor.getString(0);
			result[1] = cursor.getString(1);
			result[2] = cursor.getString(3);
			return result;
		}
		cursor.close();
		return null;
	}
	
	public ArrayList<TodoBuddy> showEvents(String query){
		ArrayList<TodoBuddy> list = new ArrayList<TodoBuddy>();
		final SQLiteDatabase db = connect();
		Cursor cursor = db.rawQuery(query, null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				TodoBuddy t = new TodoBuddy(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
				list.add(t);
			} while(cursor.moveToNext());
			db.close();
			return list;
		}
		db.close();
		return null;
	}
	
	// Get Count
	public int getCount(String table, String where_clause){
		final SQLiteDatabase db = connect();
		String sql = null;
		if(where_clause == null){
			sql = "Select * from "+ table;
		} else{
			sql = "Select * from "+ table +" Where "+ where_clause;
		}
		Cursor cur = db.rawQuery(sql, null);
		if(cur.getCount() > 0){
			Log.d("[Total]", ""+cur.getCount());
			db.close();
			cur.close();
			return cur.getCount();
		}
		Log.d("[Total]", ""+cur.getCount());
		db.close();
		return 0;
	}
	
	// Delete
	public void deleteRecords(String table, String where, String[] values){
		final SQLiteDatabase db = connect();
		try {
			Log.d(tag, "Deleting Records..");
			db.delete(
					table,  // Table name
					where,  // Where clause example _id = ?
					values); // Values
			db.close();
			Log.d(tag, "Done Deleting Records..");
		} catch (Exception e) {
			displayMessage(e.getMessage());
		}
	}
	
	// Insert
	public void insert(String table, String[] column, String[] values){
		final SQLiteDatabase db = connect();
		ContentValues val = new ContentValues();
		try {
			Log.d(tag, "Inserting Data to "+ table);
			for(int i=0; i < column.length; i++){
				val.put(column[i], values[i]);
				Log.d("[Alarm]", column[i] +": "+ values[i]);
			}
			db.insert(table, null, val);
			db.close();
			Log.d(tag, "Done Inserting Data to "+ table);
		} catch (Exception e) {
			displayMessage(e.getMessage());
		}
	}
	
	// Count Alarm Records
	public List<TodoAlarm> countRecords(String table){
		final SQLiteDatabase db = connect();
		List<TodoAlarm> list = new ArrayList<TodoAlarm>();
		Cursor cursor = db.rawQuery("Select * from "+ table, null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				TodoAlarm todo = new TodoAlarm(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(7));
				list.add(todo);
			}while(cursor.moveToNext());
			cursor.close();
			return list;
		}
		cursor.close();
		return null;
	}
	
	// Update
	public void update(String sql){
		final SQLiteDatabase db = connect();
		try {
			db.execSQL(sql);
			db.close();
		} catch (Exception e) {
			displayMessage(e.getMessage());
		}
	}
	
	// get data
	public String[][] getRecords(String sql){
		String[][] result = null;
		final SQLiteDatabase db = connect();
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor.getCount() > 0){
			result = new String[cursor.getCount()][cursor.getColumnCount()];
			int lop = 0;
			cursor.moveToFirst();
			Log.d("[Total Column]", ""+cursor.getColumnCount());
			do{
				for(int i=0; i < cursor.getColumnCount(); i++){
					result[lop][i] = cursor.getString(i);
					Log.d("[Column]", ""+ cursor.getString(i));
				}
				lop++;
			} while(cursor.moveToNext());
			cursor.close();
			return result;
		} else {
			cursor.close();
			return null;
		}
	}

	
	/**
	 *    ------------------ End CRUD --------------------- //
	 */
	
	
	
	
	/**
	 * This is our SQLiteOpenHelper
	 * @author Dennis
	 *
	 */
	private class SQLiteHelper extends SQLiteOpenHelper{

		private static final String db_name = "valhala_db.db";
		private static final int db_version = 1;
		private static final String table_blocks = "tbl_block";
		private static final String table_password = "tbl_password";
		private static final String table_alarm = "tbl_alarm";
		private static final String table_emergency = "tbl_emergency";
		private static final String table_events = "tbl_events";
		private static final String table_buddy = "tbl_buddy";
		private static final String table_buddy_2 = "tbl_buddy_2";
		private static final String table_setting = "tbl_setting";
		private static final String table_drinking_fact = "tbl_drinking_fact";
		private static final String table_block_mode = "tbl_block_mode";
		// Query
		private static final String query_01 = "CREATE TABLE "+ table_blocks +"("
				+ "_id integer primary key autoincrement,"
				+ "_phone_number text not null,"
				+ "_date_block date);";
		
		private static final String query_02 = "CREATE TABLE "+ table_password +"("
				+ "_id integer primary key autoincrement,"
				+ "_phone_number text not null,"
				+ "_password text not null);";
		
		private static final String query_03 = "CREATE TABLE "+ table_alarm +"("
				+ "_id integer primary key autoincrement,"
				+ "_hour text not null,"
				+ "_minute text not null,"
				+ "_password text not null,"
				+ "_year text not null,"
				+ "_month text not null,"
				+ "_day text not null,"
				+ "_time text);";
		
		private static final String query_04 = "CREATE TABLE "+ table_emergency +"("
				+ "_id integer primary key autoincrement,"
				+ "_phone_number text not null,"
				+ "_phone_name text not null)";
		
		private static final String query_05 = "CREATE TABLE "+ table_events +"("
				+ "_event_id text primary key,"
				+ "_event_title text not null,"
				+ "_event_date text not null,"
				+ "_event_facts text)";
		
		private static final String query_06 = "CREATE TABLE "+ table_buddy +"("
				+ "_id integer primary key autoincrement,"
				+ "_event_id text not null,"
				+ "_buddy_name text not null)";
		
		private static final String query_07 = "CREATE TABLE "+ table_buddy_2 +"("
				+ "_id integer primary key autoincrement,"
				+ "_event_id text not null,"
				+ "_buddy_name text not null)";
		
		private static final String query_08 = "CREATE TABLE "+ table_setting +"("
				+ "_id integer primary key autoincrement,"
				+ "_blocking_mode text,"
				+ "_blocking_password text,"
				+ "_blocking_hours text)";
		
		private static final String query_09 = "CREATE TABLE "+ table_drinking_fact +"("
				+ "_id integer primary key autoincrement,"
				+ "_event_id text not null,"
				+ "_alcohol text,"
				+ "_pulutan text)";
		
		private static final String query_10 = "CREATE TABLE "+ table_block_mode +"("
				+ "_id integer primary key autoincrement,"
				+ "_block_mode text)";
		
		public SQLiteHelper(Context con){
			super(con, db_name, null, db_version);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			try {
				Log.d(tag, "Creating Table from Database '"+ db_name+ "'");
				db.execSQL(query_01); // Create Table tbl_blocks
				db.execSQL(query_02); // Create table tbl_password
				db.execSQL(query_03); // Create table tbl_alarm
				db.execSQL(query_04); // Create table tbl_emergency
				db.execSQL(query_05); // Create table tbl_events
				db.execSQL(query_06); // Create table tbl_buddy
				db.execSQL(query_07); // Create table tbl_buddy
				db.execSQL(query_08); // Create table tbl_buddy
				db.execSQL(query_09); // Create table tbl_buddy
				db.execSQL(query_10); // Create table tbl_buddy
				Log.d(tag, " Done Creating Table from Database '"+ db_name+ "'");
				
				db.execSQL("insert into "+ table_block_mode +"(_block_mode) values('single')");
				db.execSQL("insert into "+ table_setting +"(_blocking_mode,_blocking_password,_blocking_hours) values('all','lira','2')");
			} catch (Exception e) {
				// TODO: handle exception
				displayMessage(e.getMessage());
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			try {
				Log.d(tag, "Upgrading Database '"+ db_name+ "'");
				db.execSQL("DROP TABLE IF EXISTS "+ table_blocks);
				db.execSQL("DROP TABLE IF EXISTS "+ table_password);
				db.execSQL("DROP TABLE IF EXISTS "+ table_alarm);
				db.execSQL("DROP TABLE IF EXISTS "+ table_emergency);
				db.execSQL("DROP TABLE IF EXISTS "+ table_events);
				db.execSQL("DROP TABLE IF EXISTS "+ table_buddy);
				db.execSQL("DROP TABLE IF EXISTS "+ table_buddy_2);
				db.execSQL("DROP TABLE IF EXISTS "+ table_drinking_fact);
				db.execSQL("DROP TABLE IF EXISTS "+ table_setting);
				db.execSQL("DROP TABLE IF EXISTS "+ table_block_mode);
				// Create new Table
				onCreate(db);
				
			} catch (Exception e) {
				// TODO: handle exception
				displayMessage(e.getMessage());
			}
		}
		
	}
	
	
	
	/**
	 * Display Message
	 * @param msg
	 */
	private void displayMessage(String msg){
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
	
	
}


















