package com.newline.housekeeper.service;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newline.C;
import com.newline.core.BaseService;
import com.newline.housekeeper.HttpRequest.RequestCallBack;
import com.newline.housekeeper.KeeperUrl;
import com.newline.housekeeper.Param;
import com.newline.housekeeper.control.LoadingDialog;
import com.newline.housekeeper.model.RemindMsgBean;


public class RemindService extends BaseService {
    
    private static RemindService service;
	
	public RemindService(Context context) {
        super(context);
    }

	public static RemindService getService(Context context) {
		if (service == null) {
			synchronized (RemindService.class) {
				if (service == null) {
					service = new RemindService(context);
				}
			}
		}
		return service;
	}
	
	public String getTitleResId(int type){
	    if(C.Remind.HOUSE == type) return "缴费提醒";
	    if(C.Remind.RENT == type) return "租金提醒";
	    if(C.Remind.REPAIR == type) return "维修提醒";
	    return "系统提醒";
	}
	
	//提醒消息汇总
	public void doRemindTotal(Context context, final Handler handler){
	    String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
            return;
        }
	    
	    request.asynRequest(context, KeeperUrl.MessageTotal, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                message.what = code;
                if(C.Code.OK == code){
                    message.what = C.Handler.TOTAL_MESSAGE;
                    message.obj = data;
                } else {
                    message.obj = dataMsg;
                }
                message.sendToTarget();
            }
            
        }, new Param("uid", uid));
	}
	
	//提醒列表
	public void doRemindList(Context context, String type){
	    String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
            return;
        }
        
        request.asynRequest(context, KeeperUrl.MessageList, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Log.d(C.TAG, "去除消息请求:code=" + code + ", dataMsg=" + dataMsg);
            }
            
        }, new Param("uid", uid), new Param("type", type));
	    
	}
	
	//未读数量
    public void doUnreadNum(Context context, final Handler handler){
        String uid = spUtil.getString(C.UserIdKey);
        if(uid.isEmpty()){
            return;
        }
        
        request.asynRequest(context, KeeperUrl.MessageUnread, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                message.what = code;
                try {
                    if(C.Code.OK == code){
                        message.what = C.Handler.LOAD_UNREAD_HOME;
                        message.obj = data;
                    } else {
                        message.obj = dataMsg;
                    }
                } catch (Exception e) {
                    message.what = C.Code.SYS_ERROR;
                    message.obj = "系统错误";
                    Log.e(C.TAG, "解析提醒未读错误", e);
                }
                message.sendToTarget();
            }
            
        }, new Param("uid", uid));
    }
    
    //删除提醒消息
    public void doDelMessage(Context context, final Handler handler, String messageId, final int index){
        
        final LoadingDialog dialog = showLoadingDialog(context);
        request.asynRequest(context, KeeperUrl.MessageDelete, new RequestCallBack() {
            
            @Override
            public void doCallBack(int code, String dataMsg, String data) {
                Message message = handler.obtainMessage();
                message.what = code;
                if(C.Code.OK == code){
                    message.arg1 = index;
                } else {
                    message.obj = dataMsg;
                }
                message.sendToTarget();
                dialog.cancel();
            }
            
        }, new Param("messageId", messageId));
    }
	
	/**房屋维修提醒数据**/
	public ArrayList<RemindMsgBean> findRepair(){
		JSONArray array = new JSONArray();
		
		int id = 1;
		JSONObject obj = new JSONObject();
		obj.put("id", id);
		obj.put("state", 0);
		obj.put("money", 1000);
		obj.put("msg", "美国加利福尼亚桑迪玫瑰大街847号美国加利福尼亚桑迪玫瑰大街847号");
		obj.put("title", "房屋维修");
		obj.put("createtime", "2015年5月1号");
		array.add(obj);
		
		id ++;
		obj = new JSONObject();
		obj.put("id", id);
		obj.put("state", 0);
		obj.put("money", 4000);
		obj.put("msg", "美国加利福尼亚桑迪玫瑰大街848号美国加利福尼亚桑迪玫瑰大街847号");
		obj.put("title", "地板维修");
		obj.put("createtime", "2015年5月2号");
		array.add(obj);
		
		id ++;
		obj = new JSONObject();
		obj.put("id", id);
		obj.put("state", 0);
		obj.put("money", 5000);
		obj.put("msg", "美国拉斯韦加斯迪拜大街964号");
		obj.put("title", "房顶漏水维修");
		obj.put("createtime", "2015年5月3号");
		array.add(obj);
		
		for (int i = 21; i <= 30; i++) {
			id ++;
			obj = new JSONObject();
			obj.put("id", id);
			obj.put("state", 0);
			obj.put("money", new Random().nextInt(10000));
			obj.put("msg", "美国加利福尼亚桑迪玫瑰大街"+i+"号");
			obj.put("title", "维修主题" + i);
			obj.put("createtime", "2015年5月"+i+"号");
			array.add(obj);
        }
		
		ArrayList<RemindMsgBean> resultList = new ArrayList<RemindMsgBean>();
		int size = (array == null ? 0 : array.size());
		for (int i = 0; i < size; i++) {
			JSONObject json = (JSONObject) array.get(i);
			resultList.add(new RemindMsgBean(json));
        }
		return resultList;
	} 

}
