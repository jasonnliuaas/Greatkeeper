package com.newline.core.utils;

import android.content.Context;
import android.text.TextUtils;

import com.lidroid.xutils.util.IOUtils;
import com.lidroid.xutils.util.LogUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class AssetsUtils {
    
    private static Properties properties;
    private static Properties en_properties;
    
    
    public static Properties init(Context context){
        if(properties == null){
            synchronized (AssetsUtils.class) {
                if(properties == null){
                    InputStream fis = null;
                    InputStream fis_en = null;
                    try {
                        properties = new Properties();
                        en_properties = new Properties();
                        fis = context.getAssets().open("error_tip.properties");
                        fis_en = context.getAssets().open("error_tip-en.properties");
                        properties.load(new InputStreamReader(fis));
                        en_properties.load(new InputStreamReader(fis_en));
                    } catch (Exception e) {
                        LogUtils.e("init properties error.", e);
                        properties = null;
                        en_properties = null;
                    } finally {
                        IOUtils.closeQuietly(fis);
                        IOUtils.closeQuietly(fis_en);
                    }
                }
            }
        }
        return properties;
    }
    
    public static String getString(String key, String defVal){
        return properties.getProperty(key, defVal);
    }
    public static String getenString(String key, String defVal){
        return en_properties.getProperty(key, defVal);
    }
    public static String getenString(String key){
        return en_properties.getProperty(key);
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
