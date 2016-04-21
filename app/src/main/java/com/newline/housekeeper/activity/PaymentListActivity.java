package com.newline.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.newline.core.BaseActivity;
import com.newline.housekeeper.model.PaymentAdapter;

@ContentView(R.layout.payment_list_layout)
public class PaymentListActivity extends BaseActivity {
    @ViewInject(R.id.textTopTitle)
    private TextView mTopTitle;
    @ViewInject(R.id.layout_gologin)
    private LinearLayout layout_gologin;
    
    @ViewInject(R.id.listPayment)
    private ListView mListPayment;
    
    @ViewInject(R.id.textEmptyTip)
    private TextView mTextEmptyTip;

    
    private boolean isLogin = true;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        
        mTopTitle.setText(R.string.homeText05);
        
        String data = getParam("data");
        isLogin = getParam("isLogin");
        try {
            JSONArray dataArray = JSON.parseArray(data);
            if(dataArray ==  null || dataArray.isEmpty()){
                mTextEmptyTip.setVisibility(View.VISIBLE);
                mListPayment.setVisibility(View.GONE);
            } else {
                mListPayment.setAdapter(new PaymentAdapter(this, dataArray));
                mListPayment.setVisibility(View.VISIBLE);
                mTextEmptyTip.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            LogUtils.e("start activity load data error.", e);
        }
        
        if(!isLogin){
            layout_gologin.setVisibility(View.VISIBLE);
        }else{
            layout_gologin.setVisibility(View.GONE);
        }
    }
    
    @OnItemClick(R.id.listPayment)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object data = parent.getAdapter().getItem(position);
        LogUtils.d(data.toString());
        Intent intent = new Intent(this, PaymentDetailActivity.class);
        intent.putExtra("data", data.toString());
        intent.putExtra("isLogin", isLogin);
        startActivity(intent);
    }

}
