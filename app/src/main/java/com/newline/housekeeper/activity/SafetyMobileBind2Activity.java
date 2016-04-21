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

@ContentView(R.layout.safety_mobilebind2_layout)
public class SafetyMobileBind2Activity extends BaseActivity {
    
    @ViewInject(R.id.textTopTitle)
    private TextView textTopTitle;
    
    @ViewInject(R.id.editSafetyPhone)
    private EditText editSafetyPhone;
    
    @ViewInject(R.id.btnSendVcode)
    private Button btnSendVcode;
    
    private SafetyHandler handler;
    
    private UserService service;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ViewUtils.inject(this);
        
        handler = new SafetyHandler(this);
        
        service = UserService.getService(this);
        
        textTopTitle.setText(getString(R.string.phone_binding));
    }
    
    @OnClick(R.id.btnSendVcode)
    public void onSendVcodeClick(View view){
        String mobile = editSafetyPhone.getText().toString();
        service.doSendMobileCode(this, handler, mobile);
        
//        handler.obtainMessage(C.Code.OK, mobile).sendToTarget();
    }
    
    static class SafetyHandler extends Handler {
        
        private WeakReference<SafetyMobileBind2Activity> weakRef;

        public SafetyHandler(SafetyMobileBind2Activity refObj) {
            weakRef = new WeakReference<SafetyMobileBind2Activity>(refObj);
        }

        @Override
        public void handleMessage(Message msg) {
            SafetyMobileBind2Activity context = weakRef.get();

            if (msg.what == C.Code.OK) {
                TipUtil.showShort(context, context.getString(R.string.codesentsuccessfully));
                
                Intent intent = new Intent(context, SafetyMobileBind3Activity.class);
                intent.putExtra("mobile", msg.obj.toString());
                context.startActivity(intent);
            } else {
                TipUtil.showShort(context, msg);
            }
        }
    }

}
