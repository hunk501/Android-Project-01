package com.example.valhala;

public class TodoAlarm {

	private String _id;
	private String _hour;
	private String _minute;
	private String _time;
	
	public TodoAlarm(String id, String hour, String minute, String time){
		this._id = id;
		this._hour = hour;
		this._minute = minute;
		this._time = time;
	}
	
	public String getId(){
		return _id;
	}
	
	public String getHour(){
		return _hour;
	}
	
	public String getMinute(){
		return _minute;
	}
	
	public String getTime(){
		return _time;
	}
	
	public String toString(){
		return "ID:"+ _id+" \tHour: "+ _hour+" \tMinute: "+ _minute;
	}
}
