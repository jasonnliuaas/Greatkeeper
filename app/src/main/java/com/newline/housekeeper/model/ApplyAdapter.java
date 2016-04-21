package com.newline.housekeeper.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatkeeper.keeper.R;
import com.lidroid.xutils.util.LogUtils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ApplyAdapter extends BaseAdapter{
    private JSONArray dataArray;
    private LayoutInflater layoutInflater;
    private Context context;
    private int apply_flag;  // 0
    public  static final  int APPLY_TAX = 0;
    public static   final  int APPLY_LOAN = 1;
    
    public final class ViewHolder {
    	ImageView mImgTaxType;		
    	TextView mTextTaxMsg;	
    	TextView mTextTaxDate;
    	TextView mTextTaxState;
    	TextView mTextBaoshuiId;
    	TextView mTextCity;
    	TextView mTextCountry;
    	TextView mTextPrice;
    	TextView mTextScale;
    	TextView mTextType;
    	TextView mTextName;
    }
    
    public void updateList(JSONArray dataArray){
        this.dataArray = dataArray;
        notifyDataSetChanged();
    }
    
    public ApplyAdapter(Context context, JSONArray dataArray,int type){
    	this.dataArray = dataArray;
    	this.layoutInflater = LayoutInflater.from(context);
    	this.context = context;
        this.apply_flag = type;
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
			convertView = layoutInflater.inflate(R.layout.apply_item_view, null); 
			holder = new ViewHolder();
			holder.mImgTaxType = (ImageView) convertView.findViewById(R.id.imgTaxType);
			holder.mTextTaxMsg = (TextView) convertView.findViewById(R.id.textTaxMsg);
			holder.mTextTaxDate = (TextView) convertView.findViewById(R.id.textTaxDate);
			holder.mTextTaxState = (TextView) convertView.findViewById(R.id.textTaxstate);
			holder.mTextBaoshuiId= (TextView) convertView.findViewById(R.id.baoshuiid);
			holder.mTextCity = (TextView) convertView.findViewById(R.id.text_city);
			holder.mTextCountry = (TextView) convertView.findViewById(R.id.text_country);
			holder.mTextName = (TextView) convertView.findViewById(R.id.text_name);
			holder.mTextPrice = (TextView) convertView.findViewById(R.id.text_price);
			holder.mTextScale = (TextView) convertView.findViewById(R.id.text_scale);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
		    JSONObject bean = getItemBean(position);
		    String id = bean.getString("id");
		    String name = bean.getString("name");
		    String city = bean.getString("city");
		    String country = bean.getString("country");
		    String price = bean.getString("price");
		    String scale = bean.getString("scale");
		    String type_name = bean.getString("type");
		    String date = bean.getString("time");
		    String state = bean.getString("state");
		    
		    holder.mTextCity.setText(city);
		    holder.mTextBaoshuiId.setText(id);
		    holder.mTextCountry.setText(country);
		    holder.mTextPrice.setText(price);
		    holder.mTextName.setText(name);
		    holder.mTextScale.setText(scale);
            if(this.apply_flag == APPLY_LOAN){
                if(state.equals("审核失败")){
                    holder.mImgTaxType.setImageResource(R.drawable.icon_loanapply_faild);
                }else{
                    holder.mImgTaxType.setImageResource(R.drawable.icon_loan_apply);
                }
                if(type_name == null || type_name.equals("贷款申请"))
                holder.mTextTaxMsg.setText(context.getString(R.string.loan_apply2));
            }else if (this.apply_flag == APPLY_TAX){
                if(state.equals("审核失败")){
                    holder.mImgTaxType.setImageResource(R.drawable.icon_taxapply_falied);
                }else{
                    holder.mImgTaxType.setImageResource(R.drawable.icon_taxapply);
                }
                if(type_name.equals("报税申请")){
                    holder.mTextTaxMsg.setText(context.getString(R.string.tax_apply));
                }
                else if(type_name.equals("报税申请")){
                    holder.mTextTaxMsg.setText(context.getString(R.string.taxnum_apply));
                }
            }
		    holder.mTextTaxDate.setText(date);
            if(state.equals("审核失败")){
                state = context.getString(R.string.audit_failure);
            }else if(state.equals("审核中")){
                state = context.getString(R.string.onaudit);
            }else if(state.equals("等待审核")){
                state = context.getString(R.string.wait_audit);
            }else if(state.equals("审核通过")){
                state = context.getString(R.string.audit_pass);
            }
		    holder.mTextTaxState.setText(state);
        } catch (Exception e) {
            LogUtils.e("个税缴纳数据解析错误", e);
        }
		
		return convertView;
	}
	
	public JSONObject getItemBean(int position){
		if(position >= 0 && position < getCount()){
			return dataArray.getJSONObject(position);
		}
		return null;
	}
	private int typeIcon(String type,String state){
	    if("1".equals(type.trim())) return state.equals("fail")?R.drawable.icon_tax_fail:R.drawable.icon_tax_apply;
	    if("2".equals(type.trim())) return R.drawable.icon_tax_num;
	    if("3".equals(type.trim())) return R.drawable.icon_tax_fail;
	    return R.drawable.ic_rental_qita;
	}
	

}
