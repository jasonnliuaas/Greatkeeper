package com.newline.housekeeper.activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatkeeper.keeper.R;
import com.lidroid.xutils.util.LogUtils;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.SPUtil;
import com.newline.core.utils.TipUtil;
import com.newline.core.utils.VerifyUtils;
import com.newline.housekeeper.HttpRequest;
import com.newline.housekeeper.KeeperUrl;
import com.newline.housekeeper.Param;
import com.newline.housekeeper.control.CommonDialog;
import com.newline.housekeeper.model.CityBean;
import com.newline.housekeeper.model.CountryBean;
import com.newline.housekeeper.service.UserService;
import com.newline.housekeeper.view.ViewMiddle;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoanApplyActivity extends BaseActivity implements View.OnFocusChangeListener{
	private TextView textTopTitle;
    private UserService userService;
    private String mobile;
    private String email;
    private int displayWidth;
    private int displayHeight;
    private  UIHandler handler;
    @InjectView(R.id.apply_et_name)
    EditText apply_et_name;

    @InjectView(R.id.layout_address)
    LinearLayout layout_address;

    @InjectView(R.id.bt_loansubmit)
    Button bt_loansubmit;
    @InjectView(R.id.apply_et_phone)
    EditText apply_et_phone;
    @InjectView(R.id.apply_et_total)
    EditText apply_et_total;

    @InjectView(R.id.apply_et_email)
    EditText apply_et_email;

    @InjectView(R.id.apply_et_bili)
    EditText apply_et_bili;
    @InjectView(R.id.apply_et_address)
    EditText apply_et_address;
    SweetAlertDialog dialog;
    private String uid; private List<CountryBean> countries = new ArrayList<CountryBean>();
    private ViewMiddle vm;
    private PopupWindow Popup;
    private String country;
    private String city;
    int[] location;
    private DialogInterface.OnDismissListener onDismissListener = null;
    private DialogInterface.OnShowListener onShowListener = null;
    InputMethodManager imm;


    @OnClick({R.id.bt_loansubmit,R.id.apply_et_address})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_loansubmit:
                String name = apply_et_name.getText().toString();
                email = apply_et_email.getText().toString();
                String phone =apply_et_phone.getText().toString();
                String area = apply_et_address.getText().toString();
                String scale = apply_et_bili.getText().toString();
                String price = apply_et_total.getText().toString();
                if(!VerifyUtils.isEmail(email)){
                    Toast.makeText(this,getString(R.string.error_email),Toast.LENGTH_SHORT).show();
                    return;
                }
                if(name.isEmpty()){
                    Toast.makeText(this,getString(R.string.name_tips),Toast.LENGTH_SHORT).show();
                    return;
                }
                if(scale.isEmpty()){
                    Toast.makeText(this,getString(R.string.scale_tips),Toast.LENGTH_SHORT).show();
                    return;
                }
                if(price.isEmpty()){
                    Toast.makeText(this,getString(R.string.asset_tips),Toast.LENGTH_SHORT).show();
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
                setDaikuan(LoanApplyActivity.this, name, phone, email, country, city, price,scale, "0");
                dialog.show();
                break;
            case R.id.apply_et_address:
                if (Popup == null) {
                    location =  new int[2];
                    layout_address.getLocationOnScreen(location);
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
                            apply_et_address.setText(country+"   "+city);
                        }
                    });
                    Popup.setFocusable(false);
                    Popup.setOutsideTouchable(true);
                    Popup.setContentView(vm);
                    if(!Popup.isShowing()){
                        Popup.showAtLocation(layout_address, Gravity.BOTTOM, 0, 0);
                    }
                    else{
                        Popup.dismiss();
                    }
                }else{
                    if(!Popup.isShowing()){
                        Popup.showAtLocation(layout_address, Gravity.BOTTOM, 0, 0);
                    }else{
                        Popup.dismiss();
                    }
                }
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i("APPLY ONRESUME");
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loan_apply);
        ButterKnife.inject(this);
        handler = new UIHandler(this);
		initView();
        LogUtils.i("APPLY ONCREATE");
		textTopTitle.setText(getString(R.string.loan_apply));
        userService = UserService.getService(this);
        userService.doGetAccountBind(this,handler);
        uid = SPUtil.getUtil(this).getString(C.UserIdKey);
	}

	public void initView(){
        displayWidth = getWindowManager().getDefaultDisplay().getWidth();
        displayHeight =getWindowManager().getDefaultDisplay().getHeight();
		textTopTitle = (TextView) findViewById(R.id.textTopTitle);
        vm = new ViewMiddle(this,countries);
        apply_et_name.setOnFocusChangeListener(this);
        apply_et_phone.setOnFocusChangeListener(this);
        apply_et_address.setOnFocusChangeListener(this);
        if(imm == null){
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(apply_et_address.getWindowToken(), 0);
            apply_et_address.setInputType(0);
        }

        apply_et_email.setOnFocusChangeListener(this);
        apply_et_bili.setOnFocusChangeListener(this);
        apply_et_total.setOnFocusChangeListener(this);
        vm.setBackgroundColor(Color.parseColor("#EA6318"));
        if(countries.isEmpty()){
            loadParams(this);
        }
          if(dialog==null) {
              dialog =new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE);
              dialog.setTitleText(getString(R.string.System_prompt))
                      .setContentText(getString(R.string.loan_apply_tip));
          }
        onShowListener = new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                hideKeybord();
            }
        };
        onDismissListener = new DialogInterface.OnDismissListener(){

            @Override
            public void onDismiss(DialogInterface dialog) {
                LoanApplyActivity.super.onBackPressed();
            }
        };
        dialog.setOnDismissListener(onDismissListener);
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
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            switch (v.getId()){
                case R.id.apply_et_name:
                case R.id.apply_et_phone:
                case R.id.apply_et_email:
                case R.id.apply_et_bili:
                case R.id.apply_et_total:
                    if(Popup!=null&&Popup.isShowing()){
                        Popup.dismiss();
                    }
                    break;
                case R.id.apply_et_address:
                    if (Popup == null) {
                        location =  new int[2];
                        layout_address.getLocationOnScreen(location);
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
                                apply_et_address.setText(country+"   "+city);
                            }
                        });
                        Popup.setFocusable(false);
                        Popup.setOutsideTouchable(true);
                        Popup.setContentView(vm);
                        if(!Popup.isShowing()){
                            Popup.showAtLocation(layout_address, Gravity.BOTTOM, 0, 0);
                        }
                        else{
                            Popup.dismiss();
                        }
                    }else{
                        if(!Popup.isShowing()){
                            Popup.showAtLocation(layout_address, Gravity.BOTTOM, 0, 0);
                        }else{
                            Popup.dismiss();
                        }
                    }

            }

        }
    }
    public void hideKeybord(){
        if(imm == null){
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        if(imm.isActive()){
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    static class UIHandler extends Handler {
        private WeakReference<LoanApplyActivity> mActivity;

        public UIHandler(LoanApplyActivity activity) {
            mActivity = new WeakReference<LoanApplyActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final LoanApplyActivity context = mActivity.get();
            if (msg.what == C.Handler.LOAD_ACCOUNT_BIND) {
                JSONObject data = JSON.parseObject((String) msg.obj);
                context.mobile = data.getString("mobile");
                context.email = data.getString("email");
                context.apply_et_email.setText(context.email);
                context.apply_et_phone.setText(context.mobile);
            }else if (msg.what == C.Handler.lOADPARAMS) {
                context.parseJson(msg.obj.toString());
                context.vm.update();
            }else if(msg.what == C.Handler.APPLY_SUCCESS){
                /*String title = "成功提示";
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
        if (LoanApplyActivity.this.countries.isEmpty()) {
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
                LoanApplyActivity.this.countries.add(countryb);
            }
        }

    }

    public void setDaikuan(Context context,String name,String mobile,String email,String country,String city,String price,String scale,String other_assets){
        HttpRequest.getInstance().asynRequest(context, KeeperUrl.SetDaiKuan, new HttpRequest.RequestCallBack() {
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
                new Param("scale",scale));
    }



}
