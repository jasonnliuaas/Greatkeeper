package com.newline.housekeeper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.newline.core.BaseActivity;

@ContentView(R.layout.payment_detail_layout)
public class PaymentDetailActivity extends BaseActivity {
    
    @ViewInject(R.id.textTopTitle)
    private TextView mTopTitle;
    
    @ViewInject(R.id.textPaymentType)
    private TextView mTextPaymentType;
    
    @ViewInject(R.id.textday_hundred)
    private TextView textday_hundred;

    @ViewInject(R.id.textday_ten)
    private TextView textday_ten;

    @ViewInject(R.id.textday_single)
    private TextView textday_single;
    
    @ViewInject(R.id.textPaymentAddress)
    private TextView mTextPaymentAddress;
    
    @ViewInject(R.id.textPaymentTime)
    private TextView mTextPaymentTime;
    
    @ViewInject(R.id.layoutDay0)
    private LinearLayout layoutDay0;
    
    @ViewInject(R.id.layoutDay1)
    private LinearLayout layoutDay1;
    
    @ViewInject(R.id.textPaymentContent)
    private TextView textPaymentContent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        
        mTopTitle.setText(getString(R.string.warned_details));
        
        String data = getParam("data");
        JSONObject object = JSON.parseObject(data);
        
        String remind_type = object.getString("remind_type_name");
        if(remind_type.equals("房产税")){
            remind_type = getString(R.string.property_taxes);
        }else if(remind_type.equals("房屋贷款")){
            remind_type = getString(R.string.housing_loan);
        }else if(remind_type.equals("交房提醒")){
            remind_type = getString(R.string.submitted_remind);
        }
        String days = object.getString("days");
        String address = object.getString("address");
        String remind_date = object.getString("remind_date");
        String content = object.getString("content");
        
        if("0".equals(days)){
            layoutDay1.setVisibility(View.GONE);
            layoutDay0.setVisibility(View.VISIBLE);
        } else {
            int day=Integer.parseInt(days);
            int hundred = day/100;
            int ten = (day%100)/10;
            int single = day%10;
            if(hundred>=1){
                textday_hundred.setText(""+hundred);
            }else{
                textday_hundred.setVisibility(View.GONE);
            }
            if(ten>=1){
                textday_ten.setText(""+ten);
            }else{
                textday_ten.setVisibility(View.GONE);
            }
            if(single>=1){
                textday_single.setText(""+single);
            }else {
                textday_single.setVisibility(View.GONE);
            }
            layoutDay1.setVisibility(View.VISIBLE);
            layoutDay0.setVisibility(View.GONE);
        }
        
        mTextPaymentType.setText(remind_type);
        textPaymentContent.setText(content);
        mTextPaymentAddress.setText(address);
        mTextPaymentTime.setText(remind_date);
        
        boolean isLogin = getParam("isLogin");

        LinearLayout layout = (LinearLayout) findViewById(R.id.unLoginLayout);
        if(!isLogin){
            layout.setVisibility(View.VISIBLE);
        }else{
            layout.setVisibility(View.GONE);
        }
    }
}
