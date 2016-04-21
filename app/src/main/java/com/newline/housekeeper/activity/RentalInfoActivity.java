package com.newline.housekeeper.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.newline.core.BaseActivity;
@ContentView(R.layout.activity_rental_info)
public class RentalInfoActivity extends BaseActivity {

    @ViewInject(R.id.textTopTitle)
    TextView textTopTitle;
    @ViewInject(R.id.textRentalType)
    TextView textRentalType;
    @ViewInject(R.id.textRentPrefix)
    TextView textRentPrefix;
    @ViewInject(R.id.textRent)
    TextView textRent;
    @ViewInject(R.id.textRentalAddress)
    TextView textRentalAddress;
    @ViewInject(R.id.textCreateTime)
    TextView textCreateTime;
    @ViewInject(R.id.imgRentalType)
    ImageView imgRentalType;
    @ViewInject(R.id.rentalFootView)
    LinearLayout rentalFootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        initView();
    }

    public void initView(){
        textTopTitle.setText(getString(R.string.bopd));
        String data = getIntent().getStringExtra("data");
        JSONObject bean = JSONObject.parseObject(data);
        if(bean!=null){
            double amount = bean.getDouble("amount");
            int type = bean.getInteger("type");
            String time = bean.getString("time");
            String type_name = bean.getString("type_name");
            String address = bean.getString("address");
            String city = bean.getString("city");
            String currency_name = bean.getString("currency_name");
            String id = bean.getString("id");
            if(type_name.equals("租金收入")){
                type_name =getString(R.string.rental_income);
            }else if(type_name.equals("管理费用")){
                type_name = getString(R.string.management_expenses);
            }else if(type_name.equals("保险费用")){
                type_name = getString(R.string.insurance_cost);
            }else if(type_name.equals("物业费用")){
                type_name = getString(R.string.property_costs);
            }else if(type_name.equals("其他")){
                type_name = getString(R.string.ohters);
            }
            if(amount>0){
                textRentPrefix.setText("+");
                imgRentalType.setImageResource(R.drawable.ic_rental_02);
            }else{
                textRentPrefix.setText("-");
                imgRentalType.setImageResource(R.drawable.ic_rental_01);
            }
            textRent.setText(Math.abs(amount)+"");
            textRentalAddress.setText(address);
            textCreateTime.setText(time);
            textRentalType.setText(type_name+"("+currency_name+")");
        }

    }

}
