package com.ainur.hidevk.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.ainur.hidevk.R;

public class MainActivity extends SherlockFragmentActivity {

	private ViewPager pager;

	private final OnPageChangeListener onPageChangeListener = new SimpleOnPageChangeListener() {

		public void onPageSelected(int position) {
			//getSupportActionBar().setSelectedNavigationItem(position);
		};
	};

	private final TabListener tabListener = new TabListener() {

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (pager != null) {
				pager.setCurrentItem(tab.getPosition());
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
//		if (actionBar != null) {
//			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//			actionBar.addTab(actionBar.newTab().setText(R.string.open)
//					.setTabListener(tabListener));
//
//			actionBar.addTab(actionBar.newTab().setText(R.string.hide)
//					.setTabListener(tabListener));
//		}

		pager = new ViewPager(this);
		pager.setId(68465468);
		pager.setOnPageChangeListener(onPageChangeListener);
		pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
		setContentView(pager);

	}
}
