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

public class PaymentAdapter extends BaseAdapter {
	
	private JSONArray dataArray;
	private LayoutInflater layoutInflater;
    private Context context;
    
    public PaymentAdapter(Context context, JSONArray dataArray){
    	this.dataArray = dataArray;
    	this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }
    
    public final class ViewHolder {
    	ImageView mImgPaymentType;		
    	TextView mTextPaymentMsg;	
    	TextView mTextPaymentDay;
    	TextView mTextPaymentType;
        TextView mTextPaymentDate;
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
			convertView = layoutInflater.inflate(R.layout.payment_item_view, null); 
			
			holder = new ViewHolder();
			
			holder.mImgPaymentType = (ImageView) convertView.findViewById(R.id.imgPaymentType);
			holder.mTextPaymentMsg = (TextView) convertView.findViewById(R.id.textPaymentMsg);
			holder.mTextPaymentDay = (TextView) convertView.findViewById(R.id.textPaymentDay);
			holder.mTextPaymentType = (TextView) convertView.findViewById(R.id.textPaymentType);
            holder.mTextPaymentDate = (TextView) convertView.findViewById(R.id.textPaymentDate);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		try {
		    JSONObject bean = getItemBean(position);
            String date = bean.getString("remind_date");
		    String address = bean.getString("address");
		    String remind_type = bean.getString("remind_type_name");
		    String day = bean.getString("days");
		    holder.mTextPaymentMsg.setText(address);
		    String paymentDay = Integer.parseInt(day) <= 0 ? context.getString(R.string.today_Expired) :context.getString(R.string.remaining_date,day);
		    holder.mTextPaymentDay.setText(paymentDay);
            if(remind_type.equals("房产税")){
                remind_type = context.getString(R.string.property_taxes);
            }else if(remind_type.equals("房屋贷款")){
                remind_type = context.getString(R.string.housing_loan);
            }else if(remind_type.equals("交房提醒")){
                remind_type = context.getString(R.string.submitted_remind);
            }
		    holder.mTextPaymentType.setText(remind_type);
            holder.mTextPaymentDate.setText(date);
        } catch (Exception e) {
            LogUtils.e("缴费提醒数据解析错误", e);
        }
		
		return convertView;
	}
	
	public JSONObject getItemBean(int position){
		if(position >= 0 && position < getCount()){
			return dataArray.getJSONObject(position);
		}
		return null;
	}
	
/*	private int typeIcon(String type){
	    if("1".equals(type.trim())) return R.drawable.ic_payment_shui;
	    if("2".equals(type.trim())) return R.drawable.ic_payment_dai;
	    if("3".equals(type.trim())) return R.drawable.ic_payment_jiao;
	    return R.drawable.ic_rental_qita;
	}*/
	
}
