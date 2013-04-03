package com.dev.streetfood;

import android.os.Build;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SendFeedback extends Activity {
	
	private static final String TAG = "Feedback";
	String subject="Feedback",Body;
	EditText etContent,etSender;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_feedback);
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
		  etContent=(EditText)findViewById(R.id.content);
		  etSender =(EditText)findViewById(R.id.Sender);
		 
		
		 
		Button btnFeedback=(Button)findViewById(R.id.btnFeedback);
		btnFeedback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnFeedbackOnClick(v);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_send_feedback, menu);
		return true;
	}
	
	//On click event for button feedback
    public void btnFeedbackOnClick(View v) {
    	
    	 Body=etContent.getText()+"\n"+"Yours sincerely\n"+etSender.getText();
    	 
         final Intent Email = new Intent(android.content.Intent.ACTION_SEND);
         Email.setType("text/html");
         Email.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ "nomadicadmin@gmail.com" });
         Email.putExtra(android.content.Intent.EXTRA_SUBJECT,subject);
         Email.putExtra(android.content.Intent.EXTRA_TEXT,Body );
         startActivity(Intent.createChooser(Email,"Send Feedback:"));
    } 

}
