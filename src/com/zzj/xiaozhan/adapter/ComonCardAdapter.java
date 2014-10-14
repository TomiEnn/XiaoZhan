package com.zzj.xiaozhan.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.model.Card;

public class ComonCardAdapter extends BaseAdapter {
	private List<Card> datas;
	private Context context;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;

	public ComonCardAdapter(Context context, List<Card> datas) {
		// TODO Auto-generated constructor stub
		this.datas = datas;
		this.context = context;
		this.inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		/*
		 * this.imageLoader = HttpManager.getInstance(this.context)
		 * .getImageLoader();
		 */
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
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
			convertView = inflater.inflate(R.layout.common_card_layout, parent,
					false);
			convertView.setTag(viewHolder);
			viewHolder.image = (ImageView) convertView
					.findViewById(R.id.shou_imageview);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.shou_text_title);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.shou_text_username);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.shou_text_content);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.image.setImageResource(R.drawable.djsz_1);
		viewHolder.title.setText(datas.get(position).getTitle());
		viewHolder.name.setText(datas.get(position).getName());
		viewHolder.content.setText(datas.get(position).getContent());

		return convertView;
	}

	static class ViewHolder {
		private ImageView image;
		private TextView title;
		private TextView name;
		private TextView content;
	}
}
