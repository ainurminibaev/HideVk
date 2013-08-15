package com.ainur.hidevk.models;

import java.io.IOException;

import android.text.Html;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
	@JsonDeserialize(using = HtmlParser.class)
	public String date;

	// message state: 0-not read 1 - read or null if this message was resended
	@DatabaseField(canBeNull = true)
	@JsonProperty("read_state")
	public Integer readState;

	// 0 - inbox 1 - outbox
	@DatabaseField(canBeNull = true)
	public Integer out;

	@DatabaseField(canBeNull = true)
	@JsonDeserialize(using = HtmlParser.class)
	public String title;

	@DatabaseField(canBeNull = true)
	@JsonProperty("photo_50")
	@JsonDeserialize(using = HtmlParser.class)
	public String photo50;

	@DatabaseField(canBeNull = true)
	@JsonDeserialize(using = HtmlParser.class)
	public String body;

	public Dialog() {
	}

	public Dialog(int count) {

	}

	public static class HtmlParser extends JsonDeserializer<String> {

		@Override
		public String deserialize(JsonParser arg0, DeserializationContext arg1)
				throws IOException, JsonProcessingException {
			return Html.fromHtml(arg0.getValueAsString()).toString();
		}
	}

}
