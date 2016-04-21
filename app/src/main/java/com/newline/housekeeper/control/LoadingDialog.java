package com.newline.housekeeper.control;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;

import com.greatkeeper.keeper.R;

public class LoadingDialog extends Dialog {
    
    public LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public LoadingDialog(Context context) {
        // dialog的视图风格
        super(context, R.style.loadind);
        // 设置布局文件
        setContentView(R.layout.loading_progressbar);

        // 单击dialog之外的地方，可以dismiss掉dialog。
        setCanceledOnTouchOutside(false);
        // 设置window属性
        LayoutParams a = getWindow().getAttributes();
        
        a.gravity = Gravity.CENTER;
        a.alpha = 1.0f;
        // a.dimAmount = 0.0f; // 添加背景遮盖
        getWindow().setAttributes(a);

        // //在下面这种情况下，后台的activity不会被遮盖，也就是只会遮盖此dialog大小的部分
        // LayoutParams lp = getWindow().getAttributes();
        // lp.gravity = Gravity.TOP;
        // lp.dimAmount = 0.0f; // 去背景遮盖
        // getWindow().setAttributes(lp);
        
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
//                handler.removeCallbacks(runShow);
                handler.removeCallbacks(runCancel);
            }
        });
    }
    
    private Handler handler = new Handler();
    private Runnable runCancel = new Runnable() {
        @Override
        public void run() {
            cancel();
//            Toast.makeText(getContext(), "网络请求超时...", Toast.LENGTH_SHORT).show();
        }
    };
    
//    private Runnable runShow = new Runnable() {
//        @Override
//        public void run() {
//            show();
//        }
//    };
    
    
    public void showDailog() {
//        handler.postDelayed(runShow, 1500);
        super.show();
        handler.postDelayed(runCancel, 11000);
    }

}