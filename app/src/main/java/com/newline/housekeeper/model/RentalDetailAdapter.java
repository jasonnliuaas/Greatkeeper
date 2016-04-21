package com.newline.housekeeper.model;

import android.content.Context;
import android.media.Image;
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

public class RentalDetailAdapter extends BaseAdapter {

	private JSONArray dataArray;
	private LayoutInflater layoutInflater;
    private Context context;
    public RentalDetailAdapter(Context context, JSONArray dataArray){
    	this.dataArray = dataArray;
    	this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }
    
    public final class ViewHolder {
    	ImageView imgRentalDetailType;
    	TextView textRentalDetailType;
        TextView textRentalInfoDate;
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
			convertView = layoutInflater.inflate(R.layout.rental_detail_item_view, null);
			
			holder = new ViewHolder();
			holder.imgRentalDetailType = (ImageView) convertView.findViewById(R.id.imgRentalDetailType);
			holder.textRentalDetailType = (TextView) convertView.findViewById(R.id.textRentalDetailType);
			holder.textRentalInfoDate = (TextView) convertView.findViewById(R.id.textRentalInfoDate);
            holder.textRentaincomel = (TextView) convertView.findViewById(R.id.textRentaincomel);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		try {
		    JSONObject bean = getItemBean(position);
		    double amount = bean.getDouble("amount");
            int type = bean.getInteger("type");
            String time = bean.getString("time");
            String type_name = bean.getString("type_name");
		    String address = bean.getString("address");
		    String city = bean.getString("city");
		    String currency_name = bean.getString("currency_name");
            String id = bean.getString("id");
		    /*holder.textRentalAddress.setText(address);
		    holder.textRentalCity.setText(city);
		    holder.textRentaincomel.setText(income);
            holder.textRentalid.setText(id);*/
            holder.textRentalInfoDate.setText(time);
            if(type_name.equals("租金收入")){
                type_name = context.getString(R.string.rental_income);
            }else if(type_name.equals("管理费用")){
                type_name = context.getString(R.string.management_expenses);
            }else if(type_name.equals("保险费用")){
                type_name = context.getString(R.string.insurance_cost);
            }else if(type_name.equals("物业费用")){
                type_name = context.getString(R.string.property_costs);
            }else if(type_name.equals("其他")){
                type_name = context.getString(R.string.ohters);
            }
            holder.textRentalDetailType.setText(type_name);
            if(amount>0){
                holder.imgRentalDetailType.setImageResource(R.drawable.ic_rental_02);
                holder.textRentaincomel.setText("+"+amount+" "+currency_name);
            }else{
                holder.imgRentalDetailType.setImageResource(R.drawable.ic_rental_01);
                holder.textRentaincomel.setText(amount+" "+currency_name);
            }
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
