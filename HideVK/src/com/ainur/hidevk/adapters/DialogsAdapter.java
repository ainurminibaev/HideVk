package com.ainur.hidevk.adapters;

import java.sql.Date;
import java.text.DateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.Views;

import com.ainur.hidevk.R;
import com.ainur.hidevk.models.Dialog;
import com.ainur.hidevk.util.DatabaseFriendsHelder;

public class DialogsAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Dialog> dialogs;

	public DialogsAdapter(Context context, List<Dialog> dialogs) {
		this.dialogs = dialogs;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return dialogs == null ? 0 : dialogs.size();
	}

	@Override
	public Dialog getItem(int index) {
		if (dialogs != null) {
			return dialogs.get(index);
		}
		return null;
	}

	public void setDialogs(List<Dialog> list) {
		this.dialogs = list;
		notifyDataSetChanged();
	}

	@Override
	public long getItemId(int index) {
		Dialog dialog = getItem(index);
		return dialog == null ? 0 : dialog.mid;
	}

	public static class ViewHolder {

		@InjectView(R.id.user_name)
		TextView user;

		@InjectView(R.id.shot_message)
		TextView text;

		@InjectView(R.id.time)
		TextView date;

		@InjectView(R.id.userImage)
		ImageView image;

		private Dialog dialog;

		public ViewHolder(View view) {
			Views.inject(this, view);
		}

		@Override
		public String toString() {
			return dialog.toString();
		}
	}

	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		if (contentView == null) {
			contentView = inflater.inflate(R.layout.dialog_item, parent, false);
			ViewHolder viewHolder = new ViewHolder(contentView);
			contentView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) contentView.getTag();

		Dialog dialog = getItem(position);
		long time = Integer.valueOf(dialog.date);
		Date date = new Date(time * 1000);
		String stringTime = DateFormat.getTimeInstance(DateFormat.SHORT)
				.format(date);
		holder.date.setText(stringTime);
		holder.dialog = dialog;
		holder.text.setText(dialog.body);
		String userName = DatabaseFriendsHelder.getInstance().getUserName(dialog.uid);
		if(userName==null){
		holder.user.setText("Айнур Минибаев");
		} else {
			holder.user.setText(userName);
		}
		return contentView;
	}

}
