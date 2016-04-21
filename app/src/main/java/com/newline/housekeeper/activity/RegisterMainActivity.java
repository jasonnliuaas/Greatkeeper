package com.newline.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.newline.core.BaseActivity;
import com.newline.core.utils.TipUtil;
import com.newline.housekeeper.KeeperUrl;

@ContentView(R.layout.register_main_layout)
public class RegisterMainActivity extends BaseActivity {
    
    @ViewInject(R.id.textTopTitle)
    private TextView textTopTitle;
    
    @ViewInject(R.id.checkAgreeMent)
    private CheckBox checkAgreeMent;

    @ViewInject(R.id.layout_RegisterMobile)
    LinearLayout layout_RegisterMobile;


    @ViewInject(R.id.layout_RegisterEmail)
    LinearLayout layout_RegisterEmail;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        
        ViewUtils.inject(this);
        
        textTopTitle.setText(getString(R.string.register));
    }
    
    @OnClick(R.id.layout_RegisterMobile)
    public void onRegByMobile(View view){
        if(!checkAgreeMent.isChecked()){
            TipUtil.showShort(this, getString(R.string.reg_tips));
            return;
        }
        startActivity(new Intent(this, RegisterMobile1Activity.class));
    }
    
    @OnClick(R.id.layout_RegisterEmail)
    public void onRegByEmail(View view){
        if(!checkAgreeMent.isChecked()){
            TipUtil.showShort(this, getString(R.string.reg_tips));
            return;
        }
        startActivity(new Intent(this, RegisterEmailActivity.class));
    }
    
    @OnClick(R.id.textAgreeMent)
    public void onReadAgreeClick(View view){
        Intent inient = new Intent(this, WebActivity.class);
        inient.putExtra("title", getString(R.string.user_agreement));
        inient.putExtra("url", KeeperUrl.UserAgreementUrl);
        startActivity(inient);
    }

}
