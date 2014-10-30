package com.zzj.xiaozhan.fragment;

import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.activity.SettingUserActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingFragment extends Fragment {

	private TextView aboutAuthor;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.setting_fragment, null, false);
		aboutAuthor = (TextView) view.findViewById(R.id.setting_author);
		return view;
	}
	
	public void author(View view){
		Intent intent = new Intent(getActivity(), SettingUserActivity.class);
		startActivity(intent);
	}
}
