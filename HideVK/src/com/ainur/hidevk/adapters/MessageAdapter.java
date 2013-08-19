package com.ainur.hidevk.adapters;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.Views;

import com.ainur.hidevk.R;
import com.ainur.hidevk.models.Message;

public class MessageAdapter extends BaseAdapter {
	private static final int LEFT_ID = 0;
	private LayoutInflater inflater;
	private List<Message> messages;

	public MessageAdapter(Context context, List<Message> messages) {
		inflater = LayoutInflater.from(context);
		this.messages = messages;
		Collections.reverse(messages);
	}

	@Override
	public int getCount() {
		return messages == null ? 0 : messages.size();
	}

	@Override
	public Message getItem(int index) {
		if (messages != null) {
			return messages.get(index);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return getItem(arg0).id;
	}

	public static class ViewHolder {

		@InjectView(R.id.message_text_left)
		TextView textViewLeft;
		
		@InjectView(R.id.message_text_right)
		TextView textViewRight;
		
		@InjectView(R.id.left_layout)
		LinearLayout layoutLeft;
		
		@InjectView(R.id.right_layout)
		LinearLayout layoutRight;
		
		public ViewHolder(View contentView) {
			Views.inject(this, contentView);
		}

		
		public void checkVisibility(Message message){
			if(message.out==LEFT_ID){
				layoutRight.setVisibility(View.GONE);
				layoutLeft.setVisibility(View.VISIBLE);
				textViewLeft.setText(message.body);
			} else {
				layoutRight.setVisibility(View.VISIBLE);
				layoutLeft.setVisibility(View.GONE);
				textViewRight.setText(message.body);
			}
		}

	}

	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		if (contentView == null) {
			contentView = inflater.inflate(R.layout.message_item, parent,
					false);
			ViewHolder viewHolder = new ViewHolder(contentView);
			contentView.setTag(viewHolder);
		}
		Message message = getItem(position);
		message.body.replaceAll("<br>", "/n");
		ViewHolder holder = (ViewHolder)contentView.getTag();
		holder.checkVisibility(message);
		return contentView;
	}
}
