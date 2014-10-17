package com.zzj.xiaozhan.activity;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.adapter.NewComicListAdapter;
import com.zzj.xiaozhan.adapter.VideoListAdapter;
import com.zzj.xiaozhan.model.Card;
import com.zzj.xiaozhan.model.Video;
import com.zzj.xiaozhan.utils.LogUtil;
import com.zzj.xiaozhan.utils.NoScrollListView;
import com.zzj.xiaozhan.volley.AppStringRequest;
import com.zzj.xiaozhan.volley.LruBitmapCache;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class NewComicDetailActivity extends Activity {

	private TextView newComicTitle;
	private TextView newComicTag;
	private TextView newComicTime;
	private TextView newComicLocal;
	private TextView newComicfocus;
	private TextView newComicContent;
	private TextView newComicComments;
	private TextView newComicUp;
	public String imageUrl;
	public String local;
	public String tag;
	public String focus;
	public String content;
	public List<Video> datas = new ArrayList<Video>();
	private NoScrollListView listView;
	private VideoListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newcomic_detail_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		init();
		// 获取到传递过来的数据
		String title = getIntent().getStringExtra("title");
		String webUrl = getIntent().getStringExtra("webUrl");
		String time = getIntent().getStringExtra("time");
		newComicTitle.setText(title);
		newComicTime.setText("首  播："+time);
		loadDatas(webUrl);
		//把数据放到listview中	
		adapter = new VideoListAdapter(getApplicationContext(), datas);
		listView.setAdapter(adapter);
	}

	private void init() {
		// TODO Auto-generated method stub
		newComicTitle = (TextView) findViewById(R.id.newcomic_title);
		newComicTag = (TextView) findViewById(R.id.newcomic_tag);
		newComicTime = (TextView) findViewById(R.id.newcomic_time);
		newComicLocal = (TextView) findViewById(R.id.newcomic_local);
		newComicfocus = (TextView) findViewById(R.id.newcomic_focus);
		newComicContent = (TextView) findViewById(R.id.newcomic_content);
		newComicComments = (TextView) findViewById(R.id.newcomic_comments);
		newComicUp = (TextView) findViewById(R.id.newcomic_up);
		listView = (NoScrollListView) findViewById(R.id.newcomic_detail_listview);
	}

	/**
	 * 从网络获取数据
	 * 
	 * @param url
	 *            网址
	 */
	public void loadDatas(String url) {
		LogUtil.i("拉数据", "当前页面： " + this + " 执行了loadDatas方法");
		RequestQueue queue = Volley.newRequestQueue(this);
		AppStringRequest request = new AppStringRequest(Request.Method.POST,
				url, new Listener<String>() {
					// 解析网页数据
					@Override
					public void onResponse(String html) {
						// TODO Auto-generated method stub
						try {
							
						
						Document doc = Jsoup.parse(html);
						Element elementMain = doc.getElementsByClass("main")
								.get(0);
						Element elementLeft = elementMain.getElementsByClass(
								"left").get(0);
						Element elementInfobox = elementLeft
								.getElementsByClass("infobox").get(0);
						// 获取封面imageUrl
						Element elementPoster = elementInfobox
								.getElementsByClass("poster-sec").get(0);
						Element elementImage = elementPoster.select("img[src]")
								.get(0);
						imageUrl = elementImage.attr("src");
						// 继续向内查找节点
						Element elementInfo = elementInfobox
								.getElementsByClass("info-sec").get(0);
						Element ElementDesc = elementInfo.getElementsByClass(
								"desc-wrapper").get(0);
						Elements rows = ElementDesc.getElementsByClass("row");

						// 获取地址
						Element span0 = rows.get(0);
						local = span0.text();

						// 获取标签
						tag = rows.get(2).text();

						// 获取看点
						Element span3 = rows.get(3);
						focus = span3.text();

						// 获取简介
						Element span4 = rows.get(4).getElementsByClass("intro")
								.get(0);
						content = span4.text();

						LogUtil.i("拉数据", "图片网址： " + imageUrl);
						LogUtil.i("拉数据", " 地址： " + local);
						LogUtil.i("拉数据", " 标签： " + tag);
						LogUtil.i("拉数据", " 看点： " + focus);
						LogUtil.i("拉数据", " 简介： " + content);

						// 继续查找视频列表
						Element elementTab = elementLeft.getElementsByClass(
								"tab").get(0);
						Element elementMain0 = elementTab.getElementsByClass(
								"main0").get(0);
						Element elementBlock = elementMain0.getElementsByClass(
								"block").get(0);
						Element elementOlt = elementBlock.getElementsByClass(
								"olt").get(0);
						Element tbody = elementOlt.getElementsByTag("tbody")
								.get(0);
						// 遍历所以视频列表
						Elements trs = tbody.getElementsByTag("tr");
						if (trs.size() > 0) {
							
							for (Element tr : trs) {
								String number = tr.getElementsByTag("td")
										.get(0).text();
								String videoName = tr.getElementsByTag("td")
										.get(1).text();
								String videoDate = tr.getElementsByTag("td")
										.get(2).text();
								LogUtil.i("拉数据", " number： " + number
										+ " videoName： " + videoName
										+ " videoDate： " + videoDate);
								Video video = new Video();
								video.setNumber(number);
								video.setTitle(videoName);
								video.setTime(videoDate);
								datas.add(video);
							}
							adapter.notifyDataSetChanged();
						}

						// 回到主线程跟新UI
						runOnUiThread(new Runnable() {
							public void run() {

								// 跟新UI
								newComicTag.setText(tag);
								newComicLocal.setText(local);
								newComicfocus.setText(focus);
								newComicContent.setText("简  介："+content);
								if(!datas.isEmpty()){
									for (Video data : datas) {
										String videoNumber = data.getNumber();
										String videoTitle = data.getTitle();
										String videoTime = data.getTime();
										LogUtil.i("拉数据", " 主线程： " + videoNumber+videoTitle+videoTime);
									}
								
									
								}
							}
						});
						} catch (Exception e) {
							// TODO: handle exception
							LogUtil.i("解析异常", e.getMessage());
						}
						
					}
						
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						LogUtil.i("拉数据", "解析错误");
						LogUtil.i("拉数据", error.getMessage());
						if (error.networkResponse == null) {
							LogUtil.i("网络错误","连接超时");
						}else{ 
							LogUtil.i("网络错误","地址不正确，解析错误");
						if(error.networkResponse.statusCode == 404){
							LogUtil.i("网络错误","地址不存在");
						}
						if(error.networkResponse.statusCode >= 500){
							LogUtil.i("网络错误","服务器发生错误");
						}
						}
						
					}
				});
		queue.add(request);

	}

}
