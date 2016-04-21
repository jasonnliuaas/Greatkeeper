package com.newline.housekeeper.activity;

import java.lang.ref.WeakReference;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.greatkeeper.keeper.R;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.StringUtils;
import com.newline.core.utils.TipUtil;
import com.newline.housekeeper.service.UserService;

public class FeedBackActivity extends BaseActivity {
    
    private UserService service;
    
    private EditText mEditFeedBack;
    
    private Handler handler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_layout);
        
        mEditFeedBack = loadControl(R.id.textFeedBack);
        
        TextView topTitle = loadControl(R.id.textTopTitle);
        topTitle.setText(getString(R.string.feedBackTitle));
        
        service = UserService.getService(this);
        service.checkLogin(this);
        
        handler = new UIHandler(this);
    }
    
    public void onClick(View view){
        int viewId = view.getId();
        if(viewId == R.id.btnFeedBack){
            String feedBack = mEditFeedBack.getText().toString();
            if(StringUtils.isTrimEmpty(feedBack)){
                TipUtil.showLong(this, "请填写反馈内容");
                return;
            }
            
            service.doUserFeedBack(this, handler, feedBack);
        }
    }
    
    static class UIHandler extends Handler {
        private WeakReference<FeedBackActivity> weakRef;
        
        public UIHandler(FeedBackActivity refObj) { 
            weakRef = new WeakReference<FeedBackActivity>(refObj); 
        }
        
        @Override
        public void handleMessage(Message msg) {
            FeedBackActivity context = weakRef.get();
            
            if(msg.what == C.Code.OK){
                context.onBackPressed();
                TipUtil.showShort(context, "提交成功,感谢您的建议!");
            } else {
               TipUtil.showShort(context, msg);
            }
        }
    }

}
