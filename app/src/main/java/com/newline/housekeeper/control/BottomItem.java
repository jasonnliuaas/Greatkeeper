package com.newline.housekeeper.control;


import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BottomItem {
    
    public int textFocused;
    public int textUnFocused;
	
	private int imageId;
	private int textId;
	private int itemLayoutId;
	
	private int onImgId;  	//被选中时,图片的编号
	private int offImgId;	//未被选中时,图片的编号
	
	private ImageView imageView;
	private TextView textView;
	private LinearLayout itemLayout;
	
	public BottomItem(int imageId, int textId, int itemLayoutId, int onImgId, int offImgId) {
        this.imageId = imageId;
        this.textId = textId;
        this.itemLayoutId = itemLayoutId;
        this.onImgId = onImgId;
        this.offImgId = offImgId;
        this.textFocused = BottomFactory.text_focused_color;
        this.textUnFocused = BottomFactory.text_unfocused_color;
    }
	
	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}
	
	/***
	 * 改变底部菜单栏状态
	 * @param on true=选中,false=未选中
	 * @param textColor 颜色值 Resource.getColor(colorResId)
	 */
	public void change(boolean on, int textColor){
		int resId = (on ? onImgId : offImgId);
		if(imageView != null){
			imageView.setImageResource(resId);
		}
		if(textView != null){
			textView.setTextColor(textColor);
		}
	}

	public TextView getTextView() {
		return textView;
	}

	public void setTextView(TextView textView) {
		this.textView = textView;
	}

	public LinearLayout getItemLayout() {
		return itemLayout;
	}

	public void setItemLayout(LinearLayout itemLayout) {
		this.itemLayout = itemLayout;
	}

	public int getImageId() { return imageId; }
	public int getTextId() { return textId; }
	public int getItemLayoutId() { return itemLayoutId; }

}
