package com.newline;

import com.newline.core.utils.ConfigUtils;
import com.newline.housekeeper.KeeperUrl;


public class C {
    public static final boolean ALWAYS_FIRST = false; // 总是开启启动引导页面

    public static String APP_ID = ConfigUtils.getString("appId");
    public static String APP_SECRET = ConfigUtils.getString("appSecret");
    public static String APP_PKG = ConfigUtils.getString("appPackage");
    public static String APP_AGENT = ConfigUtils.getString("appAgent", "");

    public static final int REQUEST_CODE_PICK_IMAGE = 1;
    public static final int REQUEST_CODE_CAPTURE_CAMEIA =2;
    public static String SHARE_TITLE = ConfigUtils.getString("shareTitle");
    public static String SHARE_CONTENT = ConfigUtils.getString("shareContent");

    public static final String WECHAT_APPID = ConfigUtils.getString("weiChatAppId");
    public static final String WECHAT_SECRET = ConfigUtils.getString("weiChatAppSecret");

    public static final String QQ_APPID = ConfigUtils.getString("qqAppId");
    public static final String QQ_SECRET = ConfigUtils.getString("qqAppSecret");

    public static final String TAG = "HouseKeeper"; // APP标记关键字
    public static final String TokenKey = "Token"; // Token储存关键字
    public static final String UserAccountKey = "UserAccount"; // UserEmail储存关键字
    public static final String UserPwdKey = "UserPwd"; // UserPwd储存关键字
    public static final String UserIdKey = "UserId"; // UserId储存关键字
    public static final String CountryData ="CountryData"; //地区数据储存关键字
    public static final String RotationKey = C.TAG + "_Rotation_";

    public static final String LoginAction = "com.greatkeeper.keeper.ACTION_LOGIN_CY";

    public static final String REMEMBER_PWD_KEY = "rememberPwdFlag";
    public static final String REMEMBER_ACCOUNT = "rememberAccount";
    public static final String REMEMBER_PWD = "rememberPwd";
    public static final String CURRENCY = "Currency";
    public static final String UMENG_TOKEN = "UmengToken";

    public static String tempData = "";

    public static boolean inMain = false;


    public static final class Code {

	    /** 没有登录 **/
        public static final int NOT_LOGIN = -3;
        /** 没有网络 **/
		public static final int NO_NETWORK = -2;
		/** 系统错误 **/
		public static final int SYS_ERROR = -1;
		/** 操作成功 **/
		public static final int OK = 0;
		/** Token失效 **/
		public static final int TOKEN_DIE = 4002;
	}

	public static final class Screen {
		/** 屏幕分辨率宽度 */
		public static int widthPixels = 0;
		/** 屏幕分辨率高度 */
		public static int heightPixels = 0;
	}

	public static final class Remind {
		/** 系统消息 **/
		public static final int SYS = 0;
		/** 缴纳金额提醒 **/
		public static final int HOUSE = 1;
		/** 租金提醒 **/
		public static final int RENT = 2;
		/** 维修提醒 **/
		public static final int REPAIR = 3;
	}

	public static final class Repair {
		/** 未确认 **/
		public static final int UNCONFIRMED = 0;
		/** 要协商 **/
		public static final int NEGOTIATE = 1;
		/** 已同意 **/
		public static final int AGREED = 2;
		/** 已确认 **/
		public static final int CONFIRMED = 3;
	}
	
	public static final class Handler {
        public static final int APPLY_SUCCESS = 96;
		public static final int lOADPARAMS = 97;
		public static final int LOADRECOMMONDDATA = 98;
		public static final int LOAD_TAX_LIST = 99;;
	    public static final int LOAD_RENTAL_LIST = 100;
        public static final int LOAD_RENTAL= 115;
	    public static final int LOAD_PAYMENT_LIST = 101;
	    public static final int LOAD_ACCOUNT_BIND = 102;
	    public static final int UPDATE_MOBILE = 103;
	    public static final int LOAD_RECOMMOND_LIST = 104;
	    public static final int LOAD_CENTER_LIST = 105;
	    public static final int LOAD_RENTAL_INCOME = 106;
	    public static final int LOAD_UNREAD_DYNAMIC = 107;
	    public static final int DO_READ_DYNAMIC = 108;
	    public static final int TOTAL_MESSAGE = 109;
	    public static final int LOAD_UNREAD_HOME = 110;
	    public static final int FIDN_PWD_EMAIL = 111;
	    public static final int FIDN_PWD_MOBILE = 112;
	    public static final int CLEARALL_UNREAD = 113;
        public static final int LOAD_LOAN_LIST = 114;
	}
	
	public static final class Currency {
	    public static final int USD = 1;   // 美元
	    public static final int AUD = 2;   // 澳元
        public static final int CAD = 3;   // 加元
        public static final int GBP = 4;   // 英镑
	    
	    public static int nowCurrency = USD;
	}
	
	public static final class MsgType {
	    public static final String HOUSE = "1";
	    public static final String RENT = "2";
	    public static final String REPAIR = "3";
	    public static final String RENTAL = "4";
	    public static final String PAYMENT = "5";
	    public static final String RECOMMEND = "6";
	}
	
}
