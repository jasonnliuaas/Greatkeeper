package com.newline.housekeeper.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatkeeper.keeper.R;
import com.lidroid.xutils.util.LogUtils;

public class RentalAdapter extends BaseAdapter {
	
	private JSONArray dataArray;
	private LayoutInflater layoutInflater;  
    
    public RentalAdapter(Context context, JSONArray dataArray){
    	this.dataArray = dataArray;
    	this.layoutInflater = LayoutInflater.from(context);
    }
    
    public final class ViewHolder {
    	ImageView mImgRentalType;		
    	TextView textRentalCity;
        TextView textRentalid;
    	TextView textRentalAddress;
    	TextView textRentaincomel;
    	TextView textRentalType;
    }
    
    public void updateList(JSONArray dataArray){
        this.dataArray = dataArray;
        notifyDataSetChanged();
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
		ViewHolder holder;
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.rental_item_view, null); 
			
			holder = new ViewHolder();
			holder.textRentalCity = (TextView) convertView.findViewById(R.id.textRentalCity);
			holder.textRentalAddress = (TextView) convertView.findViewById(R.id.textRentalAddress);
			holder.textRentaincomel = (TextView) convertView.findViewById(R.id.textRentaincomel);
            holder.textRentalid = (TextView) convertView.findViewById(R.id.textRentalid);
			holder.textRentalType = (TextView) convertView.findViewById(R.id.textRentalType);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		try {
		    JSONObject bean = getItemBean(position);
		    
		    String address = bean.getString("propertyaddress");
		    String city = bean.getString("city");
		    String income = bean.getString("income");
            String id = bean.getString("id");
		    holder.textRentalAddress.setText(address);
		    holder.textRentalCity.setText(city);
		    holder.textRentaincomel.setText(income);
            holder.textRentalid.setText(id);
        } catch (Exception e) {
            LogUtils.e("租金详情数据解析错误", e);
        }
		
		return convertView;
	}
	
	public JSONObject getItemBean(int position){
		if(position >= 0 && position < getCount()){
			return dataArray.getJSONObject(position);
		}
		return null;
	}
	
	private int typeIcon(String type){
	    if("1".equals(type.trim())) return R.drawable.ic_rental_zu;
	    if("2".equals(type.trim())) return R.drawable.ic_rental_guan;
	    if("3".equals(type.trim())) return R.drawable.ic_rental_shui;
	    if("4".equals(type.trim())) return R.drawable.ic_rental_xian;
	    if("5".equals(type.trim())) return R.drawable.ic_rental_wu;
	    return R.drawable.ic_rental_qita;
	}
	
}
