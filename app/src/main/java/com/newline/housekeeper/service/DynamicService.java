package com.newline.housekeeper.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.newline.C;
import com.newline.core.BaseService;
import com.newline.housekeeper.HttpRequest.RequestCallBack;
import com.newline.housekeeper.KeeperUrl;
import com.newline.housekeeper.Param;

public class DynamicService extends BaseService {
    
    private static DynamicService service;
    
    public DynamicService(Context context) {
        super(context);
    }

    public static DynamicService getService(Context context) {
        if (service == null) {
            synchronized (DynamicService.class) {
                if (service == null) {
                    service = new DynamicService(context);
                }
            }
        }
        return service;
    }
    
    // 动态消息推荐
    public void loadRecommondData(Context context, final int page, final int num, final Handler handler){
        request.asynRequest(context, KeeperUrl.DynamicContent, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                
                if(C.Code.OK == code){
                    message.what = C.Handler.LOAD_RECOMMOND_LIST;
                    message.arg1 = page;
                    message.arg2 = num;
                    message.obj = data;
                } else {
                    message.what = code;
                    message.obj = dataMsg;
                }
                message.sendToTarget();
            }
            
        }, 
        new Param("page", page), 
        new Param("num", num),
        new Param("typeid", "3"));
    }
    
    // 动态消息推荐
    public void loadCenterData(Context context, final int page, final int num, final Handler handler){
        request.asynRequest(context, KeeperUrl.DynamicContent, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                
                if(C.Code.OK == code){
                    message.what = C.Handler.LOAD_CENTER_LIST;
                    message.arg1 = page;
                    message.arg2 = num;
                    message.obj = data;
                } else {
                    message.what = code;
                    message.obj = dataMsg;
                }
                
                message.sendToTarget();
            }
            
        }, 
        new Param("page", page), 
        new Param("num", num),
        new Param("typeid", "4"));
    }
    

}
