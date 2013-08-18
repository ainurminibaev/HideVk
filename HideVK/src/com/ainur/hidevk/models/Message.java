package com.ainur.hidevk.models;

import com.ainur.hidevk.models.Dialog.HtmlParser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
	public int id;

	@JsonProperty("uid")
	public int userId;

	public int date;

	@JsonProperty("read_state")
	public int readState;

	public int out;

	@JsonDeserialize(using = HtmlParser.class)
	public String body;

	@Override
	public String toString() {
		return "Message [id=" + id + ", userId=" + userId + ", body=" + body
				+ "]";
	}
	
	public Message(){
		
	}
	
	public Message(int count){
		
	}
}
