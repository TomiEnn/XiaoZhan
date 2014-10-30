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

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.activity.MusicDetailActivity;
import com.zzj.xiaozhan.activity.NewComicDetailActivity;
import com.zzj.xiaozhan.activity.ShouYeDetailActivity;
import com.zzj.xiaozhan.adapter.ComonCardAdapter;
import com.zzj.xiaozhan.adapter.ComonListAdapter;
import com.zzj.xiaozhan.adapter.HotAdapter;
import com.zzj.xiaozhan.adapter.ShouYeAdapter;
import com.zzj.xiaozhan.model.Audio;
import com.zzj.xiaozhan.model.Card;
import com.zzj.xiaozhan.utils.Constants;
import com.zzj.xiaozhan.utils.LogUtil;
import com.zzj.xiaozhan.utils.NetworkUtil;
import com.zzj.xiaozhan.volley.AppStringRequest;

public class MusicFragment extends Fragment implements zrc.widget.ZrcListView.OnItemClickListener {

	// 刷新控件
	private SwipeRefreshLayout swipeLayout;
	private ListView listView;
	private HotAdapter adapter;
	private long startTime;
	private long endTime;
	private long costlTime;
	private Audio audio;
	private List<Audio> datas = new ArrayList<Audio>();
	private LinearLayout firstLayout;
	private LinearLayout secondLayout;
	private ScrollView myScorllView;
	private ZrcListView zrcListview;
	private ProgressBar loadinProgress;
	private RelativeLayout loadingLayout;
	private RelativeLayout errorLayout;
	private LinearLayout networkLoading;
	private int page = 1; 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.hotcomic_fragment, null, false);
		// listview布局
		zrcListview = (ZrcListView) view.findViewById(R.id.hot_swipe_container);
		loadinProgress = (ProgressBar) view.findViewById(R.id.progressbar_loading);		
		loadingLayout = (RelativeLayout) view.findViewById(R.id.loading_container);
		errorLayout = (RelativeLayout) view.findViewById(R.id.network_error_container);
		networkLoading = (LinearLayout) view.findViewById(R.id.network_and_loading);
		
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

		
		adapter = new HotAdapter(getActivity(), datas);
		zrcListview.setAdapter(adapter);
		zrcListview.setDivider(null);
		zrcListview.setOnItemClickListener(this);
		networkStates();
		return view;
	}

	/**
	 * 判断网络状态
	 */
	private void networkStates() {
		// TODO Auto-generated method stub
		if(NetworkUtil.isConnected(getActivity())){
			//加载数据
			loadingLayout.setVisibility(View.VISIBLE);
			loadinProgress.setVisibility(View.VISIBLE);
			loadDatas(Constants.DIAN_JIYI);
			enableNetworkErrorView(false);
		}else{
			loadingLayout.setVisibility(View.GONE);
			loadinProgress.setVisibility(View.GONE);
			//开启无网络图
			enableNetworkErrorView(true);
		}
	}
	
	
	private void enableNetworkErrorView(boolean networkAvailable) {
		// TODO Auto-generated method stub
		if(networkAvailable){
			errorLayout.setVisibility(View.VISIBLE);
			Button tryButton = (Button) errorLayout
					.findViewById(R.id.try_button);
			tryButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					refresh();
				}
			});
		}else{
			errorLayout.setVisibility(View.GONE);
		}
		
	}
	protected void loadMore() {
		// TODO Auto-generated method stub
		page++;
		loadDatas(Constants.DIAN_JIYI+ "page/"+page);
	}

	/**
	 * 刷新响应
	 */
	protected void refresh() {
		// TODO Auto-generated method stub
		//清除所有数据
		datas.clear();
		startTime = System.currentTimeMillis();
		loadDatas(Constants.DIAN_JIYI);
		costlTime = endTime - startTime;
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
						// LogUtil.i("拉数据", "html： " + html);

						Element mainElement = doc.getElementsByClass("content")
								.get(0);

						Elements links = mainElement
								.getElementsByClass("excerpt");
						LogUtil.i("打印数据", "sizes:   " + links.size());

						if (links.size() > 0) {
							for (Element link : links) {
								// 标题
								Element headElement = link.getElementsByTag(
										"header").get(0);
								String head = headElement.text();
								LogUtil.i("打印数据2", "head:   " + head);
								// 内容
								Element noteElement = link.getElementsByClass(
										"note").get(0);
								String note = noteElement.text();
								LogUtil.i("打印数据2", "note:   " + note);
								// 图片和网页地址
								Element imageElement = link.getElementsByClass(
										"focus").get(0);
								Element src = imageElement.select("img[src]")
										.get(0);
								Element href = imageElement.select("a[href]")
										.get(0);
								String imageUrl = src.attr("src");
								String webUrl = href.attr("href");
								LogUtil.i("打印数据2", "imageUrl:   " + imageUrl);
								LogUtil.i("打印数据2", "webUrl:   " + webUrl);
								// 作者
								Element nameElement = link.getElementsByClass(
										"muted").get(0);
								String name = nameElement.text();
								LogUtil.i("打印数据2", "name:   " + name);
								// 时间
								Element timeElement = link.getElementsByClass(
										"muted").get(1);
								String time = timeElement.text();
								LogUtil.i("打印数据2", "time:   " + time);

								audio = new Audio();
								audio.setWebName(head);
								audio.setWebAuthor(name);
								audio.setWebImage(imageUrl);
								audio.setWebUrl(webUrl);
								audio.setWebContent(note);
								audio.setTime(time);
								datas.add(audio);
							}
						}

						 adapter.notifyDataSetChanged();
						// endTime = System.currentTimeMillis();
						 zrcListview.setRefreshSuccess("加载成功");
							// 开启加载更多功能
							zrcListview.startLoadMore();
							endTime = System.currentTimeMillis();
							networkLoading.setVisibility(View.GONE);
							loadingLayout.setVisibility(View.GONE);
							loadinProgress.setVisibility(View.GONE);
						}catch(Exception e){
							//LogUtil.i("首页拉数据3", "Exception： " + e.getMessage());
							Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_SHORT).show();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError e) {
						// TODO Auto-generated method stub
						LogUtil.i("拉数据", "解析错误");
						LogUtil.i("拉数据", e.getMessage());
						 zrcListview.setRefreshFail("加载失败");
							loadingLayout.setVisibility(View.GONE);
							loadinProgress.setVisibility(View.GONE);
							enableNetworkErrorView(true);
					}
				});
		queue.add(request);

	}

	@Override
	public void onItemClick(ZrcListView parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), MusicDetailActivity.class);
		intent.putExtra("title", datas.get(position).getWebName());
		intent.putExtra("webUrl", datas.get(position).getWebUrl());
		intent.putExtra("image", datas.get(position).getWebImage());
		intent.putExtra("time", datas.get(position).getTime());
		intent.putExtra("author", datas.get(position).getWebAuthor());
		intent.putExtra("content", datas.get(position).getWebContent());
		startActivity(intent);
	}

}
