package com.newline.housekeeper.model;

import com.newline.core.utils.StringUtils;

public class RentBean {
    
    private String propertyid;      // 房产ID
    private String leaseid;         // 租约ID
    private String state;           // 状态:未租,已租
    private String leasename;       // 租约名称
    private String startdate;       // 开始日期
    private String enddate;         // 结束日期
    private String remiantime;      // 剩余天数
    private String monthrent;       // 月租金
    private String propertyaddress; // 房产地址
    
    public String getDate(){
        String date = "[-]";
        if(!StringUtils.isTrimEmpty(startdate) || !StringUtils.isTrimEmpty(enddate)){
            startdate = (startdate == null ? "" : startdate);
            enddate = (enddate == null ? "" : enddate);
            
            date = startdate + "至" + enddate;
        }
        return date;
    }

    public String getPropertyid() {
        return propertyid;
    }

    public void setPropertyid(String propertyid) {
        this.propertyid = propertyid;
    }

    public String getLeaseid() {
        return leaseid;
    }

    public void setLeaseid(String leaseid) {
        this.leaseid = leaseid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLeasename() {
        return leasename;
    }

    public void setLeasename(String leasename) {
        this.leasename = leasename;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getRemiantime() {
        return remiantime;
    }

    public void setRemiantime(String remiantime) {
        this.remiantime = remiantime;
    }

    public String getMonthrent() {
        return monthrent;
    }

    public void setMonthrent(String monthrent) {
        this.monthrent = monthrent;
    }

    public String getPropertyaddress() {
        return propertyaddress;
    }

    public void setPropertyaddress(String propertyaddress) {
        this.propertyaddress = propertyaddress;
    }
    
}
