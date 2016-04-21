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

@ContentView(R.layout.register_email_layout)
public class RegisterEmailActivity extends BaseActivity {
    
    @ViewInject(R.id.textTopTitle)
    private TextView textTopTitle;
    
    @ViewInject(R.id.editEmail)
    private EditText editEmail;
    
    @ViewInject(R.id.editPwd)
    private EditText editPwd;
    
    @ViewInject(R.id.editPwd1)
    private EditText editPwd1;
    
    private UserService service;
    
    private RegisterHandler handler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ViewUtils.inject(this);
        
        handler = new RegisterHandler(this);
        service = UserService.getService(this);
        
        textTopTitle.setText(getString(R.string.registerbyemail));
    }
    
    
    @OnClick(R.id.btnSendVEmail)
    public void onSendVEmail(View view){
        String email = editEmail.getText().toString();
        if(!VerifyUtils.isEmail(email)){
            TipUtil.showShort(this, getString(R.string.register_error_email));
            return; 
        }
        
        String pwd1 = editPwd.getText().toString();
        if(!VerifyUtils.validPassword(pwd1)){
            TipUtil.showShort(this, getString(R.string.register_error_password));
            return;
        }
        
        String pwd2 = editPwd1.getText().toString();
        if(!pwd1.equals(pwd2)){
            TipUtil.showShort(this, getString(R.string.register_error_confirm_password));
            return;
        }
        service.doEmailRegister(this, handler, email, pwd1);
    }

    static class RegisterHandler extends Handler {
        
        private WeakReference<RegisterEmailActivity> weakRef;

        public RegisterHandler(RegisterEmailActivity refObj) {
            weakRef = new WeakReference<RegisterEmailActivity>(refObj);
        }

        @Override
        public void handleMessage(Message msg) {
            final RegisterEmailActivity context = weakRef.get();

            if (msg.what == C.Code.OK) {
                String title = context.getString(R.string.regok);
                String content = context.getString(R.string.registerVerificationbyemail);
                
                final Bundle data = msg.getData();
                
                /*final SuccessDialog dialog = new SuccessDialog(context, title, content);
                dialog.show();
                dialog.setSuccessCallBack(new CallBack() {
                    
                    @Override
                    public void doCallBack() {
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("account", data.getString("account"));
                        intent.putExtra("pwd", data.getString("pwd"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        context.finish();
                    }
                });*/
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(title)
                        .setContentText(content)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.putExtra("account", data.getString("account"));
                                intent.putExtra("pwd", data.getString("pwd"));
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                sweetAlertDialog.dismiss();
                                context.startActivity(intent);
                                context.finish();
                            }
                        })
                        .show();
            } else {
                TipUtil.showShort(context, msg.obj.toString());
            }
        }
    }
}
