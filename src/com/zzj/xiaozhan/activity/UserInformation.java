package com.zzj.xiaozhan.activity;

import com.zzj.xiaozhan.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class UserInformation extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinformation_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	

}
