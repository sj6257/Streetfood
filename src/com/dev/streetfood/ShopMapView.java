package com.dev.streetfood;
import java.util.ArrayList;


import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.support.v4.app.FragmentActivity;

import android.annotation.TargetApi;
public class ShopMapView extends FragmentActivity implements OnInfoWindowClickListener {
  
private static final String TAG = "ShopMapView";
ArrayList<BookMark> result = new ArrayList<BookMark>();
private static LocationTracker lTracker; 
  @TargetApi(11)
    @Override
    public void onCreate(Bundle savedInstanceState) {
  Log.i(TAG,"In ShopMapView Class");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_map_view);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1)
    	{
    		//Action bar code for android devices with android version more than gingerbread
    		Log.i(TAG,"Build.VERSION.SDK_INT : "+Build.VERSION.SDK_INT);
    		Log.i(TAG,"Build.VERSION_CODES.GINGERBREAD_MR1: "+Build.VERSION_CODES.GINGERBREAD_MR1);
    		if(ViewConfiguration.get(ShopMapView.this).hasPermanentMenuKey())
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
    		
      
        //Intent intent=getIntent();
        //Bundle b = getIntent().getExtras();
      
      RadioGroup radioGroupListSelector = (RadioGroup) findViewById(R.id.radio_group_map_selector);
    radioGroupListSelector.setOnCheckedChangeListener(new OnCheckedChangeListener() 
       {
    
           public void onCheckedChanged(RadioGroup group, int checkedId) {
               // checkedId is the RadioButton selected
         
            switch (checkedId) {
       case R.id.radioPopular : 
       Log.i(TAG,"Popular Radio Button Selected");
       new GetMap().execute("showPopular");
       break;
       case R.id.radioAZ :
       Log.i(TAG,"AZ Radio Button Selected");
       new GetMap().execute("showAZ");
                  break;
      
       case R.id.radioNearBy :
       Log.i(TAG,"NearBy Radio Button Selected");
       
       //Toast.makeText(ShopMapView.this, "Gathering Near by Location...", Toast.LENGTH_LONG).show();
       Toast.makeText(ShopMapView.this,"Searching near by Street Shops..",Toast.LENGTH_LONG).show();
       new GetMap().execute("showNearBy");
                  break;
       
      }
           }
       }); 
      
    Bundle b = getIntent().getExtras();
    String item = (String) b.get("itemName");
    String fromView=(String) b.get("view");
      if(fromView.equals("Detail"))
      {
      Log.i(TAG,"In Map View.FromView "+fromView+"With Item "+item);
      new GetMap().execute("Detail",item);
      }
      else if(fromView.equals("Category"))
      {
      Log.i(TAG,"In Map View.FromView "+fromView+"With Item "+item);
      
      new GetMap().execute("showCategory",item); 
      }
      else if(fromView.equals("List")&&item.equals("showNearBy"))
      {
    	  Log.i(TAG,"In Map View.FromView "+fromView+"With Item "+item);
    	RadioButton radioNearByRadioButton=(RadioButton) findViewById(R.id.radioNearBy);
    	radioNearByRadioButton.setChecked(true);
    	
      }
      else if(fromView.equals("List")&&item.equals("showPopular"))
      {
    	  Log.i(TAG,"In Map View.FromView "+fromView+"With Item "+item);
    	  RadioButton radioPopularRadioButton=(RadioButton) findViewById(R.id.radioPopular);
    	  radioPopularRadioButton.setChecked(true);
    	
    	  
      }
      else if(fromView.equals("List")&&item.equals("showAZ"))
      {
    	  Log.i(TAG,"In Map View.FromView "+fromView+"With Item "+item);
    	  RadioButton radioAZRadioButton=(RadioButton) findViewById(R.id.radioAZ);
    	  radioAZRadioButton.setChecked(true);
    	 
      }
      else
      {
    	  
      Log.i(TAG,"In Map View.FromView "+fromView+"With Item "+item);
     // showNearBy();
      RadioButton radioPopularRadioButton=(RadioButton) findViewById(R.id.radioPopular);
	  radioPopularRadioButton.setChecked(true);
	 
      
      }
        
      //sql="select shopName  from streetShopInfo where category=\""+category+"\"";
    }

 
    
    
   ArrayList<BookMark>  showPopular()
   {
//Retrieving Values from database
   Log.i(TAG,"Populating Popular Stall list");
   ArrayList<BookMark> result = new  ArrayList<BookMark>();
   result=null;
   String sql="select  S.shopName shopName,IFNULL(S.shopInfo,\"Not Available\") shopInfo,ifnull(S.address1,\'Not Available\') address,S.latitude latitude,S.longitude longitude from streetShopInfo AS S JOIN ratings AS R  where S.shopName=R.shopName and R.overall >0 order by S.shopName";
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
   return result;
   //setView(result);
   }
   
   ArrayList<BookMark> showAZ()
   {
	   //Retrieving Values from database
   Log.i(TAG,"Populating AZ Stall list");
   ArrayList<BookMark> result = new ArrayList<BookMark>();
   String sql="select  S.shopName shopName,IFNULL(S.shopInfo,\"Not Available\") shopInfo,ifnull(S.address1,\'Not Available\') address,S.latitude latitude,S.longitude longitude from streetShopInfo AS S  order by S.shopName";
   Log.i(TAG,"Creating Adapter for Fetching Data");
   StreetFoodDataBaseAdapter mDBAdapter= new StreetFoodDataBaseAdapter(this);
   Log.i(TAG,"Adapter Ready..");
   Log.i(TAG,"Creating/Opening Database");
   mDBAdapter.createDatabase();       
   mDBAdapter.open();
   Log.i(TAG,"Requesting info from getInfo function");
   result=mDBAdapter.getInfoForMap(sql);
   Log.i(TAG,"Information Retrived Passing it to SetView");
   //setView(result);
   mDBAdapter.close();
   return result;
   }
   
   ArrayList<BookMark> showNearBy()
   {
//Retrieving Values from database
   Log.i(TAG,"Populating NearBy Stall list");
   ArrayList<BookMark> result = new ArrayList<BookMark>();
   StreetFoodDataBaseAdapter mDBAdapter= new StreetFoodDataBaseAdapter(this);
   String sql="select  S.shopName shopName,IFNULL(S.shopInfo,\"Not Available\") shopInfo,ifnull(S.address1,\'Not Available\') address,S.latitude latitude,S.longitude longitude from streetShopInfo AS S where S.distance<3  order by S.shopName LIMIT 10";
   Log.i(TAG,"Creating Adapter for Fetching Data");
   Log.i(TAG,"Adapter Ready..");
   Log.i(TAG,"Creating/Opening Database");
   mDBAdapter.createDatabase();       
   mDBAdapter.open();
   ShopListView.currentLocation=lTracker.getLocation();
	if(mDBAdapter.validDistance() && ShopListView.currentLocation!=null && ShopListView.currentLocation.getLatitude()!=0 )     
	{
     Log.i(TAG,"Now Fetching Near By Location from DB");
     result=mDBAdapter.getInfoForMap(sql);
     Log.i(TAG,"Information Retrived Passing it to SetView");
     mDBAdapter.close();
   } 
	else
	{
	    if( ShopListView.currentLocation!=null &&  ShopListView.currentLocation.getLatitude()!=0 )   
	       {
	         	   Log.i(TAG,"Location Received");   
	         	   mDBAdapter.updateDistance();
	         	  result=mDBAdapter.getInfoForMap(sql);
	         	   mDBAdapter.close();
	         	  
	       }
	}
   
   	
   return result;
   }
    
  
   
   ArrayList<BookMark> showDetail(String shopName)
   {
  
  //Retrieving Values from database
   Log.i(TAG,"Populating Detail Stall list");
   ArrayList<BookMark> result = new ArrayList<BookMark>();
   String sql="select  S.shopName shopName,IFNULL(S.shopInfo,\"Not Available\") shopInfo,ifnull(S.address1,\'Not Available\') address,S.latitude latitude,S.longitude longitude from streetShopInfo AS S  where S.shopName=\""+shopName+"\"";
   Log.i(TAG,"Creating Adapter for Fetching Data");
   StreetFoodDataBaseAdapter mDBAdapter= new StreetFoodDataBaseAdapter(this);
   Log.i(TAG,"Adapter Ready..");
   Log.i(TAG,"Creating/Opening Database");
   mDBAdapter.createDatabase();       
   mDBAdapter.open();
   Log.i(TAG,"Requesting info from getInfo function");
   result=mDBAdapter.getInfoForMap(sql);
   Log.i(TAG,"Information Retrived Passing it to SetView");
   //setView(result);
   mDBAdapter.close();
   return result;
   
   }
   
   
   
   @Override
   public void onInfoWindowClick(Marker marker) {
	Log.i(TAG,"Directions clicked");
  
 // getDirection(marker.getPosition());
  
    if(ShopListView.currentLocation!=null)
  	getDirection(marker.getPosition());
	else
    Toast.makeText(ShopMapView.this,"Sorry Your latest is Location not available..",Toast.LENGTH_LONG).show();	
	
    
   }
   
   protected void getDirection(LatLng LatLAng) {
// TODO Auto-generated method stub
Location myLocation=ShopListView.currentLocation;

Log.i("Lat and LOg","Hi"+LatLAng);
double dlongtd = LatLAng.longitude;
double dlattd=LatLAng.latitude;
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

   ArrayList<BookMark> showCategory(String category)
   {
  
  //Retrieving Values from database
   Log.i(TAG,"Populating Category Stall list");
   ArrayList<BookMark> result = new ArrayList<BookMark>();
String sql="select  S.shopName shopName,IFNULL(S.shopInfo,\"Not Available\") shopInfo,ifnull(S.address1,\'Not Available\') address,S.latitude latitude,S.longitude longitude from streetShopInfo AS S where S.category=\""+category+"\"";
Log.i(TAG,"Creating Adapter for Fetching Data");
StreetFoodDataBaseAdapter mDBAdapter= new StreetFoodDataBaseAdapter(this);
Log.i(TAG,"Adapter Ready..");
Log.i(TAG,"Creating/Opening Database");
mDBAdapter.createDatabase();       
mDBAdapter.open();
Log.i(TAG,"Requesting info from getInfo function");
result=mDBAdapter.getInfoForMap(sql);
Log.i(TAG,"Information Retrived Passing it to SetView");
//setView(result);
mDBAdapter.close();
return result;
  
   }
   
   void setView(ArrayList<BookMark> listOfBookMark)
   {
  
  double lati = 18.539394,longi=73.863206; //Co-Ordinate of Pune
  LatLng position=new LatLng(lati,longi) ;
  GoogleMap mmap;
  mmap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
  mmap.clear();
  mmap.setMyLocationEnabled(true);
  mmap.setOnInfoWindowClickListener(this);
  for(BookMark bookMark :listOfBookMark){
  
 // Log.i(TAG,bookMark.getTitle()+":"+bookMark.getSnippet());
  mmap.addMarker(new MarkerOptions()
    .position(bookMark.getPosition())
  .title(bookMark.getTitle())
  .snippet("Click To Get Directions"));

}
  
  // Move the camera instantly to position with a zoom of 15.
  //mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
  // Zoom in, animating the camera.
  //mmap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
  
  // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
  CameraPosition cameraPosition = new CameraPosition.Builder()
      .target(position)      // Sets the center of the map to Mountain View
      .zoom(11)                   // Sets the zoom
      .tilt(45)                   // Sets the tilt of the camera to 30 degrees
      .build();                   // Creates a CameraPosition from the builder
  mmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
  
 
   }
   
   /*
    * Sub Class for Asynchronous Task
    */
   class GetMap extends AsyncTask<String,Void, ArrayList<BookMark> >
   {

	   ProgressBar progressBar;
		@Override
		protected void onPreExecute() 
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
		
			
		}

		
		protected  ArrayList<BookMark>  doInBackground(String... params) 
		{
			 ArrayList<BookMark> result = null;
			 
			// TODO Auto-generated method stub
			if(params[0].equals("showNearBy"))
				result=showNearBy();
			else if(params[0].equals("showPopular"))
				result=showPopular();
			else if(params[0].equals("showAZ"))
				result=showAZ();
			else if(params[0].equals("showCategory")) 
				result=showCategory(params[1]);
			else if(params[0].equals("Detail")) 
				result=showDetail(params[1]);
		    
			return result;
		}

	
		@Override
		protected void onPostExecute( ArrayList<BookMark> result) 
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//progressBar.setVisibility(View.GONE);
			setView(result);
			
		}
   	
   }
   
   
   @Override
   public void onStart()
    {
       super.onStart();
       lTracker=new LocationTracker(ShopMapView.this);
    }
   
   @Override
	protected void onStop() {
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
   

}
    
   
   
   
   

