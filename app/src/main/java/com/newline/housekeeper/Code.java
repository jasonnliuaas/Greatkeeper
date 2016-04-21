package com.newline.housekeeper;

import android.util.SparseArray;

public class Code {
    
    public static final SparseArray<String> codeMap = new SparseArray<String>();
    
    static {
        codeMap.put(-1, "系统错误，请稍后再试");
        codeMap.put(-2, "请检查网络连接");
        codeMap.put(-3, "请先登录创赢房管家");
        codeMap.put(4001, "获取token时appid或appsecret错误");
        codeMap.put(4002, "当前token无效或超时");
        codeMap.put(4003, "保存失败");
        codeMap.put(4004, "登录失败");
        codeMap.put(4005, "缺少参数：房产ID或经理ID");
        codeMap.put(4006, "缺少参数：房产ID或维修ID");
        codeMap.put(4007, "列表为空");
        codeMap.put(4008, "缺少参数：租约ID");
        codeMap.put(4009, "缺少参数：房产ID或租约ID");
        codeMap.put(4010, "缺少参数：消息ID");
        codeMap.put(4011, "删除失败");
        codeMap.put(4012, "缺少参数：用户ID、旧密码或新密码");
        codeMap.put(4013, "旧密码不正确");
        codeMap.put(4014, "修改失败，格式不正确或新密码未变化");
        codeMap.put(4015, "登录邮箱不能为空");
        codeMap.put(4016, "登录密码不能为空");
        codeMap.put(4017, "头像文件不能为空");
        codeMap.put(4018, "您输入的邮箱不正确");
        codeMap.put(4019, "密码不正确");
        codeMap.put(4020, "您的账号未激活，请到注册邮箱激活");
        codeMap.put(4021, "账号已停用，请联系客服人员");
        codeMap.put(4022, "请输入您的联系方式");
        codeMap.put(4023, "请输入您的称谓");
        codeMap.put(4024, "您输入的邮箱已存在");
        codeMap.put(4025, "验证码已发送成功");
        codeMap.put(4026, "您输入的手机不正确");
        codeMap.put(4027, "手机验证码不能为空");
        codeMap.put(4028, "手机验证码正确");
        codeMap.put(4029, "手机验证码错误");
        codeMap.put(4031, "您输入的手机不存在");
        codeMap.put(4032, "您输入的手机未验证");
        codeMap.put(4033, "缺少租金明细单号");
        codeMap.put(4100, "缺少参数：用户ID");
        codeMap.put(4101, "缺少token参数");
        codeMap.put(4102, "缺少appid参数");
        codeMap.put(4103, "缺少refresh_token参数");
        codeMap.put(4104, "缺少appsecret参数");
        codeMap.put(4105, "未使用POST请求");
    }
    
    public static String getMsg(int code){
        return codeMap.get(code);
    }

}
