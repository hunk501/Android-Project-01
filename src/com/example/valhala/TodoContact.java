package com.example.valhala;

public class TodoContact {

	private String name;
	private boolean isok;
	private String contact;
	
	public TodoContact(String _n, boolean _b, String _c){
		this.name = _n;
		this.isok = _b;
		this.contact = _c;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isEnabled(){
		return isok;
	}
	
	public String getContact(){
		return contact;
	}
	
	public boolean getBoolean(){
		return isok;
	}
	
	public String toString(){
		return name+ "\t\t"+ contact;
	}
}
