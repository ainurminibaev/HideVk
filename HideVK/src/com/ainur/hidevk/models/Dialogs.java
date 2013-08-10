package com.ainur.hidevk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Dialogs {
	 public int mid;
	
	 public int uid;
	
	 public String date;
	
	 public int read_state;
	
	 public int out;
	
	 public String title;
	
	 public String body;
	public Dialogs() {
	}

	public Dialogs(int count) {

	}

}
