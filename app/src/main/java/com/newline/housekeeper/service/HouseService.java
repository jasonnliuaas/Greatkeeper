package com.newline.housekeeper.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.newline.C;
import com.newline.core.BaseService;
import com.newline.housekeeper.HttpRequest.RequestCallBack;
import com.newline.housekeeper.HttpRequest;
import com.newline.housekeeper.KeeperUrl;
import com.newline.housekeeper.Param;
import com.newline.housekeeper.control.LoadingDialog;

public class HouseService extends BaseService {
    
    private static HouseService service;
    
    public HouseService(Context context) {
        super(context);
    }

    public static HouseService getService(Context context) {
        if (service == null) {
            synchronized (HouseService.class) {
                if (service == null) {
                    service = new HouseService(context);
                }
            }
        }
        return service;
    }
    
    //获取房产情况列表
    public void doHouseList(Context context, final Handler handler){
        String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
            HttpRequest.forwardLogin(context);
            return;
        }
        
        final LoadingDialog dialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.HouseList, new RequestCallBack() {
            
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
                    Log.e(C.TAG, "解析房产列表数据错误", e);
                }
                
                dialog.cancel();
                message.sendToTarget();
            }
            
        }, new Param("uid", uid));
    }
    
    //获取房产详情
    public void doHouseDetail(Context context, String propertyId, final Handler handler){
        
        final LoadingDialog dialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.HouseDetail, new RequestCallBack() {
            
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
                    Log.e(C.TAG, "解析房产详情数据错误", e);
                }
                dialog.cancel();
                message.sendToTarget();
            }
            
        }, new Param("propertyId", propertyId));
    }
    
    

}
