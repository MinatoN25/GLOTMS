package com.glotms.ticketservice.util;

public class TicketConstants {
	
	//Controller Api calls
	public static final String ROOT_PATH = "/ticket-service";
	public static final String RAISE_TICKET_PATH = "/raiseTicket";
	public static final String UPDATE_TICKET_PATH = "/updateTicket/{ticketId}";
	public static final String GET_TICKET_BY_ID_PATH = "/getTicketById/{ticketId}";
	
	//Controller Messages
	public static final String CONFLICT_MSG = "Conflict Occured";
	
	//Service Messages
	public static final String TICKET_ALREADY_EXISTS_MSG = "Ticket already exists in the system";
	public static final String TICKET_DOES_NOT_EXIST_MSG="Ticket with this ticket Id does not exist";

	
}
