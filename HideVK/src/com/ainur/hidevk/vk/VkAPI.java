package com.ainur.hidevk.vk;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface VkAPI {
	
	public static final String SERVER = "https://api.vk.com/method";
	
	@GET("/messages.getDialogs")
	void getDialogs(Callback<DialogResponse> cb);
	@GET("/friends.get?&order=hints&fields=photo_50")
	void getFriendsList(@Query("user_id")int userId, Callback<FriendsResponse> cb);
}
