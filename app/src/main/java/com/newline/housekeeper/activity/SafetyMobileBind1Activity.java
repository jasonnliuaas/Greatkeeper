package com.newline.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.newline.C;
import com.newline.core.BaseActivity;

@ContentView(R.layout.safety_mobilebind1_layout)
public class SafetyMobileBind1Activity extends BaseActivity {
    
    @ViewInject(R.id.textTopTitle)
    private TextView textTopTitle;
    
    @ViewInject(R.id.textSafePhone)
    private TextView textSafePhone;
    
    @ViewInject(R.id.btnUpdatePhone)
    private Button btnUpdatePhone;
    
    private String mobile;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ViewUtils.inject(this);
        
        textTopTitle.setText(getString(R.string.safety));
        
        mobile = getParam("mobile");
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        if("未绑定".equals(mobile)){
            mobile = "未绑定";
            btnUpdatePhone.setText(getString(R.string.bindmobile));
        }
        textSafePhone.setText(getString(R.string.yourmobile) + mobile);
    }
    
    @OnClick(R.id.btnUpdatePhone)
    public void onUpdateBtnClick(View view){
        startActivity(new Intent(this, SafetyMobileBind2Activity.class));
    }
    
    @Override
    public void goBack(View view) {
        C.tempData = mobile;
        setResult(RESULT_OK);
        finish();
    }
    
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            C.tempData = mobile;
            setResult(RESULT_OK);
            finish();
        }
        return true;
    } 
    
    
    

}
