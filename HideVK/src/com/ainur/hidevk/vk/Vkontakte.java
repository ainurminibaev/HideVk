package com.ainur.hidevk.vk;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.OkClient;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.ainur.hidevk.util.Jackson;
import com.ainur.hidevk.util.Jackson.JacksonConverter;
import com.ainur.hidevk.util.Log;
import com.ainur.hidevk.util.Persistance;
import com.ainur.hidevk.util.Rest;
import com.ainur.hidevk.util.Utils;

public class Vkontakte {
	public static final int APP_ID = 3790982;
	public static final int SCOPE = 2 + 4096;

	private static class Holder {

		private static final VkAPI VKONTAKTE = new RestAdapter.Builder()
				.setClient(new OkClient(Rest.getHttpClient()))
				.setServer(VkAPI.SERVER)
				.setConverter(new JacksonConverter(Jackson.getObjectMapper()))
				.setRequestInterceptor(new RequestInterceptor() {

					@Override
					public void intercept(RequestFacade arg0) {
						try {
							Persistance.ensureAuth();
						} catch (IOException e) {
							Log.e(e);
						}

						arg0.addQueryParam("access_token",
								Persistance.getAccessToken());

						if (!Utils.isNetworkAvailable()) {
							arg0.addHeader("Cache-Control", "max-stale");
						}
					}
				}).setLog(new RestAdapter.Log() {

					@Override
					public void log(String arg0) {
						 Log.d(arg0);
					}
				}).setLogLevel(LogLevel.HEADERS).build().create(VkAPI.class);
	}

	public static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
	private static final String AUTH_URL = "https://oauth.vk.com/authorize";

	public static Map<String, String> auth() throws IOException {
		return authRecursive(authUrl());
	}

	public static String authUrl() {
		return String
				.format("%s?client_id=%s&scope=%s&redirect_uri=%s&display=touch&response_type=token",
						AUTH_URL, APP_ID, SCOPE, REDIRECT_URL);
	}

	private static Map<String, String> authRecursive(String url)
			throws IOException {
		CookieManager cookieManager = CookieManager.getInstance();

		HttpURLConnection.setFollowRedirects(false);
		HttpURLConnection connection = Rest.getHttpClient().open(new URL(url));
		connection.setRequestProperty("Cookie", cookieManager.getCookie(url));
		connection.connect();

		if (connection.getResponseCode() == 302) {
			cookieManager.setCookie(url,
					connection.getHeaderField("Set-Cookie"));

			String redirect = connection.getHeaderField("Location");

			if (redirect.startsWith(REDIRECT_URL)) {
				CookieSyncManager.getInstance().sync();
				return parseRedirectUrl(redirect);
			}

			return authRecursive(redirect);
		}
		return null;
	}

	public static Map<String, String> parseRedirectUrl(String redirect) {
		String[] params = redirect.split("#")[1].split("&");
		Map<String, String> result = new HashMap<String, String>();
		for (String param : params) {
			String[] pairs = param.split("=");
			result.put(pairs[0], pairs[1]);
		}
		return result;
	}

	public static VkAPI get() {
		return Holder.VKONTAKTE;
	}
}