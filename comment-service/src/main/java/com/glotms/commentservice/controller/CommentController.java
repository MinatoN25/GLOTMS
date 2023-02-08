package com.glotms.commentservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.glotms.commentservice.dto.CommentDTO;
import com.glotms.commentservice.exception.CommentNotExistException;
import com.glotms.commentservice.model.Comment;
import com.glotms.commentservice.service.CommentService;
import com.glotms.commentservice.util.CommentConstants;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping(CommentConstants.ROOT_PATH)
public class CommentController {

	private CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@PostMapping(value=CommentConstants.ADD_COMMENT_PATH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> addCommentToTicket(@Valid @RequestPart String commentDto, @RequestPart List<MultipartFile> file,
			@PathVariable String ticketId, @RequestAttribute Claims claims) {
		try {
			CommentDTO comment =commentService.jsonToObjectConvertor(commentDto);
			Comment addedComment = commentService.addComment(comment, file, ticketId, claims.getSubject());
			return new ResponseEntity<>(addedComment, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(CommentConstants.CONFLICT_MSG, HttpStatus.CONFLICT);
		}
	}

	@GetMapping(CommentConstants.GET_TICKET_COMMENTS_PATH)
	public ResponseEntity<?> getAllCommentsForTicket(@RequestParam String ticketId) {
		try {
			return new ResponseEntity<>(commentService.getAllCommentsForTicketId(ticketId), HttpStatus.OK);
		} catch (CommentNotExistException c) {
			return new ResponseEntity<>(c.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>(CommentConstants.CONFLICT_MSG, HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping(CommentConstants.DELETE_COMMENT_PATH)
	public ResponseEntity<?> deleteComment(@RequestParam String commentId, @RequestAttribute Claims claims) {
		try {
			return new ResponseEntity<>(commentService.deleteComment(commentId, claims.getSubject()), HttpStatus.OK);
		} catch (CommentNotExistException c) {
			return new ResponseEntity<>(c.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>(CommentConstants.CONFLICT_MSG, HttpStatus.CONFLICT);
		}
	}

	@PutMapping(CommentConstants.UPDATE_COMMENT_PATH)
	public ResponseEntity<?> editComment(@Valid @RequestBody CommentDTO commentDto, @PathVariable String commentId,
			@RequestAttribute Claims claims) {
		try {
			return new ResponseEntity<>(commentService.updateComment(commentDto, commentId, claims.getSubject()),
					HttpStatus.CREATED);
		} catch (CommentNotExistException c) {
			return new ResponseEntity<>(c.getErrorMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>(CommentConstants.CONFLICT_MSG, HttpStatus.CONFLICT);
		}
	}
}
