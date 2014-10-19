package com.zzj.xiaozhan.fragment;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;
import zrc.widget.ZrcListView.OnStartListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.adapter.ComonCardAdapter;
import com.zzj.xiaozhan.adapter.ShouYeAdapter;
import com.zzj.xiaozhan.model.Card;
import com.zzj.xiaozhan.utils.Constants;
import com.zzj.xiaozhan.utils.LogUtil;
import com.zzj.xiaozhan.volley.AppStringRequest;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint.Join;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ShouYeFragment extends Fragment implements
		zrc.widget.ZrcListView.OnItemClickListener {
	// 刷新控件
	private ZrcListView zrcListview;
	private List<Card> datas = new ArrayList<Card>();
	private ShouYeAdapter adapter;
	private Card card;
	private long startTime;
	private long endTime;
	private long costlTime;
	private ProgressDialog progressDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.shouye_fragment, null, false);
		// zrcListview布局
		zrcListview = (ZrcListView) view
				.findViewById(R.id.shouye_swipe_listview);
		adapter = new ShouYeAdapter(getActivity(), datas);

		// 设置下拉刷新的样式
		SimpleHeader header = new SimpleHeader(getActivity());
		header.setTextColor(0xff0066aa);
		header.setCircleColor(0xff33bbee);
		zrcListview.setHeadable(header);
		// 设置加载更多的样式
		SimpleFooter footer = new SimpleFooter(getActivity());
		footer.setCircleColor(0xff33bbee);
		zrcListview.setFootable(footer);

		// 设置列表项出现动画
		zrcListview.setItemAnimForTopIn(R.anim.topitem_in);
		zrcListview.setItemAnimForBottomIn(R.anim.bottomitem_in);
		// 下拉刷新事件回调
		zrcListview.setOnRefreshStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				refresh();
			}
		});

		// 加载更多事件回调
		zrcListview.setOnLoadMoreStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				loadMore();
			}
		});

		 zrcListview.setAdapter(adapter);
		 zrcListview.setDivider(null);
		 zrcListview.setOnItemClickListener(this);
		 zrcListview.refresh();
		return view;
	}

	protected void loadMore() {
		// TODO Auto-generated method stub

	}

	/**
	 * 刷新响应
	 */
	protected void refresh() {
		// TODO Auto-generated method stub
		startTime = System.currentTimeMillis();
		loadDatas(Constants.I_TOU_XIAN);
		costlTime = endTime - startTime;
		if (costlTime < 1500) {
			costlTime = 1500;
		}
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() { // TODO Auto-generated method stub
				
				zrcListview.setRefreshSuccess("加载成功");
				// 开启加载更多功能
				zrcListview.startLoadMore();

			}
		}, costlTime);
	}


	/**
	 * 从网络获取数据
	 * 
	 * @param url
	 *            网址
	 */
	public void loadDatas(String url) {
		LogUtil.i("拉数据", "执行了loadDatas方法");
		RequestQueue queue = Volley.newRequestQueue(getActivity());
		AppStringRequest request = new AppStringRequest(Request.Method.POST,
				url, new Listener<String>() {
					// 解析网页数据
					@Override
					public void onResponse(String html) {
						// TODO Auto-generated method stub
						try{
						
						Document doc = Jsoup.parse(html);
						Element content = doc.getElementsByClass("content")
								.get(0);
						Elements articles = content
								.getElementsByClass("content-main-box");
						if (articles.size() > 0) {
							for (Element article : articles) {

								if (article.getElementsByClass("details")
										.size() > 0) {
									Element elementContent = article
											.getElementsByClass("details").get(
													0);
									// 文章内容
									String text = elementContent.text();
									// 文章图片
									String tontentImage;
									if (elementContent.select("img[src]")
											.size() > 0) {
										tontentImage = elementContent
												.select("img[src]").get(0)
												.attr("src");
									} else {
										tontentImage = "nodata";
									}
									LogUtil.i("首页拉数据3", "content： " + text);
									LogUtil.i("首页拉数据3", "tontentImage： "
											+ tontentImage);
									// 用户头像地址
									Element elementAvatar = article
											.getElementsByClass("avatar")
											.get(0);
									Element elementImage = elementAvatar
											.select("img[src]").get(0);
									String imageUrl = elementImage.attr("src");
									LogUtil.i("首页拉数据3", "imageUrl： " + imageUrl);
									// 用户名
									Element elementTitle = article
											.getElementsByClass("title").get(0);
									Element elementA = elementTitle
											.getElementsByTag("a").get(2);
									String name = elementA.text();
									// 时间(需要再次转化去掉文字)
									String time = elementTitle.text();
									// 标题
									String title;
									if (elementTitle.getElementsByTag("h1")
											.size() > 0) {
										Element elementH = elementTitle
												.getElementsByTag("h1").get(0);
										Element elementA1 = elementH
												.getElementsByTag("a").get(0);
										title = elementA1.text();
									} else {
										title = "nodata";
									}

									LogUtil.i("首页拉数据3", "name： " + name);
									LogUtil.i("首页拉数据3", "time： " + time);
									LogUtil.i("首页拉数据3", "title： " + title);
									// 将数据存储到datas里，并刷新adapter
									card = new Card();
									card.setName(name);
									card.setTitle(title);
									card.setContent(text);
									card.setImageUrl(tontentImage);
									card.setUserImageUrl(imageUrl);
									card.setMore(time);
									datas.add(card);
									

								} else {
									continue;
								}
								
							}
							adapter.notifyDataSetChanged();
							endTime = System.currentTimeMillis();

						}
						}catch(Exception e){
							LogUtil.i("首页拉数据3", "Exception： " + e.getMessage());
							Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_SHORT).show();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError e) {
						// TODO Auto-generated method stub
						LogUtil.i("首页拉数据", "解析错误");
						LogUtil.i("首页拉数据", e.getMessage());
						// zrcListview.setRefreshFail("加载失败");
					}
				});
		queue.add(request);

	}

	@Override
	public void onItemClick(ZrcListView parent, View view, int position, long id) {
		// TODO Auto-generated method stub

	}

	/**
	 * listView点击事件
	 */
	/*
	 * @Override public void onItemClick(AdapterView<?> parent, View view, int
	 * position, long id) { // TODO Auto-generated method stub
	 * 
	 * Intent intent = new Intent(ShouYeFragment.this.getActivity(),
	 * ShouYeDetailActivity.class); startActivity(intent);
	 * 
	 * }
	 */
	/**
	 * 显示进度的对话框
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * 关闭进度的对话框
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	
	
}
