package com.glotms.commentservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.glotms.commentservice.model.Comment;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
	
	List<Comment> findByTicketId(String ticketId);

}
