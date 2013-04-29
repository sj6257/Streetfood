package com.dev.streetfood;

import android.os.Build;
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

		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1)
		{
			//Action bar code for android devices with android version more than gingerbread
			Log.i(TAG,"Build.VERSION.SDK_INT : "+Build.VERSION.SDK_INT);
			Log.i(TAG,"Build.VERSION_CODES.GINGERBREAD_MR1: "+Build.VERSION_CODES.GINGERBREAD_MR1);

			// to hide the action bar
			try
			{
				ActionBar actionBar = getActionBar();
				actionBar.hide();
			}
			catch (Exception ex)
			{
				Log.e(TAG,"Device Do Not Support Action Bar"+ex.toString());

			}
			Log.i(TAG,"Hardware Option Key Present");

		}
		else
		{
			Log.i(TAG,"Android version is less than 3.0");
			Log.i(TAG,"Build.VERSION.SDK_INT : "+Build.VERSION.SDK_INT);
			Log.i(TAG,"Build.VERSION_CODES.GINGERBREAD_MR1: "+Build.VERSION_CODES.GINGERBREAD_MR1);

		}
	}




}
