package com.example.valhala;

import com.valhala.Assets.AssetUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

public class Settings extends Activity {
	
	private WebView webview;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		Bundle bundle = getIntent().getExtras();		
		// get extra string
		String title = bundle.getString("_title");
		String filename = bundle.getString("_filename");
		//setTitle(title);
		
		// initialize webview
		webview = (WebView) findViewById(R.id.webView1);
		webview.setHorizontalScrollBarEnabled(false);
		
		// Check the file if exists
		boolean isExists = AssetUtils.isExists(this, filename);
		if(isExists){
			webview.loadUrl("file:///android_asset/"+ filename);
		} 
		else {
			displayToast("File was not found "+ filename);
		}
	}
	
	/**
	 * Display a Toast message
	 * @param msg
	 */
	private void displayToast(String msg){
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.menu_back) {
			finish();
			return true;
		} 
		else if(id == android.R.id.home){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
		Log.e("[onPause]", "Settings Activity was closed");
	}
}
