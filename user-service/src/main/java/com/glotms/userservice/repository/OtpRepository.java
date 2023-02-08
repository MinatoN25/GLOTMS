package com.glotms.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.glotms.userservice.model.OtpData;
@Repository
public interface OtpRepository extends MongoRepository<OtpData, String>{
	
	

}
