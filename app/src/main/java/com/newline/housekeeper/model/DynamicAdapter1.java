package com.newline.housekeeper.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatkeeper.keeper.R;
import com.lidroid.xutils.util.LogUtils;
import com.newline.core.image.ImageDownLoader;
import com.newline.core.image.ImageDownLoader.onImageLoaderListener;

public class DynamicAdapter1 extends BaseAdapter {
	
	private JSONArray dataArray;
	private LayoutInflater layoutInflater;  
	private ImageDownLoader imgDowner;
    
    public DynamicAdapter1(Context context, JSONArray dataArray){
    	this.dataArray = dataArray;
    	this.layoutInflater = LayoutInflater.from(context);
    	this.imgDowner = new ImageDownLoader(context);
    }
    
    public final class ViewHolder {
    	TextView textMsgTime;	
    	TextView textTitle;
    	TextView textMsgDate;
    	TextView textMsgContext;
    	
    	ImageView imgMsgPic;     
    }
    
    public void update(JSONArray dataArray){
        this.dataArray = dataArray;
        notifyDataSetChanged();
    }
    
	@Override
	public int getCount() {
		return (dataArray == null ? 0 : dataArray.size());
	}

	@Override
	public Object getItem(int position) {
		return getItemBean(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.dynamic_item_view1, null); 
			
			holder = new ViewHolder();
			
			holder.textMsgTime = (TextView) convertView.findViewById(R.id.textMsgTime);
			holder.textTitle = (TextView) convertView.findViewById(R.id.textTitle);
			holder.textMsgDate = (TextView) convertView.findViewById(R.id.textMsgDate);
			holder.textMsgContext = (TextView) convertView.findViewById(R.id.textMsgContext);
			
			holder.imgMsgPic = (ImageView) convertView.findViewById(R.id.imgMsgPic);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		try {
		    JSONObject bean = getItemBean(position);
		    
		    String msgTime = bean.getString("date");
		    String msgTitle = bean.getString("title");
		    String msgDate = msgTime.substring(5, 11);
		    String msgContext = bean.getString("excerpt");
		    String msgImg = bean.getString("thumb");
		    
		    imgDowner.downloadImage(msgImg, new onImageLoaderListener() {
                
                @Override
                public void onImageLoader(Bitmap bitmap, String url) {
                    if(bitmap != null){
                        holder.imgMsgPic.setImageBitmap(bitmap);
                    } else {
                        holder.imgMsgPic.setImageResource(R.drawable.nocontent);
                    }
                }
            });
		    
		    holder.textMsgTime.setText(msgTime);
		    holder.textTitle.setText(msgTitle);
		    holder.textMsgDate.setText(msgDate);
		    holder.textMsgContext.setText(msgContext);
        } catch (Exception e) {
            LogUtils.e("动态推荐消息数据解析错误", e);
        }
		
		return convertView;
	}
	
	public JSONObject getItemBean(int position){
		if(position >= 0 && position < getCount()){
			return dataArray.getJSONObject(position);
		}
		return null;
	}
	
}
