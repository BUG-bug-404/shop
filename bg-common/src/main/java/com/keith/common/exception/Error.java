package com.keith.common.exception;

import com.keith.common.utils.MessageUtils;

/**
 * 自定义异常
 *
 * @author JohnSon
 */
public class Error extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private int code;
	private String msg;

	public Error(int code) {
		this.code = code;
		this.msg = MessageUtils.getMessage(code);
	}

	public Error(int code, String... params) {
		this.code = code;
		this.msg = MessageUtils.getMessage(code, params);
	}

	public Error(int code, Throwable e) {
		super(e);
		this.code = code;
		this.msg = MessageUtils.getMessage(code);
	}

	public Error(int code, Throwable e, String... params) {
		super(e);
		this.code = code;
		this.msg = MessageUtils.getMessage(code, params);
	}

	public Error(String msg) {
		super(msg);
		this.code = ErrorCode.error;
		this.msg = msg;
	}

	public Error(String msg, Throwable e) {
		super(msg, e);
		this.code = ErrorCode.error;
		this.msg = msg;
	}


	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
