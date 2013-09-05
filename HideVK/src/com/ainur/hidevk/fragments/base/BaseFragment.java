package com.ainur.hidevk.fragments.base;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import butterknife.OnClick;

import com.actionbarsherlock.app.SherlockFragment;
import com.ainur.hidevk.R;
import com.ainur.hidevk.util.Log;
import com.ainur.hidevk.vk.SendMessageResponse;
import com.ainur.hidevk.vk.Vkontakte;

public class BaseFragment extends SherlockFragment {
	@OnClick(R.id.send_message)
	public void send() {
		String text = messageEdit.getText().toString();
		Log.d("send:" + text);
		Vkontakte.get().setMessage(userId, text,
				new Callback<SendMessageResponse>() {

					@Override
					public void failure(RetrofitError arg0) {
						// TODO
						Log.d(arg0.getMessage());
					}

					@Override
					public void success(SendMessageResponse arg0, Response arg1) {
						messageEdit.setText("");
					}
				});
	}
}
