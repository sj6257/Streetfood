package com.dev.streetfood;

import com.google.android.gms.maps.model.LatLng;

public class BookMark {

	
	String title;
	String snippet;
	double latitude;
	double longitude;
	LatLng position;
	
	public BookMark(String shopName, String shopInfo,double latitude,double longitude){
	        this.title = shopName;
	        this.snippet = shopInfo;
			this.position=new LatLng( latitude, longitude);
	    }

	// functions for retrieving values

	public String getTitle(){
		        return this.title;
		}

	public String getSnippet(){
		        return this.snippet;
		}	

	public LatLng getPosition(){
		        return this.position;
		}	
	

}
