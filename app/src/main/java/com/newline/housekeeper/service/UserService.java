package com.newline.housekeeper.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.greatkeeper.keeper.R;
import com.lidroid.xutils.util.LogUtils;
import com.newline.C;
import com.newline.core.BaseService;
import com.newline.core.utils.AssetsUtils;
import com.newline.core.utils.LanguageUtils;
import com.newline.housekeeper.HttpRequest;
import com.newline.housekeeper.HttpRequest.RequestCallBack;
import com.newline.housekeeper.KeeperUrl;
import com.newline.housekeeper.Param;
import com.newline.housekeeper.control.LoadingDialog;
import com.newline.housekeeper.model.UserBean;

public class UserService extends BaseService {
    
    private static UserService service;
    private static boolean isChinese;
    
    public UserService(Context context) {
        super(context);
    }
    
    public static UserService getService(Context context) {
        if (service == null) {
            synchronized (UserService.class) {
                if (service == null) {
                    service = new UserService(context);
                    Log.i(C.TAG, "Create LoginService...");
                }
            }
        }
        isChinese = LanguageUtils.isChinese(context);
        return service;
    }
    
    public boolean checkLogin(Context context){
        UserBean user = request.getUser();
        if(user == null){
            HttpRequest.forwardLogin(context);
            return false;
        }
        return true;
    }
    
    public UserBean getUser(){
        return request.getUser();
    }
    
    public void checkLogin(Context context, Handler handler, int idx){
        UserBean user = request.getUser();
        if(user != null){
            handler.obtainMessage(C.Code.OK, idx).sendToTarget();
        } else {
            HttpRequest.forwardLogin(context);
        }
    }
    
