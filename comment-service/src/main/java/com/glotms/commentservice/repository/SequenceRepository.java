package com.glotms.commentservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.glotms.commentservice.model.CommentSequence;

@Repository
public interface SequenceRepository extends MongoRepository<CommentSequence, String> {
	
}
