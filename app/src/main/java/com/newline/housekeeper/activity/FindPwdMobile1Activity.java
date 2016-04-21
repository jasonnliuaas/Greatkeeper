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
import com.newline.core.utils.StringUtils;
import com.newline.core.utils.TipUtil;
import com.newline.housekeeper.service.UserService;

@ContentView(R.layout.find_pwd_mobile_layout1)
public class FindPwdMobile1Activity extends BaseActivity {
    
    @ViewInject(R.id.textTopTitle)
    private TextView textTopTitle;
    
    @ViewInject(R.id.textMobile)
    private TextView textMobile;
    
    @ViewInject(R.id.editVcode)
    private EditText editVcode;
    
    private UserService service;
    
    private FindPwdHandler handler;
    
    private String mobile;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ViewUtils.inject(this);
        
        handler = new FindPwdHandler(this);
        service = UserService.getService(this);
        
        textTopTitle.setText(getString(R.string.findPwd));
        mobile = getParam("mobile");
        
        textMobile.setText(mobile);
    }
    
    
    @OnClick(R.id.btnNextStep)
    public void onNextStep(View view){
        String vcode = editVcode.getText().toString();
        if(StringUtils.isTrimEmpty(vcode)){
            TipUtil.showShort(this, getString(R.string.findPwd));
            return;
        }
        service.doConfirmVcode(this, handler, mobile, vcode);
    }

    static class FindPwdHandler extends Handler {
        
        private WeakReference<FindPwdMobile1Activity> weakRef;

        public FindPwdHandler(FindPwdMobile1Activity refObj) {
            weakRef = new WeakReference<FindPwdMobile1Activity>(refObj);
        }

        @Override
        public void handleMessage(Message msg) {
            final FindPwdMobile1Activity context = weakRef.get();

            if (msg.what == C.Code.OK) {
                TipUtil.showShort(context, "验证码验证成功,请输入新密码");
                Intent intent = new Intent(context, FindPwdMobile2Activity.class);
                intent.putExtra("mobile", msg.obj.toString());
                context.startActivity(intent);
            } else {
                TipUtil.showShort(context, msg);
            }
        }
    }
}
