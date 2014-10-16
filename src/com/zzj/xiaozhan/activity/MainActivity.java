package com.zzj.xiaozhan.activity;

import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.R.array;
import com.zzj.xiaozhan.R.drawable;
import com.zzj.xiaozhan.R.id;
import com.zzj.xiaozhan.R.layout;
import com.zzj.xiaozhan.R.menu;
import com.zzj.xiaozhan.R.string;
import com.zzj.xiaozhan.adapter.DrawerListAdapter;
import com.zzj.xiaozhan.fragment.ComicKuaiDiFragment;
import com.zzj.xiaozhan.fragment.HotComicFragment;
import com.zzj.xiaozhan.fragment.MyFavorFragment;
import com.zzj.xiaozhan.fragment.SettingFragment;
import com.zzj.xiaozhan.fragment.ShouYeFragment;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends Activity {

	private String[] mFragmentTitles;
	private DrawerLayout mDrawerLayout;
	private LinearLayout drawerView;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private DrawerListAdapter drawerListAdapter;
	private HotComicFragment hotComicFragment;
	private ComicKuaiDiFragment comicKuaiDiFragment;
	private MyFavorFragment myFavorFragment;
	private SettingFragment settingFragment;
	private ShouYeFragment shouYeFragment;
	/**
	 * 个人管理
	 */
	private ImageView loginImage;
	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 设置抽屉导航
		mTitle = mDrawerTitle = getTitle();
		mFragmentTitles = getResources().getStringArray(R.array.drawer_item);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerView = (LinearLayout) findViewById(R.id.drawer_view);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		loginImage = (ImageView) findViewById(R.id.user_login);
		//用于事物管理
		fragmentManager = getFragmentManager();
		// 为list view设置adapter
		drawerListAdapter = new DrawerListAdapter(this);
		mDrawerList.setAdapter(drawerListAdapter);
		// 为list设置click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		// 实现drawer的打开关闭功能
		mDrawerToggle = new ActionBarDrawerToggle(this, /* 承载 Activity */
		mDrawerLayout, /* DrawerLayout 对象 */
		R.drawable.ic_drawer, /* 图标用来替换'Up'符号 */
		R.string.drawer_open, /* "打开 drawer" 描述 */
		R.string.drawer_close) { /* "关闭 drawer" 描述 */

			/** 当drawer处于完全关闭的状态时调用 */
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // 创建对onPrepareOptionsMenu()的调用
			}

			/** 当drawer处于完全打开的状态时调用 */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // 创建对onPrepareOptionsMenu()的调用
			}

		};
		// 设置drawer触发器为DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		//设置默认是首页
		mDrawerList.setItemChecked(0, true);
		setFragmentSelection(0);
		setTitle("首页");
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// 在onRestoreInstanceState发生后，同步触发器状态.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 将事件传递给ActionBarDrawerToggle, 如果返回true，表示app 图标点击事件已经被处理
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// 处理你的其他action bar items...

		return super.onOptionsItemSelected(item);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			setFragmentSelection(position);
		}
	}

	/**
	 * 根据传入的index参数来设置选中的fragment页。
	 * 
	 * @param index
	 *            每个fragment页对应的下标。
	 */
	public void setFragmentSelection(int position) {
		// TODO Auto-generated method stub
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (position) {
		case 0:
			if (shouYeFragment == null) {

				shouYeFragment = new ShouYeFragment();
				transaction.add(R.id.content_frame, shouYeFragment);
			} else {

				transaction.show(shouYeFragment);
			}
			break;
		case 1:
			if (hotComicFragment == null) {
				hotComicFragment = new HotComicFragment();
				transaction.add(R.id.content_frame, hotComicFragment);
			} else {
				transaction.show(hotComicFragment);
			}
			break;
		case 2:
			if (comicKuaiDiFragment == null) {
				comicKuaiDiFragment = new ComicKuaiDiFragment();
				transaction.add(R.id.content_frame, comicKuaiDiFragment);
			} else {
				transaction.show(comicKuaiDiFragment);
			}
			break;
		case 3:
			if (myFavorFragment == null) {
				myFavorFragment = new MyFavorFragment();
				transaction.add(R.id.content_frame, myFavorFragment);
			} else {
				transaction.show(myFavorFragment);
			}
			break;
		default:
			if (settingFragment == null) {
				settingFragment = new SettingFragment();
				transaction.add(R.id.content_frame, settingFragment);
			} else {
				transaction.show(settingFragment);
			}
			break;
		}
		transaction.commit();

		// 高亮被选择的item, 更新标题, 并关闭drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mFragmentTitles[position]);
		mDrawerLayout.closeDrawer(drawerView);

	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		// TODO Auto-generated method stub

		if (shouYeFragment != null) {
			transaction.hide(shouYeFragment);
		}
		if (hotComicFragment != null) {
			transaction.hide(hotComicFragment);
		}
		if (comicKuaiDiFragment != null) {
			transaction.hide(comicKuaiDiFragment);
		}
		if (myFavorFragment != null) {
			transaction.hide(myFavorFragment);
		}
		if (settingFragment != null) {
			transaction.hide(settingFragment);
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle("mTitle");
	}

	/**
	 * 用户登陆或者查看用户信息
	 * @param view
	 */
	public void toLogin(View view){
		
		Intent intent = new Intent(MainActivity.this, UserInformation.class);
		startActivity(intent);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private void onOptionsItemSelected() {
		// TODO Auto-generated method stub

	}

}
