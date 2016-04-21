package com.newline.core.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 * Created by Administrator on 2016/1/15.
 */
public class LanguageUtils {
    static Resources resource;
    static Configuration config;
    static   Locale defualt;
    static  String country;
    public static  boolean isChinese(Context context){
        boolean flag = true;
        if(resource == null){
             resource = context.getResources();
             config = resource.getConfiguration();
        }
        defualt = config.locale;
        country = defualt.getLanguage();
        if(country.equals("en")){
           flag = false;
        }
        return flag;
    }
}
