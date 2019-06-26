package com.globant.vodauthservice.filter;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class CustomErrorFilter extends ZuulFilter {

	

	private static final String ERROR_STATUS_CODE = "error.status_code";

	@Override
	public String filterType() {
		return "error";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext context = RequestContext.getCurrentContext();
		return !context.containsKey(ERROR_STATUS_CODE);
	}

	@Override
	public Object run() {
		RequestContext context = RequestContext.getCurrentContext();
		context.put(ERROR_STATUS_CODE, HttpStatus.UNAUTHORIZED);
		return null;
	}
}