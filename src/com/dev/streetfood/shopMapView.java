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
  static final LatLng HAMBURG = new LatLng(32.7300, 74.8700);
	  static final LatLng KIEL = new LatLng(32.7500, 74.8700);
	  static final LatLng Ish = new LatLng(32.7400, 74.8700);
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
    	.position(HAMBURG)
        .title("Pahalwan")
        .snippet("I am in Jammu"));
   mmap.addMarker(new MarkerOptions()
        .position(KIEL)
        .title("Ankush Home")
        .snippet("Hi Ankush")
        );
    mmap.addMarker(new MarkerOptions()
    .position(Ish)
    .title("Ishan Home")
    .snippet("Hi Ishan")
    );

    // Move the camera instantly to hamburg with a zoom of 15.
    mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

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
