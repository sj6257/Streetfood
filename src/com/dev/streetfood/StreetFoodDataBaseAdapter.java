package com.dev.streetfood;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
//import android.content.ContentValues; //Need While inserting values
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;


public class StreetFoodDataBaseAdapter {

	protected static final String TAG = "DataAdapter"; 

	private final Context mContext; 
	private SQLiteDatabase mDb; 
	private StreetFoodDataBaseOpenHelper mDbHelper; 

	//Constructor for StreetFoodDataBaseAdapter

	public StreetFoodDataBaseAdapter(Context context)  
	{ 
		this.mContext = context; 
		mDbHelper = new StreetFoodDataBaseOpenHelper(mContext); 
	} 

	public StreetFoodDataBaseAdapter createDatabase() throws SQLException  
	{ 
		try  
		{ 
			mDbHelper.createDataBase(); 
		}  
		catch (IOException mIOException)  
		{ 
			Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase"); 
			throw new Error("UnableToCreateDatabase"); 
		} 
		return this; 
	}

	public StreetFoodDataBaseAdapter open() throws SQLException  
	{ 
		try  
		{ 
			mDbHelper.openDataBase(); 
			mDbHelper.close(); 
			Log.i(TAG,"Asking for Readable Acess to DB");
			mDb = mDbHelper.getReadableDatabase(); 
			Log.i(TAG,"Readable Access Granted");
		}  
		catch (SQLException mSQLException)  
		{ 
			Log.e(TAG, "open >>"+ mSQLException.toString()); 
			throw mSQLException; 
		} 
		return this; 
	} 

	public void close()  
	{ 
		mDbHelper.close(); 
	}	

	/* public Cursor getTestData() 
    { 
        try 
        { 
            String sql ="SELECT EmployeeId, Name, Email FROm Employees"; 

            Cursor mCur = mDb.rawQuery(sql, null); 
            if (mCur!=null) 
            { 
               mCur.moveToNext(); 
            } 
            return mCur; 
        } 
        catch (SQLException mSQLException)  
        { 
            Log.e(TAG, "getTestData >>"+ mSQLException.toString()); 
            throw mSQLException; 
        } 
    }
	 */

	public ArrayList<String> getInfo(String sql,String column)
	{
		ArrayList<String> result = new ArrayList<String>();
		Log.i(TAG,"Getting Information..");
		Cursor mCur=null;
		try 
		{ 

			Log.i(TAG,"Executing Query: "+sql);
			mCur = mDb.rawQuery(sql, null); 
			Log.i(TAG,"Query Executed Successfully");
			if(mCur!=null)
			{
				if  (mCur.moveToFirst()) 
				{

					do {

						String columnValue = mCur.getString(mCur.getColumnIndex(column));
						//Log.i(TAG,"Values: "+shopName);
						result.add(columnValue);
					}while (mCur.moveToNext());

					mCur.close();
				}
				else
				{
					mCur.close();
					Log.e(TAG,"No Data Received from Query");
				}
			}


		}
		catch (Exception e)  
		{ 
			Log.e(TAG, "getInfo >>"+ e.toString()); 
			// throw mSQLException; 
		} 
		return result;

	}


	public ArrayList<String> updateDistance(Location currentLocation)
	{

		ArrayList<String> result = new ArrayList<String>();
		String sql="select shopName shopName,latitude latitude,longitude longitude from streetShopInfo";
		Cursor mCur=null;
		try 
		{ 

			Log.i(TAG,"Executing Query: "+sql);
			mCur = mDb.rawQuery(sql, null); 
			Log.i(TAG,"Query Executed Successfully");
			if(mCur!=null)
			{
				if  (mCur.moveToFirst()) 
				{

					do {
						double latitude=  mCur.getFloat(mCur.getColumnIndex("latitude"));
						double longitude= mCur.getFloat(mCur.getColumnIndex("longitude"));
						String shopName = mCur.getString(mCur.getColumnIndex("shopName"));
						updateRow(shopName,latitude,longitude,currentLocation);


					}while (mCur.moveToNext());

					mCur.close();
				}
				else
				{
					mCur.close();
					Log.e(TAG,"No Data Received from Query");
				}
			}


		}
		catch (Exception e)  
		{ 
			Log.e(TAG, "Exception While updating distance >>"+ e.toString()); 
			// throw mSQLException; 
		} 
		return result;

	}




	// Function to update values

	public boolean updateRow(String ShopName,double lati ,double longi,Location currentLocation) 
	{
		Location shopLocation =new Location("shopLocation");
		shopLocation.setLatitude(lati);
		shopLocation.setLongitude(longi);
		double distance=0; 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
		Date currentTime=new Date();
		distance=shopLocation.distanceTo(currentLocation);
		distance=distance/1000;
		try
		{
			ContentValues cv = new ContentValues();
			cv.put("distance", distance);
			cv.put("updated_on",dateFormat.format(currentTime));
			String whereClause="shopName=\""+ShopName+"\"";
			mDb.update("streetShopInfo", cv, whereClause, null);
			Log.d("streetShopInfo", "distance saved");
			return true;

		}
		catch(Exception ex)
		{
			Log.d("streetShopInfo", ex.toString());
			return false;
		}
	}



