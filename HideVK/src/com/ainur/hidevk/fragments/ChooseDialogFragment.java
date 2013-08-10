package com.ainur.hidevk.fragments;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.SherlockListFragment;
import com.ainur.hidevk.HideVkApp;
import com.ainur.hidevk.activity.LoginActivity;
import com.ainur.hidevk.models.Dialogs;
import com.ainur.hidevk.util.Log;
import com.ainur.hidevk.util.Persistance;
import com.ainur.hidevk.vk.DialogResponse;
import com.ainur.hidevk.vk.Vkontakte;

public class ChooseDialogFragment extends SherlockListFragment {
	private static final int LOGIN_VK_CODE = 101;
	private boolean created;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (created) {
			return;
		}
		onFirstCreated(view);
		created = true;
	}

	private void onFirstCreated(View view) {
		CookieSyncManager.createInstance(HideVkApp.getContext());

		if (CookieManager.getInstance().getCookie("vk.com") == null) {
			startLoginActivity();
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					if (!Persistance.ensureAuth()) {
						getSherlockActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								startLoginActivity();
								return;
							}
						});
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		// TODO Load data to List
		loadData();
	}

	private void loadData() {
		// TODO Auto-generated method stub
		loadDialogs();
	}

	private void startLoginActivity() {
		Intent intent = new Intent(HideVkApp.getContext(), LoginActivity.class);
		startActivityForResult(intent, LOGIN_VK_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case LOGIN_VK_CODE:
			if (resultCode ==	Activity.RESULT_OK) {
				// TODO Ok, we auth vk. load Data
				loadDialogs();
			} else {
				getSherlockActivity().finish();
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void loadDialogs() {
		// TODO Auto-generated method stub
		Vkontakte.get().getDialogs(new Callback<DialogResponse>() {
			
			@Override
			public void success(DialogResponse arg0, Response arg1) {
				int i=1;
				for(Dialogs d:arg0.response){
					Log.d(i+": "+d.body);
				}
			}
			
			@Override
			public void failure(RetrofitError arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		String[] countries = new String[] {
	            "India",
	            "Pakistan",
	            "Sri Lanka",
	            "China",
	            "Bangladesh",
	            "Nepal",
	            "Afghanistan",
	            "North Korea",
	            "South Korea",
	            "Japan"
	    };
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1,countries);
			setListAdapter(adapter);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
