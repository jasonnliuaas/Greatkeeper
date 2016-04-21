package com.newline.housekeeper;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newline.C;
import com.newline.housekeeper.model.RentBean;

public class DemoData {
    
    // 示例房产列表
    public static JSONArray houseList(){
        final String keyAddress = "propertyaddress";
        final String keyBuiltyear = "builtyear";
        final String keyId = "propertyId";
        
        JSONArray array = new JSONArray();
        
        JSONObject object = new JSONObject();
        object.put(keyId, "1");
        object.put(keyAddress, "底特律 0000 Raaaaa Street Detory 643#");
        object.put(keyBuiltyear, "1938");
        array.add(object);
        
        object = new JSONObject();
        object.put(keyId, "2");
        object.put(keyAddress, "休斯顿 Wteeee RD AAAA Street Detory 643#");
        object.put(keyBuiltyear, "1983");
        array.add(object);
        return array;
    }
    
    // 示例房产详细列表
    public static String houseDetail(int  i){
        JSONObject bean = new JSONObject();
        bean.put("propertyaddress", "底特律 0000 Raaaaa Street Detory 643#");
        bean.put("builtyear", "2010");
        bean.put("usablearea", "5000.00平方英尺");
        bean.put("price", "200000.00美元");
        bean.put("purchasetime", "2015-07-01");
        bean.put("property", "独栋");
        bean.put("category", "商业");
        bean.put("room", "5室");
        bean.put("bathroom", "2.5卫");
        bean.put("propertytax", "0.00美元");
        bean.put("propertycost", "0.00美元");
        bean.put("premiums", "0.00美元");
        bean.put("propertypics", getImages());


        JSONObject bean2 = new JSONObject();
        bean2.put("propertyaddress", "休斯顿 Wteeee RD AAAA Street Detory 643#");
        bean2.put("builtyear", "2010");
        bean2.put("usablearea", "5000.00平方英尺");
        bean2.put("price", "200000.00美元");
        bean2.put("purchasetime", "2015-07-01");
        bean2.put("property", "独栋");
        bean2.put("category", "商业");
        bean2.put("room", "5室");
        bean2.put("bathroom", "2.5卫");
        bean2.put("propertytax", "0.00美元");
        bean2.put("propertycost", "0.00美元");
        bean2.put("premiums", "0.00美元");
        bean2.put("propertypics", getImages());

        if(i == 0){
            return bean.toJSONString();
        }

        return  bean2.toJSONString();

    }
    
    // 示例租赁信息列表
    public static List<RentBean> rentList(){
        
        final String keyAddress = "propertyaddress";
        final String keyStartdate = "startdate";
        final String keyEnddate = "enddate";
        
        JSONArray array = new JSONArray();
        
        JSONObject object = new JSONObject();
        object.put(keyAddress, "底特律 0000 Raaaaa Street Detory 643#");
        object.put(keyStartdate, "2015-08-01");
        object.put(keyEnddate, "2016-07-31");
        array.add(object);
        
        object = new JSONObject();
        object.put(keyAddress, "休斯顿 Wteeee RD AAAA Street Detory 643#");
        object.put(keyStartdate, "2015-08-01");
        object.put(keyEnddate, "2016-07-31");
        array.add(object);
        
        object = new JSONObject();
        object.put(keyAddress, "阿法乐特 0000 Raaaaa Street Detory 643#");
        object.put(keyStartdate, "2015-08-01");
        object.put(keyEnddate, "2016-07-31");
        array.add(object);
        
        object = new JSONObject();
        object.put(keyAddress, "纽约 0000 Raaaaa Street Detory 643#");
        object.put(keyStartdate, "2015-08-01");
        object.put(keyEnddate, "2016-07-31");
        array.add(object);
        
        object = new JSONObject();
        object.put(keyAddress, "洛杉矶 0000 Raaaaa Street Detory 643#");
        object.put(keyStartdate, "2015-08-01");
        object.put(keyEnddate, "2016-07-31");
        array.add(object);
        
        return JSON.parseArray(array.toJSONString(), RentBean.class);
    } 
    
