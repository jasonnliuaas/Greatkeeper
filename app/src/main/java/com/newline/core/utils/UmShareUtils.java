package com.newline.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.greatkeeper.keeper.R;
import com.lidroid.xutils.util.LogUtils;
import com.newline.C;
import com.newline.housekeeper.KeeperUrl;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.util.Map;
import java.util.Set;

public class UmShareUtils {
    
    private static final String WeiXinId = C.WECHAT_APPID;
    private static final String WeiXinSecret = C.WECHAT_SECRET;
    
    private static final String QQAppId = C.QQ_APPID;
    private static final String QQSecret = C.QQ_SECRET;

    public static UMSocialService umService;
    public static UMSocialService mController;
    private static String title;
    private static String content;
    private static String targetUrl;
    private static UMImage umImage;
    public static SocializeConfig config;

    public static void init(Activity activity) {
        if (umService == null) {
            umService = UMServiceFactory.getUMSocialService("com.umeng.share");
            title = C.SHARE_TITLE;
            content = C.SHARE_CONTENT;
            targetUrl = KeeperUrl.DownLoadUrl;
            umImage = new UMImage(activity, R.drawable.share_icon);
            // UMeng分享配置
            config = umService.getConfig();
            config.setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QZONE, SHARE_MEDIA.QQ);
            // 添加微信平台
            UMWXHandler wxHandler = new UMWXHandler(activity, WeiXinId, WeiXinSecret);
            wxHandler.addToSocialSDK();

            // 设置新浪SSO handler
            config.setSsoHandler(new SinaSsoHandler());
            
            // 添加微信朋友圈
            UMWXHandler wxCircleHandler = new UMWXHandler(activity, WeiXinId, WeiXinSecret);
            wxCircleHandler.setToCircle(true);
            wxCircleHandler.addToSocialSDK();


            // QQ空间
            QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, QQAppId, QQSecret);
            qZoneSsoHandler.addToSocialSDK();
            
            // QQ好友
            UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, QQAppId, QQSecret);
            qqSsoHandler.addToSocialSDK();
        }
        if(mController== null){
            mController = UMServiceFactory.getUMSocialService("com.umeng.login");
            mController.getConfig().setSsoHandler(new SinaSsoHandler());
        }
    }
    
    public static void shareQQ(Activity activity){
        shareQQ(activity, title, content, targetUrl, umImage);
    }
    
    public static void shareQZone(Activity activity){
        shareQZone(activity, title, content, targetUrl, umImage);
    }
    
    public static void shareWeiXin(Activity activity){
        shareWeiXin(activity, title, content, targetUrl, umImage);
    }
    
    public static void shareWeiXinCircle(Activity activity){
        shareWeiXinCircle(activity, title, content, targetUrl, umImage);
    }
    
    public static void shareSina(Activity activity){
        shareSina(activity, title, content, targetUrl, umImage);
    }
    
    public static void shareQQ(Activity activity, String title, String content, String targetUrl, UMImage umImage) {
        QQShareContent qqContent = new QQShareContent();
        qqContent.setShareContent(content);     // 设置分享文字
        qqContent.setTitle(title);              // 设置分享title
        qqContent.setTargetUrl(targetUrl);      // 设置点击分享内容的跳转链接
        //qqContent.setShareImage(umImage);       // 设置分享图片
        umService.setShareMedia(qqContent);
        performShare(activity, SHARE_MEDIA.QQ);
    }
    
    public static void shareQZone(Activity activity, String title, String content, String targetUrl, UMImage umImage) {
        QZoneShareContent qzoneContent = new QZoneShareContent();
        qzoneContent.setShareContent(content);     // 设置分享文字
        qzoneContent.setTitle(title);              // 设置分享title
        qzoneContent.setTargetUrl(targetUrl);      // 设置点击分享内容的跳转链接
        qzoneContent.setShareImage(umImage);       // 设置分享图片
        umService.setShareMedia(qzoneContent);
        performShare(activity, SHARE_MEDIA.QZONE);
    }

    public static void shareWeiXin(final Activity activity, String title, String content, String targetUrl, UMImage umImage) {
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(content);     // 设置分享文字
        weixinContent.setTitle(title);              // 设置title
        weixinContent.setTargetUrl(targetUrl);      // 设置分享内容跳转URL
        weixinContent.setShareImage(umImage);       // 设置分享图片
        umService.setShareMedia(weixinContent);
        performShare(activity, SHARE_MEDIA.WEIXIN);
    }

    public static void shareWeiXinCircle(Activity activity, String title, String content, String targetUrl, UMImage umImage) {
        CircleShareContent weixinContent = new CircleShareContent();
        weixinContent.setShareContent(content);     // 设置分享文字
        weixinContent.setTitle(title);              // 设置title
        weixinContent.setTargetUrl(targetUrl);      // 设置分享内容跳转URL
        weixinContent.setShareImage(umImage);       // 设置分享图片
        umService.setShareMedia(weixinContent);
        performShare(activity, SHARE_MEDIA.WEIXIN_CIRCLE);
        
    }

    public static void shareSina(Activity activity, String title, String content, String targetUrl, UMImage umImage) {
        SinaShareContent weixinContent = new SinaShareContent();
        weixinContent.setShareContent(content+"  "+targetUrl);     // 设置分享文字
        weixinContent.setTitle(title);              // 设置title
        weixinContent.setTargetUrl(targetUrl);// 设置分享内容跳转URL
        weixinContent.setShareImage(umImage);       // 设置分享图片
        umService.setShareMedia(weixinContent);
        sinaShare(activity, SHARE_MEDIA.SINA);
    }
    
    public static void onActivityResult(int requestCode, int resultCode, Intent data){
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private static void performShare(Context context, final SHARE_MEDIA platform) {
        umService.postShare(context, platform, new SnsPostListener() {
            @Override
            public void onStart() {
                LogUtils.i("分享分享,Platform=" + platform);
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                LogUtils.i("分享完成,Platform=" + platform + "eCode=" + eCode);
            }
        });
    }
    private static void sinaShare(Context context, final SHARE_MEDIA platform) {
        umService.postShare(context, platform, new SnsPostListener() {
            @Override
            public void onStart() {
                LogUtils.i("分享分享,Platform=" + platform);
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                LogUtils.i("分享完成,Platform=" + platform + "eCode=" + eCode);
            }
        });
    }

}
