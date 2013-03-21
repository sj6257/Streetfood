package com.dev.streetfood;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.widget.RadioGroup;
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
 
  @TargetApi(11)
    @Override
    public void onCreate(Bundle savedInstanceState) {
  Log.i(TAG,"In ShopMapView Class");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_map_view);
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
       showPopular();
       break;
       case R.id.radioAZ :
       Log.i(TAG,"AZ Radio Button Selected");
       showAZ();
                  break;
      
       case R.id.radioNearBy :
       Log.i(TAG,"NearBy Radio Button Selected");
       showNearBy();
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
      showDetail(item);
      }
      else if(fromView.equals("Category"))
      {
      Log.i(TAG,"In Map View.FromView "+fromView+"With Item "+item);
      showCategory(item);
      }
      else
      {
      Log.i(TAG,"In Map View.FromView "+fromView+"With Item "+item);
      showNearBy();
      
      }
        
      //sql="select shopName  from streetShopInfo where category=\""+category+"\"";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.layout.menu, menu);
        return true;
    }
    
    
   void showPopular()
   {
//Retrieving Values from database
   Log.i(TAG,"Populating Popular Stall list");
   ArrayList<BookMark> result = new ArrayList<BookMark>();
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
setView(result);
mDBAdapter.close();
   }
   
   void showAZ()
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
setView(result);
mDBAdapter.close();
   }
   
   void showNearBy()
   {
//Retrieving Values from database
   Log.i(TAG,"Populating NearBy Stall list");
   ArrayList<BookMark> result = new ArrayList<BookMark>();
String sql="select  S.shopName shopName,IFNULL(S.shopInfo,\"Not Available\") shopInfo,ifnull(S.address1,\'Not Available\') address,S.latitude latitude,S.longitude longitude from streetShopInfo AS S  order by S.shopName LIMIT 10";
Log.i(TAG,"Creating Adapter for Fetching Data");
StreetFoodDataBaseAdapter mDBAdapter= new StreetFoodDataBaseAdapter(this);
Log.i(TAG,"Adapter Ready..");
Log.i(TAG,"Creating/Opening Database");
mDBAdapter.createDatabase();       
mDBAdapter.open();
Log.i(TAG,"Requesting info from getInfo function");
result=mDBAdapter.getInfoForMap(sql);
Log.i(TAG,"Information Retrived Passing it to SetView");
setView(result);
mDBAdapter.close();
   }
    
   void showDetail(String shopName)
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
setView(result);
mDBAdapter.close();
  
   }
   
   @Override
   public void onInfoWindowClick(Marker marker) {
  Log.i(TAG,"Directions clicked");
  getDirection(marker.getPosition());
    
   }
   
   protected void getDirection(LatLng LatLAng) {
// TODO Auto-generated method stub
Location myLocation=ShopListView.currentLocation;
//double dlongtd =result.get(0).getLongitude() ,dlattd=result.get(0).getLatitude();
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

   void showCategory(String category)
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
setView(result);
mDBAdapter.close();
  
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
   
}
