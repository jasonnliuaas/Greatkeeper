package com.newline.housekeeper;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newline.C;
import com.newline.core.utils.HttpUtils;
import com.newline.core.utils.Network;
import com.newline.core.utils.SPUtil;
import com.newline.core.utils.ThreadPoolUtils;
import com.newline.core.utils.VerifyUtils;
import com.greatkeeper.keeper.R;
import com.newline.housekeeper.model.UserBean;
import com.newline.housekeeper.service.UserService;

public class HttpRequest {
    
    public interface RequestCallBack {
        public void doCallBack(int code, String dataMsg, String data);
    }
    
    private static HttpRequest instance;
    
    public static HttpRequest getInstance(){
        if (instance == null) {
            synchronized (HttpRequest.class) {
                if (instance == null) {
                    instance = new HttpRequest();
                }
            }
        }
        return instance;
    }
    
    private UserBean user;
    
    public void setUser(UserBean user){
        this.user = user;
    }
    
    public UserBean getUser(){
        return this.user;
    }
    
    //检查Token,缓存中没有则重新获取
    private String checkToken(Context context){
        String message = "未知错误";
        try {
            SPUtil spUtil = SPUtil.getUtil(context);
            String token = spUtil.getString(C.TokenKey);
            
            if(!token.isEmpty()){
                return token;
            }
            
            //从服务器获取Token
            KeeperUrl url = KeeperUrl.GetToken;
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("a", url.getAction());
            params.put("v", url.getVersion());
            params.put("appid", C.APP_ID);
            params.put("appsecret", C.APP_SECRET);
            
            String connUrl = url.getUrl() + HttpUtils.parseParam(params);
            String result = HttpUtils.doGet(connUrl, HttpUtils.UTF8);
            result = HttpUtils.deUnicode(result);
            
            JSONObject jsonObj = JSON.parseObject(result);
            
            Integer code = jsonObj.getInteger("code");
            code = (code == null ? C.Code.SYS_ERROR : code.intValue());
            String data = jsonObj.getString("data");
            if(C.Code.OK == code){
                spUtil.putString(C.TokenKey, data);
                return data;
            }else{
                message = data;
            }
        } catch (Exception e) {
            Log.e(C.TAG, message, e);
        }
        return "";
    }
    
