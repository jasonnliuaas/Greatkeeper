package com.newline.core.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import android.content.Context;
import android.text.TextUtils;

import com.lidroid.xutils.util.IOUtils;
import com.lidroid.xutils.util.LogUtils;

public class ConfigUtils {
    
    private static Properties properties;
    
    
    public static Properties init(Context context){
        if(properties == null){
            synchronized (ConfigUtils.class) {
                if(properties == null){
                    InputStream fis = null;
                    try {
                        properties = new Properties();
                        fis = context.getAssets().open("config.properties");
                        properties.load(new InputStreamReader(fis));
                    } catch (Exception e) {
                        LogUtils.e("init properties error.", e);
                        properties = null;
                    } finally {
                        IOUtils.closeQuietly(fis);
                    }
                }
            }
        }
        return properties;
    }
    
    public static String getString(String key, String defVal){
        return properties.getProperty(key, defVal);
    }
    
    public static int getInt(String key, int defVal){
        try {
            String val = getString(key);
            if(!TextUtils.isEmpty(val)){
                return Integer.valueOf(val);
            }
        } catch (Exception e) {
            LogUtils.e("", e);
        }
        return defVal;
    }
    
    public static String getString(String key){
        return properties.getProperty(key);
    } 
    

}
