package com.newline.housekeeper.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatkeeper.keeper.R;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.image.ImageDownLoader;
import com.newline.core.image.ImageDownLoader.onImageLoaderListener;
import com.newline.core.utils.TipUtil;
import com.newline.housekeeper.DemoData;
import com.newline.housekeeper.service.HouseService;

public class HouseDetailActivity extends BaseActivity {

    private HouseService service;

    private TextView mTextHouseAddress;
    private TextView mTextBulidYead;
    private TextView mTextHouseArea;
    private TextView mTextHousePrice;
    private TextView mTextHouseTracTime;
    private TextView mTextHouseType;
    private TextView mTextHouseModel;
    private TextView mTextHouseRoom;
    private TextView mTextHouseBath;
    private TextView mTextHouseTax;
    private TextView mTextHouseFee;
    private TextView mTextHouseInsurance;
    private ViewGroup mLayoutHousePic;

    private ImageDownLoader imageDownLoader;
    private LinearLayout unLoginLayout;

    private ViewPager mViewPager;
    private ArrayList<View> viewList;
    private ViewGroup viewGroupFlag;

    private TextView mTopTitle;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_detail_layout);

        initView();

        handler = new UIHandler(this);

        imageDownLoader = new ImageDownLoader(this);

        service = HouseService.getService(this);
        if(service.isLogined()){
            Intent intent = getIntent();
            String propertyId = intent.getStringExtra("propertyId");
            service.doHouseDetail(this, propertyId, handler);
            unLoginLayout.setVisibility(View.GONE);
        } else {
            String data = DemoData.houseDetail(getIntent().getIntExtra("position",0));
            handler.obtainMessage(C.Code.OK, data).sendToTarget();
            unLoginLayout.setVisibility(View.VISIBLE);
        }
        
    }

    private void initView() {
        mTextHouseAddress = loadControl(R.id.textHouseAddress);
        mTextBulidYead = loadControl(R.id.textBulidYead);
        mTextHouseArea = loadControl(R.id.textHouseArea);
        mTextHousePrice = loadControl(R.id.textHousePrice);
        mTextHouseTracTime = loadControl(R.id.textHouseTracTime);
        mTextHouseType = loadControl(R.id.textHouseType);
        mTextHouseModel = loadControl(R.id.textHouseModel);
        mTextHouseRoom = loadControl(R.id.textHouseRoom);
        mTextHouseBath = loadControl(R.id.textHouseBath);
        mTextHouseTax = loadControl(R.id.textHouseTax);
        mTextHouseFee = loadControl(R.id.textHouseFee);
        unLoginLayout = (LinearLayout) findViewById(R.id.unLoginLayout);
        mTextHouseInsurance = loadControl(R.id.textHouseInsurance);

        mViewPager = (ViewPager) findViewById(R.id.viewPageHousePic);
        viewGroupFlag = (ViewGroup) findViewById(R.id.layoutPicFlag);
        mLayoutHousePic = (ViewGroup) findViewById(R.id.layoutHousePic);

        mTopTitle = loadControl(R.id.textTopTitle);
        mTopTitle.setText(getString(R.string.houseInfo));
    }

    static class UIHandler extends Handler {
        private WeakReference<HouseDetailActivity> weakRef;

        public UIHandler(HouseDetailActivity refObj) {
            weakRef = new WeakReference<HouseDetailActivity>(refObj);
        }

        @Override
        public void handleMessage(Message msg) {
            HouseDetailActivity context = weakRef.get();

            if (msg.what == C.Code.OK) {
                try {
                    JSONObject json = JSON.parseObject(msg.obj.toString());

                    context.mTextHouseAddress.setText( json.getString("propertyaddress"));
                    context.mTextBulidYead.setText(json.getString("builtyear"));
                    context.mTextHouseArea.setText(json.getString("usablearea"));
                    context.mTextHousePrice.setText(json.getString("price"));
                    context.mTextHouseTracTime.setText(json.getString("purchasetime"));
                    context.mTextHouseType.setText(json.getString("property"));
                    context.mTextHouseModel.setText(json.getString("category"));
                    context.mTextHouseRoom.setText(json.getString("room"));
                    context.mTextHouseBath.setText(json.getString("bathroom"));
                    context.mTextHouseTax.setText(json.getString("propertytax"));
                    context.mTextHouseFee.setText(json.getString("propertycost"));
                    context.mTextHouseInsurance.setText(json.getString("premiums"));

                    JSONArray imgArray = json.getJSONArray("propertypics");
                    if (imgArray != null && !imgArray.isEmpty()) {
                        context.viewList = new ArrayList<View>();
                        LayoutInflater inflater = context.getLayoutInflater();

                        ImageView imageFlag = null;

                        for (int i = 0; i < imgArray.size(); i++) {
                            String url = imgArray.getString(i);
                            View mView = inflater.inflate(R.layout.house_pic_view, null);
                            final ImageView imgHouse = (ImageView) mView.findViewById(R.id.imgHouse);

                            context.imageDownLoader.downloadImage(url, new onImageLoaderListener() {

                                @Override
                                public void onImageLoader(Bitmap bitmap, String url) {
                                    if (bitmap == null)
                                        return;
                                    imgHouse.setImageBitmap(bitmap);
                                }
                            });

                            context.viewList.add(mView);

                            if (imgArray.size() > 1) {
                                imageFlag = new ImageView(context);
                                imageFlag.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                                imageFlag.setPadding(5, 2, 5, 2);
                                imageFlag.setImageResource(R.drawable.page_indicator_bg);
                                imageFlag.setEnabled(i == 0);

                                context.viewGroupFlag.addView(imageFlag);
                            }
                        }

                        context.mViewPager.setOnPageChangeListener(changeListener);
                        context.mViewPager.setAdapter(pagerAdapter);
                        context.mLayoutHousePic.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Log.e(C.TAG, "解析房产详情数据错误", e);
                    TipUtil.showShort(context, "系统错误");
                }
            } else {
                TipUtil.showShort(context, msg);
            }
        }

        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public int getCount() {
                return weakRef.get().viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(weakRef.get().viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(weakRef.get().viewList.get(position));
                return weakRef.get().viewList.get(position);
            }
        };

        OnPageChangeListener changeListener = new OnPageChangeListener() {

            @Override
            public void onPageSelected(int idx) {
                HouseDetailActivity context = weakRef.get();
                int size = context.viewGroupFlag.getChildCount();

                for (int i = 0; i < size; i++) {
                    View view = context.viewGroupFlag.getChildAt(i);
                    view.setEnabled(i == idx);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int idx) {

            }
        };

    }

}
