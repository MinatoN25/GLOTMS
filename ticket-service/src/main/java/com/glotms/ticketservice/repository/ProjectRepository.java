package com.glotms.ticketservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.glotms.ticketservice.model.Project;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
	
	@Query("{'users':?0}")
	Project findAccessForUser(String userEmail);

}
