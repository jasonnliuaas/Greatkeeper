package com.newline.housekeeper;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.greatkeeper.keeper.R;
import com.lidroid.xutils.util.LogUtils;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.UmShareUtils;
import com.newline.housekeeper.control.BottomFactory;
import com.newline.housekeeper.control.BottomItem;
import com.newline.housekeeper.service.RemindService;
import com.newline.housekeeper.service.UserService;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends BaseActivity {

	/** 对fragment进行增删替换处理的实例 **/
	private FragmentTransaction transaction;

	/** 底部菜单栏初始化所有控件类的一个实例 **/
	private BottomFactory bottom;

	/** 用于存放所有新建的fragment **/
	private SparseArray<Fragment> fragmentMap;

	/** fragment管理器 **/
	private FragmentManager fragmentManager;
	
	private TextView textUnreadNum;
	
	private TextView textHomeUnreadNum;
	
	private UnreadHandler unreadHandler;
	
	private UserService userService;
	private RemindService remindService;

    AlertDialog.Builder alertBuilder;
	
	@Override
	protected void onResume() {
	    super.onResume();
	    if(userService.getUser() != null){
	        userService.doGetDynamicNum(this, unreadHandler);
	        remindService.doUnreadNum(this, unreadHandler);
	    }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
        UmengUpdateAgent.setDeltaUpdate(true);
        UmengUpdateAgent.update(this);
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
        String device_token = UmengRegistrar.getRegistrationId(this);
        Log.i("TAG","device_token= "+device_token);
        //开启推送并设置注册的回调处理
        /*mPushAgent.enable(new IUmengRegisterCallback() {

            @Override
            public void onRegistered(final String registrationId) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //onRegistered方法的参数registrationId即是device_token
                        Log.d("device_token", registrationId);
                    }
                });
            }
        });*/
		C.inMain = true;
		
		textUnreadNum = loadControl(R.id.textUnreadNum);
		textHomeUnreadNum = loadControl(R.id.textHomeUnreadNum);
		
		unreadHandler = new UnreadHandler(this);
		
		userService = UserService.getService(this);
		remindService = RemindService.getService(this);
		
		fragmentMap = new SparseArray<Fragment>();
		
		bottom = BottomFactory.getInstance();
		bottom.initFragmentViews(this, new OnClickListener() {
			@Override
			public void onClick(View view) {
				int idx = bottom.getClickIdx(view.getId());
                setTabSelection(idx);
                
                // 点击动态按钮,设置动态为已读
                if(idx == 1){
                    userService.doReadDynamic(MainActivity.this, unreadHandler);
                }
			}
		});
		
		fragmentManager = getFragmentManager();
		setTabSelection(0);
	}

	/**
	 *  根据索引值切换fragment
	 */
	private void setTabSelection(int index) {
		clearSelection();
		transaction = fragmentManager.beginTransaction();
		hideFragments(transaction);
		
		BottomItem item = bottom.item(index);
		item.change(true, getResources().getColor(item.textFocused));
		
		Fragment fragment = fragmentMap.get(index);
		
		if (fragment == null) {
			fragment = bottom.initFragment(index);
			fragmentMap.put(index, fragment);
			transaction.add(R.id.main_fragment, fragment);
		} else {
			transaction.show(fragment);
		}
		transaction.commit();
	}

	/**
	 * 清空所有图标和文字状态
	 */
	private void clearSelection() {
		for (int i = 0; i < bottom.size(); i++) {
			BottomItem item = bottom.item(i);
			item.change(false, getResources().getColor(item.textUnFocused));
		}
	}

	/**
	 * 隐藏所有非空fragment
	 */
	private void hideFragments(FragmentTransaction transaction) {
		for (int i = 0; i < fragmentMap.size(); i++) {
		    int id = fragmentMap.keyAt(i);
		    Fragment fragment = fragmentMap.get(id);
		    if(fragment == null){
		        continue;
		    }
		    transaction.hide(fragment);
		}
	}
	
	static class UnreadHandler extends Handler {
        private WeakReference<MainActivity> weakRef;
        
        public UnreadHandler(MainActivity refObj) { 
            weakRef = new WeakReference<MainActivity>(refObj); 
        }
        
        @Override
        public void handleMessage(Message msg) {
            MainActivity context = weakRef.get();
            
            if(msg.what == C.Handler.LOAD_UNREAD_DYNAMIC){
                if("0".equals(msg.obj.toString())){
                    context.textUnreadNum.setVisibility(View.VISIBLE);
                } else {
                    context.textUnreadNum.setVisibility(View.GONE);
                }
            } else if(msg.what == C.Handler.DO_READ_DYNAMIC){
                context.textUnreadNum.setVisibility(View.GONE);
            } else if(msg.what == C.Handler.LOAD_UNREAD_HOME){
                String num = msg.obj.toString();
                if(!"0".equals(num)){
                    context.textHomeUnreadNum.setText(num);
                    context.textHomeUnreadNum.setVisibility(View.VISIBLE);
                } else {
                    context.textHomeUnreadNum.setVisibility(View.GONE);
                }
            }
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       UmShareUtils.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == 1){
            Activity activity = this;
            int type = data.getIntExtra("type", 0);
            switch (type) {
                case 1:
                    UmShareUtils.shareSina(activity);
                    LogUtils.d("微博分享");
                    break;
                case 2:
                    UmShareUtils.shareQZone(activity);
                    LogUtils.d("Qzone分享");
                    break;
                case 3:
                    UmShareUtils.shareQQ(activity);
                    LogUtils.d("QQ分享");
                    break;
                case 4:
                    UmShareUtils.shareWeiXin(activity);
                    LogUtils.d("微信分享");
                    break;
                case 5:
                    UmShareUtils.shareWeiXinCircle(activity);
                    LogUtils.d("朋友圈分享");
                    break;
                default:
                    break;
            }
        }
    }

}