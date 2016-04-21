package com.newline.housekeeper.activity;

import java.lang.ref.WeakReference;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.SPUtil;
import com.newline.core.utils.TipUtil;
import com.newline.core.utils.VerifyUtils;
import com.greatkeeper.keeper.R;
import com.newline.housekeeper.MainActivity;
import com.newline.housekeeper.service.UserService;

public class LoginActivity extends BaseActivity {
    
    private EditText mAccount;
    private EditText mPassword;
    
    @ViewInject(R.id.textRememberPwd)
    private TextView textRememberPwd;
    
    private UIHandler handler;
    
    private UserService userService;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        
        ViewUtils.inject(this);
        
        userService = UserService.getService(this);
        
        handler = new UIHandler(this);
        
        TextView topTitle = loadControl(R.id.textTopTitle);
        topTitle.setText(getString(R.string.LoginTitle));
        
        mAccount = loadControl(R.id.eTextAccount);
        mPassword = loadControl(R.id.eTextPassword);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        SPUtil spUtil = SPUtil.getUtil(this);
        boolean isRemember = spUtil.getBoolean(C.REMEMBER_PWD_KEY, true);
        if(isRemember){
            @SuppressWarnings("deprecation")
            Drawable drawable = getResources().getDrawable(R.drawable.img_checked);
            textRememberPwd.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            
            String account = spUtil.getString(C.REMEMBER_ACCOUNT);
            String pwd = spUtil.getString(C.REMEMBER_PWD);
            if(account.length() > 0 && pwd.length() > 0){
                mAccount.setText(account);
                mPassword.setText(pwd);
            }
        }
        
        String account = getParam("account");
        if(account != null){
            mAccount.setText(account);
            mPassword.setText("");
        }
    }
    
    public void onClick(View view){
        int viewId = view.getId();
        
        //处理登录逻辑
        if(R.id.btnLogin == viewId){
            String account = mAccount.getText().toString();
            String password = mPassword.getText().toString();
            
            if(!VerifyUtils.validAccount(account)){
                TipUtil.showLong(this, getString(R.string.account_error));
                return;
            }
            if(!VerifyUtils.validPassword(password)){
                TipUtil.showLong(this, getString(R.string.error_pwd));
            }
            userService.doLogin(this, handler, account, password);
        }
        
        //忘记密码
        else if(R.id.btnForgetPwd == viewId){
            startActivity(new Intent(this, FindPwdActivity.class));
        }
    }
    
    @OnClick(R.id.btnRegist)
    public void onRegistClick(View view){
        startActivity(new Intent(this, RegisterMainActivity.class));
    }
    
    @SuppressWarnings("deprecation")
    @OnClick(R.id.textRememberPwd)
    public void onRememberPwdClick(View view){
        SPUtil spUtil = SPUtil.getUtil(this);
        boolean isRemember = spUtil.getBoolean(C.REMEMBER_PWD_KEY, true);
        int resId = isRemember ? R.drawable.img_unchecked : R.drawable.img_checked;
        
        Drawable drawable = getResources().getDrawable(resId);
        textRememberPwd.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        
        spUtil.putBoolean(C.REMEMBER_PWD_KEY, !isRemember);
    }
    
    static class UIHandler extends Handler {
        private WeakReference<LoginActivity> mActivity;
        
        public UIHandler(LoginActivity activity) { 
            mActivity = new WeakReference<LoginActivity>(activity); 
        }
        
        @Override
        public void handleMessage(Message msg) {
            LoginActivity context = mActivity.get();
            
            // 登录成功
            if(msg.what == C.Code.OK){
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
            // 登录失败
            else {
                TipUtil.showShort(context, msg); 
            }
            
        }
    }

}
