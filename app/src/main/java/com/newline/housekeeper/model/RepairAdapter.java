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
import com.greatkeeper.keeper.R;
import com.newline.C;

public class RepairAdapter extends BaseAdapter {
	
	private JSONArray dataArray;
	private LayoutInflater layoutInflater;
    private Context context;
    
    public RepairAdapter(Context context, JSONArray dataArray){
    	this.dataArray = dataArray;
    	this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }
    
    public final class ListItem {
    	TextView mTextRepairName;		
    	TextView mTextHouseAddress;	
    	TextView mTextRepairState;
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
			convertView = layoutInflater.inflate(R.layout.repair_item_view, null); 
			
			item = new ListItem();
			
			item.mTextRepairName = (TextView) convertView.findViewById(R.id.textRepairName);
			item.mTextHouseAddress = (TextView) convertView.findViewById(R.id.textHouseAddress);
			item.mTextRepairState = (TextView) convertView.findViewById(R.id.textRepairState);
			
			convertView.setTag(item);
		} else {
			item = (ListItem) convertView.getTag();
		}
		
		JSONObject bean = getItemBean(position);
		String str = (bean == null ? "" : bean.toJSONString());
		try {
	        if(bean != null){
	            String address = "没有找到参数propertyaddress";
	            if(bean.containsKey("propertyaddress")){
	                address = bean.getString("propertyaddress");
	            }
	            
	            item.mTextRepairName.setText(bean.getString("mantenaneceitem").replace("维修项目",context.getString(R.string.maintenance_project)));
	            item.mTextHouseAddress.setText(address);
	            item.mTextRepairState.setText(bean.getString("approval_name"));
                if(bean.getString("approval_name").equals("已确认")){
                    item.mTextRepairState.setText(context.getString(R.string.already_confirmed));
                    item.mTextRepairState.setTextColor(context.getResources().getColor(R.color.text_black));
                }else if(bean.getString("approval_name").equals("未确认")){
                    item.mTextRepairState.setTextColor(context.getResources().getColor(R.color.textGray));
                    item.mTextRepairState.setText(context.getString(R.string.not_confirmed));
                }
                item.mTextRepairState.setVisibility(View.VISIBLE);
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
