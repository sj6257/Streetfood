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
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
		
		/*TextView txtShopName = (TextView) findViewById(R.id.txtShopName);	
		txtShopName.setText(shopName);
		

		TextView txtShopInfo = (TextView) findViewById(R.id.txtShopInfo);	
			
		 TextView txtAddress = (TextView) findViewById(R.id.txtAddress);	*/
			
		// get relevant info from db
		/*sql="select ifnull(shopInfo,\'Not Available\')  from streetShopInfo where shopName=\""+shopName+"\"";
		StreetFoodDataBaseAdapter mDBAdapter= new StreetFoodDataBaseAdapter(this);
		mDBAdapter.createDatabase();       
		mDBAdapter.open();
		String info=mDBAdapter.getSingleStringVal(sql);
		txtShopInfo.setText(info);
		Log.i(TAG,info);
		*/
		
	
		/*sql="select ifnull(address1,\'Not Available\')  from streetShopInfo where shopName=\""+shopName+"\"";
		String address=mDBAdapter.getSingleStringVal(sql);
		txtAddress.setText("Address: \n"+address);
		Log.i(TAG,"Got Address:"+address);
		
		mDBAdapter.close();*/
		
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
		double dlongtd =result.get(0).getLongitude() ,dlattd=result.get(0).getLatitude();
		Intent intent = new Intent(Intent.ACTION_VIEW, 
			    Uri.parse("http://maps.google.com/maps?f=d&daddr="+dlattd+","+dlongtd));
			intent.setComponent(new ComponentName("com.google.android.apps.maps", 
			    "com.google.android.maps.MapsActivity"));
		startActivity(intent);
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.option_menu, menu);
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
