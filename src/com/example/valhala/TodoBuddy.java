package com.example.valhala;

public class TodoBuddy {

	private String name;
	private int id;
	private String event_id;
	
	public TodoBuddy(int id, String event_id, String name){
		this.id = id;
		this.name = name;
		this.event_id = event_id;
	}
	
	public String getName(){
		return name;
	}
	
	public int getId(){
		return id;
	}
	
	public String getEventId(){
		return event_id;
	}
	
	public String toString(){
		return id +" : "+ name;
	}
}
