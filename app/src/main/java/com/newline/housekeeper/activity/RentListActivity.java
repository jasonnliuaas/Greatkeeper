package com.newline.housekeeper.activity;

import java.lang.ref.WeakReference;
import java.util.List;

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

import com.greatkeeper.keeper.R;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.TipUtil;
import com.newline.housekeeper.DemoData;
import com.newline.housekeeper.model.RentAdapter;
import com.newline.housekeeper.model.RentBean;
import com.newline.housekeeper.service.RemindService;
import com.newline.housekeeper.service.RentService;

public class RentListActivity extends BaseActivity {
    
    private ListView mListRent;
    private TextView mTextEmpty;
    private RentService rentService;
    
    private RemindService remindService;
    
    private Handler handler;

    private LinearLayout layout_gologin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rent_list_layout);

        TextView topTitle = loadControl(R.id.textTopTitle);
        topTitle.setText(getString(R.string.houseLease));
        layout_gologin = loadControl(R.id.layout_gologin);
        mListRent = loadControl(R.id.listRentList);
        mListRent.setOnItemClickListener(itemOnClickListener);
        
        mTextEmpty = loadControl(R.id.textEmptyTip);
        
        handler = new UIHandler(this);
        
        rentService = RentService.getService(this);
        
        remindService = RemindService.getService(this);
        
        if(rentService.isLogined()){
            rentService.doRentList(this, handler);
            layout_gologin.setVisibility(View.GONE);
        } else {

            layout_gologin.setVisibility(View.VISIBLE);
            
            List<RentBean> array = DemoData.rentList();
            handler.obtainMessage(C.Code.OK, array).sendToTarget();
        }
    }
    
    
    public OnItemClickListener itemOnClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            try {
                RentBean bean = (RentBean) adapter.getItemAtPosition(position);
               
                Intent intent = new Intent(RentListActivity.this, RentDetailActivity.class);
                intent.putExtra("propertyId", bean.getPropertyid());
                intent.putExtra("leaseId", bean.getLeaseid());
                intent.putExtra("houseAddress", bean.getPropertyaddress());
                startActivity(intent);
                
            } catch (Exception e) {
                Log.e(C.TAG, "Receive list item object error", e);
            }
        }
    };
    
    
    static class UIHandler extends Handler {
        private WeakReference<RentListActivity> weakRef;
        
        public UIHandler(RentListActivity refObj) { 
            weakRef = new WeakReference<RentListActivity>(refObj); 
        }
        
        @Override
        @SuppressWarnings("unchecked")
        public void handleMessage(Message msg) {
            RentListActivity context = weakRef.get();
            
            if(msg.what == C.Code.OK){
                List<RentBean> dataList = ((List<RentBean>) msg.obj);
                if(dataList == null || dataList.isEmpty()){
                    context.mTextEmpty.setVisibility(View.VISIBLE);
                    context.mListRent.setVisibility(View.GONE);
                } else {
                    context.remindService.doRemindList(context, C.MsgType.RENT);
                    
                    context.mListRent.setAdapter(new RentAdapter(context, dataList));
                    context.mListRent.setVisibility(View.VISIBLE);
                    context.mTextEmpty.setVisibility(View.GONE);
                }
                
            } else {
                TipUtil.showShort(context, msg);
            }
        }
    }
}
