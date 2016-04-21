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
import com.newline.housekeeper.service.UserService;

@ContentView(R.layout.register_mobile_layout2)
public class RegisterMobile2Activity extends BaseActivity {
    
    @ViewInject(R.id.textTopTitle)
    private TextView textTopTitle;
    
    @ViewInject(R.id.editVcode)
    private EditText editVcode;
    
    @ViewInject(R.id.textRegMobileTip)
    private TextView textRegMobileTip;
    
    private UserService service;
    
    private RegisterHandler handler;
    
    private String mobile;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ViewUtils.inject(this);
        
        handler = new RegisterHandler(this);
        service = UserService.getService(this);


        textTopTitle.setText(getString(R.string.registerbymobile));
        mobile = getParam("mobile");
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        String tip = getString(R.string.register_error_auth_code);
        if(VerifyUtils.isPhone(mobile)){
            String str =  mobile.substring(0, 3) + "****" + mobile.substring(mobile.length() - 4);
            tip = getString(R.string.phone) + str;
        }
        textRegMobileTip.setText(tip);
    }
    
    @OnClick(R.id.btnSendVcode)
    public void onSendVcodeClick(View view){
        String vcode = editVcode.getText().toString();
        service.doConfirmVcode(this, handler, mobile, vcode);
    }

    static class RegisterHandler extends Handler {
        
        private WeakReference<RegisterMobile2Activity> weakRef;

        public RegisterHandler(RegisterMobile2Activity refObj) {
            weakRef = new WeakReference<RegisterMobile2Activity>(refObj);
        }

        @Override
        public void handleMessage(Message msg) {
            RegisterMobile2Activity context = weakRef.get();

            if (msg.what == C.Code.OK) {
                TipUtil.showShort(context, context.getString(R.string.Mobile_success));
                
                Intent intent = new Intent(context, RegisterMobile3Activity.class);
                intent.putExtra("mobile", msg.obj.toString());
                context.startActivity(intent);
            } else {
                TipUtil.showShort(context, msg.obj.toString());
            }
        }
    }
}
