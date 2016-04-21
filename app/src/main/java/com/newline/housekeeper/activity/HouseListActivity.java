package com.newline.housekeeper.activity;

import java.lang.ref.WeakReference;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatkeeper.keeper.R;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.TipUtil;
import com.newline.housekeeper.DemoData;
import com.newline.housekeeper.model.HouseAdapter;
import com.newline.housekeeper.service.HouseService;
import com.newline.housekeeper.service.RemindService;

public class HouseListActivity extends BaseActivity {
    
    private ListView mListHouse;
    private TextView mTextEmpty;
    private HouseService service;
    private RemindService remindService;
    private Handler handler;
    private LinearLayout layout_gologin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_list_layout);
        
        TextView topTitle = loadControl(R.id.textTopTitle);
        topTitle.setText(getString(R.string.houseInfo));
        
        mListHouse = loadControl(R.id.listHouseList);
        mListHouse.setOnItemClickListener(itemOnClickListener);
        
        mTextEmpty = loadControl(R.id.textEmptyTip);
        
        handler = new UIHandler(this);
        
        remindService = RemindService.getService(this);
        layout_gologin = loadControl(R.id.layout_gologin);
        service = HouseService.getService(this);
        if(service.isLogined()){
            service.doHouseList(this, handler);
            layout_gologin.setVisibility(View.GONE);
        } else {
            layout_gologin.setVisibility(View.VISIBLE);
            JSONArray array = DemoData.houseList();
            handler.obtainMessage(C.Code.OK, array).sendToTarget();
        }
    }
    
    
    public OnItemClickListener itemOnClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            try {
                JSONObject data = (JSONObject) adapter.getItemAtPosition(position);
                
                Intent intent = new Intent(HouseListActivity.this, HouseDetailActivity.class);
                intent.putExtra("propertyId", data.getString("id"));
                intent.putExtra("position",position);
                startActivity(intent);
            } catch (Exception e) {
                Log.e(C.TAG, "Receive list item object error", e);
            }
        }
    };
    
    
    static class UIHandler extends Handler {
        private WeakReference<HouseListActivity> weakRef;
        
        public UIHandler(HouseListActivity refObj) { 
            weakRef = new WeakReference<HouseListActivity>(refObj); 
        }
        
        @Override
        public void handleMessage(Message msg) {
            HouseListActivity context = weakRef.get();
            
            if(msg.what == C.Code.OK){
                try {
                    JSONArray dataArray = JSON.parseArray(msg.obj.toString());
                    if(dataArray == null || dataArray.isEmpty()){
                        context.mTextEmpty.setVisibility(View.VISIBLE);
                        context.mListHouse.setVisibility(View.GONE);
                    } else {
                        context.remindService.doRemindList(context, C.MsgType.HOUSE);
                        
                        context.mListHouse.setAdapter(new HouseAdapter(context, dataArray));
                        context.mListHouse.setVisibility(View.VISIBLE);
                        context.mTextEmpty.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    Log.e(C.TAG, "解析房产列表数据错误", e);
                    TipUtil.showShort(context, "系统错误");
                }
            } else {
                TipUtil.showShort(context, msg);
            }
        }
    }
}
