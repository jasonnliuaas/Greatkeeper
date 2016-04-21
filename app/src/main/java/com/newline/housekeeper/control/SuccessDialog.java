package com.newline.housekeeper.control;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.greatkeeper.keeper.R;

public class SuccessDialog {
    
    public interface CallBack {
        public void doCallBack();
    }
    
    private String title;
    private String content;
    
    private TextView mTextTitle;
    private TextView mContentTitle;
    private Button mBtnConfirm;
    
    private AlertDialog callDialog;
    private CallBack successCallBack;
    
    public SuccessDialog(Context context, String title, String content){
        this.callDialog = new AlertDialog.Builder(context).create();  
        this.callDialog.setCanceledOnTouchOutside(false);
        this.title = title;
        this.content = content;
    }
    
    public void show(){
        callDialog.show();
        
        Window diaWindow = callDialog.getWindow();
        diaWindow.setContentView(R.layout.dialog_success);  
        
        this.mTextTitle = (TextView) diaWindow.findViewById(R.id.dsTitle);
        this.mContentTitle = (TextView) diaWindow.findViewById(R.id.dsContent);
        this.mBtnConfirm = (Button) diaWindow.findViewById(R.id.dsBtnCancel);
        
        this.mTextTitle.setText(title);
        this.mContentTitle.setText(content);
        
        this.mBtnConfirm.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View view) {
                callDialog.dismiss();
                if(successCallBack != null){
                    successCallBack.doCallBack();
                }
            }
        });
    }
    
    public void setSuccessCallBack(CallBack callback){
        this.successCallBack = callback;
    }

}
