package com.newline.housekeeper.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.greatkeeper.keeper.R;
import com.newline.C;
import com.newline.core.image.ImageDownLoader;
import com.newline.housekeeper.activity.WebActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果；
 * 既支持自动轮播页面也支持手势滑动切换页面
 *
 *
 */

public class SlideShowView extends FrameLayout {


    //轮播图图片数量
    private final static int IMAGE_COUNT = 5;
    //自动轮播的时间间隔
    private final static int TIME_INTERVAL = 5;
    //自动轮播启用开关
    private final static boolean isAutoPlay = true;

    //放轮播图片的ImageView 的list
    private List<ImageView> imageViewsList;
    //放圆点的View的list
    private List<View> dotViewsList;
    private ViewPager viewPager;
    //当前轮播页
    private int currentItem  = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;

    private Context context;
    private UIHandler uiHandler;

    ImageDownLoader downLoader;
    //Handler
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }

    };
    class UIHandler extends Handler {

        public UIHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == C.Code.OK) {
                Object obj = msg.obj;
                int idx = msg.arg1;
                if (obj != null) {
                    String url = obj.toString();/*
                    context.adImages[idx].setImageBitmap(bitmap);*/
                    Uri uri = Uri.parse(url);
                    imageViewsList.get(idx).setImageURI(uri);
                } else {
                    imageViewsList.get(idx).setImageResource(R.drawable.nocontent);
                }
            }
        }
    }

    public SlideShowView(Context context) {
        this(context,null);
    }
    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        uiHandler = new UIHandler();
        initData();
        if(isAutoPlay){
            startPlay();
        }

    }
    /**
     * 开始轮播图切换
     */
    private void startPlay(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 5, TimeUnit.SECONDS);
    }
    /**
     * 停止轮播图切换
     */
    private void stopPlay(){
        scheduledExecutorService.shutdown();
    }
    /**
     * 初始化相关Data
     */
    private void initData(){
        imageViewsList = new ArrayList<ImageView>();
        dotViewsList = new ArrayList<View>();
    }
    /**
     * 初始化Views等UI
     */
    public void initUI(final Context context, List<String> imageUrls, final List<String> adLinks){
        if(imageUrls == null || imageUrls.size() == 0)
            return;

        LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);

        LinearLayout dotLayout = (LinearLayout)findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();

        // 热点个数与图片个数相等
        for (int i = 0; i < imageUrls.size(); i++) {
            //ImageView view = (ImageView) LayoutInflater.from(context).inflate(R.layout.img_item, this, false);
            SimpleDraweeView view = new SimpleDraweeView(context);
            view.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
            //view.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
            final int finalI = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("url", adLinks.get(finalI));
                    intent.putExtra("title", context.getString(R.string.webViewTitle));
                    context.startActivity(intent);
                }
            });
            view.setTag(imageUrls.get(i));
            //if(i==0)//给一个默认图{}
                //view.getHierarchy().setActualImageFocusPoint(new PointF(0.5f,0.5f));
             //   view.setScaleType(ScaleType.CENTER_CROP);
            imageViewsList.add(view);

            ImageView dotView =  new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            params.leftMargin = 4;
            params.rightMargin = 4;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
        initPageIndicator(currentItem);
        downLoader = new ImageDownLoader(context);
        viewPager.setAdapter(new MyPagerAdapter());

    }

    /**
     * 填充ViewPager的页面适配器
     *
     */
    private class MyPagerAdapter  extends PagerAdapter {


        @Override
        public void destroyItem(View container, int position, Object object) {
            /*((ViewPager)container).removeView((View)object);
            position %= imageViewsList.size();
            if (position<0){
                position = imageViewsList.size()+position;
            }
            ((ViewPager)container).removeView(imageViewsList.get(position));*/
        }

        @Override
        public Object instantiateItem(View container, int position) {
            position %= imageViewsList.size();
            if (position<0){
                position = imageViewsList.size()+position;
            }
            final ImageView imageView = imageViewsList.get(position);
           // imageView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
            imageView.setImageURI(Uri.parse(imageView.getTag().toString()));
           /*downLoader.downloadImage(imageView.getTag().toString(), new ImageDownLoader.onImageLoaderListener() {

                @Override
                public void onImageLoader(Bitmap bitmap, String url) {
                    if (bitmap == null)
                        return;
                    imageView.setImageBitmap(bitmap);
                }
            });*/
            ViewParent vp =imageView.getParent();
            if (vp!=null){
                ViewGroup parent = (ViewGroup)vp;
                parent.removeView(imageView);
            }
            ((ViewPager)container).addView(imageViewsList.get(position));
            return imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            return imageViewsList.size() == 1?imageViewsList.size():Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }

    }
    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     *
     */
    private class MyPageChangeListener implements OnPageChangeListener {

        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int pos) {
            if(imageViewsList.size() == 0){
                initPageIndicator(pos);
            }else{

                pos %= imageViewsList.size();
                if (pos<0){
                    pos = imageViewsList.size()+pos;
                }
                initPageIndicator(pos);
            }
        }

    }

    public  void initPageIndicator(int pos){
        for(int i=0;i < dotViewsList.size();i++){
            if(i == pos){
                ((View)dotViewsList.get(pos)).setBackgroundResource(R.drawable.page_point_focuse);
            }else {
                ((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.page_point_unfocuse);
            }
        }
    }

    /**
     *执行轮播图切换任务
     *
     */
    private class SlideShowTask implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (viewPager) {
                if(imageViewsList.size()!=1){
                    currentItem = viewPager.getCurrentItem();
                    currentItem++;
                    handler.obtainMessage().sendToTarget();
                }
            }
        }

    }

    /**
     * 销毁ImageView资源，回收内存
     *
     */
    private void destoryBitmaps() {

        for (int i = 0; i < IMAGE_COUNT; i++) {
            ImageView imageView = imageViewsList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                //解除drawable对view的引用
                drawable.setCallback(null);
            }
        }
    }


}