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

public class RentalService extends BaseService {
    
    private static RentalService service;
    
    public RentalService(Context context) {
        super(context);
    }

    public static RentalService getService(Context context) {
        if (service == null) {
            synchronized (RentalService.class) {
                if (service == null) {
                    service = new RentalService(context);
                }
            }
        }
        return service;
    }

    public void loadRentalList(Context context, final Handler handler){
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

                try {
                    if(C.Code.OK == code){
                        message.what = C.Handler.LOAD_RENTAL;
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

        }, new Param("uid", uid), new Param("currency", C.Currency.nowCurrency));
    }
    
    public void loadRentalData(Context context, final int year, final int month, final Handler handler,final String id){
        String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
            HttpRequest.forwardLogin(context);
            return;
        }
        String date = year + "-" + (month < 10 ? ("0"+month) : month);
        final LoadingDialog dialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.GetRentalRecode, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                
                try {
                    if(C.Code.OK == code){
                        message.what = C.Handler.LOAD_RENTAL_LIST;
                        message.arg1 = year;
                        message.arg2 = month;
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
            
        }, new Param("uid", uid), new Param("date", date), new Param("currency", C.Currency.nowCurrency),new Param("propertyid",id));
    }
    
    public void loadReantalIncome(Context context, final Handler handler){
        String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
//            HttpRequest.forwardLogin(context);
            return;
        }
        
//        final LoadingDialog dialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.RentalIncome, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                
                try {
                    if(C.Code.OK == code){
                        message.what = C.Handler.LOAD_RENTAL_INCOME;
                        message.obj = data;
                    } else {
                        message.what = code;
                        message.obj = dataMsg;
                    }
                } catch (Exception e) {
                    message.what = C.Code.SYS_ERROR;
                    message.obj = "系统错误";
                    LogUtils.e("load rental income error.", e);
                }
                
//                dialog.cancel();
                message.sendToTarget();
            }
            
        }, new Param("uid", uid), new Param("currency", C.Currency.nowCurrency));
    }

}
