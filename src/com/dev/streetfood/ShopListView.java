package com.dev.streetfood;


import java.util.ArrayList;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.location.Location;
import android.location.LocationManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;



public class ShopListView extends FragmentActivity {

	private static Location currentLocation;
	private static LocationTracker lTracker;
	private static final String TAG = "ShopListView";
	private GetList mTask=null;
	private String mIntent="showPopular";
	ListView lview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_list_view);

		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1)
		{
			//Action bar code for android devices with android version more than gingerbread
			Log.i(TAG,"Build.VERSION.SDK_INT : "+Build.VERSION.SDK_INT);
			Log.i(TAG,"Build.VERSION_CODES.GINGERBREAD_MR1: "+Build.VERSION_CODES.GINGERBREAD_MR1);
			if(ViewConfiguration.get(ShopListView.this).hasPermanentMenuKey())
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

			Log.i(TAG,"Hardware Option Key not Present");
		}
		else
		{
			Log.i(TAG,"Android version is less than 3.0");
			Log.i(TAG,"Build.VERSION.SDK_INT : "+Build.VERSION.SDK_INT);
			Log.i(TAG,"Build.VERSION_CODES.GINGERBREAD_MR1: "+Build.VERSION_CODES.GINGERBREAD_MR1);

		}


		lview= (ListView) findViewById(R.id.listView1);




		RadioGroup radioGroupActivitySelector = (RadioGroup) findViewById(R.id.radio_group_activity_selector);
		RadioButton ListRadioButton=(RadioButton) findViewById(R.id.radioList);
		ListRadioButton.setChecked(true);

		final RadioButton CategoryRadioButton=(RadioButton)findViewById(R.id.radioCategory);

		radioGroupActivitySelector.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// checkedId is the RadioButton selected
				switch (checkedId) {

				case R.id.radioMap : 
					Log.i(TAG,"RadioMap Activity Selected"); 
					goToMapView();
					break;
				}
			}
		});


		RadioGroup radioGroupListSelector = (RadioGroup) findViewById(R.id.radio_group_list_selector);
		radioGroupListSelector.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// checkedId is the RadioButton selected

				switch (checkedId) {
				case R.id.radioPopular : 
					Log.i(TAG,"Popular Radio Button Selected");

					/*if(mTask!=null)
	  			  {
	  			  mTask.cancel(true);
	  			  Log.i(TAG,"Canceling Current Task");
	  			  }
	  			  else
	  			  {
	  			  mTask=new GetList();	  
	  			  mTask.execute("showPopular");
	  			  }*/
					mTask=new GetList();	  
					mTask.execute("showPopular");
					//Variable to sent an intent to map view so that it will show corresponding view
					mIntent="showPopular";

					break;
				case R.id.radioAZ :
					Log.i(TAG,"AZ Radio Button Selected");

					/*if(mTask!=null)
	  			  {
	  			  mTask.cancel(true);
	  			  Log.i(TAG,"Canceling Current Task");
	  			  }
	  			  else
	  			  {
	  				 mTask=new GetList();	  
	  			  mTask.execute("showAZ");
	  			  }*/
					mTask=new GetList();	  
					mTask.execute("showAZ");
					//Variable to sent an intent to map view so that it will show corresponding view 
					mIntent="showAZ";

					break;
				case R.id.radioCategory: 
					Log.i(TAG,"Category Radio Button Selected");

					/* if(mTask!=null)
	  			  {
	  			  mTask.cancel(true);
	  			  Log.i(TAG,"Canceling Current Task");
	  			  mTask=null;
	  			  }
	  			  else
	  			  {
	  			  mTask=new GetList();
	  			  mTask.execute("showCategory");
	  			  }*/
					mTask=new GetList();
					mTask.execute("showCategory");
					//Variable to sent an intent to map view so that it will show corresponding view
					mIntent="showPopular";

					break;

				case R.id.radioNearBy :
					Log.i(TAG,"NearBy Radio Button Selected");



					/* if(mTask!=null)
	  			  {
	  			  mTask.cancel(true);
	  			  Log.i(TAG,"Canceling Current Task");
	  			  mTask=null;
	  			  }
	  			  else
	  			  {
	  			  mTask=new GetList();
	  			  mTask.execute("showNearBy");
	  			  }*/
					mTask=new GetList();
					mTask.execute("showNearBy");
					//Variable to sent an intent to map view so that it will show corresponding view 
					mIntent="showNearBy"; 
					break;

				default:
					/* if(mTask!=null)
	  			  {
	  			  mTask.cancel(true);
	  			  Log.i(TAG,"Canceling Current Task");
	  			  mTask=null;
	  			  }
	  			  else*/


				}
			}
		});


		//int checkedRadioList = radioGroupListSelector.getCheckedRadioButtonId();





		// setting On Click event to List

		lview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				// TextView text1 = (TextView) view.findViewById(android.R.id.);
				// Log.i(TAG,"You Clicked:"+ text1.getText().toString());
				try{

					Intent intent;
					if(CategoryRadioButton.isChecked())
					{
						Log.i(TAG,"flagCategory : True");
						intent = new Intent(getApplicationContext(), ShopCategoryView.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

					}
					else
					{
						intent = new Intent(getApplicationContext(), ShopDetailView.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					}

					ListViewCustomAdapter lAdapter=(ListViewCustomAdapter)parent.getAdapter();
					String clickedItem=(String) lAdapter.getItem(position);		         
					//Create a bundle object
					Bundle b = new Bundle();

					//Inserts a String value into the mapping of this Bundle
					b.putString("itemName",clickedItem);
					// b.putString("age", age.getText().toString()); we can put as many parameters we need

					//Add the bundle to the intent.
					intent.putExtras(b);

					//start the DisplayActivity
					startActivity(intent);
				}
				catch(Exception e)
				{
					Log.e(TAG,"Exception While Creating Intent: "+e.toString());
				}
			}
		});




	}


	public boolean isConnectionsAvailable() {
		boolean lRet = false;
		try{
			ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(ShopListView.CONNECTIVITY_SERVICE);
			NetworkInfo info= conMgr.getActiveNetworkInfo();  
			if(info != null && info.isConnected()) {  
				lRet = true ;
			}else{  
				lRet = false ;
			}
		}catch (Exception e) {
			Log.d("Connection Error", e.toString());
			lRet = false ;
		}
		return lRet;
	}

	public void goToListView()
	{
		Intent intent = new Intent(getApplicationContext(),ShopListView.class);
		//start the DisplayActivity
		startActivity(intent);
	}

	public void goToMapView()
	{
		Intent intent = new Intent(this,ShopMapView.class); // change it to Map Activity
		Bundle b = new Bundle();
		//Inserts a String value into the mapping of this Bundle
		b.putString("itemName",mIntent);
		b.putString("view","List");
		intent.putExtras(b);
		startActivity(intent);
	}


	public void showFeatured()
	{
		//commented till we get advertise code

		/*flagCategory=false;
		Log.i(TAG,"Populating fetured Stall list");
		ArrayList<String> list=new ArrayList<String>();
		String sql="select  S.shopName from streetShopInfo AS S JOIN ratings AS R  where S.shopName=R.shopName and R.overall >0 order by S.shopName";
		Log.i(TAG,"Creating Adapter for Fetching Data");
		StreetFoodDataBaseAdapter mDBAdapter= new StreetFoodDataBaseAdapter(this);
		Log.i(TAG,"Adapter Ready..");
		Log.i(TAG,"Creating/Opening Database");
		mDBAdapter.createDatabase();       
		mDBAdapter.open();
		Log.i(TAG,"Requesting info from getInfo function");
		list=mDBAdapter.getInfo(sql,"shopName");
		Log.i(TAG,"Information Retrived Passing it to SetView");
		setView(list);
		mDBAdapter.close();*/

	}

	public ArrayList<String> showPopular(){

		ArrayList<String> list=new ArrayList<String>();
		String sql="select  S.shopName shopName from streetShopInfo AS S JOIN ratings AS R  where S.shopName=R.shopName and R.overall >0 order by S.shopName";
		Log.i(TAG,"Creating Adapter for Fetching Data");
		StreetFoodDataBaseAdapter mDBAdapter= new StreetFoodDataBaseAdapter(this);
		Log.i(TAG,"Adapter Ready..");
		Log.i(TAG,"Creating/Opening Database");
		mDBAdapter.createDatabase();       
		mDBAdapter.open();
		Log.i(TAG,"Requesting info from getInfo function");
		list=mDBAdapter.getInfo(sql,"shopName");
		Log.i(TAG,"Information Retrived Passing it to SetView");
		//setView(list);
		mDBAdapter.close();
		return list;
	}

	public ArrayList<String> showNearBy() {


		ArrayList<String> list=new ArrayList<String>();
		list=null;

		String sql="select shopName shopName from streetShopInfo where distance<3";


		StreetFoodDataBaseAdapter mDBAdapter= new StreetFoodDataBaseAdapter(this);
		mDBAdapter.createDatabase();       
		mDBAdapter.open();

		currentLocation=lTracker.getLocation();
		if(mDBAdapter.validDistance() && currentLocation!=null && currentLocation.getLatitude()!=0)   
		{
			Log.i(TAG,"Now Fetching Near By Location from DB");
			list=mDBAdapter.getInfo(sql,"shopName");
			Log.i(TAG,"Cursor Values Retrived into Array list");
			mDBAdapter.close();
		}
		else
		{
			if(currentLocation!=null && currentLocation.getLatitude()!=0 )   
			{
				Log.i(TAG,"Location Received");   
				mDBAdapter.updateDistance(currentLocation);
				list=mDBAdapter.getInfo(sql,"shopName");
				mDBAdapter.close();

			}
		}
		return list;
	}


	public ArrayList<String> showAZ(){
		ArrayList<String> list=new ArrayList<String>();

		String sql="select shopName from streetShopInfo order by shopName";
		StreetFoodDataBaseAdapter mDBAdapter= new StreetFoodDataBaseAdapter(this);
		mDBAdapter.createDatabase();       
		mDBAdapter.open();
		list=mDBAdapter.getInfo(sql,"shopName");
		Log.i(TAG,"Cursor Values Retrived into Array list");
		//setView(list);
		mDBAdapter.close();
		return list;
	}

	public ArrayList<String> showCategory(){
		ArrayList<String> list=new ArrayList<String>();

		String sql="select  distinct category from streetShopInfo order by category";
		StreetFoodDataBaseAdapter mDBAdapter= new StreetFoodDataBaseAdapter(this);
		mDBAdapter.createDatabase();       
		mDBAdapter.open();
		list=mDBAdapter.getInfo(sql,"category");
		Log.i(TAG,"Cursor Values Retrived into Array list");
		//setView(list);
		mDBAdapter.close();
		return list;
	}



	public void setView(ArrayList<String> info)
	{


		ListViewCustomAdapter sAdapter = new ListViewCustomAdapter(lview.getContext(),R.layout.list_item,info);
		lview.setAdapter(sAdapter);

	}







	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK))
		{
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}





	/**
	 * Dialog to prompt users to enable GPS on the device.
	 */
	@SuppressLint("ValidFragment")
	private class EnableGpsDialogFragment extends DialogFragment {


		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())
			.setTitle(R.string.enable_gps)
			.setMessage(R.string.enable_gps_dialog)
			.setPositiveButton(R.string.enable_gps, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					enableLocationSettings();
				}
			})
			.create();
		}
	}

	// Method to launch Settings
	private void enableLocationSettings() {
		Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(settingsIntent);
	}







	/*
	 * Sub Class for Asynchronous Task
	 */
	class GetList extends AsyncTask<String,Void,ArrayList<String> >
	{

		LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
		LinearLayout linlaContainer = (LinearLayout) findViewById(R.id.ListViewContainer); 
		@Override
		protected void onPreExecute() 
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
			linlaContainer.setVisibility(View.GONE);
			linlaHeaderProgress.setVisibility(View.VISIBLE);
		}


		protected ArrayList<String>  doInBackground(String... params) 
		{
			ArrayList<String> result = null;
			// TODO Auto-generated method stub
			if(params[0].equals("showNearBy"))
				result=showNearBy();
			else if(params[0].equals("showPopular"))
				result=showPopular();
			else if(params[0].equals("showAZ"))
				result=showAZ();
			else if(params[0].equals("showCategory")) 
				result=showCategory();
			return result;
		}

		/*@Override
   	    protected void onCancelled() {
   	        super.onCancelled();

   	        linlaHeaderProgress.setVisibility(View.GONE);
			linlaContainer.setVisibility(View.VISIBLE);

   	        // ask if user wants to try again
   	    }*/

		@Override
		protected void onPostExecute(ArrayList<String> result) 
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			linlaHeaderProgress.setVisibility(View.GONE);
			linlaContainer.setVisibility(View.VISIBLE);

			if(result!=null)
				setView(result);
			else
			{
				result=new 	ArrayList<String>();
				//result.add("Sorry Unable to find your latest Location");
				Toast.makeText(ShopListView.this,"Sorry Your latest Location is not available..",Toast.LENGTH_SHORT).show();
				setView(result);
			}

		}

	}



	@Override
	protected void onStart() {
		super.onStart();

		// Check if the GPS setting is currently enabled on the device.
		// This verification should be done during onStart() because the system calls this method
		// when the user returns to the activity, which ensures the desired location provider is
		// enabled each time the activity resumes from the stopped state.     
		RadioButton ListRadioButton=(RadioButton) findViewById(R.id.radioList);
		ListRadioButton.setChecked(true);   
		LocationManager locationManager =
				(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!gpsEnabled) {
			// Build an alert dialog here that requests that the user enable
			// the location services, then when the user clicks the "OK" button,
			// call enableLocationSettings()
			Log.i(TAG,"Location not enabled");
			new EnableGpsDialogFragment().show(getSupportFragmentManager(), "enableGpsDialog");
		}

		RadioButton ListRadioPopular=(RadioButton) findViewById(R.id.radioPopular);
		RadioButton ListRadioAZ=(RadioButton) findViewById(R.id.radioAZ);
		RadioButton ListRadioCategory=(RadioButton) findViewById(R.id.radioCategory);
		RadioButton ListRadioNearBy=(RadioButton) findViewById(R.id.radioNearBy);

		if(!ListRadioAZ.isChecked()&&!ListRadioPopular.isChecked()&&!ListRadioCategory.isChecked()&&!ListRadioNearBy.isChecked())
		{
			ListRadioAZ.setChecked(true);
		}

		lTracker=new LocationTracker(ShopListView.this);
		Log.i(TAG,"Location Tracker Started");


		Log.i(TAG,"I am in Main Activity Start");


	}


	@Override
	public void onPause(){
		super.onPause();
		lTracker.stopTracking();

	}

	@Override
	public void onStop(){
		super.onStop();
		lTracker.stopTracking();

	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		lTracker.stopTracking();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent=null;
		switch (item.getItemId()) {
		case R.id.getting_started:
			intent = new Intent(this,GettingStarted.class);
			startActivity(intent);
			break;
		case R.id.send_feedback:
			intent = new Intent(this,SendFeedback.class);
			startActivity(intent);
			break;
		case R.id.about:
			//Toast.makeText(this, "About is Selected", Toast.LENGTH_SHORT).show();
			intent = new Intent(this,About.class);
			startActivity(intent);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	} 


}



