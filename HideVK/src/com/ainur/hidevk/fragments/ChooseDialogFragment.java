package com.ainur.hidevk.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

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

import com.actionbarsherlock.app.SherlockListFragment;
import com.ainur.hidevk.HideVkApp;
import com.ainur.hidevk.activity.LoginActivity;
import com.ainur.hidevk.adapters.DialogsAdapter;
import com.ainur.hidevk.models.Dialog;
import com.ainur.hidevk.util.DatabaseHelper;
import com.ainur.hidevk.util.DatabaseHelper.ErrorListener;
import com.ainur.hidevk.util.Log;
import com.ainur.hidevk.util.Persistance;
import com.ainur.hidevk.vk.DialogResponse;
import com.ainur.hidevk.vk.Vkontakte;
import com.j256.ormlite.dao.Dao;

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
		loadData();
	}

	private void loadData() {
		Log.d("Load Data");
		List<Dialog> dialogs = DatabaseHelper.getInstance().getDialogs();
		Log.d("Dialogs:" + dialogs.size());
		if (dialogs == null || dialogs.size() == 0) {
			loadDialogs();
		} else {
			// TODO set Data to adapter
			Log.d("set data from DATABASE");
			for (Dialog d : dialogs) {
				Log.d(d.body);
			}
			setAdapterData(dialogs);
		}
	}

	private void setAdapterData(List<Dialog> dialogs) {
		if (getListAdapter() == null) {
			setListAdapter(new DialogsAdapter(getSherlockActivity(),
					dialogs));
		} else {
			getListAdapter().setDialogs(dialogs);
		}
	}

	private void startLoginActivity() {
		Intent intent = new Intent(HideVkApp.getContext(), LoginActivity.class);
		startActivityForResult(intent, LOGIN_VK_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case LOGIN_VK_CODE:
			if (resultCode == Activity.RESULT_OK) {
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

	private void insertData(final List<Dialog> dialogs) {
		DatabaseHelper.getInstance().runTransactionInBg(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				Dao<Dialog, Integer> audioDao = DatabaseHelper.getInstance()
						.getDialogsDao();
				audioDao.deleteBuilder().delete();
				for (Dialog d : dialogs) {
					audioDao.create(d);
				}
				Log.d("Success in writing in DB");
				return null;
			}
		}, new ErrorListener() {

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				Log.d("Error during transaction");
			}
		});
	}

	private void loadDialogs() {
		Vkontakte.get().getDialogs(new Callback<DialogResponse>() {
			@Override
			public void success(DialogResponse arg0, Response arg1) {
				Log.d("Download dialogs Success");
				int i = 1;
				for (Dialog d : arg0.response) {
					Log.d(i + ": " + d.body);
				}
				List<Dialog> dialogs = arg0.response;
				if (dialogs != null) {
					if (dialogs.size() > 1) {
						dialogs = dialogs.subList(1, dialogs.size());
					} else {
						dialogs = new ArrayList<Dialog>();
					}
					setAdapterData(dialogs);
					insertData(dialogs);
				}
			}

			@Override
			public void failure(RetrofitError arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	@Override
	public DialogsAdapter getListAdapter() {
		return (DialogsAdapter)super.getListAdapter();
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
//		String[] countries = new String[] { "India", "Pakistan", "Sri Lanka",
//				"China", "Bangladesh", "Nepal", "Afghanistan", "North Korea",
//				"South Korea", "Japan" };
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//				inflater.getContext(), android.R.layout.simple_list_item_1,
//				countries);
//		setListAdapter(adapter);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
