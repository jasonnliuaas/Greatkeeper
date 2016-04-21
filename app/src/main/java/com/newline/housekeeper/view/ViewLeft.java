package com.newline.housekeeper.view;

import java.util.ArrayList;
import java.util.List;

import com.greatkeeper.keeper.R;
import com.newline.housekeeper.model.TextAdapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;


public class ViewLeft extends RelativeLayout implements ViewBaseAction{

	private ListView mListView;
	List<String> values = new ArrayList<String>();
	private OnSelectListener mOnSelectListener;
	public TextAdapter adapter;
	private int position = 0;
	private String showText = "item1";
	private Context mContext;
    public static  final  int TYPE_CITYHASALL = 1;

	public String getShowText() {
		return showText;
	}

	public ViewLeft(Context context) {
		super(context);
		init(context);
	}
    public ViewLeft(Context context,int type) {
        super(context);
        init(context);
    }
	public ViewLeft(Context context,List<String> values) {
		super(context);
		this.values = values;
		init(context);
	}

	public ViewLeft(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ViewLeft(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_distance, this, true);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_mid));
		mListView = (ListView) findViewById(R.id.listView);
		adapter = new TextAdapter(context, values, R.drawable.choose_item_right, R.drawable.choose_eara_item_selector);
		adapter.setTextSize(17);
		if (position != 0) {
			adapter.setSelectedPosition(position);
		}
		mListView.setAdapter(adapter);
		adapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {

				if (mOnSelectListener != null) {
					showText = values.get(position);
					ViewLeft.this.position = position;
					mOnSelectListener.getValue(position, values.get(position));
				}
			}
		});
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnSelectListener {
		public void getValue(int distance, String showText);
	}
	public void update(){
		this.values.size();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void show() {
		
	}

}
