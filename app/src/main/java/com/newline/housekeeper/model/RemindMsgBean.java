package com.newline.housekeeper.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newline.core.utils.DataUtils;

public class RemindMsgBean {

	private int id; 			// 编号
	private int state; 			// 状态：0未读，1已读，2删除
	private int type; 			// 类型：0系统，1房产，2租约，3维修，4经理，5中介
	private int money;			// 金额：消息所涉及到的金额（美元）
	private String title;		// 消息小标题
	private String msg; 		// 消息内容
	private String createtime;	// 创建时间
	
	public RemindMsgBean(){}
	
	public RemindMsgBean(JSONObject data){
		this.id = DataUtils.getInt(data, "id");
		this.state = DataUtils.getInt(data, "state");
		this.type = DataUtils.getInt(data, "type");
		this.money = DataUtils.getInt(data, "money");
		this.title = DataUtils.getString(data, "title");
		this.msg = DataUtils.getString(data, "msg");
		this.createtime = DataUtils.getString(data, "createtime");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
	
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	@Override
	public String toString() {
	   return JSON.toJSONString(this);
	}

}
