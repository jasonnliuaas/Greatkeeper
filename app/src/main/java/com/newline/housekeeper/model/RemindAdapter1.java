package com.newline.housekeeper.model;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.greatkeeper.keeper.R;

/****
 * 提醒列表adapter第一种格式
 * 
 *   **********************
 *   *	      内容	      *
 *   **********************
 *   *     标题 * 时间     *
 *   **********************
 */
public class RemindAdapter1 extends BaseAdapter {
	
	private ArrayList<RemindMsgBean> dataArray;
	private LayoutInflater layoutInflater;  
    
    public RemindAdapter1(Context context, ArrayList<RemindMsgBean> dataArray){
    	this.dataArray = dataArray;
    	this.layoutInflater = LayoutInflater.from(context);
    }
    
    private final class ListItem{
    	public TextView textMsg;	
    	public TextView textTitle1;	
    	public TextView textDate1;	
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
			convertView = layoutInflater.inflate(R.layout.remind_item_view1, null); 
			
			item = new ListItem();
			item.textMsg = (TextView) convertView.findViewById(R.id.textMsg);
			item.textTitle1 = (TextView) convertView.findViewById(R.id.textTitle1);
			item.textDate1 = (TextView) convertView.findViewById(R.id.textDate1);
			convertView.setTag(item);
		} else {
			item = (ListItem) convertView.getTag();
		}
		
		int pxSize = convertView.getResources().getDimensionPixelSize(R.dimen.listPadding);
		int paddingTop = (position == 0 ? pxSize : 0);
		convertView.setPadding(pxSize, paddingTop, pxSize, pxSize);
		
		RemindMsgBean bean = getItemBean(position);
		if(bean != null){
			item.textMsg.setText(bean.getMsg());
			item.textTitle1.setText(bean.getTitle());
			item.textDate1.setText(bean.getCreatetime());
		}
		
		return convertView;
	}
	
	public RemindMsgBean getItemBean(int position){
		if(position >= 0 && position < getCount()){
			return dataArray.get(position);
		}
		return null;
	}

}
