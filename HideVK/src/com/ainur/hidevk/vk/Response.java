package com.ainur.hidevk.vk;

public class Response {
	public Error error;
	
	public boolean isError() {
		return error != null;
	}
}
