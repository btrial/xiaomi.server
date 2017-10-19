package com.btrial.xiaomi.server.entity;

public class ResponseData {
	
	private String command;
	private boolean success;
	private String text;

	public ResponseData() {
	}
	
	public ResponseData(boolean success, String text) {
		this.success = success;
		this.text = text;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	
}