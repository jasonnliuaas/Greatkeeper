package com.newline.core;

import android.content.Context;

import com.newline.core.utils.SPUtil;
import com.newline.housekeeper.HttpRequest;
import com.newline.housekeeper.control.LoadingDialog;

public class BaseService {
    
    protected SPUtil spUtil;
    protected HttpRequest request;
    
    public BaseService(Context context){
        if(spUtil == null){
            spUtil = SPUtil.getUtil(context); 
        }
        if(request == null){
            request = HttpRequest.getInstance(); 
        }
    }
    
    public LoadingDialog showLoadingDialog(Context context){
        LoadingDialog mDialog = new LoadingDialog(context);
        mDialog.showDailog();
        return mDialog;
    }
    
    public boolean isLogined(){
        return (request.getUser() != null);
    }
    

}
