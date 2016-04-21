package com.newline.housekeeper.activity;

import java.lang.ref.WeakReference;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.image.ImageDownLoader;
import com.newline.core.image.ImageDownLoader.onImageLoaderListener;
import com.newline.core.image.ImageUtils;
import com.newline.core.utils.StringUtils;
import com.newline.core.utils.TipUtil;
import com.greatkeeper.keeper.R;
import com.newline.housekeeper.DemoData;
import com.newline.housekeeper.service.RepairService;

public class RepairDetailActivity extends BaseActivity {

    private RepairService service;
    
    private TextView mTextRepairName;
    private TextView mTextRepairCost;
    private TextView mTextRepairTime;
    private TextView mTextRepairAddress;
    private ImageView mImgRepairBefore;
    private ImageView mImgRepairAfter;
    private TextView mTextNoPicBefore;
    private TextView mTextNoPicAfter;
    private Button mBtnRepairConfirm;
    private LinearLayout unLoginLayout;
    private ImageDownLoader imageDownLoader;
    
    private Handler handler;
    private Handler confirmHandler;
    
    private String propertyId;
    private String maintenanceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_detail_layout);
        
        mTextRepairName = loadControl(R.id.textRepairName);
        mTextRepairCost = loadControl(R.id.textRepairCost);
        mTextRepairTime = loadControl(R.id.textRepairTime);
        mTextRepairAddress = loadControl(R.id.textRepairAddress);
        mImgRepairBefore = loadControl(R.id.imgRepairBefore);
        mImgRepairAfter = loadControl(R.id.imgRepairAfter);
        mTextNoPicBefore = loadControl(R.id.textNoPicBefore);
        mTextNoPicAfter = loadControl(R.id.textNoPicAfter);
        mBtnRepairConfirm = loadControl(R.id.btnRepairConfirm);
        unLoginLayout = loadControl(R.id.unLoginLayout);
        mBtnRepairConfirm.setOnClickListener(onConfirmClick);

        
        TextView topTitle = loadControl(R.id.textTopTitle);
        topTitle.setText(getString(R.string.repairDetail));
        
        handler = new UIHandler(this);
        confirmHandler = new UIHandler(this);
        
        imageDownLoader = new ImageDownLoader(this);
        
        Intent intent = getIntent();
        String repairAddress = intent.getStringExtra("repairAddress");
        mTextRepairAddress.setText(repairAddress);

        service = RepairService.getService(this);
        
        if(service.isLogined()){
            propertyId = intent.getStringExtra("propertyId");
            maintenanceId = intent.getStringExtra("maintenanceId");
            service.doRepairDetail(this, propertyId, maintenanceId, handler);
            unLoginLayout.setVisibility(View.GONE);
        } else {
            String data = DemoData.repairDetail();
            Message msg = handler.obtainMessage(C.Code.OK, data);
            msg.arg1 = 1;
            msg.sendToTarget();
            unLoginLayout.setVisibility(View.VISIBLE);
            
            LinearLayout layout = (LinearLayout) findViewById(R.id.unLoginLayout);
            layout.setVisibility(View.VISIBLE);
        }
    }
    
    OnClickListener onConfirmClick = new OnClickListener() {
        
        @Override
        public void onClick(View arg0) {
            service.doRepairConfirm(RepairDetailActivity.this, propertyId, maintenanceId, 3, confirmHandler);
        }
    };
    
    static class UIHandler extends Handler {
        private WeakReference<RepairDetailActivity> weakRef;
        
        public UIHandler(RepairDetailActivity refObj) { 
            weakRef = new WeakReference<RepairDetailActivity>(refObj); 
        }
        
        @Override
        public void handleMessage(Message msg) {
            final RepairDetailActivity context = weakRef.get();
            
            //房产详情
            if(msg.what == C.Code.OK){
                try {
                    JSONObject json = JSON.parseObject(msg.obj.toString());
                    
                    context.mTextRepairName.setText(json.getString("mantenaneceitem"));
                    context.mTextRepairCost.setText(json.getString("expectedexpense"));
                    context.mTextRepairTime.setText(json.getString("expectedTakeTime"));
                    
                    String currImgUrl = null, effectImgUrl = null;
                    try {
                        currImgUrl = json.getString("currentpicture");
                        effectImgUrl = json.getString("effectpicture");
                    } catch (Exception e) {
                        Log.e(C.TAG, "No repair image.");
                    }
                    final float roundPx = 20.0F;
                    
                    if(!StringUtils.isTrimEmpty(currImgUrl)){
                        context.imageDownLoader.downloadImage(currImgUrl, new onImageLoaderListener() {
                            
                            @Override
                            public void onImageLoader(Bitmap bitmap, String url) {
                                if(bitmap == null) return;
                                bitmap = ImageUtils.getRoundedCornerBitmap(bitmap, roundPx);
                                context.mImgRepairBefore.setImageBitmap(bitmap);
                                context.mImgRepairBefore.setVisibility(View.VISIBLE);
                            }
                        });
                        
                        context.mTextNoPicBefore.setVisibility(View.GONE);
                    } else {
                        context.mImgRepairBefore.setVisibility(View.GONE);
                        context.mTextNoPicBefore.setVisibility(View.VISIBLE);
                    }
                    
                    if(!StringUtils.isTrimEmpty(effectImgUrl)){
                        context.imageDownLoader.downloadImage(effectImgUrl, new onImageLoaderListener() {
                            
                            @Override
                            public void onImageLoader(Bitmap bitmap, String url) {
                                if(bitmap == null) return;
                                bitmap = ImageUtils.getRoundedCornerBitmap(bitmap, roundPx);
                                context.mImgRepairAfter.setImageBitmap(bitmap);
                                context.mImgRepairAfter.setVisibility(View.VISIBLE);
                            }
                        });
                        
                        context.mTextNoPicAfter.setVisibility(View.GONE);
                    } else {
                        context.mImgRepairAfter.setVisibility(View.GONE);
                        context.mTextNoPicAfter.setVisibility(View.VISIBLE);
                    }
                    
                    if(json.getInteger("approval") == C.Repair.UNCONFIRMED){
                        context.mBtnRepairConfirm.setTextColor(context.getResources().getColor(R.color.white));
                        context.mBtnRepairConfirm.setBackgroundResource(R.drawable.btn_bg);
                        context.mBtnRepairConfirm.setClickable(true);
                    } else {
                        context.mBtnRepairConfirm.setTextColor(context.getResources().getColor(R.color.btnNoneColor));
                        context.mBtnRepairConfirm.setBackgroundResource(R.drawable.btn_bg1);
                        context.mBtnRepairConfirm.setClickable(false);
                    }
                    if(msg.arg1 > 0){
                        context.mBtnRepairConfirm.setClickable(false);
                    }
                } catch (Exception e) {
                    Log.e(C.TAG, "解析房屋维修详情数据错误", e);
                    TipUtil.showShort(context, "系统错误");
                }
            } else {
                TipUtil.showShort(context, msg);
            }
        }
    }
    
    static class ConfirmHandler extends Handler {
        private WeakReference<RepairDetailActivity> weakRef;
        
        public ConfirmHandler(RepairDetailActivity refObj) { 
            weakRef = new WeakReference<RepairDetailActivity>(refObj); 
        }
        
        @Override
        public void handleMessage(Message msg) {
            final RepairDetailActivity context = weakRef.get();
            
            if(msg.what == C.Code.OK){
                context.mBtnRepairConfirm.setTextColor(context.getResources().getColor(R.color.btnNoneColor));
                context.mBtnRepairConfirm.setBackgroundResource(R.drawable.btn_bg1);
                context.mBtnRepairConfirm.setClickable(false);
            } else {
                TipUtil.showShort(context, msg);
            }
        }
    }
    
    

}
