package com.newline.housekeeper.activity;

import java.lang.ref.WeakReference;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.greatkeeper.keeper.R;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.TipUtil;
import com.newline.housekeeper.model.ManagerAdapter;
import com.newline.housekeeper.service.ManagerService;

public class ManagerListActivity extends BaseActivity {
    
    private ManagerService service;

    private TextView mTextTopTitle;
    private ListView mListManager;
    private TextView mTextEmptyTip;
    
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_list_layout);
        
        mTextEmptyTip = loadControl(R.id.textEmptyTip);

        mTextTopTitle = loadControl(R.id.textTopTitle);
        mTextTopTitle.setText(R.string.manager);

        mListManager = loadControl(R.id.listManager);
        
        handler = new UIHandler(this);
        
        service = ManagerService.getService(this);
        service.doManagerList(this, handler);
    }

    static class UIHandler extends Handler {
        private WeakReference<ManagerListActivity> weakRef;

        public UIHandler(ManagerListActivity refObj) {
            weakRef = new WeakReference<ManagerListActivity>(refObj);
        }

        @Override
        public void handleMessage(Message msg) {
            ManagerListActivity context = weakRef.get();

            if (msg.what == C.Code.OK) {
                try {
                    JSONArray dataArray = JSON.parseArray(msg.obj.toString());
                    if(dataArray == null || dataArray.isEmpty()){
                        context.mTextEmptyTip.setVisibility(View.VISIBLE);
                        context.mListManager.setVisibility(View.GONE);
                    } else {
                        context.mListManager.setAdapter(new ManagerAdapter(context, dataArray));
                        context.mListManager.setVisibility(View.VISIBLE);
                        context.mTextEmptyTip.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    Log.e(C.TAG, "解析物业经理列表数据错误", e);
                    TipUtil.showShort(context, "系统错误");
                }
            } else {
                TipUtil.showShort(context, msg);
            }
        }
    }

}
