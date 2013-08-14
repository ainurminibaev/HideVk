package com.ainur.hidevk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.ainur.hidevk.util.Log;
import com.ainur.hidevk.util.Persistance;
import com.ainur.hidevk.vk.Vkontakte;

public class LoginActivity extends SherlockFragmentActivity {
	private final WebViewClient webViewClient = new WebViewClient() {
		public void onPageFinished(WebView view, String url) {
			if(url.startsWith(Vkontakte.REDIRECT_URL)){
				Persistance.saveVK(Vkontakte.parseRedirectUrl(url));
				setResult(RESULT_OK, new Intent());
				finish();
			}
		};

		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			Log.d("url:"+url);
			return true;
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView view = new WebView(this);
		view.setId(100101123);
		view.setWebViewClient(webViewClient);
		setContentView(view);
		view.loadUrl(Vkontakte.authUrl());
	}
}
