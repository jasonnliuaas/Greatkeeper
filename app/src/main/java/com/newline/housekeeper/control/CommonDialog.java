package com.newline.housekeeper.control;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.greatkeeper.keeper.R;

@SuppressWarnings("ALL")
public class CommonDialog extends Dialog {
    public OnClickListener listener;
    protected FrameLayout container;
    protected View content;
    TextView tv_apply_tip;
    private Activity context;
    protected OnClickListener dismissClick = new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };
    protected  OnDismissListener onDismissListener = new OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            context.finish();
        }
    };

    public CommonDialog(Context context) {
        this(context, R.style.dialog_common);
    }

    public CommonDialog(Context context, int defStyle) {
        super(context, defStyle);
        init(context);
    }

    protected CommonDialog(Context context, boolean flag,
                           OnCancelListener listener) {
        super(context, flag, listener);
        this.context = (Activity) context;
        init(this.context);
    }

    @SuppressLint("InflateParams")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void init(final Context context) {
        setCancelable(false);
        setCanceledOnTouchOutside(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        content = LayoutInflater.from(context).inflate(
                R.layout.loan_apply_tip, null);
        tv_apply_tip = (TextView) content.findViewById(R.id.tv_apply_tip);
        tv_apply_tip.setMovementMethod(LinkMovementMethod.getInstance());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // TODO Check content view height and change height

        } else {
//			content.addOnLayoutChangeListener(new OnLayoutChangeListener() {
//
//				@Override
//				public void onLayoutChange(View v, int left, int top,
//						int right, int bottom, int oldLeft, int oldTop,
//						int oldRight, int oldBottom) {
//					int height = v.getHeight();
//					int contentHeight = container.getHeight();
//					int winHeight = BaseApplication.getDisplaySize()[1];
//					int needHeight = height - winHeight * 8 / 10;
//					if (needHeight > 0) {
//						container
//								.setLayoutParams(new LinearLayout.LayoutParams(
//										LayoutParams.MATCH_PARENT,
//										contentHeight - needHeight));
//					}
//				}
//			});
        }

        this.setOnDismissListener(onDismissListener);
        super.setContentView(content);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.dismiss();
    }



    public void setContent(View view) {
        container.removeAllViews();
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        container.addView(view, lp);
    }

    @Override
    public void setContentView(int i) {
        setContent(null);
    }

    @Override
    public void setContentView(View view) {
        setContentView(null, null);
    }

    @Override
    public void setContentView(View view,
                               LayoutParams layoutparams) {
        throw new Error("Dialog: User setContent (View view) instead!");
    }




    /*public void setMessage(int resId) {
        setMessage(getContext().getResources().getString(resId));
    }


    public void setMessage(String message) {
        setMessage(Html.fromHtml(message));
    }*/








}
