package com.newline.housekeeper.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatkeeper.keeper.R;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.SPUtil;
import com.newline.core.utils.TipUtil;
import com.newline.core.utils.VerifyUtils;
import com.newline.housekeeper.HttpRequest;
import com.newline.housekeeper.KeeperUrl;
import com.newline.housekeeper.Param;
import com.newline.housekeeper.control.CommonDialog;
import com.newline.housekeeper.control.SuccessDialog;
import com.newline.housekeeper.model.CityBean;
import com.newline.housekeeper.model.CountryBean;
import com.newline.housekeeper.service.UserService;
import com.newline.housekeeper.view.ViewMiddle;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class TaxApplyActivity extends BaseActivity implements View.OnFocusChangeListener{

    @InjectView(R.id.textTopTitle)
    TextView textTopTitle;
    @InjectView(R.id.apply_et_name)
    EditText applyEtName;
    @InjectView(R.id.apply_et_email)
    EditText applyEtEmail;
    @InjectView(R.id.apply_et_phone)
    EditText applyEtPhone;
    @InjectView(R.id.apply_tv_area)
    EditText apply_tv_area;
    @InjectView(R.id.bt_taxsubmit)
    Button btTaxsubmit;
    @InjectView(R.id.til_name)
    LinearLayout til_name;
    @InjectView(R.id.til_email)
    LinearLayout til_email;
    @InjectView(R.id.til_area)
    LinearLayout til_area;
    @InjectView(R.id.til_phone)
    LinearLayout til_phone;
    private int displayWidth;
    private int displayHeight;
    String country;
    String city;
    int[] location;
    private String uid;
    SweetAlertDialog dialog;
    InputMethodManager imm;
    @OnClick({R.id.bt_taxsubmit,R.id.apply_tv_area})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_taxsubmit:
            String name = applyEtName.getText().toString();
            email = applyEtEmail.getText().toString();
            String phone =applyEtPhone.getText().toString();
            String area = apply_tv_area.getText().toString();
            if(!VerifyUtils.isEmail(email)){
                Toast.makeText(this,getString(R.string.error_email),Toast.LENGTH_SHORT).show();
                return;
            }
            if(name.isEmpty()){
                Toast.makeText(this,getString(R.string.name_tips),Toast.LENGTH_SHORT).show();
                return;
            }
            if(!VerifyUtils.isPhone(phone)){
                Toast.makeText(this,getString(R.string.phone_tips),Toast.LENGTH_SHORT).show();
                return;
            }
            if(area.isEmpty()){
                TipUtil.showShort(this,getString(R.string.country_tips));
                return;
            }
                setBaoshui(TaxApplyActivity.this, name, phone, email, country, city, type, "", "", "0");
                break;
            case R.id.apply_tv_area:
                if (Popup == null) {
                    location =  new int[2];
                    til_area.getLocationOnScreen(location);
                    Popup = new PopupWindow(vm,displayWidth,displayHeight-location[1]);
                    Popup.setAnimationStyle(R.style.ActionSheetDialogAnimation);
                    vm.setOnSelectListener(new ViewMiddle.OnSelectListener(){
                        @Override
                        public void getValue(String showText, int countrypostion, int cityposition) {
                            CountryBean cBean = countries.get(countrypostion);
                            country = cBean.getCountryname();
                            city = cBean.getCities().isEmpty()?"":cBean.getCities().get(cityposition).getCityname();
                            if(Popup.isShowing()){
                                Popup.dismiss();
                            }
                            apply_tv_area.setText(country+"   "+city);
                        }
                    });
                    Popup.setFocusable(false);
                    Popup.setOutsideTouchable(true);
                    Popup.setContentView(vm);
                    if(!Popup.isShowing()){
                        Popup.showAtLocation(til_area, Gravity.BOTTOM, 0, 0);
                    }
                    else{
                        Popup.dismiss();
                    }
                }else{
                    if(!Popup.isShowing()){
                        Popup.showAtLocation(til_area, Gravity.BOTTOM, 0, 0);
                    }else{
                        Popup.dismiss();
                    }
                }
                break;
        }
        }
    private List<CountryBean> countries = new ArrayList<CountryBean>();
    private ViewMiddle vm;
    private PopupWindow Popup;
    private UIHandler handler;
    private String mobile;
    private String email;
    private UserService userService;
    String typeid;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_apply);
        ButterKnife.inject(this);
        type = getIntent().getStringExtra("type");
        typeid = getIntent().getStringExtra("typeid");
        userService = UserService.getService(this);
        uid = SPUtil.getUtil(this).getString(C.UserIdKey);
        initView();
        applyEtName.setOnFocusChangeListener(this);
        applyEtPhone.setOnFocusChangeListener(this);
        applyEtEmail.setOnFocusChangeListener(this);
        apply_tv_area.setOnFocusChangeListener(this);
        displayWidth = getWindowManager().getDefaultDisplay().getWidth();
        displayHeight =getWindowManager().getDefaultDisplay().getHeight();
        textTopTitle.setText(type);

    }

    public void initView() {
        handler = new UIHandler(this);
        dialog =new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE);
        dialog.setTitleText(getString(R.string.System_prompt))
                .setContentText(getString(R.string.loan_apply_tip));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                TaxApplyActivity.this.finish();
            }
        });
        vm = new ViewMiddle(this,countries);
        vm.setBackgroundColor(Color.parseColor("#EA6318"));
        if(imm == null){
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(apply_tv_area.getWindowToken(), 0);
            apply_tv_area.setInputType(0);
        }
        userService.doGetAccountBind(this, handler);
        if(countries.isEmpty()){
            loadParams(this);
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(b){
            switch (view.getId()){
                case R.id.apply_et_name:
                case R.id.apply_et_phone:
                case R.id.apply_et_email:
                    if(Popup!=null&&Popup.isShowing()){
                        Popup.dismiss();
                    }
                    break;
                case R.id.apply_tv_area:
                    if (Popup == null) {
                        location =  new int[2];
                        til_area.getLocationOnScreen(location);
                        Popup = new PopupWindow(vm,displayWidth,displayHeight-location[1]);
                        Popup.setAnimationStyle(R.style.ActionSheetDialogAnimation);
                        vm.setOnSelectListener(new ViewMiddle.OnSelectListener(){
                            @Override
                            public void getValue(String showText, int countrypostion, int cityposition) {
                                CountryBean cBean = countries.get(countrypostion);
                                country = cBean.getCountryname();
                                city = cBean.getCities().isEmpty()?"":cBean.getCities().get(cityposition).getCityname();
                                if(Popup.isShowing()){
                                    Popup.dismiss();
                                }
                                apply_tv_area.setText(country+"   "+city);
                            }
                        });
                        Popup.setFocusable(false);
                        Popup.setOutsideTouchable(true);
                        Popup.setContentView(vm);
                        if(!Popup.isShowing()){
                            Popup.showAtLocation(til_area, Gravity.BOTTOM, 0, 0);
                        }
                        else{
                            Popup.dismiss();
                        }
                    }else{
                        if(!Popup.isShowing()){
                            Popup.showAtLocation(til_area, Gravity.BOTTOM, 0, 0);
                        }else{
                            Popup.dismiss();
                        }
                    }

            }

        }
    }

    static class UIHandler extends Handler {
        private WeakReference<TaxApplyActivity> mActivity;

        public UIHandler(TaxApplyActivity activity) {
            mActivity = new WeakReference<TaxApplyActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final TaxApplyActivity context = mActivity.get();

            if (msg.what == C.Handler.LOAD_ACCOUNT_BIND) {
                JSONObject data = JSON.parseObject((String) msg.obj);
                context.mobile = data.getString("mobile");
                context.email = data.getString("email");
                context.applyEtEmail.setText(context.email);
                context.applyEtPhone.setText(context.mobile);
            }else if (msg.what == C.Handler.lOADPARAMS) {
                context.parseJson(msg.obj.toString());
                context.vm.update();
            }else if(msg.what == C.Handler.APPLY_SUCCESS){
                   /* String title = "成功提示";
                    String content = "申请成功!";

                    SuccessDialog dialog = new SuccessDialog(context, title, content);
                    dialog.show();
                    dialog.setSuccessCallBack(new SuccessDialog.CallBack() {
                        @Override
                        public void doCallBack() {
                            context.finish();
                        }
                    });*/
                 context.dialog.show();
            }
            else {
                TipUtil.showShort(context, msg);
            }
        }
    }

    public void parseJson(String data) {
        JSONObject jsonobj = JSON.parseObject(data);
        JSONArray countries = jsonobj.getJSONArray("city");
        if (TaxApplyActivity.this.countries.isEmpty()) {
            for (int i = 0; i < countries.size(); i++) {
                JSONObject country = countries.getJSONObject(i);
                CountryBean countryb = new CountryBean();
                String countryid = country.getString("countryid");
                String countryname = country.getString("countryname");
                countryb.setCountryid(countryid);
                countryb.setCountryname(countryname);
                List<CityBean> listcity = new ArrayList<CityBean>();
                JSONArray jsoncities = country.getJSONArray("citys");
                for (int j = 0; j < jsoncities.size(); j++) {
                    CityBean city = new CityBean();
                    city.setCountryid(countryid);
                    city.setCityid(jsoncities.getJSONObject(j).getString("cityid"));
                    city.setCityname(jsoncities.getJSONObject(j).getString("cityname"));
                    listcity.add(city);
                }
                countryb.setCities(listcity);
                TaxApplyActivity.this.countries.add(countryb);
            }
        }

    }
    public void loadParams(final Context context){
        String data = SPUtil.getUtil(context).getString(C.CountryData);
        if(data == null || data.equals("")){
            HttpRequest.getInstance().asynRequest(context, KeeperUrl.GetSaleListParam, new HttpRequest.RequestCallBack() {
                @Override
                public void doCallBack(int code, String dataMsg, String data) {
                    if(C.Code.OK == code){
                        handler.obtainMessage(C.Handler.lOADPARAMS, data).sendToTarget();
                        SPUtil.getUtil(context).putString(C.CountryData,data);
                    } else {
                        //TipUtil.showShort(HouseSaleListActivity.this, "系统出错");
                        Log.i(C.TAG, "列表为空");
                    }
                }

            });
        }else{
            handler.obtainMessage(C.Handler.lOADPARAMS, data).sendToTarget();
        }
    }

    public void setBaoshui(Context context,String name,String mobile,String email,String country,String city,String type,String price,String scale,String other_assets){
        HttpRequest.getInstance().asynRequest(context, KeeperUrl.SetBaoshui, new HttpRequest.RequestCallBack() {
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                if(C.Code.OK == code){
                    handler.obtainMessage(C.Handler.APPLY_SUCCESS, data).sendToTarget();
                } else {
                    //TipUtil.showShort(HouseSaleListActivity.this, "系统出错");
                    Log.i(C.TAG, "dataMsg");
                }
            }

        },new Param("uid",uid),
                new Param("name",name),
                new Param("mobile",mobile),
                new Param("emai",email),
                new Param("country",country),
                new Param("city",city),
                new Param("price",price),
                new Param("type",type),
                new Param("scale",scale)
                ,new Param("other_assets",other_assets));
    }
}

