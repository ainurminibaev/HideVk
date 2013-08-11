package com.ainur.hidevk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Dialog {
	public static final String DATE = "date";

	// message id
	@DatabaseField
	public int mid;

	// user id
	@DatabaseField
	public int uid;

	// sending date
	@DatabaseField(canBeNull = true)
	public String date;

	// message state: 0-not read 1 - read or null if this message was resended
	@DatabaseField(canBeNull = true)
	@JsonProperty("read_state")
	public Integer readState;

	// 0 - inbox 1 - outbox
	@DatabaseField(canBeNull = true)
	public Integer out;

	@DatabaseField(canBeNull = true)
	public String title;

	@DatabaseField(canBeNull = true)
	@JsonProperty("photo_50")
	public String photo50;

	@DatabaseField(canBeNull = true)
	public String body;

	public Dialog() {
	}

	public Dialog(int count) {

	}

}
