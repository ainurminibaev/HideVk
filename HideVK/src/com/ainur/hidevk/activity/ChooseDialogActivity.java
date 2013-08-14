package com.ainur.hidevk.activity;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.ainur.hidevk.R;

public class ChooseDialogActivity extends SherlockFragmentActivity {
	PullToRefreshAttacher dialogPullToRefreshAttacher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dialogPullToRefreshAttacher = PullToRefreshAttacher.get(this);
		setContentView(R.layout.choose_dialog_layout);
	}

	public PullToRefreshAttacher getPTRAttacher() {
		return dialogPullToRefreshAttacher;
	}
}
