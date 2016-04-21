package com.newline.core;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.greatkeeper.keeper.R;
import com.newline.core.utils.UmShareUtils;
import com.newline.housekeeper.HttpRequest;
import com.newline.housekeeper.activity.ShareDialog;
import com.umeng.message.PushAgent;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BaseActivity extends Activity {

	protected String TAG = getClass().getSimpleName();

	private AlertDialog callDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	   // requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 标题栏右侧按钮点击事件
	 */
	public void callPhone(View view) {
        /*new SweetAlertDialog(this)
        .setContentText(getString(R.string.csPhone))
        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        String csPhone = getString(R.string.csPhone);
                        Uri uri = Uri.parse("tel:" + csPhone);
                        startActivity(new Intent(Intent.ACTION_CALL, uri));
                    }
                })
        .showCancelButton(true)
        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();*/
        new SweetAlertDialog(this)
                .setTitleText(getString(R.string.contact_servier))
                .setContentText(getString(R.string.csPhone))
                .setCancelText(getString(R.string.cancel))
                .setConfirmText(getString(R.string.callPhone))
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance, keep widget user state, reset them if you need
                        /*sDialog.setTitleText("Cancelled!")
                                .setContentText("Your imaginary file is safe :)")
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);*/
                        sDialog.dismiss();
                        // or you can new a SweetAlertDialog to show
                               /* sDialog.dismiss();
                                new SweetAlertDialog(SampleActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Cancelled!")
                                        .setContentText("Your imaginary file is safe :)")
                                        .setConfirmText("OK")
                                        .show();*/
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        String csPhone = getString(R.string.csPhone);
                        Uri uri = Uri.parse("tel:" + csPhone);
                        startActivity(new Intent(Intent.ACTION_CALL, uri));
                    }
                })
                .show();
		/*callDialog = new AlertDialog.Builder(this).create();
		callDialog.setCanceledOnTouchOutside(false);
		callDialog.show();
		Window diaWindow = callDialog.getWindow();
		diaWindow.setContentView(R.layout.dialog_call);

		diaWindow.findViewById(R.id.btnCallCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	callDialog.dismiss();
            }
        });

		diaWindow.findViewById(R.id.btnCall).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String csPhone = getString(R.string.csPhone);
				Uri uri = Uri.parse("tel:" + csPhone);
				startActivity(new Intent(Intent.ACTION_CALL, uri));
			}
		});*/
	}
    public void goShare(View view){
        UmShareUtils.init(this);
        Intent intent = new Intent(this, ShareDialog.class);
        startActivityForResult(intent, 1);
    }

	public void goLogin(View view){
	    HttpRequest.forwardLogin(this);
	}

	/***
	 * 返回上一页
	 */
	public void goBack(View view){
		super.onBackPressed();
	}

	@SuppressWarnings("unchecked")
    protected <T> T loadControl(int resId){
		return (T) findViewById(resId);
	}

	private Bundle bundle;
	protected Bundle loadBundle(){
	    bundle = getIntent().getExtras();
	    return bundle;
	}

	@SuppressWarnings("unchecked")
    protected <T> T getParam(String key){
	    if(bundle == null){
	        loadBundle();
	    }
	    return (T) (bundle == null ? null : bundle.get(key));
	}
}
