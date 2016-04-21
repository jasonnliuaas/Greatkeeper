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
import com.newline.housekeeper.service.UserService;

@ContentView(R.layout.safety_mobilebind3_layout)
public class SafetyMobileBind3Activity extends BaseActivity {
    
    @ViewInject(R.id.textTopTitle)
    private TextView textTopTitle;
    
    @ViewInject(R.id.editSafetyVcode)
    private EditText editSafetyVcode;
    
    @ViewInject(R.id.btnConfirmVcode)
    private Button btnConfirmVcode;
    
    private String mobile;
    
    private UserService service;
    
    private SafetyHandler handler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ViewUtils.inject(this);
        
        handler = new SafetyHandler(this);
        
        service = UserService.getService(this);
        
        mobile = getParam("mobile");
        
        textTopTitle.setText(getString(R.string.phone_binding));
    }
    
    @OnClick(R.id.btnConfirmVcode)
    public void onConfirmVcode(View view){
        String vcode = editSafetyVcode.getText().toString();
        service.doSetBindMobile(this, handler, mobile, vcode);
    }
    
    static class SafetyHandler extends Handler {
        
        private WeakReference<SafetyMobileBind3Activity> weakRef;

        public SafetyHandler(SafetyMobileBind3Activity refObj) {
            weakRef = new WeakReference<SafetyMobileBind3Activity>(refObj);
        }

        @Override
        public void handleMessage(Message msg) {
            SafetyMobileBind3Activity context = weakRef.get();

            if (msg.what == C.Code.OK) {
                Intent intent = new Intent(context, SafetyMobileBind1Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("mobile", msg.obj.toString());
                intent.putExtra("return", true);
                context.startActivity(intent);
            } else {
                TipUtil.showShort(context, msg);
            }
        }
    }

}
