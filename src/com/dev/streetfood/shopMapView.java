package com.dev.streetfood;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import android.content.Intent;
import android.util.Log;
import android.support.v4.app.FragmentActivity;

import android.annotation.TargetApi;
public class ShopMapView extends FragmentActivity {
  static final LatLng cafeDurga = new LatLng( 18.510398, 73.816323);
	  static final LatLng anaraseSamosevale = new LatLng(18.512167, 73.845769);
	  static final LatLng GardenVadapav = new LatLng(18.517681, 73.877832);
	  private GoogleMap mmap;
	  
	 
	   @TargetApi(11)
    @Override
    public void onCreate(Bundle savedInstanceState) {
		   Log.d("I am map","Map");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_map_view);
        Intent intent=getIntent();
        //Bundle b = getIntent().getExtras();
          mmap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
        .getMap();
       //addingmaker();
    mmap.addMarker(new MarkerOptions()
    	.position(cafeDurga)
        .title("Cafe Durga")
        .snippet("Place Famous for Cold Coffee"));
   mmap.addMarker(new MarkerOptions()
        .position(anaraseSamosevale)
        .title("Anarase Samosevale")
        .snippet("Best Samosa in Pune")
        );
    mmap.addMarker(new MarkerOptions()
    .position(GardenVadapav)
    .title("JJ Garden Vada Pav")
    .snippet("Famous Vada Pav Center in Pune")
    );

    // Move the camera instantly to hamburg with a zoom of 15.
    mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(anaraseSamosevale, 15));

    // Zoom in, animating the camera.
    mmap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    mmap.setMyLocationEnabled(true);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_shop_map_view, menu);
        return true;
    }
}
