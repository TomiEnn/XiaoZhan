package com.zzj.xiaozhan.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.model.Card;
import com.zzj.xiaozhan.volley.LruBitmapCache;

public class ShouYeAdapter extends BaseAdapter {
	private List<Card> datas;
	private Context context;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	private RequestQueue mRequestQueue;
	private LinearLayout imageLayout;

	public ShouYeAdapter(Context context, List<Card> datas) {
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
			convertView = inflater.inflate(R.layout.common_card_layout, parent,
					false);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.nameImage = (NetworkImageView) convertView
				.findViewById(R.id.shou_image_userpic);
		viewHolder.contentImage = (NetworkImageView) convertView
				.findViewById(R.id.shou_imageview);
		viewHolder.title = (TextView) convertView
				.findViewById(R.id.shou_text_title);
		viewHolder.content = (TextView) convertView
				.findViewById(R.id.shou_text_content);
		viewHolder.name = (TextView) convertView
				.findViewById(R.id.shou_text_username);
		imageLayout = (LinearLayout) convertView
				.findViewById(R.id.shou_image_llayout);
		
		//人物头像
		 viewHolder.nameImage.setDefaultImageResId(R.drawable.shou_button_userpic);
		 viewHolder.nameImage.setImageUrl(datas.get(position).getUserImageUrl(),
				imageLoader);
		 viewHolder.nameImage.setErrorImageResId(R.drawable.default_icon_small_1);
		 //封面图片,如果是nodata的话就没有图片显示
		 if(!datas.get(position).getImageUrl().equals("nodata")){
			 imageLayout.setVisibility(View.VISIBLE);
			 viewHolder.contentImage.setDefaultImageResId(R.drawable.icon_no_image);
			 viewHolder.contentImage.setImageUrl(datas.get(position).getImageUrl(),
					imageLoader);
			 viewHolder.contentImage.setErrorImageResId(R.drawable.icon_no_image);
		 }else{
			 imageLayout.setVisibility(View.GONE);
		 }
		 if(!datas.get(position).getTitle().equals("nodata")){
			 viewHolder.title.setText(datas.get(position).getTitle());
		 }else{
			 viewHolder.title.setText("无题");
		 }
		
		 viewHolder.name.setText(datas.get(position).getName());
		 viewHolder.content.setText(datas.get(position).getContent());
		 

		return convertView;
	}

	static class ViewHolder {
		//标题
		private TextView title;
		//作者
		private TextView name;
		//内容
		private TextView content;
		//封面图片
		private NetworkImageView contentImage;
		//头像图片
		private NetworkImageView nameImage;
		//日期
		private TextView timeImage;
	}
}
