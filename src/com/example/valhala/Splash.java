package com.example.valhala;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class Splash extends Activity {
	
	private TextView txt1, txt2;
	private Handler handler;
	private boolean keepGoing = true;
	private int lop = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		txt1 = (TextView) findViewById(R.id.textView1);
		txt2 = (TextView) findViewById(R.id.textView2);
		
		handler = new Handler();
		
		Thread t = new Thread(new Mythread());
		t.start();
	}
	
	private class UpdateGUI implements Runnable{

		@Override
		public void run() {
			try {
				
				txt1.setText(lop +"% Loading please wait....");
				
				switch(lop){
					case 2 | 0: txt2.setText("Starting Application.."); break;
					case 10: txt2.setText("Collecting Components.."); break;
					case 20: txt2.setText("Starting Application.."); break;
					case 30: txt2.setText("Checking Settings.."); break;
					case 40: txt2.setText("Setting has been Checked.."); break;
					case 50: txt2.setText("Checking Database.."); break;
					case 60: txt2.setText("Database has been checked.."); break;
					case 70: txt2.setText("Checking Events.."); break;
					case 80: txt2.setText("Events has been checked.."); break;
					case 90: txt2.setText("Calendar was setup successfully.."); break;
					case 100: txt2.setText("Application is now ready.!"); break;
				}
				
				if(lop >= 100){
					keepGoing = false;
					lop = 0;
					
					// goto main activity					
					Intent i = new Intent(Splash.this, MainActivity.class);
					finish();
					startActivity(i);
				}
				
				// increment
				lop+= 2;
				
			} catch (Exception e) {
				Log.e("[UpdateGUI]", "Error: "+ e.getMessage());
			}
		}
		
	}

	private class Mythread implements Runnable{

		@Override
		public void run() {
			while(keepGoing){
				try {
					
					handler.post(new UpdateGUI());
					
					// sleep
					Thread.sleep(100);
				} catch (Exception e) {
					Log.e("[MyThread]", "Error: "+ e.getMessage());
				}
			}
		}
		
	}
}
