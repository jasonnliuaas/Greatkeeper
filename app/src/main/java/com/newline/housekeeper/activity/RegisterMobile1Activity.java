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
import com.newline.housekeeper.service.UserService;

@ContentView(R.layout.register_mobile_layout1)
public class RegisterMobile1Activity extends BaseActivity {
    
    @ViewInject(R.id.textTopTitle)
    private TextView textTopTitle;
    
    @ViewInject(R.id.editMobile)
    private EditText editMoblie;
    
    private UserService service;
    
    private RegisterHandler handler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ViewUtils.inject(this);
        
        handler = new RegisterHandler(this);
        service = UserService.getService(this);
        
        textTopTitle.setText(getString(R.string.registerbymobile));
    }
    
    @OnClick(R.id.btnSendVcode)
    public void onSendVcodeClick(View view){
        String mobile = editMoblie.getText().toString();
        service.doSendMobileCode(this, handler, mobile);
        
//        handler.obtainMessage(C.Code.OK, mobile).sendToTarget();
    }

    static class RegisterHandler extends Handler {
        
        private WeakReference<RegisterMobile1Activity> weakRef;

        public RegisterHandler(RegisterMobile1Activity refObj) {
            weakRef = new WeakReference<RegisterMobile1Activity>(refObj);
        }

        @Override
        public void handleMessage(Message msg) {
            RegisterMobile1Activity context = weakRef.get();

            if (msg.what == C.Code.OK) {
                TipUtil.showShort(context, context.getString(R.string.codesentsuccessfully));
                
                Intent intent = new Intent(context, RegisterMobile2Activity.class);
                intent.putExtra("mobile", msg.obj.toString());
                context.startActivity(intent);
            } else {
                TipUtil.showShort(context, msg.obj.toString());
            }
        }
    }
}