    // 租赁详情
    public static String rentDetail(){
        JSONObject bean = new JSONObject();
        
        JSONObject leaseJson = new JSONObject();
        leaseJson.put("startdate", "2014-07-30");
        leaseJson.put("enddate", "2019-07-29");
        leaseJson.put("remaintime", "1825天");
        leaseJson.put("paymentfrequency", "每月");
        leaseJson.put("paymentday", "28日");
        leaseJson.put("monthrent", "$1290.00");
        leaseJson.put("rentCollectionMode", "银行卡");
        bean.put("lease", leaseJson);
        
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("realname", "张三");
        object.put("job", "经纪人");
        object.put("credithistory", "良好");
        array.add(object);
        object = new JSONObject();
        object.put("realname", "李四");
        object.put("job", "画家");
        object.put("credithistory", "良好");
        array.add(object);
        
        bean.put("renterList", array);
        return bean.toJSONString();
    }
    
    // 示例房产维修列表
    public static JSONArray repairList(){
        final String keyAddress = "propertyaddress";
        final String keyRepairName = "mantenaneceitem";
        final String keyRepairState = "approval_name";
        
        JSONArray array = new JSONArray();
        
        JSONObject object = new JSONObject();
        object.put(keyAddress, "底特律 0000 Raaaaa Street Detory 643#");
        object.put(keyRepairName, "维修项目:大门");
        object.put(keyRepairState, "未确认");
        array.add(object);
        
        object = new JSONObject();
        object.put(keyAddress, "休斯顿 Wteeee RD AAAA Street Detory 643#");
        object.put(keyRepairName, "维修项目:水管");
        object.put(keyRepairState, "未确认");
        array.add(object);
        
        object = new JSONObject();
        object.put(keyAddress, "阿法乐特 0000 Raaaaa Street Detory 643#");
        object.put(keyRepairName, "维修项目:地板");
        object.put(keyRepairState, "已确认");
        array.add(object);
        
        return array;
    }
    
    // 示例房产维修详细
    public static String repairDetail(){
        JSONObject object = new JSONObject();
        object.put("mantenaneceitem", "大门");
        object.put("expectedexpense", "122.00美元");
        object.put("expectedTakeTime", "10天");
        object.put("currentpicture", "http://www.greatkeeper.com/Public/Images/before.png");
        object.put("effectpicture", "http://www.greatkeeper.com/Public/Images/after.png");
        object.put("approval", C.Repair.UNCONFIRMED);
        
        return object.toJSONString();
    }
    
    // 示例租金明细数据
    public static String rentalList(){
        final String keyTypeName = "type_name";
        final String keyAmount = "amount";
        final String keyAddress = "propertyaddress";
        final String keyContent = "content";
        final String keyType = "type";
        final String keyCurrencyname = "currency_name";
        final String keyTime = "time";
        
        JSONArray array = new JSONArray();
        
        JSONObject object = new JSONObject();
        object.put(keyAddress, "底特律 0000 Raaaaa Street Detory 643#");
        object.put("address", "底特律 0000 Raaaaa Street Detory 643#");
        object.put(keyType, "1");
        object.put(keyTypeName, "租金收入");
        object.put(keyAmount, "+1000.00");
        object.put(keyContent, "06-25");
        object.put(keyCurrencyname, "美元");
        object.put(keyTime, "2015-06-25 09:16");
        object.put("income","0");
        object.put("city","西雅图");
        array.add(object);
        
        object = new JSONObject();
        object.put(keyAddress, "休斯顿 Wteeee RD AAAA Street Detory 643#");
        object.put("address", "休斯顿 Wteeee RD AAAA Street Detory 643#");
        object.put(keyType, "2");
        object.put(keyTypeName, "管理费用");
        object.put(keyAmount, "-650.00");
        object.put(keyContent, "06-20");
        object.put("city","休斯顿");
        object.put(keyCurrencyname, "美元");
        object.put(keyTime, "2015-06-20 09:16");
        object.put("income","0");
        array.add(object);
        
        object = new JSONObject();
        object.put(keyAddress, "阿法乐特 0000 Raaaaa Street Detory 643#");
        object.put("address", "阿法乐特 0000 Raaaaa Street Detory 643#");
        object.put(keyType, "4");
        object.put(keyTypeName, "保险费用");
        object.put(keyAmount, "-920.00");
        object.put(keyContent, "06-15");
        object.put(keyCurrencyname, "美元");
        object.put(keyTime, "2015-06-15 09:16");
        object.put("income","0");
        object.put("city","洛杉矶");
        array.add(object);
        
        object = new JSONObject();
        object.put(keyAddress, "纽约 0000 Raaaaa Street Detory 643#");
        object.put("address", "纽约 0000 Raaaaa Street Detory 643#");
        object.put(keyType, "5");
        object.put(keyTypeName, "物业费用");
        object.put(keyAmount, "-120.00");
        object.put(keyContent, "06-10");
        object.put("city","纽约");
        object.put(keyCurrencyname, "美元");
        object.put(keyTime, "2015-06-10 09:16");
        object.put("income","0");
        array.add(object);
        
        object = new JSONObject();
        object.put(keyAddress, "洛杉矶 0000 Raaaaa Street Detory 643#");
        object.put("address", "洛杉矶 0000 Raaaaa Street Detory 643#");
        object.put(keyType, "1");
        object.put(keyTypeName, "租金收入");
        object.put(keyAmount, "+1200.00");
        object.put(keyContent, "06-10");
        object.put("income","0");
        object.put("city","纽约");
        object.put(keyCurrencyname, "澳元");
        object.put(keyTime, "2015-06-10 09:16");
        array.add(object);
        
        return array.toJSONString();
    }

