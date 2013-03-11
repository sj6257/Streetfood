package com.dev.streetfood;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SendFeedback extends Activity {
	
	String subject="Feedback",Body;
	EditText etContent,etSender;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_feedback);
		
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
