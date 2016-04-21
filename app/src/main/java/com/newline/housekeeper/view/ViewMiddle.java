package com.newline.housekeeper.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.greatkeeper.keeper.R;
import com.newline.C;
import com.newline.housekeeper.model.CountryBean;
import com.newline.housekeeper.model.TextAdapter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ViewMiddle extends LinearLayout implements ViewBaseAction {
	
	private ListView countryListView;
	private ListView cityListView;
	private LinkedList<String> childrenItem = new LinkedList<String>();
	private List<CountryBean> countries;
	List<String> ctr;
	private SparseArray<LinkedList<String>> children = new SparseArray<LinkedList<String>>();
	private TextAdapter cityListViewAdapter;
	private TextAdapter countryListViewAdapter;
	private OnSelectListener mOnSelectListener;
	private int countryPosition = 0;
	private int cityposition = 0;
	private String showString = "不限";
	private String countryname;
	private String cityname;
    public  final static int  TYPE_CITYHASALL = 1;
    int type;

	public ViewMiddle(Context context) {
		super(context);
		init(context);
	}
	public ViewMiddle(Context context,List<CountryBean> countries) {
		super(context);
		this.countries = countries;
		init(context);
	}
    public ViewMiddle(Context context,List<CountryBean> countries,int type) {
        super(context);
        this.countries = countries;
        this.type = type;
        init(context);
    }

	public ViewMiddle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void updateShowText(String showArea, String showBlock) {
		if (showArea == null || showBlock == null) {
			return;
		}
		for (int i = 0; i < countries.size(); i++) {
			if (countries.get(i).equals(showArea)) {
				countryListViewAdapter.setSelectedPosition(i);
				childrenItem.clear();
				if (i < children.size()) {
					childrenItem.addAll(children.get(i));
				}
				countryPosition = i;
				break;
			}
		}
		for (int j = 0; j < childrenItem.size(); j++) {
			if (childrenItem.get(j).replace("不限", "").equals(showBlock.trim())) {
				cityListViewAdapter.setSelectedPosition(j);
				cityposition = j;
				break;
			}
		}
		setDefaultSelect();
	}

	private void init(final Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_region, this, true);
		countryListView = (ListView) findViewById(R.id.listView);
		cityListView = (ListView) findViewById(R.id.listView2);
		setBackgroundDrawable(getResources().getDrawable(
				R.drawable.choosearea_bg_mid));
		ctr = new ArrayList<String>();
		for(int i = 0;i<countries.size();i++){
			ctr.add(countries.get(i).getCountryname());
			LinkedList<String> tItem = new LinkedList<String>();
            tItem.add(context.getString(R.string.all));
			for(int j=0;j<countries.get(i).getCities().size();j++){
				tItem.add(countries.get(i).getCities().get(j).getCityname());
				children.put(i, tItem);
			}
		}

		countryListViewAdapter = new TextAdapter(context, ctr,
				R.drawable.choose_item_selected,
				R.drawable.choose_eara_item_selector);
		countryListViewAdapter.setTextSize(17);
		countryListViewAdapter.setSelectedPositionNoNotify(countryPosition);
		countryListView.setAdapter(countryListViewAdapter);
		countryListViewAdapter
				.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, int position) {
                        countryPosition = position;
                        if(position == 0){
                            if (mOnSelectListener != null && type != 0) {
                                childrenItem.clear();
                                cityListViewAdapter.notifyDataSetChanged();
                                mOnSelectListener.getValue(context.getString(R.string.all),countryPosition,cityposition);
                            }
                        }else{
                            childrenItem.clear();
                            childrenItem.addAll(children.get(countryPosition));
                            if(type!=0){
                                childrenItem.add(0,context.getString(R.string.all));
                            }
                            countryname = ctr.get(countryPosition);
                            cityListViewAdapter.notifyDataSetChanged();
                        }


					}
				});
		cityListViewAdapter = new TextAdapter(context, childrenItem,
				R.drawable.choose_item_right,
				R.drawable.choose_plate_item_selector);
		cityListViewAdapter.setTextSize(15);
		cityListViewAdapter.setSelectedPositionNoNotify(cityposition);
		cityListView.setAdapter(cityListViewAdapter);
		cityListViewAdapter
				.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, final int position) {
						cityposition = position;
						if(position != 0){
							showString = childrenItem.get(cityposition);

						}else {
							showString = ctr.get(countryPosition);
						}
						cityname = childrenItem.get(cityposition);
						if (mOnSelectListener != null) {
							mOnSelectListener.getValue(showString,countryPosition,cityposition);
						}

					}
				});
		if (cityposition < childrenItem.size())
			showString = childrenItem.get(cityposition);
		if (showString.contains("不限")) {
			showString = showString.replace("不限", "");
		}
		setDefaultSelect();

	}

	public void setDefaultSelect() {
		countryListView.setSelection(countryPosition);
		cityListView.setSelection(cityposition);
	}

	public String getShowText() {
		return showString;
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnSelectListener {
		public void getValue(String showText,int countrypostion,int cityposition);
	}
	public void update(){
		for(int i = 0;i<countries.size();i++){
			ctr.add(countries.get(i).getCountryname());
			LinkedList<String> tItem = new LinkedList<String>();
			for(int j=0;j<countries.get(i).getCities().size();j++){
				tItem.add(countries.get(i).getCities().get(j).getCityname());
			}
			children.put(i, tItem);
		}
		for(String str:ctr){
			Log.i(C.TAG, str);
		}
		this.countryListViewAdapter.notifyDataSetChanged();
		Log.i(C.TAG, ""+countries.size());
		children.clear();
		for(int i = 0;i<countries.size();i++){
			LinkedList<String> tItem = new LinkedList<String>();
			for(int j=0;j<countries.get(i).getCities().size();j++){
				tItem.add(countries.get(i).getCities().get(j).getCityname());
			}
			children.put(i, tItem);
		}
		if (countryPosition < children.size())
			childrenItem.addAll(children.get(countryPosition));
		cityListViewAdapter.notifyDataSetChanged();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}
}
