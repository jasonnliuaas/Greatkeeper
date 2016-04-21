package com.newline.housekeeper.activity;

import java.lang.ref.WeakReference;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatkeeper.keeper.R;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.TipUtil;
import com.newline.housekeeper.DemoData;
import com.newline.housekeeper.control.ListViewForScrollView;
import com.newline.housekeeper.model.RenterAdapter;
import com.newline.housekeeper.service.RentService;

public class RentDetailActivity extends BaseActivity {

    private RentService service;
    
    private TextView mTextHouseAddress;
    private TextView mTextStartDate;
    private TextView mTextEndDate;
    private TextView mTextOpDay;
    private TextView mTextRentFrey;
    private TextView mTextRentTime;
    private TextView mTextRentMonth;
    private TextView mTextRentModel;
    private ListViewForScrollView mListRenter;
    private LinearLayout unLoginLayout;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rent_detail_layout);
        
        mTextHouseAddress = loadControl(R.id.textHouseAddress);
        mTextStartDate = loadControl(R.id.textStartDate);
        mTextEndDate = loadControl(R.id.textRentEndDate);
        mTextOpDay = loadControl(R.id.textOpDay);
        mTextRentFrey = loadControl(R.id.textRentFrey);
        mTextRentTime = loadControl(R.id.textRentTime);
        mTextRentMonth = loadControl(R.id.textRentMonth);
        mTextRentModel = loadControl(R.id.textRentModel);
        
        mListRenter = loadControl(R.id.listRenter);
        unLoginLayout = (LinearLayout) findViewById(R.id.unLoginLayout);
        TextView topTitle = loadControl(R.id.textTopTitle);
        topTitle.setText(getString(R.string.rentList));
        
        handler = new UIHandler(this);
        
        service = RentService.getService(this);
        
        Intent intent = getIntent();
        String houseAddress = intent.getStringExtra("houseAddress");
        mTextHouseAddress.setText( houseAddress);
        
        if(service.isLogined()){
            String propertyId = intent.getStringExtra("propertyId");
            String leaseId = intent.getStringExtra("leaseId");
            unLoginLayout.setVisibility(View.GONE);
            service.doRentDetail(this, propertyId, leaseId, handler);
        } else {
            String data = DemoData.rentDetail();
            handler.obtainMessage(C.Code.OK, data).sendToTarget();
            
            LinearLayout layout = (LinearLayout) findViewById(R.id.unLoginLayout);
            layout.setVisibility(View.VISIBLE);
        }
        
    }
    
    static class UIHandler extends Handler {
        private WeakReference<RentDetailActivity> weakRef;
        
        public UIHandler(RentDetailActivity refObj) { 
            weakRef = new WeakReference<RentDetailActivity>(refObj); 
        }
        
        @Override
        public void handleMessage(Message msg) {
            RentDetailActivity context = weakRef.get();
            
            if(msg.what == C.Code.OK){
                try {
                    JSONObject json = JSON.parseObject(msg.obj.toString());
                    JSONObject leaseJson = json.getJSONObject("lease");
                    JSONArray renterList = json.getJSONArray("renterList");
                    
                    context.mTextStartDate.setText(leaseJson.getString("startdate"));
                    context.mTextEndDate.setText(leaseJson.getString("enddate"));
                    context.mTextOpDay.setText(leaseJson.getString("remaintime"));
                    context.mTextRentFrey.setText(leaseJson.getString("paymentfrequency"));
                    context.mTextRentTime.setText(leaseJson.getString("paymentday"));
                    context.mTextRentMonth.setText(leaseJson.getString("monthrent"));
                    context.mTextRentModel.setText(leaseJson.getString("rentCollectionMode"));
                    
                    context.mListRenter.setAdapter(new RenterAdapter(context, renterList));
                } catch (Exception e) {
                    Log.e(C.TAG, "解析租赁详情数据错误", e);
                    TipUtil.showShort(context, "系统错误");
                }
            } else {
                TipUtil.showShort(context, msg);
            }
        }
        
    }
    
    

}
