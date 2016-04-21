package com.newline.housekeeper;




public enum KeeperUrl {

    /** 获取Token **/
    GetToken("gettoken", KeeperUrl.GET, false),
    
    /** 获取启动图 **/
    GetLoading("getLoading", KeeperUrl.GET, false),
    
    /** 获取首页轮播图 **/
    GetRotation("getRotationList", KeeperUrl.GET, false),
    
    /** 登录 **/
    Login("userLogin", KeeperUrl.POST, false),
    
    /** 找回密码 **/
    FindPwd("userFindPwd", KeeperUrl.POST, false),
    
    /** 用户注册 **/
    Register("userRegister", KeeperUrl.POST, false),
    
    /** 租赁列表 **/
    LeaseList("getCurrentLeaseList", KeeperUrl.GET, true),
    
    /** 详情 **/
    LeaseDetail("getCurrentLeaseDetail", KeeperUrl.GET, true),
    
    /** 房屋维修列表 **/
    RepairList("getPropertyMaintenanceList", KeeperUrl.GET, true),
    
    /** 房屋维修详情 **/
    RepairDetail("getPropertyMaintenanceDetail", KeeperUrl.GET, true),
    
    /** 确认房产维修 **/
    RepairConfirm("updateMaintenanceApproval", KeeperUrl.POST, true),
    
    /** 获取国家/城市/类型/价格**/
    GetSaleListParam("getSaleListParam",KeeperUrl.GET,false),
    
    /** 获取房源推荐列表信息 **/
    GetHouseSaleList("getHouseSaleList",KeeperUrl.GET,false),
    
    /** 房产情况列表 **/
    HouseList("getPropertyList", KeeperUrl.GET, true),
    
    /** 房产详细 **/
    HouseDetail("getPropertyDetail", KeeperUrl.GET, true),
    
    /** 我的消息列表 **/
    MessageList("getMessageList", KeeperUrl.GET, true),
    
    /** 未读消息数量 **/
    MessageUnread("getMessageUnread", KeeperUrl.GET, true),
    
    /** 删除消息 **/
    MessageDelete("deleteMessage", KeeperUrl.POST, true),
    
    /** 租金跟踪统计 **/
    RentalTotal("getRentalTotal", KeeperUrl.GET, true),
    
    /** 租金列表 **/
    RentalList("getRentalList", KeeperUrl.GET, true),
    
    /** 用户反馈 **/
    FeedBack("createFeedback", KeeperUrl.POST, true),
    
    /** 物业经理列表 **/
    ManagerList("getManager", KeeperUrl.POST, true),
    
    /** 物业经理解除关系 **/
    ManagerRelieve("userRelieveRelation", KeeperUrl.POST, true),
    
    /** 修改用户信息 **/
    UpdateUserInfo("userChangeUserInfo", KeeperUrl.POST, true),
    
    /** 修改密码 **/
    UpdatePassword("userChangePassword", KeeperUrl.POST, true),
    
    /** 上传头像 **/
    UploadAvatar("userUpladAvatar", KeeperUrl.POST, true),
    
    /** 得到报税申请列表**/
    GetBaoshuiList("getBaoshuiList", KeeperUrl.GET, true),
    
    /** 得到报税申请列表**/
    SetBaoshui("setBaoshui", KeeperUrl.POST, true),
    
    /** 得到贷款列表**/
    GetDaiKuanList("getDaiKuanList", KeeperUrl.GET, true),
    
    /** 申请贷款*/
    SetDaiKuan("setDaiKuan", KeeperUrl.POST, true),
    
    /** 设置推送Token **/
    GetDeviceToken("setPushToken", KeeperUrl.POST, false),
    
    /** 获取租金详细 **/
    GetRentalRecode("getPropertyRecord", KeeperUrl.GET, true),
    
    /** 获取缴费提醒列表 **/
    GetPaymentList("getPaymentRemind", KeeperUrl.GET, true),
    
    /** 获取账号绑定情况 **/
    GetAccountBind("getAccountBind", KeeperUrl.GET, true),
    
    /** 发送手机验证码  **/
    SendMobileCode("getMobileSendCode", KeeperUrl.GET, false),
    
    /** 确认验证码  **/
    ConfirmVcode("getMobileAuthCode", KeeperUrl.GET, false),
    
    /** 设置绑定手机 **/
    SetBindMobile("setMobileBind", KeeperUrl.POST, true),
    
    /** 发送邮箱验证电子邮件  **/
    SendVEmail("setEmailBind", KeeperUrl.POST, false),
    
    /** 手机注册  **/
    MobileRegister("userMobileRegister", KeeperUrl.POST, false),
    
    /** 动态内容  **/
    DynamicContent("getPostList", KeeperUrl.POST, false),
    
    /** 收入统计  **/
    RentalIncome("getRentalIncome", KeeperUrl.GET, true),
    
    /** 未读动态数量  **/
    DynamicUnreadNum("getDongtaiRead", KeeperUrl.GET, true),
    
    /** 动态消息为已读  **/
    DynamicReaded("setDongtaiRead", KeeperUrl.GET, true),
    
    /** 我的提醒汇总 **/
    MessageTotal("getMessageTotal", KeeperUrl.GET, true),
    
    /** 手机找回密码 **/
    UserChangeMobilePwd("userChangeMobilePwd", KeeperUrl.POST, false),
    
    /** 手机号码是否存在 **/
    CheckMobile("userFindMobilePwd", KeeperUrl.POST, false)
    ;
    
    public static final String FORMAL_HOST = "http://www.greatkeeper.com";      //正式平台地址
    public static final String TEST_HOST = "http://greatkeeper.ronglifax.com";  //测试平台地址
    private static final String HOST = FORMAL_HOST;
    private static final String VER = "2.0";
    private static final String GET = "get";
    private static final String POST = "post";
    
    public static final String UserAgreementUrl = HOST + "/?c=Login&a=protocol_mobile";
    public static final String AboutUsUrl = HOST + "/?c=About&a=aboutUs_mobile";
    public static final String QuestionUrl = HOST + "/?c=About&a=question_mobile";
    public static final String FunctionsUrl = HOST + "/?c=About&a=functions_mobile";
    public static final String DownLoadUrl = ( HOST + "/?c=About&a=app_download") ;
    public static final String recommendHouse = HOST + "/Portal/houseSaleList";
    

    private KeeperUrl(String action, String method, boolean needLogin) {
        this.url = HOST + "/?c=api&";
        this.version = VER;
        this.needLogin = needLogin;
        this.method = method;
        this.action = action;
    }

    private boolean needLogin;
    private String url;
    private String method;
    private String action;
    private String version;

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getAction() {
        return action;
    }

    public String getVersion() {
        return version;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }
    
    public boolean isPost(){
        return POST.equals(method);
    }

}
