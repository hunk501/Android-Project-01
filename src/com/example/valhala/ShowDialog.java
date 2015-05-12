package com.example.valhala;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ShowDialog {

	private Context context;
	private String title;
	private String message;
	
	public ShowDialog(Context con, String title, String msg){
		this.context = con;
		this.title = title;
		this.message = msg;
	}
	
	/**
	 * SHow Dialog
	 */
	public void showDialog(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setIcon(R.drawable.info);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setCancelable(false);
		dialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		AlertDialog alert = dialog.create();
		alert.show();
	}
}
