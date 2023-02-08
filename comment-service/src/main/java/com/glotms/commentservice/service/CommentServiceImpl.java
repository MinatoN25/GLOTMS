package com.glotms.commentservice.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glotms.commentservice.dto.CommentDTO;
import com.glotms.commentservice.exception.CommentNotExistException;
import com.glotms.commentservice.exception.UnauthorizedException;
import com.glotms.commentservice.model.Comment;
import com.glotms.commentservice.model.CommentSequence;
import com.glotms.commentservice.repository.CommentRepository;
import com.glotms.commentservice.repository.SequenceRepository;
import com.glotms.commentservice.util.CommentConstants;

@Service
public class CommentServiceImpl implements CommentService {

	private CommentRepository commentRepository;
	private SequenceRepository sequenceRepository;

	@Autowired
	public CommentServiceImpl(CommentRepository commentRepository, SequenceRepository sequenceRepository) {
		this.commentRepository = commentRepository;
		this.sequenceRepository = sequenceRepository;
	}

	@Override
	public synchronized String getNextSequenceOfComment(String ticketId) {
		Optional<CommentSequence> seq = sequenceRepository.findById(ticketId);
		if (!seq.isPresent()) {
			CommentSequence commentSeq = new CommentSequence();
			commentSeq.setTicketId(ticketId);
			commentSeq.setSeq(1);
			sequenceRepository.save(commentSeq);
			return commentSeq.getTicketId() + "-C" + commentSeq.getSeq();
		} else {
			CommentSequence nextSeq = seq.get();
			nextSeq.setSeq(nextSeq.getSeq() + 1);
			sequenceRepository.save(nextSeq);

			return nextSeq.getTicketId() + "-C" + nextSeq.getSeq();
		}
	}

	@Override
	public CommentDTO jsonToObjectConvertor(String comment) throws JsonMappingException, JsonProcessingException {
		CommentDTO jsonComment = new CommentDTO();

		ObjectMapper objectMapper = new ObjectMapper();
		jsonComment = objectMapper.readValue(comment, CommentDTO.class);
		return jsonComment;
	}
	

	@Override
	public Comment addComment(CommentDTO commentDto, List<MultipartFile> file, String ticketId, String userEmail)
			throws IOException {
		Comment newComment = null;
		if (commentDto != null) {
			newComment = CommentDTO.converToEntity(commentDto);
			newComment.setEmail(userEmail);
			newComment.setTicketId(ticketId);
			
			List<Binary> files = file.stream().map(f -> {
				try {
					return new Binary(BsonBinarySubType.BINARY, f.getBytes());
				} catch (IOException e) {

					e.printStackTrace();
				}
				return null;
			}).collect(Collectors.toList());
			
			newComment.setAttachments(files);
			newComment.setId(getNextSequenceOfComment(ticketId));
			newComment.setCreatedDate(LocalDateTime.now());
			commentRepository.save(newComment);
		}
		return newComment;
	}

	@Override
	public Comment deleteComment(String commentId, String userEmail) {
		Optional<Comment> comment = commentRepository.findById(commentId);
		if (comment.isPresent()) {
			if (comment.get().getEmail().equals(userEmail)) {
				commentRepository.deleteById(commentId);
				return comment.get();
			} else {
				throw new UnauthorizedException(CommentConstants.UNAUTHORIZED_MSG);
			}
		}
		throw new CommentNotExistException(CommentConstants.COMMENT_NOT_EXIST_MSG);
	}

	@Override
	public Comment updateComment(CommentDTO commentDto, String commentId, String userEmail) {
		Optional<Comment> commentO = commentRepository.findById(commentId);
		if (commentO.isPresent()) {
			if (commentO.get().getEmail().equals(userEmail)) {
				Comment commentU = CommentDTO.converToEntity(commentDto);
				commentU.setId(commentId);
				commentU.setTicketId(commentO.get().getTicketId());
				commentU.setCreatedDate(commentO.get().getCreatedDate());
				commentU.setEmail(userEmail);
				commentU.setUpdatedDate(LocalDateTime.now());
				return commentRepository.save(commentU);
			} else {
				throw new UnauthorizedException(CommentConstants.UNAUTHORIZED_MSG);
			}
		}
		throw new CommentNotExistException(CommentConstants.COMMENT_NOT_EXIST_MSG);
	}

	@Override
	public List<Comment> getAllCommentsForTicketId(String ticketId) {
		List<Comment> comments = commentRepository.findByTicketId(ticketId);
		if (!comments.isEmpty()) {
			return comments;
		}
		throw new CommentNotExistException(CommentConstants.COMMENT_NOT_EXIST_MSG);
	}

}
