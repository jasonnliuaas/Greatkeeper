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

public class PaymentService extends BaseService {
    
    private static PaymentService service;
    
    public PaymentService(Context context) {
        super(context);
    }

    public static PaymentService getService(Context context) {
        if (service == null) {
            synchronized (PaymentService.class) {
                if (service == null) {
                    service = new PaymentService(context);
                }
            }
        }
        return service;
    }
    
    
    public void loadPaymentData(Context context, final Handler handler){
        String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
            HttpRequest.forwardLogin(context);
            return;
        }
        
        final LoadingDialog dialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.GetPaymentList, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                
                try {
                    if(C.Code.OK == code){
                        message.what = C.Handler.LOAD_PAYMENT_LIST;
                        message.obj = data;
                    } else {
                        message.what = code;
                        message.obj = dataMsg;
                    }
                } catch (Exception e) {
                    message.what = C.Code.SYS_ERROR;
                    message.obj = "系统错误";
                    LogUtils.e("load payment data error.", e);
                }
                
                dialog.cancel();
                message.sendToTarget();
            }
            
        }, new Param("uid", uid));
    }
    
    
    

}
