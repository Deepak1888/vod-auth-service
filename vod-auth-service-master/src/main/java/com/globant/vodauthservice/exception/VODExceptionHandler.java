package com.globant.vodauthservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author anuruddh.yadav
 *
 */
@RestControllerAdvice
public class VODExceptionHandler extends ResponseEntityExceptionHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(VODExceptionHandler.class);

	@ExceptionHandler(ZuulRuntimeException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public @ResponseBody ErrorMessage handleZuulException(ZuulRuntimeException ex) {
		LOGGER.error(ex.getMessage());
		return new ErrorMessage(HttpStatus.UNAUTHORIZED.value(),"Unauthorized","User should be logged in to access this url.");

	}

}
