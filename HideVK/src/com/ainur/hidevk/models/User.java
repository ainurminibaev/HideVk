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
public class User {
	public static final String USER_ID = "id";
	public static final int FRIEND = 1;
	public static final int NOT_FRIEND = 0;
	@JsonProperty("user_id")
	@DatabaseField(unique = true, id = true, columnName = USER_ID)
	public int id;

	@DatabaseField
	@JsonProperty("first_name")
	@JsonDeserialize(using = HtmlParser.class)
	public String firstName;

	@DatabaseField
	@JsonProperty("last_name")
	@JsonDeserialize(using = HtmlParser.class)
	public String lastName;

	@DatabaseField
	@JsonProperty("photo_50")
	@JsonDeserialize(using = HtmlParser.class)
	public String photoUrl;

	@DatabaseField
	public int isFriend;
	
	public int uid;

	public static class HtmlParser extends JsonDeserializer<String> {

		@Override
		public String deserialize(JsonParser arg0, DeserializationContext arg1)
				throws IOException, JsonProcessingException {
			return Html.fromHtml(arg0.getValueAsString()).toString();
		}
	}
	
	public void prepare(){
		id = uid;
	}

	public User() {

	}

	@Override
	public String toString() {
		return firstName + " - " + lastName;
	}

}
