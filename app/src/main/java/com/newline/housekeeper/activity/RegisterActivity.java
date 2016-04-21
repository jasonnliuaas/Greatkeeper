package com.newline.housekeeper.activity;

import java.lang.ref.WeakReference;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.greatkeeper.keeper.R;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.TipUtil;
import com.newline.core.utils.VerifyUtils;
import com.newline.housekeeper.KeeperUrl;
import com.newline.housekeeper.control.SuccessDialog;
import com.newline.housekeeper.control.SuccessDialog.CallBack;
import com.newline.housekeeper.service.UserService;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends BaseActivity {
    
    private EditText mEditEmail;
    private EditText mEditName;
    private EditText mEditPhone;
    private EditText mEditPwd1;
    private EditText mEditPwd2;
    private CheckBox mCbAgreeMent;
    
    private UIHandler handler;
    
    private UserService userService;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        
        userService = UserService.getService(this);
        
        handler = new UIHandler(this);
        
        TextView topTitle = loadControl(R.id.textTopTitle);
        topTitle.setText(getString(R.string.regTitle));
        
        mEditEmail = loadControl(R.id.regEditEmail);
        mEditName = loadControl(R.id.regEditName);
        mEditPhone = loadControl(R.id.regEditPhone);
        mEditPwd1 = loadControl(R.id.regEditPwd1);
        mEditPwd2 = loadControl(R.id.regEditPwd2);
        mCbAgreeMent = loadControl(R.id.cbAgreeMent);
    }
    
    public void onClick(View view){
        int viewId = view.getId();
        
        if(R.id.btnRegSubmit == viewId){
            doRegister();
        } 
        //查看用户协议
        else if(R.id.regBtnUserMent == viewId){
            Intent inient = new Intent(this, WebActivity.class);
            inient.putExtra("title", getString(R.string.user_agreement));
            inient.putExtra("url", KeeperUrl.UserAgreementUrl);
            startActivity(inient);
        }
        
    }
    
    private void doRegister(){
        String account = mEditEmail.getText().toString();
        if(!VerifyUtils.validAccount(account)){
            TipUtil.showShort(this,getString(R.string.error_email));
            return;
        }
        
        String realname = mEditName.getText().toString();
        if(!VerifyUtils.validRealName(realname)){
            TipUtil.showShort(this, getString(R.string.error_email));
            return;
        }
        
        String phone = mEditPhone.getText().toString();
        if(!VerifyUtils.isPhone(phone)){
            TipUtil.showShort(this, getString(R.string.error_phone));
            return;
        }
        
        String password1 = mEditPwd1.getText().toString();
        if(!VerifyUtils.validPassword(password1)){
            TipUtil.showShort(this, getString(R.string.register_error_password));
            return;
        }
        
        String password2 = mEditPwd2.getText().toString();
        if(password2 == null || !password1.equals(password2)){
            TipUtil.showShort(this, getString(R.string.register_error_confirm_password));
            return;
        }
        
        if(!mCbAgreeMent.isChecked()){
            TipUtil.showShort(this, getString(R.string.read_user_agreement));
            return;
        }
        
        userService.doRegister(this, handler, account, password1, phone, realname);
    }
    
    
    static class UIHandler extends Handler {
        private WeakReference<RegisterActivity> mActivity;
        public UIHandler(RegisterActivity activity) { 
            mActivity = new WeakReference<RegisterActivity>(activity); 
        }
        
        @Override
        public void handleMessage(Message msg) {
            final RegisterActivity context = mActivity.get();
            
            //注册成功
            if(msg.what == C.Code.OK){
                String title = context.getString(R.string.regok);
                String content = context.getString(R.string.check_email);
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(title)
                        .setContentText(content)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                context.onBackPressed();
                            }
                        })
                        .show();
                /*SuccessDialog dialog = new SuccessDialog(context, title, content);
                dialog.show();
                dialog.setSuccessCallBack(new CallBack() {
                    
                    @Override
                    public void doCallBack() {
                        context.onBackPressed();
                    }
                });*/
            } 
            //注册失败
            else {
                TipUtil.showShort(context, msg.toString());
            }
        }
    }

}
