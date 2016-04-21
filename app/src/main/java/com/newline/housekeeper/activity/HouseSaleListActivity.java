package com.newline.housekeeper.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatkeeper.keeper.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.util.LogUtils;
import com.newline.C;
import com.newline.core.BaseActivity;
import com.newline.core.utils.SPUtil;
import com.newline.core.utils.TipUtil;
import com.newline.core.utils.UmShareUtils;
import com.newline.housekeeper.KeeperUrl;
import com.newline.housekeeper.Param;
import com.newline.housekeeper.HttpRequest;
import com.newline.housekeeper.HttpRequest.RequestCallBack;
import com.newline.housekeeper.control.LoadingDialog;
import com.newline.housekeeper.model.CityBean;
import com.newline.housekeeper.model.CountryBean;
import com.newline.housekeeper.model.HouseSaleAdapter;
import com.newline.housekeeper.view.ExpandTabView;
import com.newline.housekeeper.view.ViewLeft;
import com.newline.housekeeper.view.ViewMiddle;
import com.umeng.socialize.media.UMImage;

import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HouseSaleListActivity extends BaseActivity {

	private TextView textTopTitle;
	ListView listContent;
	private CountryBean cBean;
	private List<CountryBean> countries = new ArrayList<CountryBean>(); //国家
	private List<String> prices = new ArrayList<String>(); //价格
	private List<String> types = new ArrayList<String>(); //类型
	private ExpandTabView expandTabView;
	private ArrayList<View> mViewArray = new ArrayList<View>();
	private ViewLeft viewLeft;
	private ViewMiddle viewMiddle;
	private ViewLeft viewLeft_t;
	private String type = "";
	private String country = "";
	private String city = "";
	private String price = "";
    private LinearLayout textEmptyTip;
	private UIHandler handler;
    private ImageButton imgBtnShare;

    private String shareTitle;
    private String shareContent;
    private String shareUrl;
    private String shareImg;

	
	    static class UIHandler extends Handler {
        private WeakReference<HouseSaleListActivity> weakRef;
		private HouseSaleAdapter hsa;
        
        public UIHandler(HouseSaleListActivity refObj) { 
            weakRef = new WeakReference<HouseSaleListActivity>(refObj); 
        }
        
        @Override
        public void handleMessage(Message msg) {
        	HouseSaleListActivity context = weakRef.get();
            if(msg.what == C.Handler.LOADRECOMMONDDATA){
                JSONArray ja = JSON.parseArray(msg.obj.toString());
                if(ja!=null &&ja.size()>0){
                    JSONObject object = ja.getJSONObject(0);

                    context.shareTitle = object.getString("title");
                    context.shareContent = object.getString("description");
                    context.shareUrl = object.getString("url");
                    context.shareImg = object.getString("photo");
                    if(context.listContent.getAdapter() == null){
                        hsa = new HouseSaleAdapter(context,ja);
                        context.listContent.setAdapter(hsa);
                    }else {
                        hsa.updateList(ja);
                    }

                    context.imgBtnShare.setVisibility(View.VISIBLE);
                    context.textEmptyTip.setVisibility(View.GONE);
                    context.listContent.setVisibility(View.VISIBLE);
                }else{
                    context.imgBtnShare.setVisibility(View.GONE);
                    context.textEmptyTip.setVisibility(View.VISIBLE);
                    context.listContent.setVisibility(View.GONE);
                }
            }else if (msg.what == C.Handler.lOADPARAMS) {
            	context.parseJson(msg.obj.toString());
				context.viewLeft.update();
				context.viewLeft_t.update();
				context.viewMiddle.update();
				context.expandTabView.setVisibility(View.VISIBLE);
			}
            else {
            	 TipUtil.showShort(context, msg);
			}
      }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_house_sale_list);
		initView();
		textTopTitle.setText(getResources().getString(R.string.homeText06));
		initVaule();
		handler = new UIHandler(this);
		loadParams(HouseSaleListActivity.this);
		loadRecommondData(HouseSaleListActivity.this,price,country,type,city);
		initListener();
	}
		private void initListener() {
		
		viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {

			@Override
			public void getValue(int distance, String showText) {
				onRefresh(viewLeft, showText,getString(R.string.price));
				price = distance == 0?"":prices.get(distance)+"";
                loadRecommondData(HouseSaleListActivity.this,price,country,type,city);
			}
		});
		viewLeft_t.setOnSelectListener(new ViewLeft.OnSelectListener() {
			
			@Override
			public void getValue(int distance, String showText) {
				onRefresh(viewLeft_t, showText,getString(R.string.type));
				type = distance == 0?"":distance+"";
                loadRecommondData(HouseSaleListActivity.this,price,country,type,city);
			}
		});
		viewMiddle.setOnSelectListener(new ViewMiddle.OnSelectListener() {
			@Override
			public void getValue(String showText,int countryposition,int cityposition) {
				onRefresh(viewMiddle,showText,getString(R.string.Country));
				cBean = countries.get(countryposition);
				country = cBean.getCountryid();
				if(cityposition != 0&& countryposition !=0){
					city = cBean.getCities().isEmpty()?"":cBean.getCities().get(cityposition-1).getCityid();
				}else {
					city = "";
				}
                loadRecommondData(HouseSaleListActivity.this,price,country,type,city);
			}
		});
		
		
	}
	
	private void onRefresh(View view, String showText,String def) {
		
		expandTabView.onPressBack();
		int position = getPositon(view);
		if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
			if(!showText.equals(getString(R.string.all))){
				expandTabView.setTitle(showText, position);
			}else {
				expandTabView.setTitle(def, position);
			}
		}

	}
	
	private int getPositon(View tView) {
		for (int i = 0; i < mViewArray.size(); i++) {
			if (mViewArray.get(i) == tView) {
				return i;
			}
		}
		return -1;
	}
	
	public void initView(){
		expandTabView = (ExpandTabView) findViewById(R.id.expandtab_view);
		textTopTitle = (TextView) findViewById(R.id.textTopTitle);
        imgBtnShare = (ImageButton) findViewById(R.id.btnShare);
		listContent = (ListView) findViewById(R.id.list_housesale);
        textEmptyTip = (LinearLayout) findViewById(R.id.textEmptyTip);
        listContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject object = (JSONObject) parent.getAdapter().getItem(position);
                    String url = object.getString("url");
                    String title = object.getString("title");
                    String content = object.getString("description");
                    String photo = object.getString("photo");
                    Intent intent = new Intent(HouseSaleListActivity.this, WebActivity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("title", getResources().getString(R.string.houseDetail));

                    intent.putExtra("isshare", true);
                    intent.putExtra("shareTitle", title);
                    intent.putExtra("shareContent", content);
                    intent.putExtra("shareUrl", url);
                    intent.putExtra("shareImg", photo);
                    startActivity(intent);
                } catch (Exception e) {
                    LogUtils.e("解析房产推荐数据错误", e);
                }
            }
        });
	}
	private void initVaule() {
		viewLeft = new ViewLeft(this,prices);
		viewMiddle = new ViewMiddle(this,countries,ViewMiddle.TYPE_CITYHASALL);
		viewLeft_t = new ViewLeft(this,types);
	    mViewArray.add(viewMiddle);
	  	mViewArray.add(viewLeft_t);
	  	mViewArray.add(viewLeft);
		ArrayList<String> mTextArray = new ArrayList<String>();
		mTextArray.add(getString(R.string.Country));
		mTextArray.add(getString(R.string.type));
		mTextArray.add(getString(R.string.price));
		expandTabView.setValue(mTextArray, mViewArray);
		
	}

	
	 public void loadRecommondData(final Context context, String price, String country, String type, String city){
             final LoadingDialog dialog = new LoadingDialog(HouseSaleListActivity.this);
             if(!dialog.isShowing()){
                 dialog.show();
             }
             HttpRequest.getInstance().asynRequest(context, KeeperUrl.GetHouseSaleList, new RequestCallBack() {

                 @Override
                 public void doCallBack(int code, String dataMsg, String data) {

                     if(C.Code.OK == code || 4007 == code){
                         handler.obtainMessage(C.Handler.LOADRECOMMONDDATA, data).sendToTarget();
                         dialog.dismiss();
                     } else {
                         Log.i(C.TAG, "列表为空");
                         dialog.dismiss();
                     }
                 }

             },new Param("price", price),new Param("city", city),new Param("country", country),new Param("type", type));
         }
	 public void parseJson(String data){
		    JSONObject jsonobj = JSON.parseObject(data);
			JSONArray countries = jsonobj.getJSONArray("city");
			JSONArray jsonprices = jsonobj.getJSONArray("price");
			JSONArray jsontypes = jsonobj.getJSONArray("type");
			CountryBean cBean = new CountryBean();
			if(HouseSaleListActivity.this.countries.isEmpty()){
				cBean.setCountryname("全部");
				cBean.setCities(new ArrayList<CityBean>());
				HouseSaleListActivity.this.countries.add(cBean);
				for(int i = 0;i<countries.size();i++){
					JSONObject country = countries.getJSONObject(i);
					CountryBean countryb = new CountryBean();
					String countryid = country.getString("countryid");
					String countryname = country.getString("countryname");
					countryb.setCountryid(countryid);
					countryb.setCountryname(countryname);
					List<CityBean> listcity = new ArrayList<CityBean>();
					JSONArray jsoncities = country.getJSONArray("citys");
					for(int j = 0;j<jsoncities.size();j++){
						CityBean city = new CityBean();
						city.setCountryid(countryid);
						city.setCityid(jsoncities.getJSONObject(j).getString("cityid"));
						city.setCityname(jsoncities.getJSONObject(j).getString("cityname"));
						listcity.add(city);
					}
					countryb.setCities(listcity);
					HouseSaleListActivity.this.countries.add(countryb);
				}
			}
			if(prices.isEmpty()){
				prices.add(getString(R.string.all));
				for(int k = 0;k < jsonprices.size();k++){
					JSONObject price = jsonprices.getJSONObject(k);
					String pricestr = price.getString("name");
					prices.add(pricestr);
				}
			}
			if(types.isEmpty()){
				types.add(getString(R.string.all));
				for(int l = 0;l < jsontypes.size();l++){
					JSONObject type = jsontypes.getJSONObject(l);
					String typestr = type.getString("name");
					types.add(typestr);
				}
			}
	 }
	 public void loadParams(final Context context){
            String data = SPUtil.getUtil(context).getString(C.CountryData);
            if(data == null || data.equals("")){
                final LoadingDialog dialog = new LoadingDialog(HouseSaleListActivity.this);
                HttpRequest.getInstance().asynRequest(context, KeeperUrl.GetSaleListParam, new RequestCallBack() {
                    @Override
                    public void doCallBack(int code, String dataMsg, String data) {
                        if(C.Code.OK == code){
                            handler.obtainMessage(C.Handler.lOADPARAMS, data).sendToTarget();
                            SPUtil.getUtil(context).putString(C.CountryData,data);
                            dialog.cancel();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UmShareUtils.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == 1){
            UMImage umImage = new UMImage(this, shareImg);

            Activity activity = this;
            int type = data.getIntExtra("type", 0);
            switch (type) {
                case 1:
                    UmShareUtils.shareSina(activity, shareTitle, shareContent, shareUrl, umImage);
                    LogUtils.d("微博分享");
                    break;
                case 2:
                    UmShareUtils.shareQZone(activity, shareTitle, shareContent, shareUrl, umImage);
                    LogUtils.d("Qzone分享");
                    break;
                case 3:
                    UmShareUtils.shareQQ(activity, shareTitle, shareContent, shareUrl, umImage);
                    LogUtils.d("QQ分享");
                    break;
                case 4:
                    UmShareUtils.shareWeiXin(activity, shareTitle, shareContent, shareUrl, umImage);
                    LogUtils.d("微信分享");
                    break;
                case 5:
                    UmShareUtils.shareWeiXinCircle(activity, shareTitle, shareContent, shareUrl, umImage);
                    LogUtils.d("朋友圈分享");
                    break;
                default:
                    break;
            }
        }
    }
	
	
	 

}