    // 示例报税列表数据
    public static String taxList(){
        final String keyName = "name";
        final String keyMobile= "mobile";
        final String keyType= "type";
        final String keyTypeName= "type_name";
        final String keyEmail = "email";
        final String keyCountry = "country";
        final String keyCity = "city";
        final String keyPrice= "price";
        final String keyScale= "scale";
        final String keyOther= "other_assets";
        final String keyState= "state";
        final String keyTime= "time";

        JSONArray array = new JSONArray();

        JSONObject object = new JSONObject();
        object.put(keyName, "mASAKA");
        object.put(keyType, "报税申请");
        object.put(keyCountry, "英国");
        object.put(keyMobile, "13521261254");
        object.put(keyEmail, "231622181@gmail.com");
        object.put(keyCity, "伦敦");
        object.put(keyTime, "2015-12-30");
        object.put(keyTypeName, "报税申请");
        object.put(keyPrice, "5000");
        object.put(keyOther, "0");
        object.put(keyScale,"1.5");
        object.put(keyState,"等待审核");
        array.add(object);

        object = new JSONObject();
        object.put(keyName, "taSAKA");
        object.put(keyType, "报税申请");
        object.put(keyCountry, "美国");
        object.put(keyEmail, "2316221321@gmail.com");
        object.put(keyCity, "休斯顿");
        object.put(keyState,"等待审核");
        object.put(keyTime, "2015-12-29");
        object.put(keyPrice, "506");
        object.put(keyTypeName, "报税申请");
        object.put(keyMobile, "13521261254");
        object.put(keyOther, "0");
        object.put(keyScale,"1.5");
        array.add(object);

        object = new JSONObject();
        object.put(keyName, "taSAKsA");
        object.put(keyType, "税号申请");
        object.put(keyCountry, "美国");
        object.put(keyEmail, "2316221311@gmail.com");
        object.put(keyCity, "休斯顿");
        object.put(keyPrice, "506");
        object.put(keyState,"等待审核");
        object.put(keyTime, "2015-12-28");
        object.put(keyTypeName, "税号申请");
        object.put(keyMobile, "13521261254");
        object.put(keyOther, "0");
        object.put(keyScale,"1.5");
        array.add(object);



        return array.toJSONString();
    }

