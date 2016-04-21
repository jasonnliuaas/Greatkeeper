package com.newline.housekeeper.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatkeeper.keeper.R;
import com.lidroid.xutils.util.LogUtils;

public class ManagerAdapter extends BaseAdapter {
	
	private JSONArray dataArray;
	private LayoutInflater layoutInflater;  
    
    public ManagerAdapter(Context context, JSONArray dataArray){
    	this.dataArray = dataArray;
    	this.layoutInflater = LayoutInflater.from(context);
    }
    
    public final class ViewHolder {
    	TextView textName;		
    	TextView textMobile;	
    	TextView textEmail;   
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.manager_item_view, null); 
			
			holder = new ViewHolder();
			
			holder.textName = (TextView) convertView.findViewById(R.id.textName);
			holder.textMobile = (TextView) convertView.findViewById(R.id.textMobile);
			holder.textEmail = (TextView) convertView.findViewById(R.id.textEmail);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		JSONObject bean = getItemBean(position);
		try {
		    String name = bean.getString("nickname");
            String mobile = bean.getString("mobile");
            String email = bean.getString("email");
            
            holder.textName.setText(name);
            holder.textMobile.setText(mobile);
            holder.textEmail.setText(email);
        } catch (Exception e) {
            LogUtils.e("解析物业经理数据错误", e);
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
