package com.example.valhala;

public class TodoEmergency {

	private String id;
	private String name;
	private String phone_number;
	
	public TodoEmergency(String id, String name, String phone){
		this.id = id;
		this.name = name;
		this.phone_number = phone;
	}
	
	public String getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getPhoneNumber(){
		return phone_number;
	}
	
	public String toString(){
		return id +" :" + name +" : "+ phone_number;
	}
}