    // 示例报税列表数据
    public static String loanList(){
        final String keyName = "name";
        final String keyMobile= "mobile";
        final String keyType= "type";
        final String keyTypeName= "type_name";
        final String keyEmail = "email";
        final String keyCountry = "country";
        final String keyCity = "city";
        final String keyPrice= "price";
        final String keyScale= "scale";
        final String keyOther= "other_assets";
        final String keyState= "state";
        final String keyTime= "time";

        JSONArray array = new JSONArray();

        JSONObject object = new JSONObject();
        object.put(keyName, "odels");
        object.put(keyType, "贷款申请");
        object.put(keyCountry, "英国");
        object.put(keyMobile, "13521261254");
        object.put(keyEmail, "231622181@gmail.com");
        object.put(keyCity, "伦敦");
        object.put(keyTime, "2015-12-30");
        object.put(keyTypeName, "报税申请");
        object.put(keyPrice, "5000");
        object.put(keyOther, "0");
        object.put(keyScale,"1.5");
        object.put(keyState,"等待审核");
        array.add(object);

        object = new JSONObject();
        object.put(keyName, "df");
        object.put(keyType, "贷款申请");
        object.put(keyCountry, "美国");
        object.put(keyEmail, "2316221321@gmail.com");
        object.put(keyCity, "休斯顿");
        object.put(keyState,"等待审核");
        object.put(keyTime, "2015-12-29");
        object.put(keyPrice, "506");
        object.put(keyTypeName, "报税申请");
        object.put(keyMobile, "13521261254");
        object.put(keyOther, "0");
        object.put(keyScale,"1.5");
        array.add(object);

        object = new JSONObject();
        object.put(keyName, "tes");
        object.put(keyType, "贷款申请");
        object.put(keyCountry, "美国");
        object.put(keyEmail, "2316221311@gmail.com");
        object.put(keyCity, "休斯顿");
        object.put(keyPrice, "506");
        object.put(keyState,"等待审核");
        object.put(keyTime, "2015-12-28");
        object.put(keyTypeName, "税号申请");
        object.put(keyMobile, "13521261254");
        object.put(keyOther, "0");
        object.put(keyScale,"1.5");
        array.add(object);



        return array.toJSONString();
    }
    
//    String typeId = bean.getString("remind_type");
//    String remind_type = object.getString("remind_type_name");
//    String days = object.getString("days");
//    String address = object.getString("address");
//    String remind_date = object.getString("remind_date");
//    String content = object.getString("content");
    
    
    
    // 示例缴费提醒数据
    public static String paymentList(){
        final String keyType = "remind_type";
        final String keyTypeName = "remind_type_name";
        final String keyAddress = "address";
        final String keyContent = "content";
        final String keyDays = "days";
        final String keyDate = "remind_date";
        
        JSONArray array = new JSONArray();
        
        JSONObject object = new JSONObject();
        object.put(keyAddress, "底特律 0000 Raaaaa Street Detory 643#");
        object.put(keyType, "1");
        object.put(keyTypeName, "房产税");
        object.put(keyContent, "交房产税啦,交房产税啦,交房产税啦....");
        object.put(keyDays, "26");
        object.put(keyDate, "2015-08-10");
        array.add(object);
        
        object = new JSONObject();
        object.put(keyAddress, "休斯顿 Wteeee RD AAAA Street Detory 643#");
        object.put(keyType, "2");
        object.put(keyTypeName, "房屋贷款");
        object.put(keyContent, "交房产税啦,交房产税啦,交房产税啦....");
        object.put(keyDays, "19");
        object.put(keyDate, "2015-08-10");
        array.add(object);
        
        object = new JSONObject();
        object.put(keyAddress, "阿法乐特 0000 Raaaaa Street Detory 643#");
        object.put(keyType, "3");
        object.put(keyTypeName, "交房提醒");
        object.put(keyContent, "交房产税啦,交房产税啦,交房产税啦....");
        object.put(keyDays, "145");
        object.put(keyDate, "2015-08-10");
        array.add(object);
        
        return array.toJSONString();
    }
    
    
    public static JSONArray getImages(){
        JSONArray array = new JSONArray();
        array.add("http://www.greatkeeper.com/Public/Images/head_top1.jpg");
        array.add("http://www.greatkeeper.com/Public/Images/head_top2.jpg");
        array.add("http://www.greatkeeper.com/Public/Images/head_top3.jpg");
        array.add("http://www.greatkeeper.com/Public/Images/head_top4.jpg");
        return array;
    }
    

}
