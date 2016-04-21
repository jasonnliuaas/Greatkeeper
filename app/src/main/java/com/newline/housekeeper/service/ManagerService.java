package com.newline.housekeeper.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.newline.C;
import com.newline.core.BaseService;
import com.newline.housekeeper.HttpRequest;
import com.newline.housekeeper.HttpRequest.RequestCallBack;
import com.newline.housekeeper.KeeperUrl;
import com.newline.housekeeper.Param;
import com.newline.housekeeper.control.LoadingDialog;

public class ManagerService extends BaseService {
    
    private static ManagerService service;
    
    public ManagerService(Context context) {
        super(context);
    }

    public static ManagerService getService(Context context) {
        if (service == null) {
            synchronized (ManagerService.class) {
                if (service == null) {
                    service = new ManagerService(context);
                }
            }
        }
        return service;
    }
    
    //获取物业经理列表
    public void doManagerList(Context context, final Handler handler){
        String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
            HttpRequest.forwardLogin(context);
            return;
        }
        
        final LoadingDialog dialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.ManagerList, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                message.what = code;
                
                if(C.Code.OK == code){
                    message.obj = data;
                } else {
                    message.obj = dataMsg;
                }
                
                dialog.cancel();
                message.sendToTarget();
            }
            
        }, new Param("uid", uid));
    }
    
    //解除物业经理关系
    public void doRelieve(Context context, String propertyId, String managerId, final Handler handler){
        
        final LoadingDialog dialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.ManagerRelieve, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                message.what = code;
                
                if(C.Code.OK != code){
                    message.obj = dataMsg;
                }
                
                dialog.cancel();
                message.sendToTarget();
            }
            
        }, 
        new Param("propertyId", propertyId), 
        new Param("managerId", managerId));
    }
    
    

}
