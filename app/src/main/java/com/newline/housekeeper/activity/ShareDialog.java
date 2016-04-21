package com.newline.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.newline.core.BaseActivity;

@ContentView(R.layout.share_dialog)
public class ShareDialog extends BaseActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ViewUtils.inject(this);
        
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
    
    @OnClick(R.id.textBtnShareWeibo)
    public void onWeibo(View view){
        Intent intent = new Intent();
        intent.putExtra("type", 1);
        setResult(RESULT_OK, intent);
        finish();
    }
    
    @OnClick(R.id.textBtnShareQQZone)
    public void onQQZone(View view){
        Intent intent = new Intent();
        intent.putExtra("type", 2);
        setResult(RESULT_OK, intent);
        finish();
    }
    
    @OnClick(R.id.textBtnShareQQ)
    public void onBtnQQ(View view){
        Intent intent = new Intent();
        intent.putExtra("type", 3);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.textBtnShareWeichat)
    public void onWeichat(View view){
        Intent intent = new Intent();
        intent.putExtra("type", 4);
        setResult(RESULT_OK, intent);
        finish();
    }
    
    @OnClick(R.id.textBtnShareFriend)
    public void onFriend(View view){
        Intent intent = new Intent();
        intent.putExtra("type", 5);
        setResult(RESULT_OK, intent);
        finish();
    }
    
    

}
