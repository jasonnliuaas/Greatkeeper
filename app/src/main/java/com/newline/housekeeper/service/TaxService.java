package com.newline.housekeeper.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.lidroid.xutils.util.LogUtils;
import com.newline.C;
import com.newline.core.BaseService;
import com.newline.housekeeper.HttpRequest;
import com.newline.housekeeper.HttpRequest.RequestCallBack;
import com.newline.housekeeper.KeeperUrl;
import com.newline.housekeeper.Param;
import com.newline.housekeeper.control.LoadingDialog;

public class TaxService extends BaseService {
    
    private static TaxService service;
    
    public TaxService(Context context) {
        super(context);
    }

    public static TaxService getService(Context context) {
        if (service == null) {
            synchronized (TaxService.class) {
                if (service == null) {
                    service = new TaxService(context);
                }
            }
        }
        return service;
    }
    
    
    public void loadTaxData(Context context,final Handler handler){
        String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
            HttpRequest.forwardLogin(context);
            return;
        }
        
        final LoadingDialog dialog = showLoadingDialog(context); 
        request.asynRequest(context, KeeperUrl.GetBaoshuiList, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                
                try {
                    if(C.Code.OK == code){
                        message.what = C.Handler.LOAD_TAX_LIST;
                        message.obj = data;
                    } else {
                        message.what = code;
                        message.obj = dataMsg;
                    }
                } catch (Exception e) {
                    message.what = C.Code.SYS_ERROR;
                    message.obj = "系统错误";
                    LogUtils.e("load rental data error.", e);
                }
                
                dialog.cancel();
                message.sendToTarget();
            }
        }, new Param("uid", uid),new Param("name", ""),new Param("mobile", ""),new Param("email", "")
        ,new Param("country", ""),new Param("city", ""),new Param("type", ""),new Param("price", ""),new Param("scale", ""),new Param("other_assets", ""));
    }
    
 
        
      

}
