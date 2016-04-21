package com.newline.housekeeper.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.newline.core.BaseActivity;



@ContentView(R.layout.layout_taxapply_details)
public class TaxApplyDetailsActivity extends BaseActivity{
	
	@ViewInject(R.id.textTopTitle)
	TextView textTopTitle;
	
	@ViewInject(R.id.applyde_et_name)
	TextView applyde_et_name;

	@ViewInject(R.id.applyde_et_date)
	TextView applyde_et_date;

	@ViewInject(R.id.applyde_et_country)
	TextView applyde_et_country;
	

	@ViewInject(R.id.applyde_et_state)
	TextView applyde_et_state;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		textTopTitle.setText(getString(R.string.tax_details));
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			String time = bundle.getString("time");
			String name = bundle.getString("name");
			String state = bundle.getString("state");
			String id = bundle.getString("name");
			String type = bundle.getString("type");
			String country = bundle.getString("country");
			String city = bundle.getString("city");
			String price = bundle.getString("price");
			String scale = bundle.getString("scale");

			applyde_et_date.setText(time.trim().substring(5));
			applyde_et_state.setText(state);
			applyde_et_country.setText(country+"   "+city);
			applyde_et_name.setText(name);
		}
	}

}
