package com.newline.housekeeper.activity;

import java.lang.ref.WeakReference;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.greatkeeper.keeper.R;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.TipUtil;
import com.newline.core.utils.VerifyUtils;
import com.newline.housekeeper.control.SuccessDialog;
import com.newline.housekeeper.model.UserBean;
import com.newline.housekeeper.service.UserService;

public class UpdateUserInfoActivity extends BaseActivity {

    private TextView mTextTopTitle;

    private EditText mEditNickName;
    private EditText mEditRealName;

    private UserService service;

    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_userinfo_layout);

        service = UserService.getService(this);

        handler = new UIHandler(this);

        mTextTopTitle = loadControl(R.id.textTopTitle);
        mTextTopTitle.setText(R.string.updateInfoTitle);

        mEditNickName = loadControl(R.id.editNickName);
        mEditRealName = loadControl(R.id.editRealName);

        UserBean user = service.getUser();
        if(user != null){
            mEditNickName.setText(user.getNickname());
            mEditRealName.setText(user.getRealname());
        }

    }

    public void onClick(View view){
        int viewId = view.getId();

        if(viewId == R.id.btnSaveInfo){
            String nickName = mEditNickName.getText().toString();
            if(!VerifyUtils.validString(nickName, 4, 20)){
                TipUtil.showShort(this, getString(R.string.nick_tips));
                return;
            }

            String realName = mEditRealName.getText().toString();
            if(!VerifyUtils.validRealName(realName)){
                TipUtil.showShort(this, getString(R.string.name_tips));
                return;
            }


            service.doUpdateInfo(this, handler, nickName, realName);
        }

    }

    @SuppressLint("ShowToast") static class UIHandler extends Handler {
        private WeakReference<UpdateUserInfoActivity> weakRef;

        public UIHandler(UpdateUserInfoActivity refObj) {
            weakRef = new WeakReference<UpdateUserInfoActivity>(refObj);
        }

        @Override
        public void handleMessage(Message msg) {
            UpdateUserInfoActivity context = weakRef.get();

            if(msg.what == C.Code.OK){
                String title = context.getString(R.string.remindSys);
                String content = context.getString(R.string.successful_modification);
                /*SuccessDialog dialog = new SuccessDialog(context, title, content);
                dialog.show();*/
                Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
            } else {
                TipUtil.showShort(context, msg.obj.toString());
            }
        }
    }


}
