package com.zzj.xiaozhan.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.model.Card;
import com.zzj.xiaozhan.model.Video;
import com.zzj.xiaozhan.volley.LruBitmapCache;

public class VideoListAdapter extends BaseAdapter {
	private List<Video> datas;
	private Context context;
	private LayoutInflater inflater;

	public VideoListAdapter(Context context, List<Video> datas) {
		// TODO Auto-generated constructor stub
		this.datas = datas;
		this.context = context;
		this.inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			convertView = inflater.inflate(R.layout.common_video_list, parent,
					false);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.videoTitle = (TextView) convertView
				.findViewById(R.id.video_title);
		viewHolder.videoTime = (TextView) convertView
				.findViewById(R.id.video_time);
		viewHolder.videoNumber = (TextView) convertView
				.findViewById(R.id.video_number);
		 viewHolder.videoTitle.setText(datas.get(position).getTitle());
		 viewHolder.videoTime.setText(datas.get(position).getTime());
		 viewHolder.videoNumber.setText(datas.get(position).getNumber());

		return convertView;
	}

	static class ViewHolder {
		private TextView videoTitle;
		private TextView videoNumber;
		private TextView videoTime;
	}
}
