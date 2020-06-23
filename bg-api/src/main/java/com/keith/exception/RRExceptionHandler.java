

package com.keith.exception;

import com.keith.common.exception.RRException;
import com.keith.common.utils.R;
import com.keith.common.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 *
 * @author JohnSon
 */
@RestControllerAdvice
public class RRExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(RRException.class)
	public R handleRRException(RRException e){
		R r = new R();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());
		/*Result r=new Result();
		r.getCode();
		r.getMsg();*/

		return r;
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public R handleDuplicateKeyException(DuplicateKeyException e){
		/*Result r=new Result();*/
		logger.error(e.getMessage(), e);
		return R.error("数据库中已存在该记录");
		//return r.error("数据库中已存在该记录");
	}

	@ExceptionHandler(Exception.class)
	public R handleException(Exception e){
		//Result r=new Result();
		logger.error(e.getMessage(), e);
		return R.error();
		//return r.error();
	}
}
