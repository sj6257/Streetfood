package com.dev.streetfood;

import java.util.ArrayList;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ListViewCustomAdapter extends BaseAdapter {

	private static final String TAG ="Custom Adapater";
	ArrayList <String> shopList=new ArrayList <String>();
	int resource;
	LayoutInflater inflater;
	Context context;

	private static class ViewHolder
	{
		TextView  shopName;		
		ImageView icn_Rating;	
		TextView  distance;
		//int position;
	}

	public ListViewCustomAdapter(Context ctx, int resourceId,ArrayList<String> data){
		super();
		this.resource = resourceId;
		this.inflater = LayoutInflater.from( ctx );
		this.context=ctx;
		this.shopList=data;
		Log.i(TAG,"In Constructor");

	}

	@Override
	public int getCount() {
		return shopList.size();

	}

	@Override
	public Object getItem(int position) {


		return shopList.get(position);

	}

	@Override
	public long getItemId(int position) {

		return 0;
		//return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.i(TAG,"In GetView");

		final ViewHolder holder;

		if(convertView == null) {
			Log.i(TAG,"customView Null");
			LayoutInflater vInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vInflater.inflate(resource,parent,false);
			holder = new ViewHolder();
			holder.shopName=(TextView)convertView.findViewById(R.id.txt_shopName);
			// holder.distance=(TextView)convertView.findViewById(R.id.txt_distance);
			//holder.icn_Rating=(ImageView)convertView.findViewById(R.id.icn_rating);
			convertView.setTag(holder);
			Log.i(TAG,"Holder Set as TAG");

		}
		else
		{

			holder = (ViewHolder) convertView.getTag();
			Log.i(TAG,"TAG Already Set No Need to create ");
		}

		//  holder.shopName.setText(ListViewCustomAdapter.this.ge);

		Log.i(TAG,"Setting Values Now ");

		holder.shopName.setText(shopList.get(position));
		// holder.distance.setText("10km");
		// holder.icn_Rating.setImageResource(R.drawable.star);

		Log.i(TAG,holder.shopName.toString());
		// Log.i(TAG,holder.distance.toString());

		return convertView;

	}




}
