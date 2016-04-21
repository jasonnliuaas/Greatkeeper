package com.newline.housekeeper;

import java.lang.ref.WeakReference;

import org.android.agoo.client.BaseRegistrar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.greatkeeper.keeper.R;
import com.newline.C;
import com.newline.core.image.ImageDownLoader;
import com.newline.core.utils.SPUtil;
import com.newline.core.utils.TipUtil;
import com.newline.housekeeper.HttpRequest.RequestCallBack;
import com.newline.housekeeper.service.UserService;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;

public class SplashActivity extends Activity {
    private PushAgent mPushAgent;
    private long m_dwSplashTime = 3000;
    private boolean m_bPaused = false;
    private boolean m_bSplashActive = true;

    private ImageView mImgSplash;

    private SharedPreferences setting;

    private Thread splashTimer;

    private ImageDownLoader imageDownLoader;

    private UIHandler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if(C.inMain){
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        setContentView(R.layout.splash);

        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);
        mPushAgent.enable();
        boolean registered = mPushAgent.isRegistered();
        boolean enabled = mPushAgent.isEnabled();
        if (registered && enabled) {
            String deviceToken = BaseRegistrar.getRegistrationId(this);
            SPUtil.getUtil(this).putString(C.UMENG_TOKEN, deviceToken);
            UserService.getService(this).setDeviceToken(this, deviceToken);
        }

        UmengUpdateAgent.update(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        C.Screen.widthPixels = dm.widthPixels;
        C.Screen.heightPixels = dm.heightPixels;

        handler = new UIHandler(this);

        // 自动登录
        UserService.getService(this).doAutoLogin(this);

        // 加载布局
        this.mImgSplash = (ImageView) findViewById(R.id.imgSplash);

        setting = getSharedPreferences(C.TAG, Context.MODE_PRIVATE);

        imageDownLoader = new ImageDownLoader(this);

        splashTimer = new Thread() {
            @Override
			public void run() {
                try {
                    long ms = 0;
                    while (m_bSplashActive && ms < m_dwSplashTime) {
                        sleep(100);
                        if (!m_bPaused) {
                            ms += 100;
                        }
                    }

                    boolean useFirst = setting.getBoolean("FIRST", true);

                    boolean onOff = C.ALWAYS_FIRST;
                    if (onOff || useFirst) {// 第一次
                        setting.edit().putBoolean("FIRST", false).commit();
                        Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception ex) {
                    Log.e("Splash", ex.getMessage());
                } finally {
                    finish();
                }
            }
        };

        HttpRequest.getInstance().asynRequest(this, KeeperUrl.GetLoading, new RequestCallBack() {

            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                message.what = code;
                if (code == C.Code.OK) {
                    JSONObject json = JSON.parseObject(data);
                    String url = json.getString("url");

                    Bitmap bitmap = imageDownLoader.downloadImage(url);
                    message.obj = bitmap;
                } else {
                    message.obj = dataMsg;
                }

                message.sendToTarget();
            }

        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        m_bPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_bPaused = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    
    public void onClick(View view){
        if(view.getId() == R.id.btnLoading){
            m_bSplashActive = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // 按菜单键直接进入
            m_bSplashActive = false;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 按返回键直接退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        return true;
    }

    static class UIHandler extends Handler {
        private WeakReference<SplashActivity> mActivity;

        public UIHandler(SplashActivity activity) {
            mActivity = new WeakReference<SplashActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity the = mActivity.get();

            if (msg.what == C.Code.OK) {
                if (msg.obj != null) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    the.mImgSplash.setImageBitmap(bitmap);
                }
            } else {
                TipUtil.showShort(the, msg.obj.toString());
            }
            the.splashTimer.start();
        }
    }
}
