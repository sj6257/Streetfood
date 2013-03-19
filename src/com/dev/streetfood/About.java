package com.dev.streetfood;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.ViewConfiguration;
import android.widget.TextView;

public class About extends Activity {

	private static final String TAG = "About";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		TextView about=(TextView)findViewById(R.id.textView1);
		//about.setText(R.string.abouthtml);
		
		try
		{
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		}
		catch (Exception ex)
		{
		  Log.e(TAG,"Device Do Not Support Action Bar"+ex.toString());
		  
		}
	   }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_about, menu);
		return true;
		
	}

}
