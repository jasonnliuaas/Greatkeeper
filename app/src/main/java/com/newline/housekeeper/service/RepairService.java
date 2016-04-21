package com.newline.housekeeper.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.newline.C;
import com.newline.core.BaseService;
import com.newline.housekeeper.HttpRequest;
import com.newline.housekeeper.HttpRequest.RequestCallBack;
import com.newline.housekeeper.KeeperUrl;
import com.newline.housekeeper.Param;
import com.newline.housekeeper.control.LoadingDialog;

public class RepairService extends BaseService {
    
    private static RepairService service;
    
    public RepairService(Context context) {
        super(context);
    }

    public static RepairService getService(Context context) {
        if (service == null) {
            synchronized (RepairService.class) {
                if (service == null) {
                    service = new RepairService(context);
                }
            }
        }
        return service;
    }
    
    //获取房屋维修列表
    public void doRepairList(Context context, final Handler handler){
        String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
            HttpRequest.forwardLogin(context);
            return;
        }
        
        final LoadingDialog dialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.RepairList, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                message.what = code;
                try {
                    if(C.Code.OK == code){
                        message.obj = data;
                    } else {
                        message.obj = dataMsg;
                    }
                } catch (Exception e) {
                    message.what = C.Code.SYS_ERROR;
                    Log.e(C.TAG, "解析房屋维修列表数据错误", e);
                }
                
                dialog.cancel();
                message.sendToTarget();
            }
            
        }, new Param("uid", uid));
    }
    
    //获取房屋维修详情
    public void doRepairDetail(Context context, String propertyId, String maintenanceId, final Handler handler){
        
        final LoadingDialog dialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.RepairDetail, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                message.what = code;
                try {
                    if(C.Code.OK == code){
                        message.obj = data;
                    } else {
                        message.obj = dataMsg;
                    }
                } catch (Exception e) {
                    message.what = C.Code.SYS_ERROR;
                    Log.e(C.TAG, "解析房屋维修详情数据错误", e);
                }
                dialog.cancel();
                message.sendToTarget();
            }
            
        }, 
        new Param("propertyId", propertyId), 
        new Param("maintenanceId", maintenanceId));
    }
    
    //修改维修状态
    public void doRepairConfirm(Context context, String propertyId, String maintenanceId, int approval, final Handler handler){
        
        final LoadingDialog dialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.RepairConfirm, new RequestCallBack() {
            
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
            
        }, new Param("propertyId", propertyId), 
           new Param("maintenanceId", maintenanceId), 
           new Param("approval", approval));
    }
    
    

}
