package com.newline.housekeeper.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.greatkeeper.keeper.R;
import com.lidroid.xutils.util.LogUtils;
import com.newline.core.image.ImageDownLoader;
import com.newline.core.image.ImageDownLoader.onImageLoaderListener;

public class HouseSaleAdapter extends BaseAdapter {
	
	private List<HouseBean> houseBeans =new ArrayList<HouseBean>();
	private LayoutInflater layoutInflater;
    private JSONArray dataArray;
	private ImageDownLoader imgDowner;
	public HouseSaleAdapter(){
    }
    public void updateList(JSONArray dataArray){
        this.dataArray = dataArray;
        notifyDataSetChanged();
    }
    public HouseSaleAdapter(Context context, List<HouseBean> houseBeans){
    	this.houseBeans = houseBeans;
    	this.layoutInflater = LayoutInflater.from(context);
    	this.imgDowner = new ImageDownLoader(context);
    }
    public HouseSaleAdapter(Context context, JSONArray dataArray){
        this.dataArray = dataArray;
        this.layoutInflater = LayoutInflater.from(context);
        this.imgDowner = new ImageDownLoader(context);
    }
    
    public  class ViewHolder {
    	ImageView house_img;	
    	TextView house_name;
    	TextView house_address;
    	TextView house_type;
    	TextView house_price;
    }
    
    public void update(List<HouseBean> houseBeans){
        this.houseBeans = houseBeans;
        notifyDataSetChanged();
    }
    
	@Override
	public int getCount() {
		return  dataArray.size();
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
			convertView = layoutInflater.inflate(R.layout.item_hwhouse_list, null); 
			holder = new ViewHolder();
			holder.house_img = (ImageView) convertView.findViewById(R.id.item_house_list_iv);
			holder.house_name = (TextView) convertView.findViewById(R.id.item_house_list_title_tv);
			holder.house_address = (TextView) convertView.findViewById(R.id.item_house_list_area_tv);
			holder.house_type = (TextView) convertView.findViewById(R.id.item_house_list_house_type_tv);
			holder.house_price = (TextView) convertView.findViewById(R.id.item_house_list_price_tv);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		try {
                JSONObject bean = getItemBean(position);
                String imgHouseUrl = bean.getString("photo");
                String title = bean.getString("title");
                String address = bean.getString("address");
                String type = bean.getString("category");
                String price = bean.getString("price");
			    holder.house_address.setText(address);
			    holder.house_name.setText(title);
			    holder.house_price.setText(price);
			    holder.house_type.setText(type);


            imgDowner.downloadImage(imgHouseUrl, new onImageLoaderListener() {

                @Override
                public void onImageLoader(Bitmap bitmap, String url) {
                    if (bitmap != null) {
                        holder.house_img.setImageBitmap(bitmap);
                    } else {
                        holder.house_img.setImageResource(R.drawable.nocontent);
                    }
                }
            });
        } catch (Exception e) {
            LogUtils.e("推荐房源数据解析错误", e);
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
