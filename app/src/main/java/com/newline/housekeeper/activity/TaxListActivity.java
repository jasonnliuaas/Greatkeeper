package com.newline.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.newline.housekeeper.service.TaxService;
import java.lang.ref.WeakReference;

@ContentView(R.layout.layout_taxlist)
public class TaxListActivity extends BaseActivity implements OnClickListener {
    @ViewInject(R.id.tv_tips)
    TextView tvTips;
    @ViewInject(R.id.textTopTitle)
    private TextView mTopTitle;

    @ViewInject(R.id.textDate)
    private TextView mTextDate;

    @ViewInject(R.id.bt_taxnum)
    private Button bt_taxnum;

    @ViewInject(R.id.bt_tax)
    private Button bt_tax;

    @ViewInject(R.id.listTax)
    private ListView mListTax;

    @ViewInject(R.id.taxEmptyLayout)
    private LinearLayout taxEmptyLayout;

    @ViewInject(R.id.layout_button)
    private LinearLayout layout_button;


    @ViewInject(R.id.textEmptyTip)
    private LinearLayout mTextEmptyTip;

    @ViewInject(R.id.btn_gologin)
    private LinearLayout btn_gologin;
    private ApplyAdapter adapter;
    private TaxService service;
    private UIHandler handler;

    static class UIHandler extends Handler {
        private WeakReference<TaxListActivity> weakRef;

        public UIHandler(TaxListActivity refObj) {
            weakRef = new WeakReference<TaxListActivity>(refObj);
        }

        @Override
        public void handleMessage(Message msg) {
            TaxListActivity context = weakRef.get();
            if (msg.what == C.Handler.LOAD_TAX_LIST) {
                JSONArray dataArray = null;
                try {
                    dataArray = JSON.parseArray(msg.obj.toString());

                    if (dataArray == null || dataArray.isEmpty()) {
                        context.taxEmptyLayout.setVisibility(View.VISIBLE);
                        context.tvTips.setText(context.getString(R.string.tax_tips));
                        context.mListTax.setVisibility(View.GONE);
                    } else {
                        context.mListTax.setVisibility(View.VISIBLE);
                        context.taxEmptyLayout.setVisibility(View.GONE);
                        if (context.adapter == null) {
                            context.adapter = new ApplyAdapter(context, dataArray, ApplyAdapter.APPLY_TAX);
                            context.mListTax.setAdapter(context.adapter);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        mTopTitle.setText(R.string.homeText07);
        initView();
    }

    public void initView() {
        handler = new UIHandler(this);
        bt_taxnum.setOnClickListener(this);
        bt_tax.setOnClickListener(this);
        service = TaxService.getService(this);
        if (service.isLogined()) {
            btn_gologin.setVisibility(View.GONE);
            layout_button.setVisibility(View.VISIBLE);
            mListTax.setVisibility(View.VISIBLE);
            mListTax.setFooterDividersEnabled(false);
            service.loadTaxData(this,handler);
        } else {

            String data = DemoData.taxList();
            Message message =handler.obtainMessage();
            message.obj = data;
            message.what = C.Handler.LOAD_TAX_LIST;
            message.sendToTarget();
            taxEmptyLayout.setVisibility(View.GONE);
            tvTips.setVisibility(View.GONE);
            mListTax.setVisibility(View.VISIBLE);
            layout_button.setVisibility(View.GONE);
            btn_gologin.setVisibility(View.VISIBLE);
        }
        mListTax.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(TaxListActivity.this, TaxApplyDetailsActivity.class);
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
        String type = null;
        String typeid = null;
        switch (v.getId()) {
            case R.id.bt_taxnum:
                //税号申请
                type = getString(R.string.taxnum_apply);
                typeid = "1";
                break;
            case R.id.bt_tax:
                //报税申请
                type = getString(R.string.tax_apply);
                typeid = "2";
                break;
        }
        Intent intent = new Intent(TaxListActivity.this, TaxApplyActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("typeid", typeid);
        startActivity(intent);

    }

}
