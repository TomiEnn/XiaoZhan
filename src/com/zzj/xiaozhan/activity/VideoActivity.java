package com.zzj.xiaozhan.activity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.protocol.ResponseConnControl;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.MediaController.MediaPlayerSetData;
import io.vov.vitamio.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.model.Video;
import com.zzj.xiaozhan.utils.AppMediaController;
import com.zzj.xiaozhan.utils.Base64;
import com.zzj.xiaozhan.utils.Constants;
import com.zzj.xiaozhan.utils.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class VideoActivity extends Activity implements OnInfoListener,
		OnBufferingUpdateListener {

	private VideoView mVideoView;
	private LinearLayout bufferLayout;
	private TextView bufferText;
	private AppMediaController mediaController;
	private List<Video> files = null;
	private String path = "http://14.17.81.206/youku/6567E30CF14175D3E7494150/0300010100543A512BDCDC04A4526E9B50A1D4-6E2B-6A65-1F0F-83E7BE4EA159.flv";
	private ProgressBar pb;
	private Uri uri;
	private boolean isStart;
	/** 是否需要自动恢复播放，用于自动暂停，恢复播放 */
	private boolean needResume;
	private AudioManager mAudioManager;
	/** 最大声音 */
	private int mMaxVolume;
	/** 当前声音 */
	private int mVolume = -1;
	/** 当前亮度 */
	private float mBrightness = -1f;
	/** 当前缩放模式 */
	private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
	private GestureDetector mGestureDetector;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 初始化库
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		setContentView(R.layout.videoview);
		mVideoView = (VideoView) findViewById(R.id.video_view);
		bufferLayout = (LinearLayout) findViewById(R.id.buffering_group);
		bufferText = (TextView) findViewById(R.id.buffering_text);
		// downloadRateView = (TextView) findViewById(R.id.download_rate);
		// loadRateView = (TextView) findViewById(R.id.load_rate);
		// 设置最大音量
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mMaxVolume = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		// 手势监听
		mGestureDetector = new GestureDetector(this, new MyGestureListener());
		// 转换网络地址
		String webUrl = getIntent().getExtras().getString("playVideoUrl");
		LogUtil.i("new网络地址", "VideoPlayAvticity 更改后的地址： " + webUrl);
		// String webUrl =
		// "http://www.tudou.com/v/-n8XmoKveRw/&autoPlay=true&icode=-n8XmoKveRw&iid=132257582/v.swf";
		if (webUrl != null) {
			bufferLayout.setVisibility(View.VISIBLE);

			webUrl = webUrl.replace("://", ":##");
			webUrl = Base64.encodeBytes(webUrl.getBytes());
			if (webUrl.contains("+/")) {
				webUrl = webUrl.replaceAll("+/", "-_");
			}
		}
		LogUtil.i("网络地址", "更改后的地址： " + webUrl);
		getVideoUrl(webUrl);
	}

	/**
	 * 通过视屏页面地址得到可以播放视屏的地址
	 * 
	 * @param webUrl
	 *            视屏所在网页的地址
	 */
	public void getVideoUrl(String webUrl) {
		RequestQueue queue = Volley.newRequestQueue(this);
		LogUtil.i("getVideoUrl", "getVideoUrl isDone ");
		LogUtil.i("webUrl: ", Constants.API_URL + webUrl);
		StringRequest request = new StringRequest(Request.Method.GET,
				Constants.API_URL + webUrl, new Response.Listener<String>() {

					@Override
					public void onResponse(String xml) {
						// TODO Auto-generated method stub

						try {
							/**
							 * 创建XmlPullParser实例
							 */
							XmlPullParser parser = Xml.newPullParser();
							InputStream is = new ByteArrayInputStream(
									xml.getBytes());
							parser.setInput(is, "UTF-8");
							int eventType = parser.getEventType();
							Video video = null;
							String furl = null;

							while (eventType != XmlPullParser.END_DOCUMENT) {
								switch (eventType) {
								/**
								 * 文档开始事件,可以进行数据初始化处理
								 */
								case XmlPullParser.START_DOCUMENT:
									files = new ArrayList<Video>();
									video = new Video();
									break;

								/**
								 * 开始元素事件
								 */
								case XmlPullParser.START_TAG:
									if (parser.getName().equalsIgnoreCase(
											"furl")) {
										/**
										 * 如果后面是Text元素,即返回它的值
										 */
										furl = parser.nextText();
										LogUtil.i("furl: ", " " + furl);
										video.setPlayVideoUrl(furl);
										files.add(video);
									}
									break;

								/**
								 * 结束元素事件
								 */
								case XmlPullParser.END_TAG:
									if (parser.getName().equalsIgnoreCase(
											"furl")) {
										video = null;
										furl = null;
									}
									break;
								}

								eventType = parser.next();
							}

							is.close();

							playVideo(files.get(3).getPlayVideoUrl());

						} catch (XmlPullParserException e) {
							LogUtil.i("视频xml解析出错：", "" + e);
						} catch (IOException e) {
							LogUtil.i("视频IO出错：", "" + e);
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						if (error.networkResponse == null) {
							LogUtil.i("错误信息: ", "" + error.getMessage());
							Toast.makeText(VideoActivity.this, "发生什么事了...",
									Toast.LENGTH_SHORT).show();
						} else {
							if (error.networkResponse.statusCode == 404) {
								Toast.makeText(VideoActivity.this, "视频请求地址不存在",
										Toast.LENGTH_SHORT).show();

							} else if (error.networkResponse.statusCode >= 500) {
								Toast.makeText(VideoActivity.this, "视频服务器发生错误",
										Toast.LENGTH_SHORT).show();

							}
						}

					}
				});
		queue.add(request);
	}

	/**
	 * 播放视屏
	 * 
	 * @param url
	 */
	public void playVideo(String url) {
		LogUtil.i("playVideo", "playVideo isDone ");
		if (url == "") {
			// Tell the user to provide a media file URL/path.
			Toast.makeText(this, "视频源错误,无法播放", Toast.LENGTH_LONG).show();
			return;
		} else {

			mediaController = new AppMediaController(this);
			mediaController.setDataMediaPlayer(new MediaPlayerSetData() {

				@Override
				public void setTitle(TextView view) {
					// TODO Auto-generated method stub

				}

				@Override
				public void setTime(TextView view) {
					// TODO Auto-generated method stub

				}

				@Override
				public void setNetwork(TextView view) {
					// TODO Auto-generated method stub

				}
			});
			/*
			 * // 按下时显示控制按钮 mediaController.setOnTouchListener(new
			 * OnTouchListener() {
			 * 
			 * @Override public boolean onTouch(View v, MotionEvent event) { //
			 * TODO Auto-generated method stub switch (event.getAction()) { case
			 * MotionEvent.ACTION_DOWN: if (mediaController.isShowing()) {
			 * mediaController.hide(); } return true; } return false; } });
			 */

			mVideoView.setVideoURI(Uri.parse(url));
			mVideoView.setMediaController(mediaController);
			mVideoView.requestFocus();
			mVideoView.setOnInfoListener(this);
			mVideoView.setOnBufferingUpdateListener(this);
			mVideoView
					.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						@Override
						public void onPrepared(MediaPlayer mediaPlayer) {
							mediaPlayer.setPlaybackSpeed(1.0f);
						}
					});
			mVideoView.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mVideoView.stopPlayback();
				}
			});
		}
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			// 开始缓存，暂停播放
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
				needResume = true;
				bufferLayout.setVisibility(View.VISIBLE);

			}
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			// 缓存完成，继续播放
			if (needResume)
				mVideoView.start();
			bufferLayout.setVisibility(View.GONE);

			break;
		}
		return true;
	}

	/**
	 * 缓冲进度
	 */
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		bufferText.setText("正在缓冲 " + percent + "%");
	}

	/**
	 * 手势判断开始
	 */

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event))
			return true;

		// 处理手势结束
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_UP:
			endGesture();
			break;
		}

		return super.onTouchEvent(event);
	}

	/** 手势结束 */
	private void endGesture() {
		mVolume = -1;
		mBrightness = -1f;

		// 隐藏
		mDismissHandler.removeMessages(0);
		mDismissHandler.sendEmptyMessageDelayed(0, 800);
	}

	private class MyGestureListener extends SimpleOnGestureListener {

		/** 双击 */
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM)
				mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
			else
				mLayout++;
			if (mVideoView != null)
				mVideoView.setVideoLayout(mLayout, 0);
			return true;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub

			if (mVideoView != null && mediaController.isShowing())
				mediaController.show();
				
			

				
			return true;
		}

		/** 滑动 */
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			float mOldX = e1.getX(), mOldY = e1.getY();
			int y = (int) e2.getRawY();
			Display disp = getWindowManager().getDefaultDisplay();
			int windowWidth = disp.getWidth();
			int windowHeight = disp.getHeight();

			if (mOldX > windowWidth * 4.0 / 5)// 右边滑动
				onVolumeSlide((mOldY - y) / windowHeight);
			else if (mOldX < windowWidth / 5.0)// 左边滑动
				onBrightnessSlide((mOldY - y) / windowHeight);

			return super.onScroll(e1, e2, distanceX, distanceY);
		}

	}

	/** 定时隐藏 */
	private Handler mDismissHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mediaController.setVisibility(View.GONE);
		}
	};

	/**
	 * 滑动改变声音大小
	 * 
	 * @param percent
	 */
	private void onVolumeSlide(float percent) {
		if (mVolume == -1) {
			mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			if (mVolume < 0)
				mVolume = 0;

			/*
			 * // 显示 mOperationBg.setImageResource(R.drawable.video_volumn_bg);
			 * mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
			 */
		}

		int index = (int) (percent * mMaxVolume) + mVolume;
		if (index > mMaxVolume)
			index = mMaxVolume;
		else if (index < 0)
			index = 0;

		// 变更声音
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

		/*
		 * // 变更进度条 ViewGroup.LayoutParams lp =
		 * mOperationPercent.getLayoutParams(); lp.width =
		 * findViewById(R.id.operation_full).getLayoutParams().width index /
		 * mMaxVolume; mOperationPercent.setLayoutParams(lp);
		 */
	}

	/**
	 * 滑动改变亮度
	 * 
	 * @param percent
	 */
	private void onBrightnessSlide(float percent) {
		if (mBrightness < 0) {
			mBrightness = getWindow().getAttributes().screenBrightness;
			if (mBrightness <= 0.00f)
				mBrightness = 0.50f;
			if (mBrightness < 0.01f)
				mBrightness = 0.01f;

			/*
			 * // 显示
			 * mOperationBg.setImageResource(R.drawable.video_brightness_bg);
			 * mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
			 */
		}
		WindowManager.LayoutParams lpa = getWindow().getAttributes();
		lpa.screenBrightness = mBrightness + percent;
		if (lpa.screenBrightness > 1.0f)
			lpa.screenBrightness = 1.0f;
		else if (lpa.screenBrightness < 0.01f)
			lpa.screenBrightness = 0.01f;
		getWindow().setAttributes(lpa);

		/*
		 * ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		 * lp.width = (int)
		 * (findViewById(R.id.operation_full).getLayoutParams().width *
		 * lpa.screenBrightness); mOperationPercent.setLayoutParams(lp);
		 */
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (mVideoView != null)
			mVideoView.setVideoLayout(mLayout, 0);
		super.onConfigurationChanged(newConfig);
	}

}
