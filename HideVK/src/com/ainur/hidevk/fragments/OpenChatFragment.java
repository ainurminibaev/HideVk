package com.ainur.hidevk.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.InjectView;
import butterknife.Views;

import com.actionbarsherlock.app.SherlockFragment;
import com.ainur.hidevk.R;

public class OpenChatFragment extends SherlockFragment {

	@InjectView(R.id.message_edit_open)
	EditText messageEdit;

	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab2.xml
		View view = inflater.inflate(R.layout.open_char_fragment, container,
				false);
		Views.inject(this,view);
		messageEdit.setText("Ainur");
		return view;
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