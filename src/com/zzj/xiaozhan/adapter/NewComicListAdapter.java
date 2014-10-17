package com.zzj.xiaozhan.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.model.Card;
import com.zzj.xiaozhan.volley.LruBitmapCache;

public class NewComicListAdapter extends BaseAdapter {
	private List<Card> datas;
	private Context context;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	private RequestQueue mRequestQueue;

	public NewComicListAdapter(Context context, List<Card> datas) {
		// TODO Auto-generated constructor stub
		this.datas = datas;
		this.context = context;
		this.inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
		this.imageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(
				LruBitmapCache.getCacheSize(context)));

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
			convertView = inflater.inflate(R.layout.common_list_layout, parent,
					false);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.image = (NetworkImageView) convertView
				.findViewById(R.id.common_list_image);
		viewHolder.title = (TextView) convertView
				.findViewById(R.id.common_list_title);
		viewHolder.focusView = (TextView) convertView
				.findViewById(R.id.common_list_name);
		//设置数据到布局
		 viewHolder.image.setDefaultImageResId(R.drawable.icon_no_image);
		viewHolder.image.setImageUrl(datas.get(position).getImageUrl(),
				imageLoader);
		 viewHolder.image.setErrorImageResId(R.drawable.icon_no_data);
		 viewHolder.title.setText(datas.get(position).getTitle());
		 //viewHolder.timeImage.setImageResource(R.drawable.detail_icon_time);
		 viewHolder.focusView.setText(datas.get(position).getMore());

		return convertView;
	}

	static class ViewHolder {
		//标题
		private TextView title;
		//看点
		private TextView focusView;
		//封面图片
		private NetworkImageView image;
		//发行日期图标
		private ImageView timeImage;
	}
}
