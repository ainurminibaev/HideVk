package com.ainur.hidevk.adapters;

import java.sql.Date;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Context;
import android.graphics.Bitmap;
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
import com.ainur.hidevk.models.User;
import com.ainur.hidevk.util.DatabaseFriendsHelder;
import com.ainur.hidevk.util.Log;
import com.ainur.hidevk.vk.FriendsResponse;
import com.ainur.hidevk.vk.Vkontakte;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

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

		private String url;

		private SimpleImageLoadingListener listener;

		public ViewHolder(View view) {
			Views.inject(this, view);
			listener = new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					// TODO Auto-generated method stub
					if (imageUri.equals(url)) {
						image.setImageBitmap(loadedImage);
					}
					super.onLoadingComplete(imageUri, view, loadedImage);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO show stub image
					Log.d("loading failed:" + failReason.toString());
					image.setImageResource(R.drawable.ic_launcher);
					super.onLoadingFailed(imageUri, view, failReason);
				}
			};
		}

		@Override
		public String toString() {
			return dialog.toString();
		}

		public void updateNames(User user2) {
			user.setText(user2.toString());
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
		String stringTime = DateFormat.getTimeInstance(DateFormat.SHORT,
				Locale.ROOT).format(date);
		holder.date.setText(stringTime);
		holder.dialog = dialog;
		holder.text.setText(dialog.body);
		String userName = DatabaseFriendsHelder.getInstance().getUserName(
				dialog.uid);
		loadImage(dialog, holder);
		if (userName == null) {
			holder.user.setText("Айнур Минибаев");
		} else {
			holder.user.setText(userName);
		}
		return contentView;
	}

	private void loadImage(Dialog dialog, final ViewHolder holder) {
		if (dialog.photo50 == null) {
			String url = DatabaseFriendsHelder.getInstance().getFriendImageURL(
					dialog.uid);
			holder.url = url;
			if (url == null) {
				Vkontakte.get().getUserInfo(dialog.uid,
						new Callback<FriendsResponse>() {

							@Override
							public void success(FriendsResponse arg0,
									Response arg1) {
								User user = arg0.response.get(0);
								Log.d(user.photoUrl + " my photo");
								holder.url = user.photoUrl;
								ImageLoader.getInstance().loadImage(
										user.photoUrl, holder.listener);
								user.isFriend = User.NOT_FRIEND;
								user.prepare();
								holder.updateNames(user);
								DatabaseFriendsHelder.getInstance().addFriend(
										user);
							}

							@Override
							public void failure(RetrofitError arg0) {
								Log.d("Fail:" + arg0.getMessage());
							}
						});
			} else {
				ImageLoader.getInstance().loadImage(url, holder.listener);
			}
		} else {
			holder.url = dialog.photo50;
			ImageLoader.getInstance()
					.loadImage(dialog.photo50, holder.listener);
		}
	}

}
