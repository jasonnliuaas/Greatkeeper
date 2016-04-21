package com.newline.housekeeper.control;

import android.app.Activity;
import android.app.Fragment;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greatkeeper.keeper.R;
import com.newline.housekeeper.activity.CenterFragment;
import com.newline.housekeeper.activity.DynamicFragment;
import com.newline.housekeeper.activity.IndexFragment;

public class BottomFactory {
    
    //字体颜色
    public static final int text_focused_color = R.color.bottom_text_focused;
    public static final int text_unfocused_color = R.color.bottom_text_unfocused;
	
	private BottomItem[] items = new BottomItem[3];
	
	private void init(){
	    
		//首页
		items[0] = new BottomItem(
				R.id.index_image, 
				R.id.index_text, 
				R.id.index_layout,
				R.drawable.btn_index_focused, 
				R.drawable.btn_index_unfocused
		);
		
		//动态
		items[1] = new BottomItem(
				R.id.remind_image, 
				R.id.remind_text, 
				R.id.remind_layout,
				R.drawable.btn_remind_focused, 
				R.drawable.btn_remind_unfocused
		);
		
		//我的
		items[2] = new BottomItem(
				R.id.center_image, 
				R.id.center_text, 
				R.id.center_layout,
				R.drawable.btn_center_focused, 
				R.drawable.btn_center_unfocused
		);
		
	}
	
	/**初始化内容Fragment**/
	public Fragment initFragment(int idx){
		if(idx == 1) return new DynamicFragment();
		if(idx == 2) return new CenterFragment();
		return new IndexFragment();
	}
	
	private static BottomFactory instance;

	public static BottomFactory getInstance() {
		if (instance == null) {
			synchronized (BottomFactory.class) {
				if (instance == null) {
					instance = new BottomFactory();
					instance.init();
				}
			}
		}
		return instance;
	}
	
	/** Fragment Menu init **/
	public void initFragmentViews(Activity activity, OnClickListener listener){
		for (BottomItem item : items) {
			ImageView imgView = (ImageView) activity.findViewById(item.getImageId());
			TextView textView = (TextView) activity.findViewById(item.getTextId());
			
			LinearLayout layout = (LinearLayout) activity.findViewById(item.getItemLayoutId());
			layout.setOnClickListener(listener);
			
			item.setImageView(imgView);
			item.setTextView(textView);
			item.setItemLayout(layout);
        }
	}
	
	/**根据点击的viewId获取坐标**/
	public int getClickIdx(int itemLayoutId){
		for (int i = 0; i < size(); i++) {
			BottomItem item = items[i];
			if(item.getItemLayoutId() == itemLayoutId){
				return i;
			}
        }
		return 0;
	}
	
	public BottomItem item(int idx){
		return items[idx];
	}
	
	public int size(){
		return items.length;
	}
	
}
