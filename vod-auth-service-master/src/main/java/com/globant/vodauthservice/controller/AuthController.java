package com.globant.vodauthservice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.globant.vodauthservice.exception.UnAuthorizedException;
import com.globant.vodauthservice.service.GoogleAuthService;
import com.globant.vodauthservice.vo.TokenDetails;


/**
 * @author anuruddh.yadav
 *
 */
@RestController
public class AuthController {

	@Autowired
	private GoogleAuthService googleAuthService;
 
	
	@GetMapping("/prelogin")
	public ResponseEntity<TokenDetails> logincallback(@RequestParam(name = "code") String code, HttpServletRequest request,
		HttpServletResponse response) throws UnAuthorizedException {
		TokenDetails tokenDetails = googleAuthService.authenticate(code);
		
		
//		Cookie userCookie = new Cookie("user", tokenDetails.getToken());
//		userCookie.setMaxAge(tokenDetails.getExpiresInMs()); // set cookie expire time equivalent to expire time of token
//		response.addCookie(userCookie);	
		
		return new ResponseEntity<>(tokenDetails, HttpStatus.OK);
		//return tokenDetails;
	}
	
}
