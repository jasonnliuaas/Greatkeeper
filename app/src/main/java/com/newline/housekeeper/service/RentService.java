package com.newline.housekeeper.service;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.newline.C;
import com.newline.core.BaseService;
import com.newline.housekeeper.HttpRequest;
import com.newline.housekeeper.HttpRequest.RequestCallBack;
import com.newline.housekeeper.KeeperUrl;
import com.newline.housekeeper.Param;
import com.newline.housekeeper.control.LoadingDialog;
import com.newline.housekeeper.model.RentBean;

public class RentService extends BaseService {
    
    private static RentService service;
    
    public RentService(Context context) {
        super(context);
    }

    public static RentService getService(Context context) {
        if (service == null) {
            synchronized (RentService.class) {
                if (service == null) {
                    service = new RentService(context);
                }
            }
        }
        return service;
    }
    
    //获取租赁管理列表
    public void doRentList(Context context, final Handler handler){
        String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
            HttpRequest.forwardLogin(context);
            return;
        }
        
        final LoadingDialog dialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.LeaseList, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                message.what = code;
                
                try {
                    if(C.Code.OK == code){
                        List<RentBean> remindList = JSON.parseArray(data, RentBean.class);
                        message.obj = remindList;
                    } else {
                        message.obj = dataMsg;
                    }
                } catch (Exception e) {
                    message.what = C.Code.SYS_ERROR;
                    Log.e(C.TAG, "解析租赁列表数据错误", e);
                }
                
                dialog.cancel();
                message.sendToTarget();
            }
            
        }, new Param("uid", uid));
    }
    
    //获取租赁详情
    public void doRentDetail(Context context, String propertyId, String leaseId, final Handler handler){
        
        final LoadingDialog dialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.LeaseDetail, new RequestCallBack() {
            
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
                    Log.e(C.TAG, "解析租赁详情数据错误", e);
                }
                dialog.cancel();
                message.sendToTarget();
            }
            
        }, new Param("propertyId", propertyId), new Param("leaseId", leaseId));
    }
    
    

}
