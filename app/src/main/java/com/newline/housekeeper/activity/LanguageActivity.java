package com.newline.housekeeper.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.newline.core.BaseActivity;

import java.util.Locale;

import butterknife.InjectView;

@ContentView(R.layout.layout_language)
public class LanguageActivity extends BaseActivity {

    @ViewInject(R.id.textTopTitle)
    TextView textTopTitle;
    @ViewInject(R.id.tv_save)
    TextView tvSave;
    @ViewInject(R.id.rb_lgcn)
    RadioButton rbLgcn;
    @ViewInject(R.id.rb_lgen)
    RadioButton rbLgen;
    @ViewInject(R.id.rg_language)
    RadioGroup rgLanguage;
    Resources resource;
    Configuration config;
    String country;
    String lg;
    Locale defualt;
    public  static  final  int LANGUAGE_EN = 1;
    public  static  final  int LANGUAGE_CN = 2;
    private int language;
    @OnClick(R.id.tv_save)
    public void onClick(View view){
        Intent data =new Intent();
        data.putExtra("language",language);
        data.putExtra("country",lg);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        textTopTitle.setText(getString(R.string.language));
        resource = getResources();
        config = resource.getConfiguration();
        defualt = config.locale;
        country = defualt.getLanguage();
        initView();
        rgLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_lgcn:
                        if(country.endsWith("en")){
                            tvSave.setVisibility(View.VISIBLE);
                            language = LANGUAGE_CN;
                            lg = "zh";
                        }else{
                            tvSave.setVisibility(View.GONE);
                        }
                        break;

                    case R.id.rb_lgen:
                        if(country.endsWith("zh")){
                            tvSave.setVisibility(View.VISIBLE);
                            language =LANGUAGE_EN;
                            lg = "en";
                        }else{
                            tvSave.setVisibility(View.GONE);
                        }
                        break;
                }
            }
        });
    }
    public void initView(){
        tvSave.setVisibility(View.GONE);
        if(country.equals("zh")){
            rbLgcn.setChecked(true);
            language = LANGUAGE_CN;
        }else if(country.equals("en")){
            rbLgen.setChecked(true);
            language =LANGUAGE_EN;
        }
    }
}
