package com.glotms.commentservice.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.glotms.commentservice.dto.CommentDTO;
import com.glotms.commentservice.model.Comment;

public interface CommentService {
	
	Comment addComment(CommentDTO commentDto, List<MultipartFile> file, String ticketId, String userEmail) throws IOException;
	Comment deleteComment(String commentId, String userEmail);
	List<Comment> getAllCommentsForTicketId(String ticketId);
	Comment updateComment(CommentDTO commentDto,String commentId, String userEmail);
	String getNextSequenceOfComment(String ticketId);
	CommentDTO jsonToObjectConvertor(String comment) throws JsonMappingException, JsonProcessingException;

}
