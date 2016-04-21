package com.newline.core.utils;

import com.newline.C;
import com.newline.housekeeper.Code;

import android.content.Context;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

public class TipUtil {
    
    private static final int yOffSet = C.Screen.heightPixels/2;
    private static Toast toast;
    
    
    private synchronized static void show(Context context, String msg, int duration){
        if(toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(context, msg, duration);
        toast.setGravity(Gravity.TOP, 0, yOffSet);
        toast.show();
    }
    
    /**
     * 显示短时间toast信息
     */
    public static void showShort(Context context, String msg) {
        show(context, msg, Toast.LENGTH_SHORT);
    }
    
    /**
     * 显示长时间toast信息
     */
    public static void showLong(Context context, String msg) {
        show(context, msg, Toast.LENGTH_LONG);
    }
    
    public static void showShort(Context context, Message msg) {
        String errMsg = Code.getMsg(msg.what);
        if(errMsg == null){
            errMsg = (msg.obj == null ? "系统错误" : msg.obj.toString());
        }
        showShort(context, errMsg);
    }

}
