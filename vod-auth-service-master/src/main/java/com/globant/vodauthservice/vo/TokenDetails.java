package com.globant.vodauthservice.vo;

/**
 * This class represents a google token, this is used for validation of incoming
 * requests.
 *
 *
 */

/**
 * @author anuruddh.yadav
 *
 */
public class TokenDetails {

	
	public static final String TOKEN_DETAILS_KEY = "tokenDetails";
	private String emailId;
	private int expiresInMs;
	private final Long expireDateInMil;
	private String token;
	private String name;
	public TokenDetails(String emailId, int expiresInMs, Long expireDateInMil, String token,String name) {
		this.emailId = emailId;
		this.expiresInMs = expiresInMs;
		this.expireDateInMil = expireDateInMil;
		this.token = token;
		this.name=name;
	}

	public int getExpiresInMs() {
		return expiresInMs;
	}

	public void setExpiresInMs(int expiresInMs) {
		this.expiresInMs = expiresInMs;
	}

	

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public boolean isExpired() {
		return this.expireDateInMil < System.currentTimeMillis();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
