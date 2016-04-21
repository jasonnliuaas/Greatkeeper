package com.newline.housekeeper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.greatkeeper.keeper.R;
import com.newline.C;

public class AvatarPopWindow extends PopupWindow implements OnClickListener {
    private Activity mActivity;

    public AvatarPopWindow(Activity activity) {
        super(activity);
        this.mActivity = activity;
        initView(activity);
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.avatar_pop_window, null);
        rootView.findViewById(R.id.photograph_tv).setOnClickListener(this);
        rootView.findViewById(R.id.select_from_photo_tv).setOnClickListener(this);
        rootView.findViewById(R.id.cancel).setOnClickListener(this);
        setContentView(rootView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        setTouchable(true);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        int id = v.getId();
        switch (id) {
            case R.id.photograph_tv:
                getImageFromCamera();
                break;
            case R.id.select_from_photo_tv:
                getImageFromAlbum();
                break;
            default:
                break;
        }

    }

    private void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");// 相片类型
        this.mActivity.startActivityForResult(intent, C.REQUEST_CODE_PICK_IMAGE);
    }

    protected void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            this.mActivity.startActivityForResult(getImageByCamera, C.REQUEST_CODE_CAPTURE_CAMEIA);
        } else {
            Toast.makeText(this.mActivity, mActivity.getString(R.string.sd_tip), Toast.LENGTH_LONG).show();
        }
    }

}
