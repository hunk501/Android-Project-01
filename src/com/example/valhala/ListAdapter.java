package com.example.valhala;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This will handle the display of Contacts Image and Name
 * in the List View 
 * @author Dennis
 *
 */
public class ListAdapter extends ArrayAdapter<TodoContact> {

	private Context context;
	private final String tag = "[ListAdapter]";
	
	private ArrayList<TodoContact> alist = new ArrayList<TodoContact>();
	
	private DBHelper helper;
	private boolean isBlockedMode = false;
	
	public ListAdapter(Activity con, ArrayList<TodoContact> _alist){
		super(con, R.layout.row_list, _alist);
		
		this.context = con;
		this.alist = _alist;
		helper = new DBHelper(context);
		int count = helper.getCount("tbl_block_mode", "_block_mode='all'");
		isBlockedMode = (count > 0) ? true : false;
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
			TodoContact c = alist.get(position);
			if (c.isEnabled()) { // Contact is on the blocked list
				holder.text.setText(c.getName() + "\n" + c.getContact());
				holder.text.setTextColor(Color.WHITE);
				// Image
				holder.image = (ImageView) view.findViewById(R.id.imageView1);
				holder.image.setImageResource(R.drawable.user);
				// Image
				holder.image2 = (ImageView) view.findViewById(R.id.imageView2);
				if(isBlockedMode){
					holder.image2.setImageResource(R.drawable.lock2);
				} else {
					holder.image2.setImageResource(R.drawable.lock2);
				}
			} else { // Contact is not on the blocked list
				holder.text.setText(c.getName() + "\n" + c.getContact());
				holder.text.setTextColor(Color.WHITE);
				// Image
				holder.image = (ImageView) view.findViewById(R.id.imageView1);
				holder.image.setImageResource(R.drawable.user);
				// Image
				holder.image2 = (ImageView) view.findViewById(R.id.imageView2);
				if(isBlockedMode){
					holder.image2.setImageResource(R.drawable.lock2);
				} else {
					holder.image2.setImageResource(R.drawable.info);
				}
			}
		}

		view.setTag(holder);
		
		return view;
	}
	
	/**
	 * Remove from the list
	 * @param pos
	 */
	public void removeList(int pos){
		try {
			if(pos > 0){
				pos = pos - 1;
			}
			alist.remove(pos);
		} catch (Exception e) {
			Log.e(tag, "Unable to remove list.!");
		}
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










