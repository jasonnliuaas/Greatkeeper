package com.newline.housekeeper.activity;

import java.lang.ref.WeakReference;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.newline.C;
import com.newline.core.image.ImageDownLoader;
import com.newline.core.image.ImageDownLoader.onImageLoaderListener;
import com.newline.core.image.ImageUtils;
import com.newline.core.utils.AndroidUtils;
import com.newline.core.utils.StringUtils;
import com.newline.core.utils.TipUtil;
import com.newline.core.utils.UmShareUtils;
import com.newline.housekeeper.model.UserBean;
import com.newline.housekeeper.service.UserService;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class CenterFragment extends Fragment {

    private UserService service;

    /* private final UMSocialService mController = UMServiceFactory.getUMSocialService(C.DESCRIPTOR);*/
    private TextView mTextTopTitle;
    private RelativeLayout shareLayout, mLayoutBtnFeedBack, mLayoutBtnManager,mLayoutSetting;
    private RelativeLayout mLayoutMore;

    private TextView mTextNickName;
    private TextView mTextAccountInfo;

    private ImageView mImgAvatar;

    private ImageDownLoader downLoader;

    private LinearLayout mLayoutAvatar;

    private View mRootView;

    private CenterHandler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.main_view_center, container, false);
        mTextTopTitle = (TextView) mRootView.findViewById(R.id.textTopTitle);
        shareLayout = (RelativeLayout) mRootView.findViewById(R.id.share_layout);
        mLayoutBtnFeedBack = (RelativeLayout) mRootView.findViewById(R.id.layoutBtnFeedBack);
        mLayoutSetting = (RelativeLayout) mRootView.findViewById(R.id.layoutSetting);
        mLayoutMore = (RelativeLayout) mRootView.findViewById(R.id.layoutMore);
        mLayoutBtnManager = (RelativeLayout) mRootView.findViewById(R.id.layoutBtnManager);
        mTextTopTitle.setText(R.string.center);

        downLoader = new ImageDownLoader(mRootView.getContext());
        service = UserService.getService(mRootView.getContext());

        mImgAvatar = (ImageView) mRootView.findViewById(R.id.imgCenterHead);
        mTextNickName = (TextView) mRootView.findViewById(R.id.textCenterNickName);
        mTextAccountInfo = (TextView) mRootView.findViewById(R.id.textAcountInfo);
        mLayoutBtnFeedBack.setOnClickListener(onClickListener);
        mLayoutMore.setOnClickListener(onClickListener);
        shareLayout.setOnClickListener(onClickListener);
        mLayoutBtnManager.setOnClickListener(onClickListener);
        mLayoutSetting.setOnClickListener(onClickListener);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewUtils.inject(this);


        handler = new CenterHandler(this);
    }

    @OnClick(R.id.avatars_layout)
    public void onAvatarsClick(View view){
        if (service.checkLogin(getActivity())) {
            Intent intent = new Intent(getActivity(), UserInfoActivity.class);
            getActivity().startActivity(intent);
        }
    }

    @OnClick(R.id.layoutBtnSafety)
    public void onSafetyClick(View view){
        if (service.checkLogin(getActivity())) {
            service.doGetAccountBind(getActivity(), handler);
        }
    }

    @OnClick(R.id.layoutBtnCheckUpdate)
    public void onCheckUpdateClick(View view){
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                        UmengUpdateAgent.showUpdateDialog(getActivity(), updateInfo);
                        break;
                    case UpdateStatus.No: // has no update
                        Toast.makeText(getActivity(), "当前已是最新版本（"+ AndroidUtils.getVersion(getActivity())+")", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.NoneWifi: // none wifi
                        Toast.makeText(getActivity(), "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.Timeout: // time out
                        Toast.makeText(getActivity(), "超时", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        UmengUpdateAgent.forceUpdate(getActivity());
    }


    @Override
    public void onResume() {
        super.onResume();

        final UserBean user = service.getUser();
        if (user != null) {
            mTextNickName.setText(user.getNickname());
            mTextAccountInfo.setText(getString(R.string.account)+(user.getEmail().isEmpty()?user.getMobile():user.getEmail()));
        } else {
            mTextNickName.setText(getString(R.string.Notloggedin));
            mTextAccountInfo.setText(getString(R.string.account)+getString(R.string.logintips));
        }

        if (user != null && !StringUtils.isTrimEmpty(user.getAvatar())) {
            if (user.getBitmapAvatar() == null) {
                downLoader.downloadImage(user.getAvatar(), new onImageLoaderListener() {

                    @Override
                    public void onImageLoader(Bitmap bitmap, String url) {
                        if (bitmap != null) {
                            int bitmapWidth = bitmap.getWidth();
                            int bitmapHeight = bitmap.getHeight();
                            if (bitmapWidth != bitmapHeight) {
                                int len = bitmap.getWidth() > bitmap.getHeight() ? bitmap.getHeight() : bitmap.getWidth();
                                len = (len > 640 ? 640 : len);
                                bitmap = ImageUtils.centerSquareScaleBitmap(bitmap, len);
                            }

                            user.setBitmapAvatar(bitmap);
                            mImgAvatar.setImageBitmap(bitmap);
                        }
                    }
                });
            } else {
                mImgAvatar.setImageBitmap(user.getBitmapAvatar());
            }
        } else {
            mImgAvatar.setImageResource(R.drawable.img_avatar);
        }
    }





    OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            // 更多
            if (viewId == R.id.layoutMore) {
                startActivity(new Intent(getActivity(), MoreActivity.class));
            }
            // 个人用户中心
            else if (viewId == R.id.avatars_layout) {
                // Intent intent = new Intent(getActivity(),
                // AvatarDemoActivity.class);
                // getActivity().startActivity(intent);

                if (service.checkLogin(getActivity())) {
                    Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                    getActivity().startActivity(intent);
                }
            }
            // 分享
            else if (viewId == R.id.share_layout) {
                UmShareUtils.init(getActivity());
                Intent intent = new Intent(getActivity(), ShareDialog.class);
                getActivity().startActivityForResult(intent, 1);
            }
            // 意见反馈
            else if (viewId == R.id.layoutBtnFeedBack) {
                if (service.checkLogin(getActivity())) {
                    Intent intent = new Intent(getActivity(), FeedBackActivity.class);
                    getActivity().startActivity(intent);
                }
            }
            // 物业经理
            else if (viewId == R.id.layoutBtnManager) {
                if (service.checkLogin(getActivity())) {
                    Intent intent = new Intent(getActivity(), ManagerListActivity.class);
                    getActivity().startActivity(intent);
                }
            }else if (viewId == R.id.layoutSetting){
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                getActivity().startActivity(intent);
            }
            // //注销登录
            // else if(viewId == R.id.layoutBtnLogout){
            // mTextNickName.setText("昵称:无");
            // mTextAccount.setText("邮箱:无");
            // service.doLogout(getActivity());
            // }
        }
    };


    static class CenterHandler extends Handler {
        private WeakReference<CenterFragment> mActivity;

        public CenterHandler(CenterFragment activity) {
            mActivity = new WeakReference<CenterFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CenterFragment context = mActivity.get();

            if (msg.what == C.Handler.LOAD_ACCOUNT_BIND) {
                String data = (String) msg.obj;
                Intent intent = new Intent(context.getActivity(), SafetyActivity.class);
                intent.putExtra("data", data);
                context.startActivity(intent);
            } else {
                TipUtil.showShort(context.getActivity(), msg);
            }
        }
    }
}
