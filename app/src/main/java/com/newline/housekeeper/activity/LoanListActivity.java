package com.newline.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.TipUtil;
import com.newline.housekeeper.DemoData;
import com.newline.housekeeper.model.ApplyAdapter;
import com.newline.housekeeper.service.LoanService;
import com.newline.housekeeper.service.UserService;

import java.lang.ref.WeakReference;

import butterknife.InjectView;

@ContentView(R.layout.layout_loanlist)
public class LoanListActivity extends BaseActivity implements OnClickListener {
    @ViewInject(R.id.tv_tips)
    TextView tvTips;
    private LoanService service;
    @ViewInject(R.id.textTopTitle)
    private TextView mTopTitle;
    @ViewInject(R.id.textDate)
    private TextView mTextDate;

    @ViewInject(R.id.listLoan)
    private ListView listLoan;

    @ViewInject(R.id.loanEmptyLayout)
    private LinearLayout loanEmptyLayout;

    @ViewInject(R.id.textEmptyTip)
    private LinearLayout mTextEmptyTip;

    @ViewInject(R.id.btn_gologin)
    private LinearLayout btn_gologin;

    @ViewInject(R.id.bt_loanapply)
    private Button bt_loanapply;
    private ApplyAdapter adapter;
    private UIHandler handler;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        mTopTitle.setText(R.string.homeText08);
        initView();
        handler = new UIHandler(this);

    }

    static class UIHandler extends Handler {
        private WeakReference<LoanListActivity> weakRef;

        public UIHandler(LoanListActivity refObj) {
            weakRef = new WeakReference<LoanListActivity>(refObj);
        }

        @Override
        public void handleMessage(Message msg) {
            LoanListActivity context = weakRef.get();
            if (msg.what == C.Handler.LOAD_LOAN_LIST) {
                JSONArray dataArray = null;
                try {
                    dataArray = JSON.parseArray(msg.obj.toString());

                    if (dataArray == null || dataArray.isEmpty()) {
                        context.loanEmptyLayout.setVisibility(View.VISIBLE);
                        context.tvTips.setText(context.getString(R.string.loan_tips));
                        context.listLoan.setVisibility(View.GONE);
                    } else {
                        context.listLoan.setVisibility(View.VISIBLE);
                        context.loanEmptyLayout.setVisibility(View.GONE);
                        if (context.adapter == null) {
                            context.adapter = new ApplyAdapter(context, dataArray, ApplyAdapter.APPLY_LOAN);
                            context.listLoan.setAdapter(context.adapter);
                        } else {
                            context.adapter.updateList(dataArray);
                        }
                    }
                } catch (Exception e) {
                    LogUtils.e("加载租金明细列表错误", e);
                }
            } else {
                TipUtil.showShort(context, msg);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (service.isLogined()) {
            btn_gologin.setVisibility(View.GONE);
            bt_loanapply.setVisibility(View.VISIBLE);
            listLoan.setVisibility(View.VISIBLE);
            listLoan.setFooterDividersEnabled(false);
            service.loadLoanData(this, handler);
        } else {
            loanEmptyLayout.setVisibility(View.GONE);
            tvTips.setVisibility(View.GONE);
            String data = DemoData.loanList();
            Message message =handler.obtainMessage();
            message.obj = data;
            message.what = C.Handler.LOAD_LOAN_LIST;
            message.sendToTarget();
            listLoan.setVisibility(View.VISIBLE);
            bt_loanapply.setVisibility(View.GONE);
            btn_gologin.setVisibility(View.VISIBLE);
        }
    }

    public void initView() {
        handler = new UIHandler(this);
        bt_loanapply.setOnClickListener(this);
        service = LoanService.getService(this);
        listLoan.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(LoanListActivity.this, LoanApplyDetailsActivity.class);
                TextView text_city = (TextView) view.findViewById(R.id.text_city);
                TextView text_country = (TextView) view.findViewById(R.id.text_country);
                TextView text_name = (TextView) view.findViewById(R.id.text_name);
                TextView text_price = (TextView) view.findViewById(R.id.text_price);
                TextView text_scale = (TextView) view.findViewById(R.id.text_scale);
                TextView mTextTaxDate = (TextView) view.findViewById(R.id.textTaxDate);
                TextView mTextTaxState = (TextView) view.findViewById(R.id.textTaxstate);
                TextView mTextBaoshuiId = (TextView) view.findViewById(R.id.baoshuiid);
                TextView mTextTaxMsg = (TextView) view.findViewById(R.id.textTaxMsg);
                Bundle bundle = new Bundle();
                bundle.putString("name", text_name.getText().toString());
                bundle.putString("time", mTextTaxDate.getText().toString());
                bundle.putString("state", mTextTaxState.getText().toString());
                bundle.putString("id", mTextBaoshuiId.getText().toString());
                bundle.putString("type", mTextTaxMsg.getText().toString());
                bundle.putString("country", text_country.getText().toString());
                bundle.putString("city", text_city.getText().toString());
                bundle.putString("price", text_price.getText().toString());
                bundle.putString("scale", text_scale.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_loanapply:
                Intent intent = new Intent(LoanListActivity.this, LoanApplyActivity.class);
                startActivity(intent);
                break;
        }

    }

}
