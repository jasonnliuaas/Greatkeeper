package com.newline.housekeeper.model;

import java.io.Serializable;

import android.graphics.Bitmap;

public class UserBean implements Serializable {

    private static final long serialVersionUID = 1290038982482635909L;
    
    private String uid;         // UID
    private String email;       // 邮箱地址
    private String avatar;      // 头像路径
    private String realname;    // 真实姓名
    private String nickname;    // 昵称
    private String mobile;      // 联系方式、手机
    private String password;    // 密码
    
    private Bitmap bitmapAvatar;
    
    public String getUid() {
        return uid;
    }
    
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Bitmap getBitmapAvatar() {
        return bitmapAvatar;
    }

    public void setBitmapAvatar(Bitmap bitmapAvatar) {
        this.bitmapAvatar = bitmapAvatar;
    }

}
