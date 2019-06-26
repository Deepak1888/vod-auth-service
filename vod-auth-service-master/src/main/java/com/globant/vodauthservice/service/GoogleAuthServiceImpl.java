package com.globant.vodauthservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.globant.vodauthservice.config.OauthConfigProperties;
import com.globant.vodauthservice.domain.User;
import com.globant.vodauthservice.exception.UnAuthorizedException;
import com.globant.vodauthservice.vo.GoogleTokenResult;
import com.globant.vodauthservice.vo.TokenDetails;
import com.globant.vodauthservice.vo.TokenInfo;


/**
 * @author anuruddh.yadav
 *
 */
@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

	@Autowired
	private OauthConfigProperties oauthProperties;

	@Autowired
	private UserService userService;

	@Override
	public TokenDetails authenticate(String code) throws UnAuthorizedException {

		String payload = getPayLoadForAuthToken(code);

		HttpEntity<Object> requestEntity = getRequestEntity(payload);

		GoogleTokenResult googleTokenResult = new RestTemplate().postForObject(oauthProperties.getTokenUri(),
				requestEntity, GoogleTokenResult.class);

		TokenDetails tokenDetails = validateToken(googleTokenResult.getId_token());

		//saveRefreshToken(tokenDetails.getEmailId(), googleTokenResult.getRefresh_token());

		return tokenDetails;
	}

	private void saveRefreshToken(String emailId, String refresh_token) {

		User user = userService.findByEmail(emailId);
		if (null == user) {
			user = new User();
		}
		user.setEmailId(emailId);
		user.setRefreshToken(refresh_token);

		userService.saveUser(user);
	}

	public TokenDetails validateToken(String token) throws UnAuthorizedException {

		TokenInfo tokenInfo = null;
		try {

			tokenInfo = new RestTemplate().getForObject(oauthProperties.getTokenValidationURL() + token,
					TokenInfo.class);

		} catch (Exception e) {
			throw new UnAuthorizedException("Invalid access token");
		}

		// validateClientIdAndDomain(clientId, tokenInfo.getDomain());
		int expireInMil = tokenInfo.getExpires_in() * 1000;
		Long expires = expireInMil + System.currentTimeMillis();
		String name=tokenInfo.getEmail().split("\\.")[0];
		name=name.substring(0, 1).toUpperCase() + name.substring(1);
		return new TokenDetails(tokenInfo.getEmail(), expireInMil, expires, token,name);

	}

	@Override
	public GoogleTokenResult getAuthTokenByForUser(String email) {
		User user = userService.findByEmail(email);
		String payload = getPayloadForRefresh(user.getRefreshToken());
		return executePayload(payload);
	}

	private GoogleTokenResult executePayload(String payload) {
		HttpEntity<Object> httpEntity = getRequestEntity(payload);
		ResponseEntity<GoogleTokenResult> exchange = new RestTemplate().exchange(oauthProperties.getTokenUri(),
				HttpMethod.POST, httpEntity, GoogleTokenResult.class);
		GoogleTokenResult tokenResult = exchange.getBody();
		return tokenResult;
	}

	private String getPayLoadForAuthToken(String code) {
		StringBuilder payload = new StringBuilder();
		payload.append("code=").append(code).append("&client_id=").append(oauthProperties.getClientId())
				.append("&client_secret=").append(oauthProperties.getSecretKey()).append("&redirect_uri=")
				.append(oauthProperties.getRedirectUri()).append("&grant_type=").append(oauthProperties.getGrantType());
		return payload.toString();
	}

	private String getPayloadForRefresh(String refreshToken) {
		StringBuilder payload = new StringBuilder();
		payload.append("&client_id=").append(oauthProperties.getClientId()).append("&client_secret=")
				.append(oauthProperties.getSecretKey()).append("&refresh_token=").append(refreshToken)
				.append("&grant_type=refresh_token");
		return payload.toString();
	}

	private HttpEntity<Object> getRequestEntity(String payload) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(payload, headers);
		return requestEntity;
	}

}

