package com.ainur.hidevk.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ChatActivity extends SherlockFragmentActivity {

	private ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pager = new ViewPager(this);
		pager.setId(68465468);
		pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
		setContentView(pager);

	}
}
