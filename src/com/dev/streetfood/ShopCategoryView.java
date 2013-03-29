package com.dev.streetfood;

import java.util.ArrayList;

import android.os.Build;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ShopCategoryView extends Activity {

	protected static final String TAG = "ShopCategoryView";
	String sql;
	ListView ListShopInfo;
	String category;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_category_view);
		
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1)
		{
			//Action bar code for android devices with android version more than gingerbread
			Log.i(TAG,"Build.VERSION.SDK_INT : "+Build.VERSION.SDK_INT);
			Log.i(TAG,"Build.VERSION_CODES.GINGERBREAD_MR1: "+Build.VERSION_CODES.GINGERBREAD_MR1);
			if(ViewConfiguration.get(ShopCategoryView.this).hasPermanentMenuKey())
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
			
				
		RadioGroup radioGroupActivitySelector = (RadioGroup) findViewById(R.id.radio_group_activity_selector);
		radioGroupActivitySelector.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	    {
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	            // checkedId is the RadioButton selected
		switch (checkedId) {
		  case R.id.radioList :
			  Log.i(TAG,"RadioList Activity Selected");
			  goToListView();
		                   	              break;
		  case R.id.radioMap :
			  Log.i(TAG,"RadioMap Activity Selected");
			  goToMapView();
		                   	              break;                 	              
		 
		 }
	        }
	    });
		
		Bundle b = getIntent().getExtras();
		category=(String) b.get("itemName");
		
		
		TextView txtCategory = (TextView) findViewById(R.id.txtView_Category);	
		txtCategory.setText(category);
		
		
		ListShopInfo = (ListView) findViewById(R.id.listViewCategory);	
		
		// get relevant info from db
		sql="select shopName  from streetShopInfo where category=\""+category+"\"";
		
		ArrayList<String> list=new ArrayList<String>();
		
		StreetFoodDataBaseAdapter mDBAdapter= new StreetFoodDataBaseAdapter(this);
		mDBAdapter.createDatabase();       
		mDBAdapter.open();
		list=mDBAdapter.getInfo(sql,"shopName");
		Log.i(TAG,"Cursor Values Retrived into Array list");
		setView(list);
		mDBAdapter.close();
		ListShopInfo.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	       // TextView text1 = (TextView) view.findViewById(android.R.id.text1);
	       // Log.i(TAG,"You Clicked:"+ text1.getText().toString());
         try{
        	 Intent intent= new Intent(getApplicationContext(), ShopDetailView.class);
        	 
        	 ListViewCustomAdapter lAdapter=(ListViewCustomAdapter)parent.getAdapter();
	         String clickedItem=(String) lAdapter.getItem(position);		         
	         //Create a bundle object
	         Bundle b = new Bundle();

	             //Inserts a String value into the mapping of this Bundle
	         
	             b.putString("itemName",clickedItem);
	             
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
	
	public void setView(ArrayList<String> info)
	{
		/*Log.i(TAG,"Setting View");

		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1,info);

		Log.i(TAG,"Array Adapter Set");
		Log.i(TAG,info.toString());
		// Assign adapter to ListView
		Log.i(TAG,"Attaching Arrya Adapter to List View");
		ListShopInfo.setAdapter(adapter);
		Log.i(TAG,"View Set Succesfully");
*/
		
		 ListViewCustomAdapter sAdapter = new ListViewCustomAdapter(ListShopInfo.getContext(),R.layout.list_item,info);
	        
	        /* set the list's adapter */
		 ListShopInfo.setAdapter(sAdapter);
		
	}

	public void goToListView()
	{
		 Intent intent = new Intent(getApplicationContext(),ShopListView.class);
		 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 //start the DisplayActivity
         startActivity(intent);
	}
	
	public void goToMapView()
	{
		 Intent intent = new Intent(getApplicationContext(),ShopMapView.class); // change it to Map Activity
		 Bundle b = new Bundle();
         //Inserts a String value into the mapping of this Bundle
         b.putString("itemName",category);
         b.putString("view","Category");
         Log.i(TAG,"ItemName: "+category+"view:Catgory");
         intent.putExtras(b);
         startActivity(intent);

	}
	
	@Override
	protected void onStart() {
	    super.onStart();
	 
	    RadioButton ListRadioButton=(RadioButton) findViewById(R.id.radioList);
  		ListRadioButton.setChecked(false);
  		 RadioButton MapRadioButton=(RadioButton) findViewById(R.id.radioMap);
  		MapRadioButton.setChecked(false);
  		
  		
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