    public void doAutoLogin(Context context){
        request.doAutoLogin(context, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                if(C.Code.OK == code){
                    UserBean user = JSON.parseObject(data, UserBean.class);
                    request.setUser(user);
                    spUtil.putString(C.UserIdKey, user.getUid());
                }
            }
            
        });
    }
    
    //登录操作
    public void doLogin(final Context context, final Handler handler, final String account, final String password){
        final LoadingDialog mDialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.Login, new RequestCallBack() {

            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                message.what = code;
                if (C.Code.OK == code) {
                    
                    //保存用户信息到内存中
                    UserBean user = JSON.parseObject(data, UserBean.class);
                    request.setUser(user);
                    
                    //保存用户名,密码,用户编号
                    spUtil.putString(C.UserAccountKey, account);
                    spUtil.putString(C.UserPwdKey, password);
                    spUtil.putString(C.UserIdKey, user.getUid());
                    
                    // 新帐号登录绑定UmengToken
                    String token = spUtil.getString(C.UMENG_TOKEN);
                    setDeviceToken(context, token);
                    
                    boolean isRemember = spUtil.getBoolean(C.REMEMBER_PWD_KEY, true);
                    if(isRemember){
                        spUtil.putString(C.REMEMBER_ACCOUNT, account);
                        spUtil.putString(C.REMEMBER_PWD, password);
                    } else {
                        spUtil.remove(C.REMEMBER_ACCOUNT);
                        spUtil.remove(C.REMEMBER_PWD);
                    }
                } else {
                    String errorTint="errorTint";
                    String errortip;
                    if(isChinese){
                        errortip = AssetsUtils.getString(errorTint+code);
                    }else{
                        errortip = AssetsUtils.getenString(errorTint+code);
                    }
                    message.obj = errortip;
                }
                mDialog.cancel();
                message.sendToTarget();
            }
        }, 
        new Param("email", account),
        new Param("pwd", password));
    }
    
    //注册操作
    public void doRegister(Context context, final Handler handler, final String account, final String password, String phone, String name){
        final LoadingDialog mDialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.Register, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                message.what = code;
                if (C.Code.OK == code) {
                    Log.i(C.TAG, "注册成功：" + data);
                    message.obj = account + "|" + password;
                } else {
                    String errorTint="errorTint";
                    String errortip;
                    if(isChinese){
                        errortip = AssetsUtils.getString(errorTint+code);
                    }else{
                        errortip = AssetsUtils.getenString(errorTint+code);
                    }
                    message.obj = errortip;
                }
                mDialog.cancel();
                message.sendToTarget();
            }
        }, 
        new Param("email", account),
        new Param("pwd", password),
        new Param("realname", name),
        new Param("mobile", phone));
    }
    
    //找回密码Email
    public void doFindPassword(Context context, final Handler handler, String email){
        final LoadingDialog mDialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.FindPwd, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                
                if (C.Code.OK == code) {
                    code = C.Handler.FIDN_PWD_EMAIL;
                    message.obj = data;
                } else {
                    String errorTint="errorTint";
                    String errortip;
                    if(isChinese){
                        errortip = AssetsUtils.getString(errorTint+code);
                    }else{
                        errortip = AssetsUtils.getenString(errorTint+code);
                    }
                    message.obj = errortip;
                }
                mDialog.cancel();
                message.what = code;
                message.sendToTarget();
            }
        }, new Param("email", email));
    }
    
    // 找回密码 发送验证码
    @SuppressLint("HandlerLeak")
    public void doFindPwdMobile1(final Context context, final Handler handler, final String mobile){
        final LoadingDialog mDialog = showLoadingDialog(context);
        
//        final Handler tempHandler = new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                
//                request.asynRequest(context, KeeperUrl.SendMobileCode, new RequestCallBack() {
//                    
//                    @Override
//                    public void doCallBack(int code, String dataMsg, String data) {
//                        Message message = handler.obtainMessage();
//                        
//                        if(code == 4025){
//                            code = C.Code.OK;
//                        }
//                        
//                        if (C.Code.OK == code) {
//                            code = C.Handler.FIDN_PWD_MOBILE;
//                            message.obj = mobile;
//                        } else {
//                            message.obj = dataMsg;
//                        }
//                        mDialog.cancel();
//                        message.what = code;
//                        message.sendToTarget();
//                    }
//                }, new Param("mobile", mobile));
//                
//            }
//        };
        
        // 检查手机号码是否存在
        request.asynRequest(context, KeeperUrl.CheckMobile, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                if(code == 4025){
                    code = C.Code.OK;
                }
                
                if (C.Code.OK == code) {
                    code = C.Handler.FIDN_PWD_MOBILE;
                    message.obj = mobile;
                } else {
                    String errorTint="errorTint";
                    String errortip;
                    if(isChinese){
                        errortip = AssetsUtils.getString(errorTint+code);
                    }else{
                        errortip = AssetsUtils.getenString(errorTint+code);
                    }
                    message.obj = errortip;
                }
                
                mDialog.cancel();
                message.what = code;
                message.sendToTarget();
            }
        }, new Param("mobile", mobile));
        
    }
    
    // 找回密码 手机修改密码
    public void doFindPwdMobile2(Context context, final Handler handler, final String mobile, final String newpwd){
        final LoadingDialog mDialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.UserChangeMobilePwd, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                
                if (C.Code.OK == code) {
                    
                    Bundle bundle = new Bundle();
                    bundle.putString("account", mobile);
                    bundle.putString("pwd", newpwd);
                    
                    message.setData(bundle);
                } else {
                    String errorTint="errorTint";
                    String errortip;
                    if(isChinese){
                        errortip = AssetsUtils.getString(errorTint+code);
                    }else{
                        errortip = AssetsUtils.getenString(errorTint+code);
                    }
                    message.obj = errortip;
                }
                mDialog.cancel();
                message.what = code;
                message.sendToTarget();
            }
        }, 
        new Param("mobile", mobile), new Param("newpwd", newpwd));
    }
    
    
    
    //用户反馈
    public void doUserFeedBack(Context context, final Handler handler, String content){
        String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
            HttpRequest.forwardLogin(context);
            return;
        }
        
        final LoadingDialog mDialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.FeedBack, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                message.what = code;
                message.obj = dataMsg;
                mDialog.cancel();
                message.sendToTarget();
            }
        }, 
        new Param("uid", uid),
        new Param("content", content));
    }
    
    public void setDeviceToken(Context context,String token){
        String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
            return;
        }
        
        request.asynRequest(context, KeeperUrl.GetDeviceToken, new RequestCallBack() {
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
              Log.i("HouseService", "设备编号发送结果======"+code);
            }
        }, 
        new Param("uid", uid), 
        new Param("pushToken", token),
        new Param("tokenType", 2));
    }
    
    //用户登出
    public void doLogout(Context context){
        request.setUser(null);
        
        spUtil.remove(C.UserAccountKey);
        spUtil.remove(C.UserPwdKey);
        spUtil.remove(C.UserIdKey);
    }
    
    //修改用户信息
    public void doUpdateInfo(Context context, final Handler handler, final String nickname, final String realname){
        String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
            HttpRequest.forwardLogin(context);
            return;
        }
        
        final LoadingDialog mDialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.UpdateUserInfo, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                message.what = code;
                if(code == C.Code.OK){
                    UserBean user = getUser();
                    if(user != null){
                        user.setNickname(nickname);
                        user.setRealname(realname);
                        request.setUser(user);
                    }
                    message.obj = dataMsg;

                }else{
                    String errorTint="errorTint";
                    String errortip;
                    if(isChinese){
                        errortip = AssetsUtils.getString(errorTint+code);
                    }else{
                        errortip = AssetsUtils.getenString(errorTint+code);
                    }
                    message.obj = errortip;
                }
                

                mDialog.cancel();
                message.sendToTarget();
            }
        }, 
        new Param("uid", uid),
        new Param("nickname", nickname),
        new Param("realname", realname));
    }
    
    //修改密码
    public void doUpdatePassword(final Context context, final Handler handler, String oldPwd, final String newPwd){
        String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
            HttpRequest.forwardLogin(context);
            return;
        }
        
        final LoadingDialog mDialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.UpdatePassword, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {

                Message message = handler.obtainMessage();
                if(code == C.Code.OK){
                    doLogout(context);
                    mDialog.dismiss();
                    message.obj =dataMsg;
                }
                else{
                    String errorTint="errorTint";
                    String errortip;
                    if(isChinese){
                        errortip = AssetsUtils.getString(errorTint+code);
                    }else{
                        errortip = AssetsUtils.getenString(errorTint+code);
                    }
                    message.obj = errortip;
                }
                message.what = code;
                mDialog.cancel();
                message.sendToTarget();
            }
        }, 
        new Param("uid", uid),
        new Param("oldpwd", oldPwd),
        new Param("newpwd", newPwd));
    }
    
    // 获取账号绑定情况
    public void doGetAccountBind(Context context, final Handler handler){
        String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
            HttpRequest.forwardLogin(context);
            return;
        }
        
        final LoadingDialog mDialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.GetAccountBind, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                
                try {
                    if(C.Code.OK == code){
                        message.what = C.Handler.LOAD_ACCOUNT_BIND;
                        message.obj = data;
                    } else {
                        message.what = code;
                        message.obj = dataMsg;
                    }
                } catch (Exception e) {
                    message.what = C.Code.SYS_ERROR;
                    message.obj = "系统错误";
                    LogUtils.e("load account bind error.", e);
                }
                
                mDialog.cancel();
                message.sendToTarget();
            }
        }, 
        new Param("uid", uid));
    }
    
    // 上传头像
    public void doUploadAvatar(Context context, final Handler handler, final Bitmap bitmap){
        String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
            HttpRequest.forwardLogin(context);
            return;
        }
        
        final LoadingDialog mDialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.UploadAvatar, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                message.what = code;
                if(code == C.Code.OK){
                    message.obj = bitmap;
                } else {
                    String errorTint="errorTint";
                    String errortip;
                    if(isChinese){
                        errortip = AssetsUtils.getString(errorTint+code);
                    }else{
                        errortip = AssetsUtils.getenString(errorTint+code);
                    }
                    message.obj = errortip;
                }
                mDialog.cancel();
                message.sendToTarget();
            }
        }, 
        "avatar",
        bitmap,
        new Param("uid", uid));
    }
    
    // 发送验证码请求
    public void doSendMobileCode(final Context context, final Handler handler, final String mobile){
        final LoadingDialog mDialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.SendMobileCode, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                
                // 兼容服务器的 成功Code 4025 
                if(code == 4025){
                    code = C.Code.OK;
                }
                
                message.what = code;
                if(code == C.Code.OK){
                    message.obj = mobile;
                } else {
                    String errorTint="errorTint";
                    String errortip;
                    if(isChinese){
                        errortip = AssetsUtils.getString(errorTint+code);
                    }else{
                        errortip = AssetsUtils.getenString(errorTint+code);
                    }
                    message.obj = errortip;
                }
                
                mDialog.cancel();
                message.sendToTarget();
            }
        }, new Param("mobile", mobile));
    }
    
    // 验证手机验证码请求
    public void doConfirmVcode(final Context context, final Handler handler, final String mobile, String vcode){
        final LoadingDialog mDialog = showLoadingDialog(context);
        
        request.asynRequest(context, KeeperUrl.ConfirmVcode, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                
                if(code == 4028){
                    code = C.Code.OK;
                }
                
                message.what = code;
                if(code == C.Code.OK){
                    message.obj = mobile;
                } else {
                    String errorTint="errorTint";
                    String errortip;
                    if(isChinese){
                        errortip = AssetsUtils.getString(errorTint+code);
                    }else{
                        errortip = AssetsUtils.getenString(errorTint+code);
                    }
                    message.obj = errortip;
                }
                
                mDialog.cancel();
                message.sendToTarget();
            }
        }, 
        new Param("mobile", mobile), 
        new Param("code", vcode));
    }
    
    // 设置绑定手机号码
    @SuppressLint("HandlerLeak")
    public void doSetBindMobile(final Context context, final Handler handler, final String mobile, final String code){
        final String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
            HttpRequest.forwardLogin(context);
            return;
        }
        
        final LoadingDialog mDialog = showLoadingDialog(context);
        
        final Handler tempHandler = new Handler(){
            
            @Override
            public void handleMessage(Message msg) {
                Message message = handler.obtainMessage();
                
                message.what = msg.what;
                if(message.what == C.Code.OK){
                    // 设置手机号码
                    request.asynRequest(context, KeeperUrl.SetBindMobile, new RequestCallBack() {
                        
                        @Override
                        public void doCallBack(int code, String dataMsg, String data) {
                            Message message = handler.obtainMessage();
                            
                            message.what = code;
                            if(code == C.Code.OK){
                                message.obj = mobile;
                            }else{
                                String errorTint="errorTint";
                                String errortip;
                                if(isChinese){
                                    errortip = AssetsUtils.getString(errorTint+code);
                                }else{
                                    errortip = AssetsUtils.getenString(errorTint+code);
                                }
                                message.obj = errortip;
                            }
                            mDialog.cancel();
                            message.sendToTarget();
                        }
                    }, 
                    new Param("mobile", mobile), 
                    new Param("uid", uid));
                } else {
                    message.obj = msg.obj;
                    mDialog.cancel();
                    message.sendToTarget();
                }
            }
        };
        
        // 确认手机验证码
        request.asynRequest(context, KeeperUrl.ConfirmVcode, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = tempHandler.obtainMessage();
                
                if(code == 4028){
                    code = C.Code.OK;
                }
                
                message.what = code;
                if(code == C.Code.OK){
                    message.obj = mobile;
                } else {
                    String errorTint="errorTint";
                    String errortip;
                    if(isChinese){
                        errortip = AssetsUtils.getString(errorTint+code);
                    }else{
                        errortip = AssetsUtils.getenString(errorTint+code);
                    }
                    message.obj = errortip;
                }
                
                mDialog.cancel();
                message.sendToTarget();
            }
        }, 
        new Param("mobile", mobile), 
        new Param("code", code));
    }
    
    // 验证邮箱验证请求
    public void doSendVEmail(Context context, final Handler handler, final String email){
        String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
            HttpRequest.forwardLogin(context);
            return;
        }
        
        final LoadingDialog mDialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.SendVEmail, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                
                message.what = code;
                if(code == C.Code.OK){
                    message.obj = email;
                } else {
                    String errorTint="errorTint";
                    String errortip;
                    if(isChinese){
                        errortip = AssetsUtils.getString(errorTint+code);
                    }else{
                        errortip = AssetsUtils.getenString(errorTint+code);
                    }
                    message.obj = errortip;
                }
                
                mDialog.cancel();
                message.sendToTarget();
            }
        }, 
        new Param("email", email),
        new Param("uid", uid));
    }
    
    // 手机注册
    public void doMobileRegister(Context context, final Handler handler, final String mobile, final String pwd){
        final LoadingDialog mDialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.MobileRegister, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                // 兼容服务器
                if(code == 4030){
                    code = C.Code.OK;
                }
                
                message.what = code;
                if(code == C.Code.OK){
                    Bundle bundle = new Bundle();
                    bundle.putString("account", mobile);
                    bundle.putString("pwd", pwd);
                    message.setData(bundle);
                } else {
                    String errorTint="errorTint";
                    String errortip;
                    if(isChinese){
                        errortip = AssetsUtils.getString(errorTint+code);
                    }else{
                        errortip = AssetsUtils.getenString(errorTint+code);
                    }
                    message.obj = errortip;
                }
                
                mDialog.cancel();
                message.sendToTarget();
            }
        }, 
        new Param("mobile", mobile),
        new Param("pwd", pwd));
    }
    
    // 邮箱注册
    public void doEmailRegister(Context context, final Handler handler, final String email, final String pwd){
        final LoadingDialog mDialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.Register, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                
                message.what = code;
                if(code == C.Code.OK){
                    Bundle bundle = new Bundle();
                    bundle.putString("account", email);
                    bundle.putString("pwd", pwd);
                    message.setData(bundle);
                } else {
                    String errorTint="errorTint";
                    String errortip;
                    if(isChinese){
                        errortip = AssetsUtils.getString(errorTint+code);
                    }else{
                        errortip = AssetsUtils.getenString(errorTint+code);
                    }
                    message.obj = errortip;
                }
                
                mDialog.cancel();
                message.sendToTarget();
            }
        }, 
        new Param("email", email),
        new Param("pwd", pwd));
    }
    
    // 获取动态未读记录
    public void doGetDynamicNum(Context context, final Handler handler){
        String pushToken = spUtil.getString(C.UMENG_TOKEN);
        
        request.asynRequest(context, KeeperUrl.DynamicUnreadNum, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                
                message.what = code;
                if(code == C.Code.OK){
                    message.what = C.Handler.LOAD_UNREAD_DYNAMIC;
                    message.obj = data;
                } else {
                    String errorTint="errorTint";
                    String errortip;
                    if(isChinese){
                        errortip = AssetsUtils.getString(errorTint+code);
                    }else{
                        errortip = AssetsUtils.getenString(errorTint+code);
                    }
                    message.obj = errortip;
                }
                
                message.sendToTarget();
            }
        }, 
        new Param("pushtoken", pushToken));
    }
    
    // 设置动态消息为已读
    public void doReadDynamic(Context context, final Handler handler){
        if(!isLogined()){
            return;
        }
        
        String pushToken = spUtil.getString(C.UMENG_TOKEN);
        
        request.asynRequest(context, KeeperUrl.DynamicReaded, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                
                message.what = code;
                if(code == C.Code.OK){
                    message.what = C.Handler.DO_READ_DYNAMIC;
                    message.obj = data;
                } else {
                    String errorTint="errorTint";
                    String errortip;
                    if(isChinese){
                        errortip = AssetsUtils.getString(errorTint+code);
                    }else{
                        errortip = AssetsUtils.getenString(errorTint+code);
                    }
                    message.obj = errortip;
                }
                
                message.sendToTarget();
            }
        }, 
        new Param("pushtoken", pushToken));
    }

}
