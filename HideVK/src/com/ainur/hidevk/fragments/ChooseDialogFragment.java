package com.ainur.hidevk.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher.OnRefreshListener;
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
import com.ainur.hidevk.activity.ChooseDialogActivity;
import com.ainur.hidevk.activity.LoginActivity;
import com.ainur.hidevk.adapters.DialogsAdapter;
import com.ainur.hidevk.models.Dialog;
import com.ainur.hidevk.models.User;
import com.ainur.hidevk.util.DatabaseDialogHelper;
import com.ainur.hidevk.util.DatabaseDialogHelper.ErrorListener;
import com.ainur.hidevk.util.DatabaseFriendsHelder;
import com.ainur.hidevk.util.Log;
import com.ainur.hidevk.util.Persistance;
import com.ainur.hidevk.vk.DialogResponse;
import com.ainur.hidevk.vk.FriendsResponse;
import com.ainur.hidevk.vk.Vkontakte;
import com.j256.ormlite.dao.Dao;

public class ChooseDialogFragment extends SherlockListFragment {
	private static final int LOGIN_VK_CODE = 101;
	private boolean created;
	private PullToRefreshAttacher dialogPullToRefreshAttacher;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		dialogPullToRefreshAttacher = ((ChooseDialogActivity) getActivity())
				.getPTRAttacher();
		dialogPullToRefreshAttacher.addRefreshableView(getListView(),
				new PTRListener());
		if (created) {
			return;
		}
		onFirstCreated(view);
		created = true;
	}

	private class PTRListener implements OnRefreshListener {

		@Override
		public void onRefreshStarted(View view) {
			Log.d("Refreshing");
			loadDialogs();
			loadFriendsList();
		}

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
		List<Dialog> dialogs = DatabaseDialogHelper.getInstance().getDialogs();
		Log.d("Dialogs:" + dialogs.size());
		if (dialogs == null || dialogs.size() == 0) {
			loadDialogs();
			loadFriendsList();
		} else {
			Log.d("set data from DATABASE");
			for (Dialog d : dialogs) {
				Log.d(d.body);
			}
			setAdapterData(dialogs);
		}
	}

	private void setAdapterData(List<Dialog> dialogs) {
		if (getListAdapter() == null) {
			setListAdapter(new DialogsAdapter(getSherlockActivity(), dialogs));
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
				loadFriendsList();
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
		DatabaseDialogHelper.getInstance().runTransactionInBg(
				new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						Dao<Dialog, Integer> audioDao = DatabaseDialogHelper
								.getInstance().getDialogsDao();
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
				dialogPullToRefreshAttacher.setRefreshComplete();
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

	private void insertFriendList(final List<User> friends) {
		DatabaseFriendsHelder.getInstance().runTransactionInBg(
				new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						Dao<User, Integer> audioDao = DatabaseFriendsHelder
								.getInstance().getFriendsDao();
						audioDao.deleteBuilder().delete();
						for (User u : friends) {
							u.isFriend = User.FRIEND;
							audioDao.create(u);
						}
						Log.d("Success in writing in DB");
						return null;
					}

				}, new ErrorListener() {

					@Override
					public void onError(Exception e) {
						Log.d("error occurred during friends transaction");
					}
				});
	}

	private void loadFriendsList() {
		final boolean empty = DatabaseFriendsHelder.getInstance().isEmptyDB();
		if (!empty) {
			return;
		}
		Vkontakte.get().getFriendsList(Persistance.getUserId(),
				new Callback<FriendsResponse>() {

					@Override
					public void failure(RetrofitError arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void success(FriendsResponse arg0, Response arg1) {
						Log.d("Success loading friends");
						List<User> friends = arg0.response;
						if (empty) {
							loadDialogs();
						}
						insertFriendList(friends);
					}
				});
	}

	@Override
	public DialogsAdapter getListAdapter() {
		return (DialogsAdapter) super.getListAdapter();
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
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
