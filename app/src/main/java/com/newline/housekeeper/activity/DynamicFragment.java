package com.newline.housekeeper.activity;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.newline.C;
import com.newline.core.utils.AndroidUtils;
import com.newline.core.utils.TipUtil;
import com.newline.housekeeper.control.XListView;
import com.newline.housekeeper.control.XListView.IXListViewListener;
import com.newline.housekeeper.model.DynamicAdapter1;
import com.newline.housekeeper.model.DynamicAdapter2;
import com.newline.housekeeper.service.DynamicService;

public class DynamicFragment extends Fragment implements IXListViewListener {

    @ViewInject(R.id.btnMsgRecommend)
    private Button btnMsgRecommend;

    @ViewInject(R.id.btnMsgCenter)
    private Button btnMsgCenter;

    @ViewInject(R.id.listContent)
    private XListView listContent;

    private JSONArray recommendArr, centerArr;

    private DynamicAdapter1 recommendAdapter;
    private DynamicAdapter2 centerAdapter;

    private UIHandler handler;
    private DynamicService service;

    private int pageSize = 10;
    private int pageIndex = 1;
    private int type = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_view_dynamic, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewUtils.inject(this);

        handler = new UIHandler(this);

        service = DynamicService.getService(getActivity());

        listContent.setPullLoadEnable(true);
        listContent.setXListViewListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(recommendArr == null){
            service.loadRecommondData(getActivity(), 1, pageSize, handler);
        }
    }

    private void switchTab(Button button, String flag){
        if("checked".equals(flag)){
            button.setBackgroundResource(R.drawable.tab_checked);
            button.setTextColor(getResources().getColor(R.color.white));
            button.setTag("checked");
        } else {
            button.setBackgroundResource(R.drawable.tab_uncheck);
            button.setTextColor(getResources().getColor(R.color.textGray1));
            button.setTag("uncheck");
        }
        int padding = AndroidUtils.dip2px(getActivity(), 10f);
        button.setPadding(0, padding, 0, padding);
    }

    @OnClick(R.id.btnMsgRecommend)
    public void onRecommend(View view){
        String flag = (String) btnMsgRecommend.getTag();
        if("checked".equals(flag)){
            return;
        }

        if(recommendArr == null || recommendArr.isEmpty()){
            service.loadRecommondData(getActivity(), 1, pageSize, handler);
        } else {
            switchTab(btnMsgRecommend, "checked");
            switchTab(btnMsgCenter, "uncheck");
            listContent.setAdapter(new DynamicAdapter1(getActivity(), recommendArr));
        }
        type = 0;
    }

    @OnClick(R.id.btnMsgCenter)
    public void onCenter(View view){
        String flag = (String) btnMsgCenter.getTag();
        if("checked".equals(flag)){
            return;
        }

        if(centerArr == null || centerArr.isEmpty()){
            service.loadCenterData(getActivity(), 1, pageSize, handler);
        } else {
            switchTab(btnMsgCenter, "checked");
            switchTab(btnMsgRecommend, "uncheck");
            listContent.setAdapter(new DynamicAdapter2(getActivity(), centerArr));
        }
        type = 1;
    }

    @OnItemClick(R.id.listContent)
    public void onListItemClick(AdapterView<?> adapter, View view, int position, long id) {
        try {
            JSONObject bean = (JSONObject) adapter.getItemAtPosition(position);
            String url = bean.getString("url");

            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("isshare", true);
            intent.putExtra("shareTitle","消息推荐");
            intent.putExtra("shareUrl", url);
            intent.putExtra("title",getString(R.string.message_recommendation));
            intent.putExtra("shareContent", bean.getString("excerpt"));
            intent.putExtra("shareImg", bean.getString("thumb"));
            startActivity(intent);

        } catch (Exception e) {
            LogUtils.e("消息推荐异常", e);
        }
    }

    static class UIHandler extends Handler {
        private WeakReference<DynamicFragment> weakRef;

        public UIHandler(DynamicFragment refObj) {
            weakRef = new WeakReference<DynamicFragment>(refObj);
        }

        @Override
        public void handleMessage(Message msg) {
            DynamicFragment context = weakRef.get();

            try {
                if(msg.what == C.Handler.LOAD_RECOMMOND_LIST){
                    int pageIndex = msg.arg1;
                    if(pageIndex <= 1){
                        context.pageIndex = 1;
                        context.recommendArr = JSON.parseArray(msg.obj.toString());
                        if(context.recommendAdapter == null){
                            context.recommendAdapter = new DynamicAdapter1(context.getActivity(), context.recommendArr);
                            context.listContent.setAdapter(context.recommendAdapter);
                        } else {
                            context.recommendAdapter.update(context.recommendArr);
                        }
                    } else {
                        JSONArray array = JSON.parseArray(msg.obj.toString());
                        if(array != null && !array.isEmpty()){
                            context.pageIndex = pageIndex;
                            array.addAll(0,context.recommendArr);
                            context.recommendArr = array;
                            context.recommendAdapter.update(array);
                        } else {
//                            TipUtil.showShort(context.getActivity(), "已经是最后一页");
                        }
                    }
                    context.onLoad();
                } else if(msg.what == C.Handler.LOAD_CENTER_LIST){
                    int pageIndex = msg.arg1;
                    if(pageIndex <= 1){
                        context.pageIndex = 1;
                        context.centerArr = JSON.parseArray(msg.obj.toString());
                        if(context.centerAdapter == null){
                            context.centerAdapter = new DynamicAdapter2(context.getActivity(), context.centerArr);
                            context.listContent.setAdapter(context.centerAdapter);
                        } else {
                            context.centerAdapter.update(context.centerArr);
                        }
                    } else {
                        JSONArray array = JSON.parseArray(msg.obj.toString());
                        if(array != null && !array.isEmpty()){
                            context.pageIndex = pageIndex;
                            array.addAll(context.centerArr);
                            context.centerArr = array;
                            context.centerAdapter.update(array);
                        }
                    }
                    context.onLoad();
                    context.switchTab(context.btnMsgCenter, "checked");
                    context.switchTab(context.btnMsgRecommend, "uncheck");
                } else {
                    TipUtil.showShort(context.getActivity(), msg);
                }
            } catch (Exception e) {
                Log.e(C.TAG, "系统错误");
            }
        }
    }

    @Override
    public void onRefresh() {
        if(type == 1){
            service.loadCenterData(getActivity(), 1, pageSize, handler);
        } else {
            service.loadRecommondData(getActivity(), 1, pageSize, handler);
        }
    }

    @Override
    public void onLoadMore() {
        if(type == 1){
            service.loadCenterData(getActivity(), pageIndex + 1, pageSize, handler);
        } else {
            service.loadRecommondData(getActivity(), pageIndex + 1, pageSize, handler);
        }
    }

    private void onLoad() {
        listContent.stopRefresh();
        listContent.stopLoadMore();
        listContent.setRefreshTime(formatTime(new Date()));
    }

    @SuppressLint("SimpleDateFormat")
    public String formatTime(Date date){
        SimpleDateFormat format = new SimpleDateFormat("今天 HH:mm");  ;
        return format.format(date);
    }

    public int getDay(Date date){
        GregorianCalendar d = new GregorianCalendar();
        int currDay = d.get(Calendar.DAY_OF_YEAR);

        d.setTime(date);

        int oldDay = d.get(Calendar.DAY_OF_YEAR);

        return currDay - oldDay;
    }

}
