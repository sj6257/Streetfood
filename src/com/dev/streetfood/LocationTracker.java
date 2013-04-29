package com.dev.streetfood;
import java.util.Date;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class LocationTracker extends FragmentActivity {
	private static final String TAG = "Location Tracker";
	private static final int TEN_SECONDS = 10000;
	private static final int TEN_METERS = 10;
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	private Location currentLocation=null;
	Context Mycontext;
	LocationManager mLocationManager;
	public LocationTracker(Context context)
	{
		this.Mycontext=context;
		Log.i(TAG,"I am in constructor");

		// mLocationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mLocationManager= (LocationManager) Mycontext.getSystemService(Context.LOCATION_SERVICE);
		Location gpsLocation = null;
		Location networkLocation = null;
		mLocationManager.removeUpdates(listener);
		// Request updates from both fine (gps) and coarse (network) providers.
		gpsLocation = requestUpdatesFromProvider(LocationManager.GPS_PROVIDER, R.string.not_support_gps);
		networkLocation = requestUpdatesFromProvider(LocationManager.NETWORK_PROVIDER, R.string.not_support_network);
		updateUILocation(getBestLocation(gpsLocation, networkLocation)); 



	}






	private final LocationListener listener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			// A new location update is received.  Do something useful with it.  Update the UI with
			// the location update.
			updateUILocation(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};


	/**
	 * Method to register location updates with a desired location provider.  If the requested
	 * provider is not available on the device, the app displays a Toast with a message referenced
	 * by a resource id.
	 *
	 * @param provider Name of the requested provider.
	 * @param errorResId Resource id for the string message to be displayed if the provider does
	 *                   not exist on the device.
	 * @return A previously returned {@link android.location.Location} from the requested provider,
	 *         if exists.
	 */
	private Location requestUpdatesFromProvider(final String provider,final int errorResId) {
		Log.i(TAG,"requestUpdatesFromProvider");
		Location location = null;
		if (mLocationManager.isProviderEnabled(provider)) {
			mLocationManager.requestLocationUpdates(provider, TEN_SECONDS, TEN_METERS, listener);
			location = mLocationManager.getLastKnownLocation(provider);
		} else {
			Log.e(TAG,"Error:No GPS Access");
			Toast.makeText(Mycontext, errorResId, Toast.LENGTH_LONG).show();

		}
		return location;
	}

	protected Location getBestLocation(Location gpsLocation,Location networkLocation){
		//if we have only one location available, the choice is easy

		if(gpsLocation!=null && networkLocation!=null){

			// both are old return the newer of those two
			if (gpsLocation.getTime() > networkLocation.getTime() && isLatest(gpsLocation)) {
				Log.d(TAG, "Returning Latest GPS Location");
				return gpsLocation;

			} else if (isLatest(networkLocation))  {
				Log.d(TAG, "GPS is old, Network is current, returning network");
				return networkLocation;
			}
			else
				return null;
		}
		else if (gpsLocation == null && networkLocation!=null) {
			Log.d(TAG, "No GPS Location available.");
			Log.d(TAG, "Returning Network Location .");

			if (isLatest(networkLocation)) {
				Log.d(TAG, "GPS is old, Network is current, returning network");
				return networkLocation;
			}	
			else
				return null;
		}

		else if (networkLocation == null && gpsLocation != null ) {

			Log.d(TAG, "No Network Location available");
			Log.d(TAG, "Returning GPS Location .");

			if (isLatest(gpsLocation)) {
				Log.d(TAG, "Returning Latest GPS Location");
				return gpsLocation;
			}
			else
				return null;
		}
		else
			return null;



	}


	boolean isLatest(Location currentLocation)
	{
		try {
			Date now= new Date();  
			long delta=now.getTime()-currentLocation.getTime();	
			Log.i(TAG,"Current Location delta is: "+delta);
			if (delta < TWO_MINUTES)
				return true;
			else
				return  false;
		}
		catch(Exception ex)
		{
			Log.e(TAG,ex.toString());	
			return  false;
		}

	}

	/** Determines whether one Location reading is better than the current Location fix.
	 * Code taken from
	 * http://developer.android.com/guide/topics/location/obtaining-user-location.html
	 *
	 * @param newLocation  The new Location that you want to evaluate
	 * @param currentBestLocation  The current Location fix, to which you want to compare the new
	 *        one
	 * @return The better Location object based on recency and accuracy.
	 */
	protected Location getBetterLocation(Location newLocation, Location currentBestLocation) {
		Log.i(TAG,"Selecting BetterLocation");
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return newLocation;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use the new location
		// because the user has likely moved.
		if (isSignificantlyNewer) {
			return newLocation;
			// If the new location is more than two minutes older, it must be worse
		} else if (isSignificantlyOlder) {
			return currentBestLocation;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(newLocation.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and accuracy
		if (isMoreAccurate) {
			return newLocation;
		} else if (isNewer && !isLessAccurate) {
			return newLocation;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return newLocation;
		}
		return currentBestLocation;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		Log.i(TAG,"isSameProvider");
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}


	private void updateUILocation(Location location) {
		// We're sending the update to a handler which then updates the UI with the new
		// location.
		Log.i(TAG,"Location Update Received");
		try{
			if(isLatest(location))
			{
				currentLocation=location;
				Log.i(TAG,"latest Location set");
			}
			else
				Log.i(TAG,"Scrapping Location");	  
		}
		catch(Exception ex)
		{
			Log.e(TAG,ex.toString());	
		}
	}


	public Location getLocation()
	{
		Log.i(TAG,"Returning latest Location from Tracker");
		return currentLocation;

	}

	public void stopTracking()
	{
		mLocationManager.removeUpdates(listener);
	}

}	
