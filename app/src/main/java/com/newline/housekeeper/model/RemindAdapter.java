package com.newline.housekeeper.model;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.newline.C;
import com.greatkeeper.keeper.R;

public class RemindAdapter extends BaseAdapter {
	
	private List<RemindBean> dataArray;
	private LayoutInflater layoutInflater;  
    
    public RemindAdapter(Context context, List<RemindBean> dataArray){
    	this.dataArray = dataArray;
    	this.layoutInflater = LayoutInflater.from(context);
    }
    
    public final class ListItem {
    	public ImageView imgRemind;			// 提醒类别图片
    	public TextView textRemindNum;		// 提醒数量
    	public TextView textRemindType;		// 提醒类型文字
    	public TextView textRemindResume;	// 提醒摘要
    	public TextView textRemindTime;		// 提醒时间
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
			convertView = layoutInflater.inflate(R.layout.remind_item_view, null); 
			
			item = new ListItem();
			item.imgRemind = (ImageView) convertView.findViewById(R.id.imgRemind);
			item.textRemindNum = (TextView) convertView.findViewById(R.id.textRemindNum);
			item.textRemindType = (TextView) convertView.findViewById(R.id.textRemindType);
			item.textRemindResume = (TextView) convertView.findViewById(R.id.textRemindResume);
			item.textRemindTime = (TextView) convertView.findViewById(R.id.textRemindTime);
			convertView.setTag(item);
		} else {
			item = (ListItem) convertView.getTag();
		}
		
		RemindBean bean = getItemBean(position);
		if(bean != null){
			int[] resIds = getRemindTypeResId(bean.getType());
			item.imgRemind.setImageResource(resIds[0]);
			item.textRemindType.setText(resIds[1]);
			item.textRemindResume.setText(bean.getMsg());
			item.textRemindTime.setText(bean.getCreatetime());
			
			if(bean.getUnread() > 0){
				item.textRemindNum.setText(String.valueOf(bean.getUnread()));
				item.textRemindNum.setVisibility(View.VISIBLE);
			} else {
				item.textRemindNum.setVisibility(View.GONE);
			}
		}
		return convertView;
	}
	
	public RemindBean getItemBean(int position){
		if(position >= 0 && position < getCount()){
			return dataArray.get(position);
		}
		return null;
	}
	
	private int[] getRemindTypeResId(int type){
        int[] resIds = new int[2];
        if(C.Remind.HOUSE == type){
            resIds[0] = R.drawable.img_type_house;
            resIds[1] = R.string.remindHouse;
        } else if(C.Remind.RENT == type){
            resIds[0] = R.drawable.img_type_rent;
            resIds[1] = R.string.remindRent;
        } else if(C.Remind.REPAIR == type){
            resIds[0] = R.drawable.img_type_repair;
            resIds[1] = R.string.houseRepair;
        } else {
            resIds[0] = R.drawable.img_type_sys;
            resIds[1] = R.string.remindSys;
        }
        return resIds;
    }
	
}
