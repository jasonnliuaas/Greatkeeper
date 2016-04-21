package com.newline.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.SPUtil;
import com.newline.core.utils.TipUtil;
import com.newline.housekeeper.DemoData;
import com.newline.housekeeper.model.RentalAdapter;
import com.newline.housekeeper.model.RentalDetailAdapter;
import com.newline.housekeeper.service.RentalService;
import com.newline.housekeeper.service.UserService;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;

@ContentView(R.layout.rental_detail_layout)
public class RentalDetailActivity extends BaseActivity {

    @ViewInject(R.id.textTopTitle)
    private TextView mTopTitle;
    @ViewInject(R.id.layout_rental_foot)
    private LinearLayout layout_rental_foot;

    @ViewInject(R.id.imgToLeft)
    private ImageView mTextToLeft;

    @ViewInject(R.id.imgToRight)
    private ImageView mTextToRight;

    @ViewInject(R.id.textDate)
    private TextView mTextDate;

    @ViewInject(R.id.listRental)
    private ListView mListRental;

    @ViewInject(R.id.textEmptyTip)
    private LinearLayout mTextEmptyTip;
    @ViewInject(R.id.textTotalMoney)
    private TextView textTotalMoney;

    private int mYear;
    private int mMonth;
    private int currYear, currMonth;
    String id;
    private RentalService rentalService;

    private UIHandler handler;
    private String country;
    private RentalDetailAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        mTopTitle.setText(R.string.homeText04);
        handler = new UIHandler(this);
        rentalService = RentalService.getService(this);
        layout_rental_foot.setVisibility(View.GONE);
        id = getIntent().getStringExtra("id");
        country = SPUtil.getUtil(this).getString("country");
        initView();
    }

    public void initView(){
        Calendar now=Calendar.getInstance();
        // 初始化日期,为当前时间
        currYear = mYear = now.get(Calendar.YEAR);
        currMonth = mMonth = now.get(Calendar.MONTH)+1;

        mTextDate.setText(getDate());
        if(rentalService.isLogined()){
            /*View footView = getLayoutInflater().inflate(R.layout.rental_foot_view, null);
            textTotalMoney = (TextView) footView.findViewById(R.id.textTotalMoney);*/
            /*footView.setClickable(false);
            mListRental.addFooterView(footView);
            mListRental.setFooterDividersEnabled(false);*/

            /*String data = getParam("data");*/
            reLoadList();
        } else {
            mTextToLeft.setClickable(false);
            mTextToRight.setClickable(false);

            String data = DemoData.rentalList();
            reLoadList(data);
        }
    }

    private String getDate(){
        if(country.endsWith("en")){
            return  (  (mMonth < 10 ? ("0" + mMonth) : mMonth) + ", "+mYear);
        }
        return (mYear + "年" + (mMonth < 10 ? ("0" + mMonth) : mMonth) + "月");
    }

    @OnClick(R.id.imgToLeft)
    public void onLeftClick(View view){
        mMonth --;
        if(mMonth <= 0){
            mMonth = 12;
            mYear --;
        }
        mTextDate.setText(getDate());
        mTextToRight.setClickable(true);
        reLoadList();
    }

    @OnClick(R.id.imgToRight)
    public void onRightClick(View view){
        if(mYear > currYear || (mYear == currYear && mMonth >= currMonth)){
            TipUtil.showShort(this, "已经是最后一页");
            mTextToRight.setClickable(false);
            return;
        }

        mMonth ++;
        if(mMonth > 12){
            mMonth = 1;
            mYear ++;
        }
        mTextDate.setText(getDate());
        reLoadList();
    }

    @OnItemClick(R.id.listRental)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Object data = parent.getAdapter().getItem(position);
            Intent intent = new Intent(this, RentalInfoActivity.class);
            intent.putExtra("data", data.toString());
            startActivity(intent);
    }

    private void reLoadList(){
        rentalService.loadRentalData(this, mYear, mMonth, handler,id);
    }

    private void reLoadList(String data){
        JSONArray dataArray = null;
        try {
            dataArray = JSON.parseArray(data);

            if(dataArray == null || dataArray.isEmpty()){
                mTextEmptyTip.setVisibility(View.VISIBLE);
                mListRental.setVisibility(View.GONE);
                layout_rental_foot.setVisibility(View.GONE);
            } else {
                layout_rental_foot.setVisibility(View.VISIBLE);
                if(textTotalMoney != null){
                    float countMoney = 0;
                    for (int i = 0; i < dataArray.size(); i++) {
                        try {
                            JSONObject bean = dataArray.getJSONObject(i);
                            countMoney += Float.parseFloat(bean.getString("amount"));
                        } catch (Exception e) {
                            LogUtils.e("统计租金金额错误", e);
                        }
                    }
                    textTotalMoney.setText(getString(R.string.net_income) + String.format("%1$.2f", countMoney));
                }

                if(adapter == null){
                    adapter = new RentalDetailAdapter(RentalDetailActivity.this,dataArray);
                    mListRental.setAdapter(adapter);
                } else {
                    adapter.updateList(dataArray);
                }
                mListRental.setVisibility(View.VISIBLE);
                mTextEmptyTip.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            LogUtils.e("加载租金明细列表错误", e);
        }
    }



    static class UIHandler extends Handler {
        private WeakReference<RentalDetailActivity> mActivity;

        public UIHandler(RentalDetailActivity activity) {
            mActivity = new WeakReference<RentalDetailActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            RentalDetailActivity context = mActivity.get();

            if (msg.what == C.Handler.LOAD_RENTAL_LIST) {
                String data = (String) msg.obj;
                context.reLoadList(data);
            }
            else if(msg.what == C.Handler.LOAD_RENTAL){
                String data = (String) msg.obj;
                context.reLoadList(data);
            }else {
                TipUtil.showShort(context, msg);
            }
        }
    }
}
