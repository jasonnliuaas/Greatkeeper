package com.newline.housekeeper.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.greatkeeper.keeper.R;
import com.lidroid.xutils.util.LogUtils;
import com.newline.core.BaseActivity;
import com.newline.core.utils.Network;
import com.newline.core.utils.TipUtil;
import com.newline.core.utils.UmShareUtils;
import com.newline.housekeeper.control.LoadingDialog;
import com.umeng.socialize.media.UMImage;

@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends BaseActivity {
    ImageButton btnShare;
    private boolean isshare;
    private WebView mWebView;
    private TextView mTextTitle;
    private ProgressBar pb;
    private LoadingDialog mLoadDealog;
    String shareTitle;
    String shareContent;
    String shareUrl;
    String shareImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_web_layout);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");
        isshare = intent.getBooleanExtra("isshare", false);
       /* intent.putExtra("shareTitle", title);
        intent.putExtra("shareContent", content);
        intent.putExtra("shareUrl", url);
        intent.putExtra("shareImg", photo);*/
        pb = (ProgressBar) findViewById(R.id.pb);
        pb.setMax(100);
        mWebView = loadControl(R.id.webView);
        btnShare = (ImageButton) findViewById(R.id.btnShare);
        if(isshare){
            btnShare.setVisibility(View.VISIBLE);
            shareTitle=intent.getStringExtra("shareTitle");
            shareContent=intent.getStringExtra("shareContent");
            shareUrl=intent.getStringExtra("shareUrl");
            shareImg=intent.getStringExtra("shareImg");
        }
        mTextTitle = loadControl(R.id.textTopTitle);
        mTextTitle.setText(title);

        if (!Network.checkNetWork(this)) {
            TipUtil.showShort(this, getString(R.string.netWorkError));
            return;
        }

        /*mLoadDealog = new LoadingDialog(this);
        mLoadDealog.showDailog();*/

        mWebView.loadUrl(url);
        mWebView.getSettings().setJavaScriptEnabled(true);

        //自适应屏幕
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        //支持缩放
        mWebView.getSettings().setSupportZoom(true);
        if(!url.contains("zujinbao")){
            mWebView.getSettings().setBuiltInZoomControls(true);
        }
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.setWebChromeClient(new WebViewClient());
        mWebView.setWebViewClient(new android.webkit.WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")){
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                } else if(url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                }
                return true;
            }



            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //mLoadDealog.cancel();
            }

        });
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
                    UmShareUtils.shareSina(activity,shareTitle, shareContent, shareUrl, umImage);
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

    @Override
    public void goBack(View view) {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.goBack(view);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLoadDealog != null&& mLoadDealog.isShowing())
            mLoadDealog.dismiss();
    }

    private class WebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            pb.setProgress(newProgress);
            if(newProgress==100){
                pb.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }

    }


}
