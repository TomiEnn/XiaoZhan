package com.zzj.xiaozhan.fragment;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.activity.MainActivity;
import com.zzj.xiaozhan.activity.ShouYeDetailActivity;
import com.zzj.xiaozhan.activity.UserInformation;
import com.zzj.xiaozhan.adapter.ComonCardAdapter;
import com.zzj.xiaozhan.model.Card;
import com.zzj.xiaozhan.utils.LogUtil;
import com.zzj.xiaozhan.volley.AppStringRequest;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Paint.Join;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ShouYeFragment extends Fragment implements OnRefreshListener,
		OnItemClickListener {
	// 刷新控件
	private SwipeRefreshLayout swipeLayout;
	private List<Card> datas = new ArrayList<Card>();
	private ListView shouListView;
	private ComonCardAdapter adapter;
	private Card card;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.shouye_fragment, null, false);
		// listview布局
		shouListView = (ListView) view.findViewById(R.id.shou_listview_content);
		adapter = new ComonCardAdapter(getActivity(), datas);
		shouListView.setAdapter(adapter);
		shouListView.setDivider(null);
		shouListView.setOnItemClickListener(this);
		// 下拉刷新
		swipeLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.shou_swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		swipeLayout.setOnRefreshListener(this);
		// 获取数据
		getDatas();
	//	loadDatas("http://www.xiaozhan.cc/");
		return view;
	}

	private void getDatas() {
		// TODO Auto-generated method stub
		String[] name = { "Tomi_Enn", "Sam" };
		String[] title = { "东京食尸鬼", "海贼王" };
		String[] content = {
				"在纷乱嘈杂的现代化城市—东京，蔓延着一种吞食死尸的怪人，人们称之为食尸鬼。那一日，金木研遇上了某位神秘女子，进而卷入了一场迷之事故。自此，充满波折的命运齿轮开始转动了……",
				"生长在东海某小村庄的路飞受到海贼香克斯的精神指引，决定成为一名出色的海盗。为了达成这个目标，并找到万众瞩目的One Piece，路飞踏上艰苦的旅程。一路上他遇到了无数磨难，也结识了索隆、娜美、乌索普、香吉、罗宾等一众性格各异的好友。他们携手一同展开充满传奇色彩的大冒险。" };
		for (int i = 0; i < name.length; i++) {
			card = new Card();
			card.setName(name[i]);
			card.setTitle(title[i]);
			card.setContent(content[i]);
			datas.add(card);
		}
		adapter.notifyDataSetChanged();
	}

	public void loadDatas(String url) {
		LogUtil.i("拉数据", "执行了loadDatas方法");
		RequestQueue queue = Volley.newRequestQueue(getActivity());
		AppStringRequest request = new AppStringRequest(Request.Method.POST,
				url, new Listener<String>() {
					// 解析网页数据
					@Override
					public void onResponse(String html) {
						// TODO Auto-generated method stub
						Document doc = Jsoup.parse(html);
						Element content = doc.getElementsByClass("left").get(0);
						Elements links = content.getElementsByClass("item");
						if (links.size() > 0) {
							for (Element link : links) {
								// 根据html的节点一步步寻找需要的数据
								Element elementBg = link.getElementsByClass(
										"bd").get(0);
								Element elementContents = elementBg
										.getElementsByClass("content").get(0);
								Element elementTitle = elementContents
										.getElementsByClass("title").get(0);
								Element elementUrl = elementTitle.select(
										"a[href]").get(0);
								// 网络地址
								String url = elementUrl.attr("href");
								// 数据的标题
								String title = elementUrl.text();
								// 文章正文
								Elements elementSpan = elementContents
										.getElementsByTag("span");
								String detail = elementSpan.text();
								// 获取图标地址
								Element elementIcon = link.getElementsByClass(
										"avatar").get(0);
								Element elementImage = elementIcon.select(
										"img[src]").get(0);
								String imageUrl = elementImage.attr("src");
								LogUtil.i("拉数据", "网址： " + url + " 标题： " + title
										+ " 图片地址： " + imageUrl);
								LogUtil.i("拉数据", "正文： " + detail);
								
								//将数据存储到datas里，并刷新adapter
								card = new Card();
								card.setTitle(title);
								card.setImageUrl(imageUrl);
								card.setContent(detail);
								card.setWebUrl(url);
								datas.add(card);

							}
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						LogUtil.i("拉数据", "解析错误");
					}
				});
		queue.add(request);

	}

	/**
	 * 刷新响应
	 */
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				swipeLayout.setRefreshing(false);

			}
		}, 3000);
	}

	/**
	 * listView点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		Intent intent = new Intent(ShouYeFragment.this.getActivity(),
				ShouYeDetailActivity.class);
		startActivity(intent);

	}
}
