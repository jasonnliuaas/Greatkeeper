package com.newline.housekeeper.model;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.greatkeeper.keeper.R;

/***
 * 提醒列表adapter第2种格式
 * 
 *   ***********************
 *   * XXXXXXXXXXX         *
 *   ***********************
 *   * Date:2015-01-01     *
 *   ***********************
 */
public class RemindAdapter2 extends BaseAdapter {
	
	private List<RemindBean> dataArray;
	private LayoutInflater layoutInflater;  
    
    public RemindAdapter2(Context context, List<RemindBean> dataArray){
    	this.dataArray = dataArray;
    	this.layoutInflater = LayoutInflater.from(context);
    }
    
    private final class ListItem{
    	public TextView textMsg;
    	public TextView textDate;
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
			convertView = layoutInflater.inflate(R.layout.remind_item_view2, null); 
			
			item = new ListItem();
			item.textMsg = (TextView) convertView.findViewById(R.id.textMsg2);
			item.textDate = (TextView) convertView.findViewById(R.id.textDate2);
			convertView.setTag(item);
		} else {
			item = (ListItem) convertView.getTag();
		}
		
		int pxSize = convertView.getResources().getDimensionPixelSize(R.dimen.listPadding);
		int paddingTop = (position == 0 ? pxSize : 0);
		convertView.setPadding(pxSize, paddingTop, pxSize, pxSize);
		
		RemindBean bean = getItemBean(position);
		if(bean != null){
		    item.textMsg.setText(bean.getMsg());
		    item.textDate.setText("日期：" + bean.getCreatetime());
		}
		
		return convertView;
	}
	
	public RemindBean getItemBean(int position){
		if(position >= 0 && position < getCount()){
			return dataArray.get(position);
		}
		return new RemindBean();
	}

}
