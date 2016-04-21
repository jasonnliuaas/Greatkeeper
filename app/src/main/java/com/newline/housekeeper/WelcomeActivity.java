package com.newline.housekeeper;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;

import com.greatkeeper.keeper.R;

public class WelcomeActivity extends Activity {

    private View mView1, mView2, mView3, mView4;
    private ViewPager mViewPager;
    
//    private Button mBtnStartNow;

    private ArrayList<View> viewList;

    private boolean isAgain = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_layout);

        isAgain = getIntent().getBooleanExtra("isAgain", false);

        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPageWelcome);
        LayoutInflater inflater = getLayoutInflater();

        mView1 = inflater.inflate(R.layout.welcome_view1, null);
        mView2 = inflater.inflate(R.layout.welcome_view2, null);
        mView3 = inflater.inflate(R.layout.welcome_view3, null);
        mView4 = inflater.inflate(R.layout.welcome_view4, null);
        
//        mBtnStartNow = (Button) mView4.findViewById(R.id.btnStartNow);
//        if (!isAgain) {
//            mBtnStartNow.setVisibility(View.VISIBLE);
//            mBtnStartNow.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View arg0) {
//                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
//                }
//            });
//        } else {
//            mView1.setOnClickListener(viewOnClickListener);
//            mView2.setOnClickListener(viewOnClickListener);
//            mView3.setOnClickListener(viewOnClickListener);
//            mView4.setOnClickListener(viewOnClickListener);
//            mBtnStartNow.setVisibility(View.GONE);
//        }
        
        if (isAgain) {
            mView1.setOnClickListener(viewOnClickListener);
            mView2.setOnClickListener(viewOnClickListener);
            mView3.setOnClickListener(viewOnClickListener);
            mView4.setOnClickListener(viewOnClickListener);
        } else {
            mView4.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }

        viewList = new ArrayList<View>();
        viewList.add(mView1);
        viewList.add(mView2);
        viewList.add(mView3);
        viewList.add(mView4);

        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(pageChangeListener);
    }

    PagerAdapter pagerAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }
    };

    OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int index) {

        }

        @Override
        public void onPageScrolled(int index, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            
        }
        
    };

    OnClickListener viewOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            onBackPressed();
        }
    };
    
}