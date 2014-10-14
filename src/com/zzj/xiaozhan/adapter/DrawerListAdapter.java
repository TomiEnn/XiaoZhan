package com.zzj.xiaozhan.adapter;

import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.R.array;
import com.zzj.xiaozhan.R.drawable;
import com.zzj.xiaozhan.R.id;
import com.zzj.xiaozhan.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerListAdapter extends BaseAdapter {

	private Context context;
	private String[] datas;
	private LayoutInflater inflater;

	public DrawerListAdapter(Context context) {

		this.context = context;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		datas = this.context.getResources().getStringArray(R.array.drawer_item);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.length;
	}

	@Override
	public Object getItem(int postion) {
		// TODO Auto-generated method stub
		return datas[postion];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.drawer_list_item, parent,
					false);
			viewHolder.icon = (ImageView) convertView
					.findViewById(R.id.drawer_list_icon);
			viewHolder.text = (TextView) convertView
					.findViewById(R.id.drawer_list_text);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		/**
		 * 设置drawer的icon图标
		 */
		switch (position) {
		case 0:
			viewHolder.icon.setImageResource(R.drawable.drawer_list_item_1);
			
			break;
		case 1:
			viewHolder.icon.setImageResource(R.drawable.drawer_list_item_2);
			

			break;
		case 2:
			viewHolder.icon.setImageResource(R.drawable.drawer_list_item_3);
		

			break;
		case 3:
			viewHolder.icon.setImageResource(R.drawable.drawer_list_item_4);
			

			break;
		case 4:
			viewHolder.icon.setImageResource(R.drawable.drawer_list_item_5);
			

			break;
		default:
			break;
		}
		/**
		 * 设置text文本
		 */
		viewHolder.text.setText(datas[position]);
		return convertView;
	}

	static class ViewHolder {
		private ImageView icon;
		private TextView text;

	}

}
