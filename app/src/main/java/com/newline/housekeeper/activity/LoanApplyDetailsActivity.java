package com.newline.housekeeper.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.greatkeeper.keeper.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.newline.core.BaseActivity;


@ContentView(R.layout.layout_loanapply_details)
public class LoanApplyDetailsActivity extends BaseActivity{
	
	@ViewInject(R.id.textTopTitle)
	TextView textTopTitle;
	
	@ViewInject(R.id.applyde_et_name)
	TextView applyde_et_name;

	@ViewInject(R.id.applyde_et_country)
	TextView applyde_et_country;
	

	@ViewInject(R.id.applyde_et_state)
	TextView applyde_et_state;

    @ViewInject(R.id.applyde_et_interest)
    TextView applyde_et_interest;

    @ViewInject(R.id.applyde_et_price)
    TextView applyde_et_price;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		textTopTitle.setText(getString(R.string.loan_details));
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
			applyde_et_state.setText(state);
			applyde_et_country.setText(country+"   "+city);
			applyde_et_name.setText(name);
            applyde_et_interest.setText(scale);
            applyde_et_price.setText(price);
		}
	}

}
