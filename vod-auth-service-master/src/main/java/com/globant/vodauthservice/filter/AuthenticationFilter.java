package com.globant.vodauthservice.filter;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.stereotype.Component;

import com.globant.vodauthservice.exception.UnAuthorizedException;
import com.globant.vodauthservice.service.GoogleAuthService;
import com.globant.vodauthservice.vo.TokenDetails;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * @author anuruddh.yadav
 *
 */
@Component
public class AuthenticationFilter extends ZuulFilter {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GoogleAuthService googleauthService;

	@Override
	public Object run() throws ZuulException {

		String requestURI = RequestContext.getCurrentContext().getRequest().getRequestURI();

		if (!requestURI.contains("/admin/")) {
			validateToken();

		}
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;

	}

	@Override
	public int filterOrder() {
		return -2;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	private void validateToken() {
		HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
		String authorizationHeader = request.getHeader("Authorization");
		String token = authorizationHeader.replaceFirst("Bearer ", "").trim();
		TokenDetails tokenDetails = null;
		try {
			tokenDetails=googleauthService.validateToken(token);
		} catch (UnAuthorizedException e) {
			//e.printStackTrace();
		}

		if (null == tokenDetails) {
			RequestContext ctx = RequestContext.getCurrentContext();
			ctx.set("error.status_code", HttpStatus.SC_UNAUTHORIZED);
			throw new ZuulRuntimeException(
					new ZuulException("Please login to continue.", HttpStatus.SC_UNAUTHORIZED, "User not logged in."));
		}

		RequestContext.getCurrentContext().getZuulRequestHeaders().put("userEmail",tokenDetails.getEmailId());
	}
}
