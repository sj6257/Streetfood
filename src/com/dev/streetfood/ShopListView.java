package com.dev.streetfood;


import java.util.ArrayList;



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
import android.widget.ProgressBar;
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

	public static Location currentLocation;
	LocationTracker lTracker;
	private static final String TAG = "ShopListView";
	private String mIntent="showPopular";
	ListView lview;
	boolean flagCategory=false;
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
	  			  //Variable to sent an intent to map view so that it will show corresponding view
	  			  mIntent="showPopular";
	  			  new GetList().execute("showPopular");
	  		                   	              break;
	  		  case R.id.radioAZ :
	  			  Log.i(TAG,"AZ Radio Button Selected");
	  			//Variable to sent an intent to map view so that it will show corresponding view
	  			 mIntent="showAZ";
	  			  //showAZ();
	  			new GetList().execute("showAZ");
	               break;
	  		  case R.id.radioCategory: 
	  			  Log.i(TAG,"Category Radio Button Selected");
	  			//Variable to sent an intent to map view so that it will show corresponding view
	  			 mIntent="showPopular";
	  			 // showCategory();
	  			new GetList().execute("showCategory");
	               break;
	  		  case R.id.radioNearBy :
	  			  Log.i(TAG,"NearBy Radio Button Selected");
	  			//Variable to sent an intent to map view so that it will show corresponding view
	  			 mIntent="showNearBy";
	  			//showNearBy();
	  			new GetList().execute("showNearBy");
	               break;
	  		  default: showFeatured();
	  		      Log.i(TAG,"No Radio Selected");
	  		 
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
		         if(flagCategory)
		         {
		          Log.i(TAG,"flagCategory : True");
		         intent = new Intent(getApplicationContext(), ShopCategoryView.class);
		         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		         
		         flagCategory=false;
		         Log.i(TAG,"flagCategory forced to : False");
		         }
		         else
		         {
            	 intent = new Intent(getApplicationContext(), ShopDetailView.class);
            	 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	 Log.i(TAG,"flagCategory : false");
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
		flagCategory=false;
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
		
		flagCategory=false;
		ArrayList<String> list=new ArrayList<String>();
		list=null;
		if(currentLocation!=null)
		{
		String sql="select shopName shopName from streetShopInfo where distance<3";
		StreetFoodDataBaseAdapter mDBAdapter= new StreetFoodDataBaseAdapter(this);
		mDBAdapter.createDatabase();       
		mDBAdapter.open();
		//mDBAdapter.updateDistance();
		Log.i(TAG,"Requesting info from getInfo function");
		list=mDBAdapter.getInfo(sql,"shopName");
		Log.i(TAG,"Cursor Values Retrived into Array list");
		//setView(list);
		mDBAdapter.close();
		
		}
			
		 return list;
	}


	public ArrayList<String> showAZ(){
		ArrayList<String> list=new ArrayList<String>();
		flagCategory=false;
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
		flagCategory=true;
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
		
		/*
		 * Log.i(TAG,"Setting View");
		 

		ArrayAdapter<String> adapter = new ArrayAdapter<String>
		(this,android.R.layout.simple_list_item_1, android.R.id.text1,info);

		Log.i(TAG,"Array Adapter Set");
		Log.i(TAG,info.toString());
		// Assign adapter to ListView
		Log.i(TAG,"Attaching Arrya Adapter to List View");
		lview.setAdapter(adapter);
		Log.i(TAG,"View Set Succesfully");
		
		*/
		 /* create and Adapter* */
        ListViewCustomAdapter sAdapter = new ListViewCustomAdapter(lview.getContext(),R.layout.list_item,info);
        
        /* set the list's adapter */
        lview.setAdapter(sAdapter);

	}
	
	

	@Override
	protected void onStart() {
	    super.onStart();
		
		 // Check if the GPS setting is currently enabled on the device.
	     // This verification should be done during onStart() because the system calls this method
	     // when the user returns to the activity, which ensures the desired location provider is
	     // enabled each time the activity resumes from the stopped state.     
	       
	    LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            // Build an alert dialog here that requests that the user enable
            // the location services, then when the user clicks the "OK" button,
            // call enableLocationSettings()
            new EnableGpsDialogFragment().show(getSupportFragmentManager(), "enableGpsDialog");
        }
        lTracker=new LocationTracker(ShopListView.this);
		currentLocation=lTracker.getLocation();
		Log.i(TAG,"Location Tracker Started");
	    new Thread(new Runnable() {
	            public void run() {
	            	
	            	
	            	
	        		if(currentLocation!=null)   
	        		{Log.i(TAG,"Location Received");   
	        		StreetFoodDataBaseAdapter mDBAdapter= new StreetFoodDataBaseAdapter(ShopListView.this);
	        		mDBAdapter.createDatabase();       
	        		mDBAdapter.open();
	        		mDBAdapter.updateDistance();
	        		mDBAdapter.close();
	        		}
	            }
	          }).start();
	    
	    
	    
		   
	  //default show Popular Shops
	    Log.i(TAG,"I am in Main Activity Start");
	 /*   
	    RadioButton ListRadioButton=(RadioButton) findViewById(R.id.radioList);
  		ListRadioButton.setChecked(true);
  	    RadioButton ListRadioPopular=(RadioButton) findViewById(R.id.radioPopular);
 	    RadioButton ListRadioAZ=(RadioButton) findViewById(R.id.radioAZ);
 	    RadioButton ListRadioCategory=(RadioButton) findViewById(R.id.radioCategory);
 	    RadioButton ListRadioNearBy=(RadioButton) findViewById(R.id.radioNearBy);
	  
	    if(ListRadioPopular.isChecked())
	    {
	    	 mIntent="showPopular";
	    	new GetList().execute("showPopular");
	    }
	    else if (ListRadioAZ.isChecked()){
	    	 mIntent="showAZ";
	    	new GetList().execute("showAZ");
	    }
	    else if (ListRadioCategory.isChecked()){
	    	 mIntent="showCategory";
	    	new GetList().execute("showCategory");
	    }
	    else if (ListRadioNearBy.isChecked())
	    {
	    	 mIntent="showNearBy";
	    	new GetList().execute("showNearBy");
	    }
	    else showFeatured();
	  		  */   
	  				
	}
	
	@Override
	 public void onResume()
	    {
	       super.onResume();
	       Log.d(TAG, "In the onResume() event");
	       
	    }

	@Override
	public void onStop(){
		super.onStop();
		lTracker.stopTracking();
		
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

   	
   		@Override
   		protected void onPostExecute(ArrayList<String> result) 
   		{
   			// TODO Auto-generated method stub
   			super.onPostExecute(result);
   		    if(result!=null)
   		    {
   			linlaHeaderProgress.setVisibility(View.GONE);
   			linlaContainer.setVisibility(View.VISIBLE);
   			
   			setView(result);
   		    }
   		    /*else
   		    Toast.makeText(ShopListView.this,"Sorry Your Location not available..",Toast.LENGTH_LONG).show();
   				*/
   			
   		}
       	
       }
   

}
       
     

