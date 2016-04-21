package com.newline.core.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.newline.C;

public class SPUtil {

    private static SPUtil util;

    private SharedPreferences shared;

    public static SPUtil getUtil(Context context) {
        if (util == null) {
            synchronized (SPUtil.class) {
                if (util == null) {
                    util = new SPUtil();
                    util.shared = context.getSharedPreferences(C.TAG, Context.MODE_PRIVATE);
                }
            }
        }
        return util;
    }

    public String getString(String key) {
        return shared.getString(key, "");
    }

    public boolean putString(String key, String value) {
        return shared.edit().putString(key, value).commit();
    }
    
    public boolean putInt(String key, int value){
        return shared.edit().putInt(key, value).commit();
    }
    
    public int getInt(String key){
        return shared.getInt(key, 0);
    }
    
    public int getInt(String key, int def){
        return shared.getInt(key, def);
    }

    public boolean putObject(String key, Object obj) {
        String objStr = JSON.toJSONString(obj);
        return putString(key, objStr);
    }

    public <T> T getObject(String key, Class<T> cls) {
        String objStr = getString(key);
        return JSON.parseObject(objStr, cls);
    }
    
    public boolean putBoolean(String key, boolean bool){
        return shared.edit().putBoolean(key, bool).commit();
    }
    
    public boolean getBoolean(String key){
        return getBoolean(key, false);
    }
    
    public boolean getBoolean(String key, boolean defValue){
        return shared.getBoolean(key, defValue);
    }

    public boolean remove(String key) {
        return shared.edit().remove(key).commit();
    }
}
