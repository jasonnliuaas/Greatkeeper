package com.newline.housekeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.SPUtil;

import butterknife.InjectView;

@ContentView(R.layout.currency_layout)
public class CurrencyActivity extends BaseActivity {

    @ViewInject(R.id.btnUSDCurrency)
    RelativeLayout btnUSDCurrency;
    @ViewInject(R.id.btnAUDCurrency)
    RelativeLayout btnAUDCurrency;
    @ViewInject(R.id.btnCADCurrency)
    RelativeLayout btnCadCurrency;
    @ViewInject(R.id.btnGbpCurrency)
    RelativeLayout btnGbpCurrency;
    @ViewInject(R.id.textTopTitle)
    private TextView mTextTopTitle;
    String nowCurrName;

    @ViewInject(R.id.textNowCurrencyName)
    private TextView textNowCurrencyName;

    @ViewInject(R.id.textNowCurrencyAd)
    private TextView textNowCurrencyAd;

    @ViewInject(R.id.imgNowCurreny)
    private ImageView imgNowCurreny;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        mTextTopTitle.setText(getString(R.string.currency));

        setCurrency(C.Currency.nowCurrency);
    }

    private synchronized void setCurrency(int nowCurrency) {
        nowCurrName = "";
        String nowCurrAd = "";
        int nowIcon = R.drawable.ic_currency_usd;
        switch (nowCurrency){
            case C.Currency.AUD://澳元
                nowCurrName = getString(R.string.AUD);
                nowCurrAd = "AUD";
                nowIcon = R.drawable.ic_currency_aud;
                break;
            case C.Currency.CAD: //加元
                nowCurrName = getString(R.string.CAD);
                nowCurrAd = "CAD";
                nowIcon = R.drawable.ic_currency_cad;
                break;
            case C.Currency.USD: //美元
                nowCurrName = getString(R.string.USD);
                nowIcon = R.drawable.ic_currency_usd;
                nowCurrAd = "USD";
                break;
            case C.Currency.GBP: //英镑
                nowCurrName = getString(R.string.GBP);
                nowIcon = R.drawable.ic_currency_gbp;
                nowCurrAd = "GBP";
                break;
        }
        textNowCurrencyName.setText(nowCurrName);
        textNowCurrencyAd.setText(nowCurrAd);
        imgNowCurreny.setImageResource(nowIcon);
        C.Currency.nowCurrency = nowCurrency;
        SPUtil spUtil = SPUtil.getUtil(this);
        spUtil.putInt(C.CURRENCY, nowCurrency);
    }

    @Override
    public void goBack(View view) {
        Intent intent = new Intent();
        intent.putExtra("nowCurrName",nowCurrName);
        setResult(RESULT_OK,intent);
        finish();
    }

    @OnClick({R.id.btnAUDCurrency,R.id.btnUSDCurrency,R.id.btnCADCurrency,R.id.btnGbpCurrency})
    public void onCurrencyClick(View view) {
        switch (view.getId()){
            case R.id.btnAUDCurrency:
                if(C.Currency.nowCurrency !=C.Currency.AUD){
                    setCurrency(C.Currency.AUD);
                }
                break;
            case R.id.btnUSDCurrency:
                if(C.Currency.nowCurrency !=C.Currency.USD){
                setCurrency(C.Currency.USD);
            }
                break;
            case R.id.btnCADCurrency:
                if(C.Currency.nowCurrency !=C.Currency.CAD){
                    setCurrency(C.Currency.CAD);
                }
                break;
            case R.id.btnGbpCurrency:
                if(C.Currency.nowCurrency !=C.Currency.GBP){
                    setCurrency(C.Currency.GBP);
                }
                break;
        }
    }

}
