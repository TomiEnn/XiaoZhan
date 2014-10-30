package com.zzj.xiaozhan.activity;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;

import java.lang.ref.WeakReference;
import java.util.Random;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.utils.LogUtil;
import com.zzj.xiaozhan.utils.MusicUrl;
import com.zzj.xiaozhan.volley.LruBitmapCache;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.PaletteAsyncListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MusicDetailActivity extends Activity {

	private TextView artcleTitle;
	private TextView artcleAuthor;
	private TextView artcleTime;
	private TextView artcleContent;
	private NetworkImageView imageView;
	private Button palyButton;
	private ImageLoader imageLoader;
	private RequestQueue mRequestQueue;
	private boolean isPlay = true;
	private boolean isImgLoad = true;
	private ImageLoadedHandler handler;
	private RelativeLayout midContainer;
	private LinearLayout textContainer;
	private int color;
	private String path;
	private MediaPlayer mMediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_activity);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		artcleTitle = (TextView) findViewById(R.id.music_detail_title);
		artcleAuthor = (TextView) findViewById(R.id.music_detail_user);
		artcleTime = (TextView) findViewById(R.id.music_detail_time);
		artcleContent = (TextView) findViewById(R.id.music_detail_content);
		imageView = (NetworkImageView) findViewById(R.id.music_detail_image);
		palyButton = (Button) findViewById(R.id.music_play);
		midContainer = (RelativeLayout) findViewById(R.id.music_mid_container);
		textContainer = (LinearLayout) findViewById(R.id.music_title_container);
		show();
		startMusic();
		handler = new ImageLoadedHandler(this);

		new Thread(new Runnable() {
			@Override
			public void run() {
				LogUtil.i("变色", "子线程执行中");
				while (isImgLoad) {
					if (imageView.getDrawable() != null) {
						isImgLoad = false;
						handler.sendEmptyMessage(1);
					}
				}
			}
		}).start();

	}

	private void startMusic() {
		// TODO Auto-generated method stub
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		try {
			
			Random r = new Random();
			int nextInt = r.nextInt(MusicUrl.musicUrls.length);
			path = MusicUrl.musicUrls[nextInt];
			if (path == "") {
				// Tell the user to provide an audio file URL.
				Toast.makeText(this, "No Music", Toast.LENGTH_LONG).show();
				return;
			}
			Uri uri = Uri.parse(path);
			mMediaPlayer = new MediaPlayer(this);
	// mMediaPlayer.setDataSource(webUrl);
			mMediaPlayer.setDataSource(getApplicationContext(), uri);
			mMediaPlayer.prepare();
			mMediaPlayer.start();

		} catch (Exception e) {
			//Log.i("music play", "error: " + e.getMessage(), e);
		}
		
	}

	static class ImageLoadedHandler extends Handler {

		WeakReference<MusicDetailActivity> weakReference;

		ImageLoadedHandler(MusicDetailActivity activity) {
			weakReference = new WeakReference<MusicDetailActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			final MusicDetailActivity activity = weakReference.get();

			/**
			 * 标题背景颜色渐变
			 */
			if (msg.what == 1) {
				if (((BitmapDrawable) activity.imageView.getDrawable())
						.getBitmap() == null) {
					return;
				}

				Palette.generateAsync(((BitmapDrawable) activity.imageView
						.getDrawable()).getBitmap(),
						new PaletteAsyncListener() {
							@SuppressWarnings("deprecation")
							@Override
							public void onGenerated(Palette palette) {
								/**
								 * 有活力暗色
								 */
								if (palette.getDarkVibrantColor() != null) {
									activity.color = palette
											.getDarkVibrantColor().getRgb();
									LogUtil.i("变色", "颜色值：" + activity.color);
									Drawable[] drawables = new Drawable[2];
									drawables[0] = new ColorDrawable(
											activity.getResources()
													.getColor(
															R.color.layout_default_background));
									drawables[1] = new ColorDrawable(
											activity.color);
									TransitionDrawable transitionDrawable = new TransitionDrawable(
											drawables);
									transitionDrawable.startTransition(1500);
									activity.textContainer
											.setBackgroundDrawable(transitionDrawable);
									activity.midContainer
											.setBackgroundColor(activity.color);
									activity.midContainer.findViewById(
											R.id.music_title_container)
											.setBackgroundColor(activity.color);

									return;
								}

								if (palette.getDarkMutedColor() != null) {
									activity.color = palette
											.getDarkMutedColor().getRgb();
									LogUtil.i("变色", "颜色值：" + activity.color);
									Drawable[] drawables = new Drawable[2];
									drawables[0] = new ColorDrawable(
											activity.getResources()
													.getColor(
															R.color.layout_default_background));
									drawables[1] = new ColorDrawable(
											activity.color);
									TransitionDrawable transitionDrawable = new TransitionDrawable(
											drawables);
									transitionDrawable.startTransition(1500);
									activity.textContainer
									.setBackgroundDrawable(transitionDrawable);
									activity.midContainer
									.setBackgroundColor(activity.color);
									activity.midContainer.findViewById(
									R.id.music_title_container)
									.setBackgroundColor(activity.color);
								}
							}
						});
			}
		}
	}

	private void show() {
		// TODO Auto-generated method stub
		String title = getIntent().getExtras().getString("title");
		String time = getIntent().getExtras().getString("time");
		String content = getIntent().getExtras().getString("content");
		// String webUrl = getIntent().getExtras().getString("webUrl");
		String image = getIntent().getExtras().getString("image");
		String author = getIntent().getExtras().getString("author");
		artcleTitle.setText(title);
		artcleAuthor.setText(author);
		artcleTime.setText(time);
		artcleContent.setText(content);
		LogUtil.i("Music", "time:   " + time);
		LogUtil.i("Music", "title:   " + title);
		LogUtil.i("Music", "content:   " + content);
		LogUtil.i("Music", "image:   " + image);
		LogUtil.i("Music", "author:   " + author);
		// 加载图片
		mRequestQueue = Volley.newRequestQueue(this);
		imageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(
				LruBitmapCache.getCacheSize(this)));
		//imageView.setDefaultImageResId(R.drawable.default_music_image_m);
		imageView.setImageUrl(image, imageLoader);
		imageView.setErrorImageResId(R.drawable.icon_no_image);

	}

	/**
	 * 开启音乐
	 */
	public void musicPlay(View view) {
		// 改变播放图标
	
		
		if (!isPlay) {
			palyButton.setBackgroundResource(R.drawable.player_pause);
			mMediaPlayer.start();
			isPlay = true;
		} else {
			palyButton.setBackgroundResource(R.drawable.player_play);
			mMediaPlayer.stop();
			isPlay = false;
		}

	}

}
