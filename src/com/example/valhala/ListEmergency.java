package com.example.valhala;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListEmergency extends ArrayAdapter<TodoEmergency> {
	
	private Context context;
	private final String tag = "[ListAdapter]";
	private ArrayList<TodoEmergency> alist = new ArrayList<TodoEmergency>();
	
	public ListEmergency(Activity activity, ArrayList<TodoEmergency> alist2){
		super(activity, R.layout.row_list, alist2);
		
		this.context = activity;
		this.alist = alist2;
	}
	
	@Override
	public View getView(int position, View convertview, ViewGroup parent){
		
		// Inflate our Custom List view
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View view = inflater.inflate(R.layout.row_list, null);

		// Configure View Holder
		ViewHolder holder = new ViewHolder();

		holder.text = (TextView) view.findViewById(R.id.lblPass);

		if (alist.size() > 0) {
			// Loop List
			TodoEmergency c = alist.get(position);
			holder.text.setText(c.getName() + "\n" + c.getPhoneNumber());
			holder.text.setTextColor(Color.WHITE);
			// Image
			holder.image = (ImageView) view.findViewById(R.id.imageView1);
			holder.image.setImageResource(R.drawable.user);
			// Image
			holder.image2 = (ImageView) view.findViewById(R.id.imageView2);
			holder.image2.setVisibility(View.GONE);
		}

		view.setTag(holder);

		return view;
	}
	
	
	
	
	/**
	 * View Holder
	 * @author Dennis
	 *
	 */
	private static class ViewHolder{
		public TextView text;
		public ImageView image;
		public ImageView image2;
	}
}
