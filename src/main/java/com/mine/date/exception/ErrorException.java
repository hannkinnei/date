package com.mine.date.exception;

public class ErrorException extends Exception {
	private String msg;

	public ErrorException() {
		super();
	}

	public ErrorException(String message) {
		super(message);
		this.msg = message;
	}

	public String getMsg() {
		return msg;
	}
}
