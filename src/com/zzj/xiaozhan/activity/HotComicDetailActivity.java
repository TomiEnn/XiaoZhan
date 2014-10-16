package com.zzj.xiaozhan.activity;

import java.util.List;

import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.model.Card;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;

public class HotComicDetailActivity extends Activity {

	private TextView hotComicTitle;
	private TextView hotComicName;
	private TextView hotComicContent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_detail_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		init();
		//获取到传递过来的数据
		String title = getIntent().getStringExtra("title");
		String webUrl = getIntent().getStringExtra("webUrl");
		hotComicTitle.setText(title);
	}

	private void init() {
		// TODO Auto-generated method stub
		hotComicTitle = (TextView) findViewById(R.id.common_detail_title);
		hotComicName = (TextView) findViewById(R.id.common_detail_user);
		hotComicContent = (TextView) findViewById(R.id.common_detail_content);
	}
	
}
