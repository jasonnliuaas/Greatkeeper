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

public class RenterAdapter extends BaseAdapter {
	
	private JSONArray dataArray;
	private LayoutInflater layoutInflater;  
    
    public RenterAdapter(Context context, JSONArray dataArray){
    	this.dataArray = dataArray;
    	this.layoutInflater = LayoutInflater.from(context);
    }
    
    public final class ListItem {
    	public TextView mTextRenterName;		
    	public TextView mTextRenterJob;	
    	public TextView mTextRenterCredit; 
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
			convertView = layoutInflater.inflate(R.layout.renter_item_view, null); 
			
			item = new ListItem();
			
			item.mTextRenterName = (TextView) convertView.findViewById(R.id.textRenterName);
			item.mTextRenterJob = (TextView) convertView.findViewById(R.id.textRenterJob);
			item.mTextRenterCredit = (TextView) convertView.findViewById(R.id.textRepairState);
			
			convertView.setTag(item);
		} else {
			item = (ListItem) convertView.getTag();
		}
		
		JSONObject bean = getItemBean(position);
		String str = (bean == null ? "" : bean.toJSONString());
		try {
	        if(bean != null){
	            String name = bean.getString("realname");
                String job = bean.getString("job");
                String credit = bean.getString("credithistory");
	            
	            item.mTextRenterName.setText(name);
	            item.mTextRenterJob.setText(job);
	            item.mTextRenterCredit.setText(credit);
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
