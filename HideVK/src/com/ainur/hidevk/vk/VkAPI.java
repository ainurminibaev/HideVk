package com.ainur.hidevk.vk;

import retrofit.Callback;
import retrofit.http.GET;

public interface VkAPI {
	
	public static final String SERVER = "https://api.vk.com/method";
	
	@GET("/messages.getDialogs")
	void getDialogs(Callback<MessageResponse> cb);
}
