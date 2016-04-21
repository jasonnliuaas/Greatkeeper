package com.newline.housekeeper.model;

import com.umeng.socialize.media.UMImage;

public class ShareBean {
    private String title;
    private String content;
    private String targetUrl;
    private UMImage image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public UMImage getImage() {
        return image;
    }

    public void setImage(UMImage image) {
        this.image = image;
    }

}
