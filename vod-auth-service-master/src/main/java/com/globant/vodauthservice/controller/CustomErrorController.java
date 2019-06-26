package com.globant.vodauthservice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.globant.vodauthservice.vo.ErrorInfo;

@Controller
public class CustomErrorController implements ErrorController {

	@Value("${error.path:/error}")
	private String errorPath;
	

	@Override
	public String getErrorPath() {
		return errorPath;
	}

	@RequestMapping(value = "${error.path:/error}", produces = "application/json")
	public @ResponseBody ResponseEntity<ErrorInfo> error(HttpServletRequest request, HttpServletResponse response) {
		
		ErrorInfo info = new ErrorInfo();

		Integer status = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (null == status) {
			status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		}
		info.setCode(status);

		Throwable exc = (Throwable) request.getAttribute("javax.servlet.error.exception");
		String msg = exc != null ? exc.getMessage() : "Unexpected error occurred";
		info.setMessage(msg);
		return ResponseEntity.status(info.getCode()).body(info);
	}
}