package com.ainur.hidevk.vk;

import java.util.List;

import com.ainur.hidevk.models.Dialog;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class DialogResponse extends Response {
	public List<Dialog> response;
}
