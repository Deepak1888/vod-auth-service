package com.globant.vodauthservice.service;

import com.globant.vodauthservice.domain.User;

/**
 * @author anuruddh.yadav
 *
 */
public interface UserService {
	public void saveUser(User user);
	public User findByEmail(String email);
}
