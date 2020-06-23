package com.keith.common.exception;

import com.keith.common.utils.MessageUtils;

/**
 * 自定义异常
 *
 * @author JohnSon
 */
public class TokenRRException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private int code;
	private String msg;

	public TokenRRException(int code) {
		this.code = code;
		this.msg = MessageUtils.getMessage(code);
	}

	public TokenRRException(int code, String... params) {
		this.code = code;
		this.msg = MessageUtils.getMessage(code, params);
	}

	public TokenRRException(int code, Throwable e) {
		super(e);
		this.code = code;
		this.msg = MessageUtils.getMessage(code);
	}

	public TokenRRException(int code, Throwable e, String... params) {
		super(e);
		this.code = code;
		this.msg = MessageUtils.getMessage(code, params);
	}

	public TokenRRException(String msg) {
		super(msg);
		this.code = ErrorCode.TOKEN_FAILE;
		this.msg = msg;
	}

	public TokenRRException(String msg, Throwable e) {
		super(msg, e);
		this.code = ErrorCode.TOKEN_FAILE;
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
