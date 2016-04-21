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
import com.newline.housekeeper.HttpRequest;
import com.newline.housekeeper.MainActivity;
import com.newline.housekeeper.service.UserService;

public class UpdatePasswordActivity extends BaseActivity {
    
    private TextView mTextTopTitle;
    
    private EditText mEditOldPwd;
    private EditText mEditNewPwd;
    private EditText mEditNewPwd1;
    
    private UserService service;
    
    private Handler handler;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password_layout);
        
        service = UserService.getService(this);
        
        handler = new UIHandler(this);
        
        mTextTopTitle = loadControl(R.id.textTopTitle);
        mTextTopTitle.setText(R.string.updatePwdTitle);
        
        mEditOldPwd = loadControl(R.id.editOldPwd);
        mEditNewPwd = loadControl(R.id.editNewPwd);
        mEditNewPwd1 = loadControl(R.id.editPwdConfirm);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if(service.getUser() == null){
            startActivity(new Intent(this, MainActivity.class));
        }
    }
    
    public void onClick(View view){
        int viewId = view.getId();
        
        if(viewId == R.id.btnSavePwd){
            String oldPwd = mEditOldPwd.getText().toString();
            if(!VerifyUtils.validPassword(oldPwd)){
                TipUtil.showShort(this, getString(R.string.error_pwd));
                return;
            }
            
            String newPwd = mEditNewPwd.getText().toString();
            if(!VerifyUtils.validPassword(newPwd)){
                TipUtil.showShort(this, getString(R.string.register_error_password));
                return;
            }
            
            String newPwd1 = mEditNewPwd1.getText().toString();
            if(!newPwd.equals(newPwd1)){
                TipUtil.showShort(this, getString(R.string.register_error_confirm_password));
                return;
            }
            
            service.doUpdatePassword(this, handler, oldPwd, newPwd1);
        }
        
    }
    
    static class UIHandler extends Handler {
        private WeakReference<UpdatePasswordActivity> weakRef;
        
        public UIHandler(UpdatePasswordActivity refObj) { 
            weakRef = new WeakReference<UpdatePasswordActivity>(refObj); 
        }
        
        @Override
        public void handleMessage(Message msg) {
            final UpdatePasswordActivity context = weakRef.get();
            
            if(msg.what == C.Code.OK){
                TipUtil.showLong(context, context.getString(R.string.success_changepwd));
                HttpRequest.forwardLogin(context);
            } else {
                TipUtil.showShort(context, msg.obj.toString());
            }
        }
    }
    

}
