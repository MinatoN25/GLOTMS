package com.glotms.commentservice.util;

public class CommentConstants {
	
	//Controller Api calls
	public static final String ROOT_PATH = "/comment-service";
	public static final String ADD_COMMENT_PATH = "/addComment/{ticketId}";
	public static final String UPDATE_COMMENT_PATH = "/updateComment/{ticketId}/{commentId}";
	public static final String DELETE_COMMENT_PATH = "/deleteComment/{commentId}";
	public static final String GET_TICKET_COMMENTS_PATH = "/getAllCommentsForTicket";
	
	//Controller Messages
	public static final String CONFLICT_MSG = "Conflict occured";
	
	//Service Messages
	public static final String UNAUTHORIZED_MSG = "You are not authorized to perform this action";
	public static final String COMMENT_NOT_EXIST_MSG = "No comments to show";

}
