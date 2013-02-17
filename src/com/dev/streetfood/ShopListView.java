package com.dev.streetfood;


import java.util.ArrayList;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.app.ActionBar;

public class ShopListView extends Activity {

	private static final String TAG = "ShopListView";
	ListView lview;
	boolean flagCategory=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_list_view);
		lview= (ListView) findViewById(R.id.listView1);
		 
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
	  			  showPopular();
	  		                   	              break;
	  		  case R.id.radioAZ :
	  			  Log.i(TAG,"AZ Radio Button Selected");
	  			  showAZ();
	               break;
	  		  case R.id.radioCategory: 
	  			  Log.i(TAG,"Category Radio Button Selected");
	  			 
	  			  showCategory();
	               break;
	  		  case R.id.radioNearBy :
	  			  Log.i(TAG,"NearBy Radio Button Selected");
	  			showNearBy();
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
         b.putString("itemName","List");
         b.putString("view","List");
         intent.putExtras(b);
         startActivity(intent);
	}
	

	public void showFeatured()
	{
		flagCategory=false;
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
		mDBAdapter.close();
	}

	public void showPopular(){
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
		setView(list);
		mDBAdapter.close();
	}

	public void showNearBy() {
		flagCategory=false;
		ArrayList<String> list=new ArrayList<String>();
		String sql="select shopName from streetShopInfo LIMIT 10";
		StreetFoodDataBaseAdapter mDBAdapter= new StreetFoodDataBaseAdapter(this);
		mDBAdapter.createDatabase();       
		mDBAdapter.open();
		list=mDBAdapter.getInfo(sql,"shopName");
		Log.i(TAG,"Cursor Values Retrived into Array list");
		setView(list);
		mDBAdapter.close();

	}


	public void showAZ(){
		ArrayList<String> list=new ArrayList<String>();
		flagCategory=false;
		String sql="select shopName from streetShopInfo order by shopName";
		StreetFoodDataBaseAdapter mDBAdapter= new StreetFoodDataBaseAdapter(this);
		mDBAdapter.createDatabase();       
		mDBAdapter.open();
		list=mDBAdapter.getInfo(sql,"shopName");
		Log.i(TAG,"Cursor Values Retrived into Array list");
		setView(list);
		mDBAdapter.close();
	}

	public void showCategory(){
		ArrayList<String> list=new ArrayList<String>();
		flagCategory=true;
		String sql="select  distinct category from streetShopInfo order by category";
		StreetFoodDataBaseAdapter mDBAdapter= new StreetFoodDataBaseAdapter(this);
		mDBAdapter.createDatabase();       
		mDBAdapter.open();
		list=mDBAdapter.getInfo(sql,"category");
		Log.i(TAG,"Cursor Values Retrived into Array list");
		setView(list);
		mDBAdapter.close();
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
	  //default show Popular Shops
	    Log.i(TAG,"I am in Main Activity Start");
	    RadioButton ListRadioButton=(RadioButton) findViewById(R.id.radioList);
  		ListRadioButton.setChecked(true);
  	    RadioButton ListRadioPopular=(RadioButton) findViewById(R.id.radioPopular);
 	    RadioButton ListRadioAZ=(RadioButton) findViewById(R.id.radioAZ);
 	    RadioButton ListRadioCategory=(RadioButton) findViewById(R.id.radioCategory);
 	    RadioButton ListRadioNearBy=(RadioButton) findViewById(R.id.radioNearBy);
	  
	    if(ListRadioPopular.isChecked())
	    	showPopular();
	    else if (ListRadioAZ.isChecked())
	    	showAZ();
	    else if (ListRadioCategory.isChecked())
	        showCategory();
	    else if (ListRadioNearBy.isChecked())
	    	showNearBy();
	    else showFeatured();
	  		     
	  				
	}
	
	 public void onResume()
	    {
	       super.onResume();
	       Log.d(TAG, "In the onResume() event");
	       
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.option_menu, menu);
		return true;
	}
	
	
}
