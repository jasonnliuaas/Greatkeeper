package com.newline.core.utils;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.newline.C;

public class DataUtils {
	
	public static int getInt(JSONObject data, String key){
		int num = 0;
		try {
			num = data.getIntValue(key);
        } catch (Exception e) {
        	String log = (data == null ? "" : data.toString());
        	Log.w(C.TAG, "Receive int param key=" + key + ", JSON=" + log, e);
        }
		return num;
	}
	
	public static String getString(JSONObject data, String key){
		String str = "";
		try {
			str = data.getString(key).trim();
        } catch (Exception e) {
        	String log = (data == null ? "" : data.toString());
        	Log.w(C.TAG, "Receive String param key=" + key + ", JSON=" + log, e);
        }
		return str;
	}

}
