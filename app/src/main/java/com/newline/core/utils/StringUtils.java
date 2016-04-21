package com.newline.core.utils;

import android.util.Log;

public class StringUtils {
    
    public static boolean isTrimEmpty(String str){
        return str == null || str.trim().isEmpty();
    }
    
    public static int parseInt(String str){
        int num = 0;
        try {
            num = Integer.parseInt(str);
        } catch (Exception e) {
            Log.e("StringUtils", "", e);
        }
        return num;
    }

}