	public int getSingleIntVal(String sql)
	{
		int columnVal = 0;
		Log.i(TAG,"Getting Information..");
		Cursor mCur=null;
		try 
		{ 

			Log.i(TAG,"Executing Query : "+sql);
			mCur = mDb.rawQuery(sql, null);
			Log.i(TAG,"Query Executed Successfully");
			if(mCur!=null)
			{
				if  (mCur.moveToFirst()) 
				{

					do {

						columnVal= mCur.getInt(0);

					}while (mCur.moveToNext());

					mCur.close();
				}
				else
				{
					mCur.close();
					Log.e(TAG,"No Data Received from Query");
				}
			}

		}
		catch (Exception e)  
		{ 
			Log.e(TAG, "getSingleIntVal >>"+ e.toString()); 
			// throw mSQLException; 
		} 
		return columnVal;

	}

	public String getSingleStringVal(String sql)
	{
		String columnVal = null;
		Log.i(TAG,"Getting Information..");
		Cursor mCur=null;
		try 
		{ 

			Log.i(TAG,"Executing Query : "+sql);
			mCur = mDb.rawQuery(sql, null); 
			Log.i(TAG,"Query Executed Successfully");
			if(mCur!=null)
			{
				Log.i(TAG,"cursor not null");
				Log.i(TAG,"Cursor Contains:"+mCur.toString());
				if  (mCur.moveToFirst()) 
				{
					Log.i(TAG,"moved to 1st");

					do {
						Log.i(TAG,"in do");

						columnVal= mCur.getString(0);

						Log.i(TAG,"Column Val"+columnVal);

					}while (mCur.moveToNext());

					mCur.close();
					Log.i(TAG,"Curor Closeed");
				}
				else
				{
					Log.i(TAG,"I m in Else");
					mCur.close();
					Log.e(TAG,"No Data Received from Query");
				}
			}


		}
		catch (Exception e)  
		{ 
			Log.e(TAG, "getSingleStringVal >>"+ e.toString()); 
			// throw mSQLException; 
		} 
		return columnVal;

	}



	public ArrayList<BookMark> getInfoForMap(String sql)
	{
		ArrayList<BookMark> result = new ArrayList<BookMark>();

		Log.i(TAG,"Getting Information..");
		Cursor mCur=null;
		try 
		{ 

			Log.i(TAG,"Executing Query: "+sql);
			mCur = mDb.rawQuery(sql, null); 
			Log.i(TAG,"Query Executed Successfully");
			if(mCur!=null)
			{
				if  (mCur.moveToFirst()) 
				{

					do {

						String shopName = mCur.getString(mCur.getColumnIndex("shopName"));

						int hygiene=mCur.getInt(mCur.getColumnIndex("hygiene"));
						int quality=mCur.getInt(mCur.getColumnIndex("quality"));
						int service=mCur.getInt(mCur.getColumnIndex("service"));
						String shopInfo = mCur.getString(mCur.getColumnIndex("shopInfo"));
						String address  = mCur.getString(mCur.getColumnIndex("address"));
						double latitude=  mCur.getFloat(mCur.getColumnIndex("latitude"));
						double longitude= mCur.getFloat(mCur.getColumnIndex("longitude"));

						// Log.i(TAG,"BookMark: "+shopName+":"+shopInfo+":"+Double.toString(latitude)+":"+Double.toString(longitude));
						BookMark b1=new BookMark(shopName,hygiene,quality,service,shopInfo,address,latitude,longitude);
						result.add(b1);


					}while (mCur.moveToNext());

					mCur.close();
				}
				else
				{
					mCur.close();
					Log.e(TAG,"No Data Received from Query");
				}
			}


		}
		catch (Exception e)  
		{ 
			Log.e(TAG, "getInfo >>"+ e.toString()); 
			// throw mSQLException; 
		} 
		return result;

	}

	public boolean validDistance()
	{

		Log.i(TAG,"Checking Distance..");
		String sql="select distance,updated_on from streetShopInfo  order by updated_on desc limit 1";
		//String sql="select distance from streetShopInfo limit 1";
		String updatedOn = null;
		float distance = 0;
		long seconds=0;
		Cursor mCur=null;
		try 
		{ 

			Log.i(TAG,"Executing Query: "+sql);
			mCur = mDb.rawQuery(sql, null); 
			Log.i(TAG,"Query Executed Successfully");
			if(mCur!=null)
			{
				if  (mCur.moveToFirst()) 
				{

					do {
						distance=mCur.getFloat(mCur.getColumnIndex("distance"));
						Log.i(TAG,"distance: "+distance);
						updatedOn=mCur.getString(mCur.getColumnIndex("updated_on"));
						Log.i(TAG,"updated_on: "+updatedOn);
					}while (mCur.moveToNext());

					mCur.close();
				}
				else
				{
					mCur.close();
					Log.e(TAG,"No Data Received from Query");
				}
			}


		}
		catch (Exception e)  
		{ 
			Log.e(TAG, "getInfo >>"+ e.toString()); 
			// throw mSQLException; 
		} 


		try
		{
			Date CurrentTime=new Date();
			Log.i(TAG,"CurrentTime: "+CurrentTime);
			Date UpdateTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()).parse(updatedOn);

			Log.i(TAG,"UpdateTime: "+UpdateTime);
			seconds=(CurrentTime.getTime()-UpdateTime.getTime())/1000;
			Log.i(TAG,"seconds: "+seconds);
		}
		catch (Exception e)  
		{ 
			Log.e(TAG, "gotError while calculating date diff>>"+ e.toString()); 
			// throw mSQLException; 
		} 



		if(distance!=0 && seconds<10)
			//if(distance!=0)
		{
			Log.i(TAG,"Distance is Latest");
			return true;
		}
		else
		{
			Log.i(TAG,"Distance Outdated");

			return false;
		}

	}


}
