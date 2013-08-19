package com.ainur.hidevk.fragments;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import butterknife.InjectView;
import butterknife.Views;

import com.actionbarsherlock.app.SherlockFragment;
import com.ainur.hidevk.R;
import com.ainur.hidevk.adapters.MessageAdapter;
import com.ainur.hidevk.models.Message;
import com.ainur.hidevk.util.Log;
import com.ainur.hidevk.vk.MessageResponse;
import com.ainur.hidevk.vk.Vkontakte;

public class OpenChatFragment extends SherlockFragment {

	@InjectView(R.id.message_edit_open)
	EditText messageEdit;

	@InjectView(R.id.dialogs_list_view)
	ListView listView;
	
	private int userId;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.open_char_fragment, container,
				false);
		Views.inject(this, view);
		messageEdit.setText("Ainur");
		Intent intent = getActivity().getIntent();
		userId = intent.getIntExtra(ChooseDialogFragment.CHAT_USER_ID, 0);
		if (userId == 0) {
			Log.d("0 user id");
			getSherlockActivity().finish();
		}
		loadMessages();
		return view;
	}

	private void loadMessages() {
		Vkontakte.get().getHistory(0, userId, new Callback<MessageResponse>() {

			@Override
			public void success(MessageResponse arg0, Response arg1) {
				Log.d("Success in History loading");
				List<Message> list = arg0.response.subList(1,
						arg0.response.size());
				listView.setAdapter(new MessageAdapter(getSherlockActivity(),list));
				// TODO show data
			}

			@Override
			public void failure(RetrofitError arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		Views.reset(this);
		super.onDestroyView();
	}

}