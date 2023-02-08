package com.glotms.ticketservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.glotms.ticketservice.model.User;

public interface UserRepository extends MongoRepository<User, String> {
	User findByUserEmail(String userEmail);

}
