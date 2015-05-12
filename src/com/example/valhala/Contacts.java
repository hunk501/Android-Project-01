package com.example.valhala;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

public class Contacts {

	private Context context;
	private String id = ContactsContract.Contacts._ID;
	private String name = ContactsContract.Contacts.DISPLAY_NAME;
	private String phone_number = ContactsContract.CommonDataKinds.Phone.NUMBER;
	private String has_phone_number = ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER;
	
	private List<TodoContact> alist = new ArrayList<TodoContact>();
	
	private DBHelper helper;
	
	public Contacts(Context con){
		this.context = con;
		/**
		 * Instantiate Database Helper
		 */
		helper = new DBHelper(context);
		
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(
				ContactsContract.Contacts.CONTENT_URI, 
				null, null, null,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME +" ASC");
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			// Browse Contacts
			do {
				/**
				 * Get Contact ID and Name
				 */
				String _id = cursor.getString(cursor.getColumnIndex(id));
				String _name = cursor.getString(cursor.getColumnIndex(name));
				String _phoneNumber = null;
				boolean _isBlocked = false;
				/**
				 * Get Phone Number
				 */
				int hasphone = Integer.parseInt(cursor.getString(cursor.getColumnIndex(has_phone_number)));
				if(hasphone > 0){
					Cursor pCursor = resolver.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
							null, 
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", 
							new String[]{ _id }, null);
					pCursor.moveToFirst();
					do{
						// Get phone number
						_phoneNumber = pCursor.getString(pCursor.getColumnIndex(phone_number));
						// Check if the Phone Number was on the Blocked List
						if(helper.getCount("tbl_block", "_phone_number='"+ _phoneNumber +"'") > 0){
							_isBlocked = true;
							//Log.d("[Contacts]", "Name: "+ _name +"\tEnabled: true");
						} else {
							//Log.d("[Contacts]", "Name: "+ _name +"\tEnabled: false");
							_isBlocked = false;
						}
					}while(pCursor.moveToNext());
					pCursor.close(); // Close
				}
				
				// Add to List
				TodoContact list = new TodoContact(_name, _isBlocked, _phoneNumber);
				alist.add(list);
				
			} while(cursor.moveToNext());
			
			cursor.close(); // Close
		} else {
			Toast.makeText(context, "No result found for Contacts.!", Toast.LENGTH_LONG).show();
		}
	}
	
	
	/**
	 * Get All Contacts
	 * @return
	 */
	public List<TodoContact> getContacts(){
		return alist;
	}
	
	
}


















