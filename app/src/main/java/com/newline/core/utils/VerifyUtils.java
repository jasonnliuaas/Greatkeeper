package com.newline.core.utils;

import java.util.regex.Pattern;


public class VerifyUtils {
    
    private final static String Reg_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    private final static String Reg_PHONE = "^[1][3,4,5,7,8]+\\d{9}$";
    private final static String Reg_azAZ09 = "^[A-Za-z0-9]+$";
    
    public static boolean matches(String reg, String input){
        if(input == null || input.isEmpty()) return false;
        return Pattern.matches(reg, input);
    }
    
    public static boolean isEmail(String email){
        return matches(Reg_EMAIL, email);
    }
    
    public static boolean isPhone(String phone){
        return matches(Reg_PHONE, phone);
    }
    
    public static boolean isAzAz09(String str, int minLen, int maxLen){
        boolean valid = matches(Reg_azAZ09, str);
        if(valid){
            valid = validString(str, minLen, maxLen);
        }
        return valid;
    }
    
    /** 验证字符串的字节数 **/
    public static boolean validString(String str, int minLen, int maxLen){
        int len = (str == null || str.isEmpty() ? 0 : calString(str));
        return len >= minLen && len <= maxLen;
    }
    
    /** 计算字符串字节数 **/
    private static int calString(String str){
        str = str.replaceAll("[^\\x00-\\xff]", "**");
        return str.length();
    }
    
    public static boolean validAccount(String account){
        return isEmail(account) || isPhone(account);
    }
    
    public static boolean validPassword(String password){
        return isAzAz09(password, 6, 20);
    }
    
    public static boolean validRealName(String realname){
        return validString(realname, 4, 20);
    }
    
    public static void main(String[] args) {
        String eMail = "lzy.do@qq.com";
        System.out.println(isEmail(eMail));
    }
    
}
