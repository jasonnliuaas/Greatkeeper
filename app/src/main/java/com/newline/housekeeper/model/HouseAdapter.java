package com.newline.housekeeper.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newline.C;
import com.greatkeeper.keeper.R;

public class HouseAdapter extends BaseAdapter {
	
	private JSONArray dataArray;
	private LayoutInflater layoutInflater;
    private Context context;
    
    public HouseAdapter(Context context, JSONArray dataArray){
    	this.dataArray = dataArray;
    	this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }
    
    public final class ListItem {
    	TextView mTextCreateTime;		
    	TextView mTextHouseAddress;
    }

	@Override
	public int getCount() {
		return (dataArray == null ? 0 : dataArray.size());
	}

	@Override
	public Object getItem(int position) {
		return getItemBean(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListItem item;
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.house_item_view, null); 
			
			item = new ListItem();
			
			item.mTextCreateTime = (TextView) convertView.findViewById(R.id.textCreateTime);
			item.mTextHouseAddress = (TextView) convertView.findViewById(R.id.textHouseAddress);
			
			convertView.setTag(item);
		} else {
			item = (ListItem) convertView.getTag();
		}
		
		JSONObject bean = getItemBean(position);
		String str = (bean == null ? "" : bean.toJSONString());
		try {
	        if(bean != null){
	            String address = "圣地亚哥(San Digeo)1233 S Joyce ST";
	            if(bean.containsKey("propertyaddress")){
	                address = bean.getString("propertyaddress");
	            }
	            String bulidYear = "1986-04";
	            if(bean.containsKey("builtyear")){
	                bulidYear = bean.getString("builtyear");
	            }
	            
	            item.mTextCreateTime.setText(context.getString(R.string.labHouseBulidYear) + bulidYear);
	            item.mTextHouseAddress.setText(address);
	        }
        } catch (Exception e) {
            Log.e(C.TAG, str, e);
        }
		
		return convertView;
	}
	
	public JSONObject getItemBean(int position){
		if(position >= 0 && position < getCount()){
			return dataArray.getJSONObject(position);
		}
		return null;
	}
	
}
