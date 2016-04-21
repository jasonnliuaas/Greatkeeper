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

public class DynamicAdapter2 extends BaseAdapter {
	
	private JSONArray dataArray;
	private LayoutInflater layoutInflater;  
    
    public DynamicAdapter2(Context context, JSONArray dataArray){
    	this.dataArray = dataArray;
    	this.layoutInflater = LayoutInflater.from(context);
    }
    
    public final class ViewHolder {
    	
    	TextView textDate;	
    	TextView textTitle;
    	TextView textContent;
    	
    }
    
    public void update(JSONArray dataArray){
        this.dataArray = dataArray;
        notifyDataSetChanged();
    }
    
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    
    @Override
    public boolean isEnabled(int position) {
        return false;
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
		final ViewHolder holder;
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.dynamic_item_view2, null); 
			
			holder = new ViewHolder();
			
			holder.textDate = (TextView) convertView.findViewById(R.id.textMsgDate);
			holder.textTitle = (TextView) convertView.findViewById(R.id.textTitle);
			holder.textContent = (TextView) convertView.findViewById(R.id.textContent);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		try {
		    JSONObject bean = getItemBean(position);
		    
		    String msgDate = bean.getString("date");
		    String msgTitle = bean.getString("title");
		    String msgContent = bean.getString("excerpt");
		    
		    holder.textDate.setText(msgDate.substring(0, msgDate.indexOf(" ")));
		    holder.textTitle.setText(msgTitle);
		    holder.textContent.setText(msgContent);
        } catch (Exception e) {
            LogUtils.e("动态消息中心数据解析错误", e);
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
