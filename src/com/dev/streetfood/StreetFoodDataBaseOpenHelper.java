package com.dev.streetfood;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StreetFoodDataBaseOpenHelper extends SQLiteOpenHelper 
{
	
	private static String TAG = "DataBaseHelper"; // Tag just for the LogCat window 	 
	private static String DB_PATH = "";           // destination path (location) of our database on device
	private static String DB_NAME ="Brain";       // Database name 
	private SQLiteDatabase mDataBase;             // Database Variable
	private final Context mContext;               // Context Variable
	private final static int DB_VERSION=1;          // Database Version
	
	// constructor for the StreetFoodDataBaseOpenHelper class
	
	public StreetFoodDataBaseOpenHelper(Context context)  
	{ 
	    super(context, DB_NAME, null, DB_VERSION);   
	    DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";  //location of Database to copy
	    this.mContext = context; 
	} 
	
	
	public void createDataBase() throws IOException 
	{ 
	    //If database not exists copy it from the assets 

	    boolean mDataBaseExist = checkDataBase(); 
	    if(!mDataBaseExist) 
	    { 
	    	
	        this.getReadableDatabase(); 
	        this.close(); 
	        try  
	        { 
	            //Copy the database from assets
	        	Log.i(TAG,"Database does not Exists");
	        	Log.i(TAG,"Copying Database from Assets ");
	            copyDataBase(); 
	            Log.e(TAG, "Database Created Succesfully"); 
	        }  
	        catch (IOException mIOException)  
	        { 
	            throw new Error("ErrorCopyingDataBase"); 
	        } 
	    } 
	} 
	
	//Check that the database exists here: /data/data/your package/databases/DB Name
	
    private boolean checkDataBase() 
    { 
        Log.i(TAG,"Checking Database is Already extest or Not");
    	File dbFile = new File(DB_PATH + DB_NAME); 
        Log.v("dbFile", dbFile + "   "+ dbFile.exists()); 
        return dbFile.exists(); 
    } 
 
    //Copy the database from assets 
    
    private void copyDataBase() throws IOException 
    { 
    	Log.i(TAG,"Copying Database from Assets Started ");
        InputStream mInput = mContext.getAssets().open(DB_NAME); 
        String outFileName = DB_PATH + DB_NAME; 
        OutputStream mOutput = new FileOutputStream(outFileName); 
        byte[] mBuffer = new byte[1024]; 
        int mLength; 
        while ((mLength = mInput.read(mBuffer))>0) 
        { 
            mOutput.write(mBuffer, 0, mLength); 
        } 
        mOutput.flush(); 
        mOutput.close(); 
        mInput.close(); 
        Log.i(TAG,"Copying Database from Assets Completed ");
    } 
    
  //Open the database, so we can query it 
    
    public boolean openDataBase() throws SQLException 
    { 
        Log.i(TAG,"Opening Database Started");
    	String mPath = DB_PATH + DB_NAME; 
        Log.v("mPath", mPath); 
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY); 
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS); 
        Log.i(TAG,"Opening Database Completed");
        return mDataBase != null; 
    }
    
    @Override 
    public synchronized void close()  
    { 
        if(mDataBase != null) 
            mDataBase.close(); 
        super.close(); 
    }

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	} 

}