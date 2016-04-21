package com.newline.housekeeper.model;



public class RemindBean {

    private String id;              // 消息编号
	private int    type;        	// 提醒类别 0系统，1房产，2租约，3维修
	private int    unread;   	    // 未读消息数
	private String msg; 	        // 消息内容
	private String createtime;  	// 消息时间

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
