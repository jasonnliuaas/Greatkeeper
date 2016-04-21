package com.newline.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/***
 * 检查网络
 */
public class Network {

    /** 网络不可用 */
    public static final int NONETWORK = 0;
    
    /** 是WIFI连接 */
    public static final int WIFI = 1;
    
    /** 不是WIFI连接 */
    public static final int NOWIFI = 2;

    /**
     * 检验网络连接 并判断是否是WIFI连接
     * 
     * @param context
     * @return <li>没有网络：Network.NONETWORK;</li> 
     *         <li>WIFI连接：Network.WIFI;</li>
     *         <li>Mobile连接：Network.NOWIFI</li>
     */
    public static int checkNetWorkType(Context context) {
        if (!checkNetWork(context)) {
            return Network.NONETWORK;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting()){
            return Network.WIFI;
        } else {
            return Network.NOWIFI;
        }
    }

    /**
     * 检测网络是否连接
     * 
     * @param context
     * @return
     */
    public static boolean checkNetWork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isAvailable()) {
            return false;
        }
        return true;
    }
}
