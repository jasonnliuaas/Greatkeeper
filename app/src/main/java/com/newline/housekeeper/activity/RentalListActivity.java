package com.newline.housekeeper.activity;

import java.lang.ref.WeakReference;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.TipUtil;
import com.newline.housekeeper.DemoData;
import com.newline.housekeeper.model.RentalAdapter;
import com.newline.housekeeper.service.RentalService;

@ContentView(R.layout.rental_list_layout)
public class RentalListActivity extends BaseActivity {
    
    @ViewInject(R.id.textTopTitle)
    private TextView mTopTitle;
    @ViewInject(R.id.layout_gologin)
    private LinearLayout layout_gologin;
    @ViewInject(R.id.listRental)
    private ListView mListRental;
    @ViewInject(R.id.textEmptyTip)
    private LinearLayout mTextEmptyTip;
    
    private RentalService rentalService;
    
    private UIHandler handler;
    
    private RentalAdapter adapter;
    
    private TextView textTotalMoney;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        mTopTitle.setText(R.string.homeText04);
        handler = new UIHandler(this);
        rentalService = RentalService.getService(this);
        
        initView();
    }
    
    public void initView(){
        if(rentalService.isLogined()){
           /* View footView = getLayoutInflater().inflate(R.layout.rental_foot_view, null);
            textTotalMoney = (TextView) footView.findViewById(R.id.textTotalMoney);
            mListRental.addFooterView(footView);
            mListRental.setFooterDividersEnabled(false);*/
            
            String data = getParam("data");
            /*reLoadList(data);*/
            layout_gologin.setVisibility(View.GONE);
            reLoaddata();
        } else {

            layout_gologin.setVisibility(View.VISIBLE);
           /* String data = DemoData.rentalList();
            reLoadList(data);*/
            String data = DemoData.rentalList();
            reLoaddata(data);
        }
    }
    @OnItemClick(R.id.listRental)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject data = (JSONObject) parent.getAdapter().getItem(position);

        Intent intent = new Intent(this, RentalDetailActivity.class);
        intent.putExtra("id", data.getString("id"));
        startActivity(intent);
    }

    private void reLoaddata(){
        rentalService.loadRentalList(this,handler);
    }

    private void reLoadList(String data){
        JSONArray dataArray = null;
        try {
            dataArray = JSON.parseArray(data);
            
            if(dataArray == null || dataArray.isEmpty()){
                mTextEmptyTip.setVisibility(View.VISIBLE);
                mListRental.setVisibility(View.GONE);
            } else {
                if(adapter == null){
                    adapter = new RentalAdapter(this, dataArray);
                    mListRental.setAdapter(adapter);
                } else {
                    adapter.updateList(dataArray);
                }
                mListRental.setVisibility(View.VISIBLE);
                mTextEmptyTip.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            LogUtils.e("加载租金明细列表错误", e);
        }
    }
    private void reLoaddata(String data){
        JSONArray dataArray = null;
        try {
            dataArray = JSON.parseArray(data);

            if(dataArray == null || dataArray.isEmpty()){
                mTextEmptyTip.setVisibility(View.VISIBLE);
                mListRental.setVisibility(View.GONE);
            } else {
                if(adapter == null){
                    adapter = new RentalAdapter(this, dataArray);
                    mListRental.setAdapter(adapter);
                } else {
                    adapter.updateList(dataArray);
                }
                mListRental.setVisibility(View.VISIBLE);
                mTextEmptyTip.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            LogUtils.e("加载租金明细列表错误", e);
        }
    }
    
    static class UIHandler extends Handler {
        private WeakReference<RentalListActivity> mActivity;
        
        public UIHandler(RentalListActivity activity) { 
            mActivity = new WeakReference<RentalListActivity>(activity); 
        }
        
        @Override
        public void handleMessage(Message msg) {
            RentalListActivity context = mActivity.get();
            
            if (msg.what == C.Handler.LOAD_RENTAL_LIST) {
                String data = (String) msg.obj;
                context.reLoadList(data);
            }
            else if(msg.what == C.Handler.LOAD_RENTAL){
                String data = (String) msg.obj;
                context.reLoaddata(data);
            }else {
                TipUtil.showShort(context, msg);
            }
        }
    }
}
