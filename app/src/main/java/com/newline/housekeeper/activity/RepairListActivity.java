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
import com.newline.housekeeper.model.RepairAdapter;
import com.newline.housekeeper.service.RemindService;
import com.newline.housekeeper.service.RepairService;

public class RepairListActivity extends BaseActivity {
    
    private ListView mListRepair;
    private RepairService service;
    
    private RemindService remindService;
    
    private TextView mTextEmptyTip;
    
    private Handler handler;
    private LinearLayout layout_gologin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_list_layout);
        
        TextView topTitle = loadControl(R.id.textTopTitle);
        layout_gologin = loadControl(R.id.layout_gologin);
        topTitle.setText(getString(R.string.houseRepair));
        
        mListRepair = loadControl(R.id.listRepairList);
        mListRepair.setOnItemClickListener(itemOnClickListener);
        
        mTextEmptyTip = loadControl(R.id.textEmptyTip);
        
        handler = new UIHandler(this);
        
        remindService = RemindService.getService(this);
        
        service = RepairService.getService(this);
        
        if(service.isLogined()){
            service.doRepairList(this, handler);
            layout_gologin.setVisibility(View.GONE);
        } else {

            layout_gologin.setVisibility(View.VISIBLE);
            
            JSONArray array = DemoData.repairList();
            handler.obtainMessage(C.Code.OK, array).sendToTarget();
        }
    }
    
    
    public OnItemClickListener itemOnClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            try {
                JSONObject data = (JSONObject) adapter.getItemAtPosition(position);
                
                String address = "没有找到参数propertyaddress";
                if(data.containsKey("propertyaddress")){
                    address = data.getString("propertyaddress");
                }
                
                Intent intent = new Intent(RepairListActivity.this, RepairDetailActivity.class);
                intent.putExtra("propertyId", data.getString("propertyid"));
                intent.putExtra("maintenanceId", data.getString("id"));
                intent.putExtra("repairAddress", address);
                startActivity(intent);
            } catch (Exception e) {
                Log.e(C.TAG, "Receive list item object error", e);
            }
        }
    };
    
    
    static class UIHandler extends Handler {
        private WeakReference<RepairListActivity> weakRef;
        
        public UIHandler(RepairListActivity refObj) { 
            weakRef = new WeakReference<RepairListActivity>(refObj); 
        }
        
        @Override
        public void handleMessage(Message msg) {
            RepairListActivity context = weakRef.get();
            
            if(msg.what == C.Code.OK){
                try {
                    JSONArray dataArray = JSON.parseArray(msg.obj.toString());
                    if(dataArray == null || dataArray.isEmpty()){
                        context.mTextEmptyTip.setVisibility(View.VISIBLE);
                        context.mListRepair.setVisibility(View.GONE);
                    } else {
                        context.remindService.doRemindList(context, C.MsgType.REPAIR);
                        
                        context.mListRepair.setAdapter(new RepairAdapter(context, dataArray));
                        context.mListRepair.setVisibility(View.VISIBLE);
                        context.mTextEmptyTip.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    Log.e(C.TAG, "解析房产维修列表数据错误", e);
                    TipUtil.showShort(context, "系统错误");
                }
            } else {
                TipUtil.showShort(context, msg);
            }
        }
    }
}