    public static void forwardLogin(Context context){
        Intent intent = new Intent(C.LoginAction);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    
    public void doAutoLogin(Context context, RequestCallBack callBack){
        if(user != null){
            return;
        }
        
        SPUtil spUtil = SPUtil.getUtil(context);

        String account = spUtil.getString(C.UserAccountKey);
        String password = spUtil.getString(C.UserPwdKey);
        
        if(!VerifyUtils.validAccount(account) || !VerifyUtils.validPassword(password)){
            return;
        }
        
        asynRequest(context, KeeperUrl.Login, callBack, new Param("email", account), new Param("pwd", password));
        
    }
    
    //检查是否已经登录
    private boolean checkLogin(final Context context, String token){
        // 检查缓存中的登录信息
        if (user == null) {
            SPUtil spUtil = SPUtil.getUtil(context);

            String account = spUtil.getString(C.UserAccountKey);
            String password = spUtil.getString(C.UserPwdKey);

            // 自动登录
            if (VerifyUtils.validAccount(account) && VerifyUtils.validPassword(password)) {
                UserService userService = UserService.getService(context);
                final KeeperUrl url = KeeperUrl.Login;
                final HashMap<String, String> params = new HashMap<String, String>();
                params.put("a", url.getAction());
                params.put("v", url.getVersion());
                params.put("token", token);
                params.put("email", account);
                params.put("pwd", password);

                try {
                    String result = HttpUtils.doPost(url.getUrl(), HttpUtils.parseParam(params), HttpUtils.UTF8);
                    result = HttpUtils.deUnicode(result);
                    JSONObject jsonObj = JSON.parseObject(result);

                    Integer code = jsonObj.getInteger("code");
                    code = (code == null ? C.Code.SYS_ERROR : code.intValue());
                    String data = jsonObj.getString("data");

                    if (C.Code.OK == code) {
                        user = JSON.parseObject(data, UserBean.class);
                        setUser(user);
                        return true;
                    } else {
                        userService.doLogout(context);
                        Log.e(C.TAG, "自动登录异常：data＝" + jsonObj);
                    }
                } catch (Exception e) {
                    userService.doLogout(context);
                    Log.e(C.TAG, "自动登录错误", e);
                }
            }
        }
        
        if(user == null){
            forwardLogin(context);
            return false;
        } else {
            return true;
        }
    }
    
    /** 同步请求数据 **/
    private boolean syncRequest(Context context, KeeperUrl url, RequestCallBack callBack, Param... params){
        boolean isNetWork = Network.checkNetWork(context);
        if(!isNetWork){
            callBack.doCallBack(C.Code.NO_NETWORK, context.getString(R.string.netWorkError), null);
            return false;
        }
        
        Integer code = C.Code.SYS_ERROR;
        String message = "系统错误", data = "";
        int counter = 0;

        do {
            try {

                // 加载参数
                HashMap<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("a", url.getAction());
                paramMap.put("v", url.getVersion());
                paramMap.put("agent", ""); 

                int len = (params == null ? 0 : params.length);
                for (int i = 0; i < len; i++) {
                    Param param = params[i];
                    paramMap.put(param.getName(), param.getValue());
                }

                // 检查Token
                String token = checkToken(context);
                if (token.isEmpty()) {
                    message = "获取Token失败";
                    data = message;
                    callBack.doCallBack(code, message, data);
                    return false;
                }
                paramMap.put("token", token);

                // 检查登录
                if (url.isNeedLogin() && !checkLogin(context, token)) {
                    code = C.Code.NOT_LOGIN;
                    message = "获取Token失败";
                    data = message;
                    callBack.doCallBack(code, message, data);
                    return false;
                }

                // 分发请求
                String result;
                if (url.isPost()) {
                    result = HttpUtils.doPost(url.getUrl(), HttpUtils.parseParam(paramMap), HttpUtils.UTF8);
                } else {
                    String connUrl = url.getUrl() + HttpUtils.parseParam(paramMap);
                    result = HttpUtils.doGet(connUrl, HttpUtils.UTF8);
                }
                
                if(result == null){
                    callBack.doCallBack(C.Code.NO_NETWORK, context.getString(R.string.netWorkError), null);
                    return false; 
                }

                // 解码UNICODE
                result = HttpUtils.deUnicode(result);
                Log.d(C.TAG ,"action:"+url.getAction()+",Http Request result :" + result);
                JSONObject jsonObj = JSON.parseObject(result);
                code = jsonObj.getInteger("code");
                code = (code == null ? C.Code.SYS_ERROR : code);
                message = jsonObj.getString("message");
                data = jsonObj.getString("data");

                // 处理Token失效
                if (C.Code.TOKEN_DIE == code) {
                    SPUtil.getUtil(context).remove(C.TokenKey);
                }

                counter++;
            } catch (Exception e) {
                Log.e(C.TAG, "网络请求错误", e);
            }
        } while (counter <= 1 && C.Code.TOKEN_DIE == code);
        
        
        callBack.doCallBack(code, message, data);
        return C.Code.OK == code;
    }
    
    /** 同步请求数据 **/
    public  void syncRequestWithBitmap(Context context, KeeperUrl url, RequestCallBack callBack, String bitmapName, Bitmap bitmap, Param... params){
        boolean isNetWork = Network.checkNetWork(context);
        if(!isNetWork){
            callBack.doCallBack(C.Code.NO_NETWORK, context.getString(R.string.netWorkError), null);
            return;
        }
        
        Integer code = C.Code.SYS_ERROR;
        String message = "系统错误", data = "";

        try {

            // 加载参数
            HashMap<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("a", url.getAction());
            paramMap.put("v", url.getVersion());

            int len = (params == null ? 0 : params.length);
            for (int i = 0; i < len; i++) {
                Param param = params[i];
                paramMap.put(param.getName(), param.getValue());
            }
            
            // 检查Token
            String token = checkToken(context);
            if (token.isEmpty()) {
                message = "获取Token失败";
                data = message;
                callBack.doCallBack(code, message, data);
                return;
            }
            paramMap.put("token", token);

            String result = HttpUtils.doPostWithBitmap(url.getUrl(), paramMap, bitmapName, bitmap);

            if(result == null){
                callBack.doCallBack(C.Code.NO_NETWORK, context.getString(R.string.netWorkError), null);
                return; 
            }

            // 解码UNICODE
            result = HttpUtils.deUnicode(result);

            Log.d(C.TAG, "Http Request result :" + result);
            JSONObject jsonObj = JSON.parseObject(result);

            code = jsonObj.getInteger("code");
            code = (code == null ? C.Code.SYS_ERROR : code);
            message = jsonObj.getString("message");
            data = jsonObj.getString("data");
        } catch (Exception e) {
            Log.e(C.TAG, "网络请求错误", e);
        }
        
        callBack.doCallBack(code, message, data);
    }
    
    public void asynRequest(final Context context, final KeeperUrl url, final RequestCallBack callBack, final String bitmapName, final Bitmap bitmap, final Param... params){
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                syncRequestWithBitmap(context, url, callBack, bitmapName, bitmap, params);
            }
        });
    }
    
    /** 异步请求数据 **/
    public void asynRequest(final Context context, final KeeperUrl url, final RequestCallBack callBack, final Param... params){
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                syncRequest(context, url, callBack, params);
            }
        });
    }
    
}
