package com.globant.vodauthservice.service;


import com.globant.vodauthservice.exception.UnAuthorizedException;
import com.globant.vodauthservice.vo.GoogleTokenResult;
import com.globant.vodauthservice.vo.TokenDetails;

/**
 * @author anuruddh.yadav
 *
 */
public interface GoogleAuthService {

	public TokenDetails authenticate(String code) throws UnAuthorizedException;
	
	public GoogleTokenResult getAuthTokenByForUser(String email);
	
	public TokenDetails validateToken(String token) throws UnAuthorizedException;
	
}
