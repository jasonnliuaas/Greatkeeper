package com.newline.housekeeper.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.newline.C;
import com.newline.core.image.ImageDownLoader;
import com.newline.core.utils.SPUtil;
import com.newline.core.utils.TipUtil;
import com.newline.housekeeper.DemoData;
import com.newline.housekeeper.HttpRequest;
import com.newline.housekeeper.HttpRequest.RequestCallBack;
import com.newline.housekeeper.KeeperUrl;
import com.newline.housekeeper.control.ScrollLayout;
import com.newline.housekeeper.service.PaymentService;
import com.newline.housekeeper.service.RemindService;
import com.newline.housekeeper.service.RentalService;
import com.newline.housekeeper.service.UserService;
import com.newline.housekeeper.view.SlideShowView;

public class IndexFragment extends Fragment {

    @ViewInject(R.id.textNum01)
    private TextView textNum01;

    @ViewInject(R.id.textNum02)
    private TextView textNum02;

    @ViewInject(R.id.textNum03)
    private TextView textNum03;

    @ViewInject(R.id.textNum04)
    private TextView textNum04;

    @ViewInject(R.id.textNum05)
    private TextView textNum05;

    @ViewInject(R.id.textNum06)
    private TextView textNum06;

    @ViewInject(R.id.btnHome01)
    private Button mBtnHome01; // 房产情况

    @ViewInject(R.id.btnHome02)
    private Button mBtnHome02; // 租赁管理

    @ViewInject(R.id.btnHome03)
    private Button mBtnHome03; // 房产维修

    @ViewInject(R.id.btnHome04)
    private Button mBtnHome04; // 租金明细

    @ViewInject(R.id.btnHome05)
    private Button mBtnHome05; // 缴费提醒

    @ViewInject(R.id.btnHome06)
    private Button mBtnHome06; // 房源推荐

    @ViewInject(R.id.btnHome07)
    private Button mBtnHome07; // 个税缴纳

    @ViewInject(R.id.btnHome08)
    private Button mBtnHome08; // 贷款办理

    @ViewInject(R.id.btnHome09)
    private Button mBtnHome09; // 租金宝

    @ViewInject(R.id.textMoneyType)
    private TextView mTextMoneyType; // 币种

    @ViewInject(R.id.textCountIncome)
    private TextView mTextCountIncome; // 总收入

    @ViewInject(R.id.textBeforeIncome)
    private TextView mTextBeforeIncome; // 上月收入

    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    private IntentFilter intentFilter;
    private NetworkChangeReceive networkChangeReceive;

    private SlideShowView mScrollLayout;

    private List<String> adLinks = new ArrayList<String>();
    private List<String> imgUrls = new ArrayList<String>();;

    private ImageDownLoader downLoader;

