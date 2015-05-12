package com.example.valhala;


public class CheckEvent {

	public static boolean hasEvent(String date, DBHelper helper){
		int count = helper.getCount("tbl_events", "_event_date='"+ date +"' ");
		helper.close();
		return (count > 0) ? true : false;
	}
}
