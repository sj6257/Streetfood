package com.dev.streetfood;

import android.net.Uri;
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
import android.widget.LinearLayout;

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
		//		  etContent=(EditText)findViewById(R.id.content);
		//		  etSender =(EditText)findViewById(R.id.Sender);
		//		 
		//		
		//		 
		//		Button btnFeedback=(Button)findViewById(R.id.btnFeedback);
		//		btnFeedback.setOnClickListener(new OnClickListener() {
		//			
		//			@Override
		//			public void onClick(View v) {
		//				// TODO Auto-generated method stub
		//				btnFeedbackOnClick(v);
		//			}
		//		});

		LinearLayout lemail=(LinearLayout)findViewById(R.id.llay_email);
		LinearLayout lgplus=(LinearLayout)findViewById(R.id.llay_gplus);
		LinearLayout lfbook=(LinearLayout)findViewById(R.id.llay_fbook);

		lemail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				directToMail();
			}
		});

		lgplus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				directToGplus();
			}
		});

		lfbook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				directToFbook();

			}
		});

	}



	//On click event for button feedback
	public void directToMail() {



		final Intent eMail = new Intent(android.content.Intent.ACTION_SEND);
		eMail.setType("text/html");
		eMail.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ "nomadicfoodadm@gmail.com" });
		eMail.putExtra(android.content.Intent.EXTRA_SUBJECT,"Feedback of Nomadic food");
		eMail.putExtra(android.content.Intent.EXTRA_TEXT,"Hi\nI am using Nomadic Food App.\nBelow is my feedback and suggestions:" );
		startActivity(Intent.createChooser(eMail,"Choose Email Application :"));
	} 

	public void directToGplus()
	{
		String URL = "https://plus.google.com/103651699600936943134/posts";
		Uri uri = Uri.parse(URL);
		final Intent gPlus = new Intent(Intent.ACTION_VIEW,uri);
		startActivity(Intent.createChooser(gPlus,"Choose Browser or Google+ App :"));
	}

	public void directToFbook(){
		String URL1 = "https://www.facebook.com/NomadicFood";
		String URL2 = "fb://profile/125421887648178";
		Intent fBook=null;
		Uri uri =null;;
		try{
			uri=Uri.parse(URL2);
			fBook=new Intent(Intent.ACTION_VIEW,uri );
			startActivity(fBook);
		}
		catch (Exception e) {
			uri=Uri.parse(URL1);
			fBook=new Intent(Intent.ACTION_VIEW,uri );
			startActivity(Intent.createChooser(fBook,"Choose Browser or facebook App :"));
		}

	}
}