    private UIHandler handler;
    private RentalService rentalService;
    private PaymentService paymentService;
    private UserService userService;
    private RemindService remindService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_view_home, container, false);

        handler = new UIHandler(this);
        downLoader = new ImageDownLoader(view.getContext());
        rentalService = RentalService.getService(getActivity());
        paymentService = PaymentService.getService(getActivity());
        userService = UserService.getService(getActivity());
        remindService = RemindService.getService(getActivity());

        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this);
        // 收入统计
        if(userService.isLogined()){
            rentalService.loadReantalIncome(getActivity(), handler);
        }
        intentFilter = new IntentFilter();
        //addAction
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceive = new NetworkChangeReceive();
        getActivity().registerReceiver(networkChangeReceive, intentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!userService.isLogined()){
            mTextCountIncome.setText("0");
            mTextBeforeIncome.setText("0");
        }

        // 加载之前设置的币种
        C.Currency.nowCurrency = SPUtil.getUtil(getActivity()).getInt(C.CURRENCY, C.Currency.USD);
        String moneyType = "AUD";
        switch (C.Currency.nowCurrency){
            case C.Currency.AUD://澳元
                moneyType = "AUD";
                break;
            case C.Currency.CAD: //加元
                moneyType = "CAD";
                break;
            case C.Currency.USD: //美元
                moneyType = "USD";
                break;
            case C.Currency.GBP: //英镑
                moneyType = "GBP";
                break;
        }
        String oldType = mTextMoneyType.getText().toString();
        mTextMoneyType.setText(moneyType);
        if(!moneyType.equals(oldType)){
            rentalService.loadReantalIncome(getActivity(), handler);
        }

        if(userService.getUser() != null){
            remindService.doRemindTotal(getActivity(), handler);
        } else {
            handler.obtainMessage(C.Handler.CLEARALL_UNREAD).sendToTarget();
        }
    }

    private void initView(View view) {
        // 处理滑动广告
        mScrollLayout = (SlideShowView) view.findViewById(R.id.ScrollLayout);
        mScrollLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }



    @OnClick(R.id.btnHome01)
    public void onHomeBtnClick01(View view) {
        // 房产情况
        startActivity(new Intent(getActivity(), HouseListActivity.class));
    }

    @OnClick(R.id.btnHome02)
    public void onHomeBtnClick02(View view) {
        // 租赁管理
        startActivity(new Intent(getActivity(), RentListActivity.class));
    }

    @OnClick(R.id.btnHome03)
    public void onHomeBtnClick03(View view) {
        // 房产维修
        startActivity(new Intent(getActivity(), RepairListActivity.class));
    }

    @OnClick(R.id.btnHome04)
    public void onHomeBtnClick04(View view) {
        GregorianCalendar d = new GregorianCalendar();
        int year = d.get(Calendar.YEAR);//获得年
        int month = d.get(Calendar.MONTH) + 1;

        if(rentalService.isLogined()){
            rentalService.loadRentalData(getActivity(), year, month, handler,"");
        } else {
            Message msg = handler.obtainMessage(C.Handler.LOAD_RENTAL_LIST);
            msg.obj = DemoData.rentalList();
            msg.arg1 = year;
            msg.arg2 = month;
            msg.sendToTarget();
        }
    }

    @OnClick(R.id.btnHome05)
    public void onHomeBtnClick05(View view) {
        // 缴费提醒
        if(paymentService.isLogined()){
            paymentService.loadPaymentData(getActivity(), handler);
        } else {
            String data = DemoData.paymentList();
            Message msg = handler.obtainMessage(C.Handler.LOAD_PAYMENT_LIST, data);
            msg.arg1 = 1;
            msg.sendToTarget();
        }
    }

    @OnClick(R.id.btnHome06)
    public void onHomeBtnClick06(View view) {
        // 房源推荐
        Intent intent = new Intent(getActivity(), HouseSaleListActivity.class);
        startActivity(intent);
        remindService.doRemindList(getActivity(), C.MsgType.RECOMMEND);
    }
    @OnClick(R.id.btnHome07)
    public void onHomeBtnClick07(View view) {
        // 个税缴纳
        Intent intent = new Intent(getActivity(), TaxListActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.btnHome08)
    public void onHomeBtnClick08(View view) {
        // 贷款办理
        Intent intent = new Intent(getActivity(), LoanListActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.btnHome09)
    public void onHomeBtnClick09(View view) {
        // 租金宝
        /*if(alertDialog == null){
            builder = new AlertDialog.Builder(getActivity());
            builder.setPositiveButton("OK",null);
            alertDialog = builder.create();
            alertDialog .setTitle(getString(R.string.System_prompt));
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.setMessage(getString(R.string.in_development));
        }
        alertDialog.show();*/
        Intent intent = new Intent(getActivity(),Web2Activity.class);
        intent.putExtra("title",getActivity().getString(R.string.homeText09));
        String uid = SPUtil.getUtil(getActivity()).getString(C.UserIdKey);
        String lg = SPUtil.getUtil(getActivity()).getString("country");
        intent.putExtra("url",KeeperUrl.FORMAL_HOST+"/zujinbao?uid="+uid+"&lg="+lg+"&agent="+C.APP_AGENT);
        startActivity(intent);
    }

    static class UIHandler extends Handler {
        private WeakReference<IndexFragment> mActivity;

        public UIHandler(IndexFragment activity) {
            mActivity = new WeakReference<IndexFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            IndexFragment context = mActivity.get();
            if (msg.what == C.Code.OK) {
                Object obj = msg.obj;
                if (obj != null) {
                    JSONArray array = JSON.parseArray(obj.toString());
                    int size = (array == null ? 0 : array.size());
                    for (int i = 0; i < size; i++) {
                        JSONObject object = array.getJSONObject(i);
                        String url =  object.getString("url");
                        String link = object.getString("link");
                        context.imgUrls.add(url);
                        context.adLinks.add(link);

                    }
                    context.mScrollLayout.initUI(context.getActivity(),context.imgUrls,context.adLinks);

                }
            } else if (msg.what == C.Handler.LOAD_RENTAL_LIST) {
                String data = (String) msg.obj;

                Intent intent = new Intent(context.getActivity(), RentalListActivity.class);
                intent.putExtra("data", data);
                intent.putExtra("year", msg.arg1);
                intent.putExtra("month", msg.arg2);
                context.startActivity(intent);

                context.remindService.doRemindList(context.getActivity(), C.MsgType.RENTAL);
            } else if (msg.what == C.Handler.LOAD_PAYMENT_LIST) {
                String data = (String) msg.obj;

                Intent intent = new Intent(context.getActivity(), PaymentListActivity.class);
                intent.putExtra("data", data);
                intent.putExtra("isLogin", msg.arg1 == 0);
                context.startActivity(intent);

                context.remindService.doRemindList(context.getActivity(), C.MsgType.PAYMENT);
            } else if(msg.what == C.Handler.LOAD_RENTAL_INCOME){
                String balance = "0", last_month= "0";
                try {
                    JSONObject bean = JSON.parseObject(msg.obj.toString());
                    balance = bean.getString("balance");
                    last_month = bean.getString("last_month");
                } catch (Exception e) {
                    LogUtils.e("解析收入统计数据错误", e);
                }
                context.mTextCountIncome.setText(balance);
                context.mTextBeforeIncome.setText(last_month);
            } else if(msg.what == C.Handler.TOTAL_MESSAGE){
                String num1 = "0", num2 = "0", num3 = "0", num4 = "0", num5 = "0", num6 = "0";
                try {
                    JSONObject bean = JSON.parseObject(msg.obj.toString());
                    for (String key : bean.keySet()) {
                        if(C.MsgType.HOUSE.equals(key)){
                            num1 = bean.getString(C.MsgType.HOUSE);
                        } else if(C.MsgType.RENT.equals(key)){
                            num2 = bean.getString(C.MsgType.RENT);
                        } else if(C.MsgType.REPAIR.equals(key)){
                            num3 = bean.getString(C.MsgType.REPAIR);
                        } else if(C.MsgType.RENTAL.equals(key)){
                            num4 = bean.getString(C.MsgType.RENTAL);
                        } else if(C.MsgType.PAYMENT.equals(key)){
                            num5 = bean.getString(C.MsgType.PAYMENT);
                        } else if(C.MsgType.RECOMMEND.equals(key)){
                            num6 = bean.getString(C.MsgType.RECOMMEND);
                        }
                    }

                    int visibility = "0".equals(num1) ? View.GONE : View.VISIBLE;
                    if(visibility == View.VISIBLE) context.textNum01.setText(num1);
                    context.textNum01.setVisibility(visibility);

                    visibility = "0".equals(num2) ? View.GONE : View.VISIBLE;
                    if(visibility == View.VISIBLE) context.textNum02.setText(num2);
                    context.textNum02.setVisibility(visibility);

                    visibility = "0".equals(num3) ? View.GONE : View.VISIBLE;
                    if(visibility == View.VISIBLE) context.textNum03.setText(num3);
                    context.textNum03.setVisibility(visibility);

                    visibility = "0".equals(num4) ? View.GONE : View.VISIBLE;
                    if(visibility == View.VISIBLE) context.textNum04.setText(num4);
                    context.textNum04.setVisibility(visibility);

                    visibility = "0".equals(num5) ? View.GONE : View.VISIBLE;
                    if(visibility == View.VISIBLE) context.textNum05.setText(num5);
                    context.textNum05.setVisibility(visibility);

                    visibility = "0".equals(num6) ? View.GONE : View.VISIBLE;
                    if(visibility == View.VISIBLE) context.textNum06.setText(num6);
                    context.textNum06.setVisibility(visibility);
                } catch (Exception e) {
                    LogUtils.e("解析消息统计数据错误", e);
                }
            }  else if(msg.what == C.Handler.CLEARALL_UNREAD){
                context.textNum01.setVisibility(View.GONE);
                context.textNum02.setVisibility(View.GONE);
                context.textNum03.setVisibility(View.GONE);
                context.textNum04.setVisibility(View.GONE);
                context.textNum05.setVisibility(View.GONE);
                context.textNum06.setVisibility(View.GONE);
            } else {
                TipUtil.showShort(context.getActivity(), msg);
            }
        }
    }


    class NetworkChangeReceive extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                //Toast.makeText(getActivity(), "net is available", Toast.LENGTH_SHORT).show();
                if(imgUrls.isEmpty()){
                    HttpRequest.getInstance().asynRequest(getActivity(), KeeperUrl.GetRotation, new HttpRequest.RequestCallBack() {
                        @Override
                        public void doCallBack(int code, String dataMsg, String data) {
                            if (code == C.Code.OK) {
                                Message message = handler.obtainMessage();
                                message.what = code;
                                message.obj = data;
                                message.sendToTarget();
                            }

                            else {
                                handler.obtainMessage(code, dataMsg);
                            }
                        }

                    });
                }
            } else {
                Toast.makeText(getActivity(), "网络连接不可用...", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("tag", "程序退出");
        getActivity().unregisterReceiver(networkChangeReceive);
    }
}
