package com.newline.housekeeper.activity;

import java.lang.ref.WeakReference;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.greatkeeper.keeper.R;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.TipUtil;
import com.newline.core.utils.VerifyUtils;
import com.newline.housekeeper.control.SuccessDialog;
import com.newline.housekeeper.control.SuccessDialog.CallBack;
import com.newline.housekeeper.service.UserService;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FindPwdActivity extends BaseActivity {
    
    private EditText mEditFindEmail;
    
    private UIHandler handler;
    
    private UserService userService;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_pwd_layout);
        
        userService = UserService.getService(this);
        
        handler = new UIHandler(this);
        
        TextView topTitle = loadControl(R.id.textTopTitle);
        topTitle.setText(getString(R.string.findPwdTitle));
        
        mEditFindEmail = loadControl(R.id.editFindEmail);
    }
    
    public void onClick(View view){
        int viewId = view.getId();
        
        if(R.id.btnFindPwd == viewId){
            doFindPassword();
        } 
    }
    
    private void doFindPassword(){
        String account = mEditFindEmail.getText().toString();
        
        if(VerifyUtils.isPhone(account)){
            userService.doFindPwdMobile1(this, handler, account);
        } else if(VerifyUtils.isEmail(account)){
            userService.doFindPassword(this, handler, account);
        } else {
            TipUtil.showShort(this, getString(R.string.account_error));
        }
    }
    
    
    static class UIHandler extends Handler {
        private WeakReference<FindPwdActivity> mActivity;
        
        public UIHandler(FindPwdActivity activity) { 
            mActivity = new WeakReference<FindPwdActivity>(activity); 
        }
        
        @Override
        public void handleMessage(Message msg) {
            final FindPwdActivity context = mActivity.get();
            
            //找回密码成功
            if(msg.what == C.Handler.FIDN_PWD_EMAIL){
                String title = context.getString(R.string.success_tips);
                String content = context.getString(R.string.resetpwd_tips);
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
            } else if(msg.what == C.Handler.FIDN_PWD_MOBILE){
                Intent intent = new Intent(context, FindPwdMobile1Activity.class);
                intent.putExtra("mobile", msg.obj.toString());
                context.startActivity(intent);
            } else { 
                TipUtil.showShort(context, msg);
            }
        }
    }

}
