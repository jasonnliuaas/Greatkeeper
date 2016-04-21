package com.newline.housekeeper.activity;

import java.lang.ref.WeakReference;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.TipUtil;
import com.newline.core.utils.VerifyUtils;
import com.newline.housekeeper.control.SuccessDialog;
import com.newline.housekeeper.control.SuccessDialog.CallBack;
import com.newline.housekeeper.service.UserService;

import cn.pedant.SweetAlert.SweetAlertDialog;

@ContentView(R.layout.safety_emailbind_layout)
public class SafetyEmailBindActivity extends BaseActivity {
    
    @ViewInject(R.id.textTopTitle)
    private TextView textTopTitle;
    
    @ViewInject(R.id.editSafetyEmail)
    private EditText editSafetyEmail;
    
    @ViewInject(R.id.btnSendVEmail)
    private Button btnSendVEmail;
    
    private UserService service;
    
    private SafetyHandler handler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ViewUtils.inject(this);
        
        handler = new SafetyHandler(this);
        
        service = UserService.getService(this);
        
        textTopTitle.setText(getString(R.string.mailbox_binding));
        
        if(getParam("update") != null){
            btnSendVEmail.setText( getString(R.string.replace_mailbox));
        }
    }
    
    @OnClick(R.id.btnSendVEmail)
    public void onSendVEmail(View view){
        String email = editSafetyEmail.getText().toString();
        if(!VerifyUtils.isEmail(email)){
            TipUtil.showShort(this, getString(R.string.error_email));
            return;
        }
        
        service.doSendVEmail(this, handler, email);
    }
    
    static class SafetyHandler extends Handler {
        
        private WeakReference<SafetyEmailBindActivity> weakRef;

        public SafetyHandler(SafetyEmailBindActivity refObj) {
            weakRef = new WeakReference<SafetyEmailBindActivity>(refObj);
        }

        @Override
        public void handleMessage(Message msg) {
            final SafetyEmailBindActivity context = weakRef.get();

            if (msg.what == C.Code.OK) {
                String title = context.getString(R.string.sentsuccessfully);
                String content = context.getString(R.string.login_email)+ msg.obj + context.getString(R.string.check_email);
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(title)
                        .setContentText(content)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                context.btnSendVEmail.setText(context.getString(R.string.Resend_mail));
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            } else {
                TipUtil.showShort(context, msg);
            }
        }
    }

}
