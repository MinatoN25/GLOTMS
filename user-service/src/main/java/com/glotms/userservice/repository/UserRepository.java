package com.glotms.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.glotms.userservice.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, Integer> {
	
	@Query("{'userEmail':?0}")
	User findByUserEmail(String userEmail);

}
