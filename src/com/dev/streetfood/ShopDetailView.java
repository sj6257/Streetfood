package com.dev.streetfood;

import java.util.ArrayList;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ShopDetailView extends Activity {

	private static final String TAG = "ShowDetails";
	String sql;
	String shopName;
	ArrayList<BookMark> result = new ArrayList<BookMark>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_detail_view);
		if(ViewConfiguration.get(ShopDetailView.this).hasPermanentMenuKey())
		{
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
			Log.i(TAG,"Hardware Option Key not Present");
				
		Bundle b = getIntent().getExtras();
		shopName=(String) b.get("itemName");
		getInfo(shopName);	
		setView();
		
		RadioGroup radioGroupActivitySelector = (RadioGroup) findViewById(R.id.radio_group_activity_selector);
		radioGroupActivitySelector.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	    {
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	            // checkedId is the RadioButton selected
		switch (checkedId) {
		  case R.id.radioList :
			  Log.i(TAG,"RadioList Activity Selected");
			  goToListView();
		                   	              break;
		  case R.id.radioMap :
			  Log.i(TAG,"RadioMap Activity Selected");
			  goToMapView();
		                   	              break;                 	              
		 
		 }
	        }
	    });
		
		
		
		Button  btnDirections=(Button) findViewById(R.id.btn_directions);
		btnDirections.setOnClickListener(new OnClickListener() {
			
			//@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDirections();
			}
		} );
		
		
		
	}
	
	
	private void setView() {
		// TODO Auto-generated method stub
		
		TextView txtShopName = (TextView) findViewById(R.id.txtShopName);	
		txtShopName.setText(shopName);
		

		TextView txtShopInfo = (TextView) findViewById(R.id.txtShopInfo);	
		txtShopInfo.setText(result.get(0).getSnippet());
		
		TextView txtAddress = (TextView) findViewById(R.id.txtAddress);	
		txtAddress.setText("Address: \n"+result.get(0).getAddress());
		
	}


	public void getInfo(String shopName){
		 
			Log.i(TAG,"gettng Dettail Info");
		    
			String sql="select  S.shopName shopName,IFNULL(S.shopInfo,\"Not Available\") shopInfo,ifnull(S.address1,\'Not Available\') address,S.latitude latitude,S.longitude longitude from streetShopInfo AS S where S.shopName=\""+shopName+"\"";
			Log.i(TAG,"Creating Adapter for Fetching Data");
			StreetFoodDataBaseAdapter mDBAdapter= new StreetFoodDataBaseAdapter(this);
			Log.i(TAG,"Adapter Ready..");
			Log.i(TAG,"Creating/Opening Database");
			mDBAdapter.createDatabase();       
			mDBAdapter.open();
			Log.i(TAG,"Requesting info from getInfo function");
			result=mDBAdapter.getInfoForMap(sql);
			Log.i(TAG,"Information Retrived Passing it to SetView");			
			mDBAdapter.close();
			
	}
	
	protected void getDirections() {
		// TODO Auto-generated method stub
		Location myLocation=ShopListView.currentLocation;
		
		double dlongtd =result.get(0).getLongitude() ,dlattd=result.get(0).getLatitude();
		
		if(myLocation!=null){
			// got location from the LocationTracker Class
			Log.i(TAG,"got location from the LocationTracker Class");
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
		    Uri.parse("http://maps.google.com/maps?saddr="+myLocation.getLatitude()+","+myLocation.getLongitude()+"&daddr="+dlattd+","+dlongtd));
			intent.setComponent(new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"));
			startActivity(intent);
			
		}
		else{
			//since we don't have location from Location Tracker.Use last know location
			Log.i(TAG,"since we don't have location from Location Tracker.Use last know location");
			Intent intent = new Intent(Intent.ACTION_VIEW, 
			Uri.parse("http://maps.google.com/maps?f=d&daddr="+dlattd+","+dlongtd));
			intent.setComponent(new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"));
		    startActivity(intent);
		}
		
	}

	public void goToListView()
	{
		 Intent intent = new Intent(getApplicationContext(),ShopListView.class);
		 //start the DisplayActivity
         startActivity(intent);
	}
	
	public void goToMapView()
	{
	
		
		 Intent intent = new Intent(getApplicationContext(),ShopMapView.class); // change it to Map Activity
		 Bundle b = new Bundle();
         //Inserts a String value into the mapping of this Bundle
         b.putString("itemName",shopName);
         b.putString("view","Detail");
         intent.putExtras(b);
         startActivity(intent);
	}
	
	
	


	 @Override
	   public boolean onCreateOptionsMenu(Menu menu) {
	     MenuInflater menuInflater = getMenuInflater();
	     menuInflater.inflate(R.layout.menu, menu);
	     return super.onCreateOptionsMenu(menu);
	   }

	   @Override
	   public boolean onOptionsItemSelected(MenuItem item) {
	     Intent intent = new Intent(Intent.ACTION_VIEW);
	     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
	     switch (item.getItemId()) {
	       case R.id.getting_started:
	       	 Toast.makeText(this, "Getting Started is Selected", Toast.LENGTH_SHORT).show();
	         break;
	        case R.id.send_feedback:
	       	// Toast.makeText(this, "Send Feedback is Selected", Toast.LENGTH_SHORT).show();
	        	 Intent intentFeedback = new Intent(this,SendFeedback.class);
	      	   startActivity(intentFeedback);
	         break;
	       case R.id.about:
	       	 //Toast.makeText(this, "About is Selected", Toast.LENGTH_SHORT).show();
	    	   Intent intentAbout = new Intent(this,About.class);
	    	   startActivity(intentAbout);
	         break;
	       default:
	       	 	 return super.onOptionsItemSelected(item);
	     }
	     return true;
	   } 
	
	@Override
	protected void onStart() {
	    super.onStart();
	  //default show Popular Shops
	    
	    RadioButton ListRadioButton=(RadioButton) findViewById(R.id.radioList);
  		ListRadioButton.setChecked(false);
  		 RadioButton MapRadioButton=(RadioButton) findViewById(R.id.radioMap);
  		MapRadioButton.setChecked(false);
  		
  		
	}

}
