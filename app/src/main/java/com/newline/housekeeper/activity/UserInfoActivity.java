package com.newline.housekeeper.activity;

import java.lang.ref.WeakReference;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.greatkeeper.keeper.R;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.image.ImageDownLoader;
import com.newline.core.image.ImageDownLoader.onImageLoaderListener;
import com.newline.core.image.ImageUtils;
import com.newline.core.utils.FastBlur;
import com.newline.core.utils.StringUtils;
import com.newline.core.utils.TipUtil;
import com.newline.housekeeper.MainActivity;
import com.newline.housekeeper.control.SuccessDialog;
import com.newline.housekeeper.model.UserBean;
import com.newline.housekeeper.service.UserService;
import com.newline.housekeeper.view.SelectHeadTools;

public class UserInfoActivity extends BaseActivity {
    
    private TextView mTextTopTitle;
    
    private ImageView mImgAvatar;
    
    private RelativeLayout mLayoutAvatar;
    
    private UserService service;
    
    private ImageDownLoader downLoader;
    
    private Handler handler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_layout);
        
        handler = new UIHandler(this);
        
        downLoader = new ImageDownLoader(this);
        service = UserService.getService(this);
        
        mTextTopTitle = loadControl(R.id.textTopTitle);
        mTextTopTitle.setText(R.string.myInfo);
        
        mImgAvatar = loadControl(R.id.imgUserAvatar);
        
        mLayoutAvatar = loadControl(R.id.layoutAvatar);
        
        UserBean user = service.getUser();
        if(user != null && !StringUtils.isTrimEmpty(user.getAvatar())){
            if(user.getBitmapAvatar() == null){
                downLoader.downloadImage(user.getAvatar(), new onImageLoaderListener() {
                    
                    @Override
                    public void onImageLoader(Bitmap bitmap, String url) {
                        if(bitmap != null){
                            mImgAvatar.setImageBitmap(bitmap);
                            Drawable drawable = FastBlur.boxBlurFilter(UserInfoActivity.this, bitmap);
                            mLayoutAvatar.setBackground(drawable);
                        }
                    }
                });
            } else {
                mImgAvatar.setImageBitmap(user.getBitmapAvatar());
                Drawable drawable = FastBlur.boxBlurFilter(UserInfoActivity.this, user.getBitmapAvatar());
                mLayoutAvatar.setBackground(drawable);
            }
        } else {
            mImgAvatar.setImageResource(R.drawable.img_addavatar);
        }
       // LayoutInflater layoutInflater = getLayoutInflater();
       // final View rootView = layoutInflater.inflate(R.layout.user_info_layout, null);
        mLayoutAvatar.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View view) {
               /* AvatarPopWindow avatarPopWindow = new AvatarPopWindow(UserInfoActivity.this);
                avatarPopWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);*/
            	 SelectHeadTools.openDialog(UserInfoActivity.this);
            	
            }
        });
    }
    
    public void onClick(View view){
        int viewId = view.getId();
        
        if(viewId == R.id.layoutBtnUpdateInfo){
            startActivity(new Intent(this, UpdateUserInfoActivity.class));
        } else if(viewId == R.id.layoutBtnUpdatePwd){
            startActivity(new Intent(this, UpdatePasswordActivity.class));
        } else if(viewId == R.id.layoutBtnLogout){
            service.doLogout(this);
            TipUtil.showShort(this, getString(R.string.account_cancellation_success));
            startActivity(new Intent(this, MainActivity.class));
        }
        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        
        if (data != null) {
            Uri uri = null;
            if (requestCode == C.REQUEST_CODE_PICK_IMAGE) {
                uri = data.getData();
            } else if (requestCode == C.REQUEST_CODE_CAPTURE_CAMEIA) {
                uri = data.getData();
                if (uri == null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        bitmap = (Bitmap) bundle.get("data");
                    }
                }
            }
            if (uri != null) {
                bitmap = getBitmapFromUri(uri);
            }
        }
        if (bitmap != null) {
            int len = bitmap.getWidth() > bitmap.getHeight() ? bitmap.getHeight() : bitmap.getWidth();
            len = (len > 640 ? 640 : len);
            bitmap = ImageUtils.centerSquareScaleBitmap(bitmap, len);
            
            service.doUploadAvatar(this, handler, bitmap);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    static class UIHandler extends Handler {
        private WeakReference<UserInfoActivity> weakRef;
        
        public UIHandler(UserInfoActivity refObj) { 
            weakRef = new WeakReference<UserInfoActivity>(refObj); 
        }
        
        @Override
        public void handleMessage(Message msg) {
            UserInfoActivity context = weakRef.get();
            
            if(msg.what == C.Code.OK){
                Bitmap bitmap = (Bitmap) msg.obj;
                
                UserBean bean = context.service.getUser();
                if(bean != null){
                    bean.setBitmapAvatar(bitmap);
                }
                context.mImgAvatar.setImageBitmap(bitmap);
                Drawable drawable = FastBlur.boxBlurFilter(context, bitmap);
                context.mLayoutAvatar.setBackground(drawable);
                String content = context.getString(R.string.avatar_modification_success);
                /*
                String title = "系统提示";
                SuccessDialog dialog = new SuccessDialog(context, title, content);
                dialog.show();*/
                Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
            }else {
                TipUtil.showShort(context, msg);
            }
        }
    }
    

}
