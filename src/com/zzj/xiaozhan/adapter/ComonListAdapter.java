package com.zzj.xiaozhan.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.model.Card;
import com.zzj.xiaozhan.utils.BitmapCache;
import com.zzj.xiaozhan.volley.LruBitmapCache;

public class ComonListAdapter extends BaseAdapter {
	private List<Card> datas;
	private Context context;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	private RequestQueue mRequestQueue;

	public ComonListAdapter(Context context, List<Card> datas) {
		// TODO Auto-generated constructor stub
		this.datas = datas;
		this.context = context;
		this.inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
		this.imageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(
				LruBitmapCache.getCacheSize(context)));
		//imageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
		   

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
		viewHolder.name = (TextView) convertView
				.findViewById(R.id.common_list_name);
		//ImageListener listener = ImageLoader.getImageListener(viewHolder.image,  
		//		R.drawable.icon_no_image, R.drawable.icon_no_data);  
		//imageLoader.get(datas.get(position).getImageUrl(), listener);
		
		 viewHolder.image.setDefaultImageResId(R.drawable.icon_no_image);
		viewHolder.image.setImageUrl(datas.get(position).getImageUrl(),
				imageLoader);
		 viewHolder.image.setErrorImageResId(R.drawable.icon_no_data);
		//viewHolder.title.setText(datas.get(position).getTitle());
		 viewHolder.name.setText(datas.get(position).getName());

		return convertView;
	}

	static class ViewHolder {
		private TextView title;
		private TextView name;
		private NetworkImageView image;
	}
}
