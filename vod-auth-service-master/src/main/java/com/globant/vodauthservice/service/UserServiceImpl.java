package com.globant.vodauthservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.globant.vodauthservice.domain.User;
import com.globant.vodauthservice.repository.UserRepository;

/**
 * @author anuruddh.yadav
 *
 */
@Service
public class UserServiceImpl implements UserService {

   @Autowired
   private UserRepository userRepository;
	public void saveUser(User user) {
		userRepository.save(user);
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmailId(email);
	}
}
