package com.zzj.xiaozhan.activity;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class AudioActivity extends Activity {

	private String webUrl = "http://ear.duomi.com/wp-content/plugins/audio-player/assets/player.swf?ver=2.0.4.1";
	private static final String TAG = "MediaPlayerDemo";
	private MediaPlayer mMediaPlayer;
	private static final String MEDIA = "media";
	private static final int LOCAL_AUDIO = 1;
	private static final int STREAM_AUDIO = 2;
	private static final int RESOURCES_AUDIO = 3;
	private static final int LOCAL_VIDEO = 4;
	private static final int STREAM_VIDEO = 5;
	private String path;

	private TextView tx;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		tx = new TextView(this);
		setContentView(tx);
		Bundle extras = getIntent().getExtras();
		playAudio();
	}

	private void playAudio() {
		try {
			path = "http://hcl929.oss-cn-hangzhou.aliyuncs.com/Good%20To%20See%20You.mp3";
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
			Log.e(TAG, "error: " + e.getMessage(), e);
		}

	}

	public MediaPlayer createMediaPlayer(Context context, int resid) {
		try {
			AssetFileDescriptor afd = context.getResources().openRawResourceFd(
					resid);
			MediaPlayer mp = new MediaPlayer(context);
			mp.setDataSource(afd.getFileDescriptor());
			afd.close();
			mp.prepare();
			return mp;
		} catch (IOException ex) {
			Log.d(TAG, "create failed:", ex);
			// fall through
		} catch (IllegalArgumentException ex) {
			Log.d(TAG, "create failed:", ex);
			// fall through
		} catch (SecurityException ex) {
			Log.d(TAG, "create failed:", ex);
			// fall through
		}
		return null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}

	}

}
