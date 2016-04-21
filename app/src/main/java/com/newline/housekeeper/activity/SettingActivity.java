package com.newline.housekeeper.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.LanguageUtils;
import com.newline.core.utils.SPUtil;
import com.newline.housekeeper.MainActivity;

import java.util.Locale;

import butterknife.InjectView;

@ContentView(R.layout.layout_setting)
public class SettingActivity extends BaseActivity {

    @ViewInject(R.id.textTopTitle)
    TextView textTopTitle;
    @ViewInject(R.id.layoutBtnLanguage)
    RelativeLayout layoutBtnLanguage;
    @ViewInject(R.id.layoutBtnBizhong)
    RelativeLayout layoutBtnBizhong;
    @ViewInject(R.id.tv_language)
    TextView tvLanguage;
    @ViewInject(R.id.tv_current)
    TextView tvCurrent;
    SPUtil spUtil = SPUtil.getUtil(this);


    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        textTopTitle.setText(getString(R.string.setting));
        initView();
    }

    public void initView(){
        String country = spUtil.getString("country");
        int currency = spUtil.getInt(C.CURRENCY);
        switch (currency){
            case C.Currency.AUD:
                tvCurrent.setText(getString(R.string.AUD));
                break;
            case C.Currency.CAD:
                tvCurrent.setText(getString(R.string.CAD));
                break;
            case C.Currency.GBP:
                tvCurrent.setText(getString(R.string.GBP));
                break;
            case C.Currency.USD:
                tvCurrent.setText(getString(R.string.USD));
                break;

        }
        if(country.equals("zh")){
            tvLanguage.setText(getString(R.string.simplified_Chinese));
        }else if(country.equals("en")){
            tvLanguage.setText(getString(R.string.English));
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutBtnBizhong:
                startActivityForResult(new Intent(this,CurrencyActivity.class),1);
                break;
            case R.id.layoutBtnLanguage:
               // startActivityForResult(new Intent(this,LanguageActivity.class),2);
               startActivityForResult(new Intent(this,LanguageActivity.class),2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 2){

                Locale locale;
                int language =data.getIntExtra("language",LanguageActivity.LANGUAGE_CN);
                String country = data.getStringExtra("country");
                if(language == LanguageActivity.LANGUAGE_EN){
                    locale = Locale.US;

                }else{
                    locale = Locale.SIMPLIFIED_CHINESE;
                }
                if(locale!=null){
                    SPUtil.getUtil(this).putObject("language",locale);
                    SPUtil.getUtil(this).putString("country",country);
                    Configuration config = getResources().getConfiguration();// 获得设置对象
                    Resources resources = getResources();// 获得res资源对象
                    DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
                    config.locale = locale; //
                    LanguageUtils.isChinese(this);
                    resources.updateConfiguration(config, dm);
                    Intent it = new Intent(this, MainActivity.class); //MainActivity是你想要重启的activity
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it);
                }
            }
            else if(requestCode ==1){
                String nowCurrName = data.getStringExtra("nowCurrName");
                tvCurrent.setText(nowCurrName);
            }
        }
    }
}
