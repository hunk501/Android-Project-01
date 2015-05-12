package com.example.valhala;

import java.util.ArrayList;
import java.util.List;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BrowseContact extends DialogFragment {

	private Contacts contact;
	private ListView listview;
	private ArrayList<TodoContact> alist;
	private ArrayAdapter<TodoContact> adapter;
	
	public BrowseContact(){
		contact = new Contacts(getActivity().getApplicationContext());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.browse_contact, null);
		
		listview = (ListView) view.findViewById(R.id.listView1);
		alist = new ArrayList<TodoContact>();
		adapter = new ArrayAdapter<TodoContact>(getActivity(), android.R.layout.simple_list_item_1, alist);
		listview.setAdapter(adapter);
		
		List<TodoContact> lists = contact.getContacts();
		
		for(TodoContact list : lists){
			alist.add(list);
			adapter.notifyDataSetChanged();
		}
		
		
		return view;
	}
}
