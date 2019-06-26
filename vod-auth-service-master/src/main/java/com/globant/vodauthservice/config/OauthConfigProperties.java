package com.globant.vodauthservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author anuruddh.yadav
 *
 */
@ConfigurationProperties(prefix="oauth")
public class OauthConfigProperties {

	private String clientId;
	
	private String secretKey;
	
	private String authUri;
	
	private String tokenUri;

	private String redirectUri;
	
	private String grantType;
	
	private String tokenValidationURL;
	
	
	public String getTokenValidationURL() {
		return tokenValidationURL;
	}

	public void setTokenValidationURL(String tokenValidationURL) {
		this.tokenValidationURL = tokenValidationURL;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getAuthUri() {
		return authUri;
	}

	public void setAuthUri(String authUri) {
		this.authUri = authUri;
	}

	public String getTokenUri() {
		return tokenUri;
	}

	public void setTokenUri(String tokenUri) {
		this.tokenUri = tokenUri;
	}
	
	
}
