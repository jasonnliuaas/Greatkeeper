package com.newline.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.StringUtils;

@ContentView(R.layout.safety_layout)
public class SafetyActivity extends BaseActivity {
    
    @ViewInject(R.id.textTopTitle)
    private TextView textTopTitle;
    
    @ViewInject(R.id.imgSafeLevel)
    private ImageView imgSafeLevel;
    
    @ViewInject(R.id.textSafePhone)
    private TextView textSafePhone;
    
    @ViewInject(R.id.textSafeEmail)
    private TextView textSafeEmail;
    
    private String mobile, email;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ViewUtils.inject(this);
        
        textTopTitle.setText(getString(R.string.account_security));
        
        String _data = getParam("data");
        JSONObject data = JSON.parseObject(_data) ;
        
        mobile = data.getString("mobile");
        email = data.getString("email");
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        int startNum = 0;
        if(!StringUtils.isTrimEmpty(mobile) && !"未绑定".equals(mobile)){
            startNum += 3;
        } else {
            mobile = "未绑定";
        }
        
        if(!StringUtils.isTrimEmpty(email) && !"未绑定".equals(email)){
            startNum += 2;
        } else {
            email = "未绑定";
        }
        
        textSafePhone.setText(mobile);
        textSafeEmail.setText(email);
        
        imgSafeLevel.setImageResource(getLevelResId(startNum));
    }
    
    @OnClick(R.id.btnSafePhone)
    public void onPhoneClick(View view){
        if(StringUtils.isTrimEmpty(mobile) || "未绑定".equals(mobile)){
            Intent intent = new Intent(this, SafetyMobileBind2Activity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, SafetyMobileBind1Activity.class);
            intent.putExtra("mobile", mobile);
            startActivityForResult(intent, C.Handler.UPDATE_MOBILE);
        }
    }
    
    @OnClick(R.id.btnSafeEmail)
    public void onEmailClick(View view){
        Intent intent = new Intent(this, SafetyEmailBindActivity.class);
        startActivity(intent);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(requestCode == C.Handler.UPDATE_MOBILE){
            mobile = C.tempData;
            textSafePhone.setText(mobile);
        }
    }
    
    private int getLevelResId(int level){
        if(level == 1) return R.drawable.img_level1;
        if(level == 2) return R.drawable.img_level2;
        if(level == 3) return R.drawable.img_level3;
        if(level == 4) return R.drawable.img_level4;
        if(level == 5) return R.drawable.img_level5;
        return R.drawable.img_level0;
    }

}
