package com.newline.housekeeper.model;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.greatkeeper.keeper.R;

public class RentAdapter extends BaseAdapter {
	
	private List<RentBean> dataArray;
	private LayoutInflater layoutInflater;  
	private Context context;
    
    public RentAdapter(Context context, List<RentBean> dataArray){
    	this.dataArray = dataArray;
    	this.context = context;
    	this.layoutInflater = LayoutInflater.from(context);
    }
    
    public final class ListItem {
    	TextView mTextRentName;		// 提醒数量
    	TextView mTextRentAddress;	// 提醒类型文字
    	TextView mTextRentTime;	    // 提醒摘要
    	TextView mTextRentState;	// 提醒时间
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
			convertView = layoutInflater.inflate(R.layout.rent_item_view, null); 
			
			item = new ListItem();
			item.mTextRentAddress = (TextView) convertView.findViewById(R.id.textRentAddress);
			item.mTextRentTime = (TextView) convertView.findViewById(R.id.textRentTime);
			item.mTextRentState = (TextView) convertView.findViewById(R.id.textRentState);
			convertView.setTag(item);
		} else {
			item = (ListItem) convertView.getTag();
		}
		RentBean bean = getItemBean(position);
		if(bean != null){
		    item.mTextRentAddress.setText(bean.getPropertyaddress());
		    item.mTextRentTime.setText(bean.getDate());
		    String state = bean.getState();
            state = state == null?"出租中":state;
            if(state.equals("闲置中")){
                item.mTextRentState.setText(context.getString(R.string.in_idle));
            }
           else if(state.equals("出租中")){
                item.mTextRentState.setText(context.getString(R.string.in_retal));
            }
		    
		    Resources resources = context.getResources();
		    if("闲置中".equals(state)){
		        item.mTextRentState.setTextColor(resources.getColor(R.color.textGray));
		    } else {
		        item.mTextRentState.setTextColor(resources.getColor(R.color.text_black));
		    }
		}
		
		return convertView;
	}
	
	public RentBean getItemBean(int position){
		if(position >= 0 && position < getCount()){
			return dataArray.get(position);
		}
		return null;
	}
	
}
