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

@ContentView(R.layout.find_pwd_mobile_layout2)
public class FindPwdMobile2Activity extends BaseActivity {
    
    @ViewInject(R.id.textTopTitle)
    private TextView textTopTitle;
    
    @ViewInject(R.id.editPwd)
    private EditText editPwd;
    
    @ViewInject(R.id.editPwd1)
    private EditText editPwd1;
    
    private UserService service;
    
    private RegisterHandler handler;
    
    private String mobile;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ViewUtils.inject(this);
        
        handler = new RegisterHandler(this);
        service = UserService.getService(this);
        
        textTopTitle.setText(getString(R.string.findPwd));
        mobile = getParam("mobile");
    }
    
    
    @OnClick(R.id.btnFinishReg)
    public void onFinishReg(View view){
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
        service.doFindPwdMobile2(this, handler, mobile, pwd2);
    }

    static class RegisterHandler extends Handler {
        
        private WeakReference<FindPwdMobile2Activity> weakRef;

        public RegisterHandler(FindPwdMobile2Activity refObj) {
            weakRef = new WeakReference<FindPwdMobile2Activity>(refObj);
        }

        @Override
        public void handleMessage(Message msg) {
            final FindPwdMobile2Activity context = weakRef.get();

            if (msg.what == C.Code.OK) {
                String title = "修改成功";
                String content = "密码已经成功重置。";
                
                final Bundle data = msg.getData();
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
                                context.startActivity(intent);
                                sweetAlertDialog.dismiss();
                                context.finish();
                            }
                        })
                        .show();

            } else {
                TipUtil.showShort(context, msg);
            }
        }
    }
}
