package com.globant.vodauthservice.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.globant.vodauthservice.domain.User;

/**
 * @author anuruddh.yadav
 *
 */
@Repository
public interface UserRepository extends ElasticsearchRepository<User, String> {

	public User findByEmailId(String email);
}
