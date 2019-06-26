package com.globant.vodauthservice.filter;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;

import com.globant.vodauthservice.exception.UnAuthorizedException;
import com.globant.vodauthservice.service.GoogleAuthService;
import com.globant.vodauthservice.vo.GoogleTokenResult;
import com.globant.vodauthservice.vo.TokenDetails;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * @author anuruddh.yadav
 *
 */

public class AuthenticationFilterOld extends ZuulFilter{
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GoogleAuthService googleauthService;

	 // Bearer Tokens from Gmail Actions will always be issued to this authorized party.
    private static final String GMAIL_AUTHORIZED_PARTY = "gmail@system.gserviceaccount.com";

    // Intended audience of the token, based on the sender's domain
    private static final String AUDIENCE = "https:/localhost:4200";
    
	@Override
	public Object run() throws ZuulException {
		
		String requestURI = RequestContext.getCurrentContext().getRequest().getRequestURI();
		
		if (!requestURI.contains("/admin/")) {
			RequestContext currentContext = RequestContext.getCurrentContext();
			HttpServletRequest request = currentContext.getRequest();
			
			HttpServletResponse response=currentContext.getResponse();
			try {

				validateToken(request,response);
				// filterChain.doFilter(request, response);

			} catch (UnAuthorizedException e) {
				logger.warn(e.getMessage());

				try {
					currentContext.getResponse().sendError(HttpStatus.SC_UNAUTHORIZED, e.getMessage());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public boolean shouldFilter() {
//		String requestURI = RequestContext.getCurrentContext().getRequest().getRequestURI();
//		if (requestURI.contains("/admin/")) {
//			return true;
//		} else {
//			return true;
//		}
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

	private void validateToken(HttpServletRequest request,HttpServletResponse response) throws UnAuthorizedException, ZuulException {
		
		Cookie[] cookies = request.getCookies();
		Cookie uidCookie=null;
		Cookie userCookie=null;
		if (cookies != null) {
			 for (Cookie cookie : cookies) {
			   if (cookie.getName().equals("uid")) {
				   uidCookie=cookie;
			    }
			   if (cookie.getName().equals("user")) {
				   userCookie=cookie;
			    }
			  }
			}
		
		
		if (null==uidCookie ) {
			//throw new UnAuthorizedException("User is not authorized to access this url. Please login and try again.");
			RequestContext ctx = RequestContext.getCurrentContext();
			ctx.set("error.status_code", HttpStatus.SC_UNAUTHORIZED);
			throw new ZuulRuntimeException(new ZuulException("Please login to continue.", HttpStatus.SC_UNAUTHORIZED, "User not logged in."));
		}

		if (null!=uidCookie) {
			//If userCookie which holds token is expired then get the refresh token
			GoogleTokenResult token=null;
			if(null==userCookie ||( null!=userCookie && userCookie.getMaxAge() < System.currentTimeMillis())) {
				 token = googleauthService.getAuthTokenByForUser(uidCookie.getValue());
				
			}
			TokenDetails tokenDetails = googleauthService.validateToken(token.getAccess_token());
			
			//if tokenDetails is not null that is valid then refresh the cookie
			userCookie = new Cookie("user", tokenDetails.getToken());
			userCookie.setMaxAge(tokenDetails.getExpiresInMs()); // set cookie expire time equivalent to expire time of token
			response.addCookie(userCookie);	

		}

		RequestContext.getCurrentContext().getZuulRequestHeaders().put("userEmail", uidCookie.getValue());
	}
	

}
