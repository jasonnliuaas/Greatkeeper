package com.newline;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.newline.core.utils.AssetsUtils;
import com.newline.core.utils.ConfigUtils;
import com.newline.core.utils.SPUtil;
import com.umeng.message.PushAgent;

import java.util.Locale;

/**
 * Created by Liuwei on 2015/12/30.
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        ConfigUtils.init(this);
        Fresco.initialize(this);
        AssetsUtils.init(this);
        switchLanguage(SPUtil.getUtil(this).getObject("language",Locale.class));
    }
    public void switchLanguage(Locale locale) {
        Configuration config = getResources().getConfiguration();// 获得设置对象
        Resources resources = getResources();// 获得res资源对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        config.locale = locale; // 简体中文
        resources.updateConfiguration(config, dm);
    }
}
