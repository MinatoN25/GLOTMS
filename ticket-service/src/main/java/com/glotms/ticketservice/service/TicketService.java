package com.glotms.ticketservice.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.glotms.ticketservice.dto.TicketDTO;
import com.glotms.ticketservice.model.Ticket;

public interface TicketService {
	
	Ticket raiseTicket(TicketDTO ticketDto,List<MultipartFile> files, String projectCode, String userEmail);
	Ticket getTicket(String ticketId, String userEmail);
	Ticket updateTicket(TicketDTO ticketDto, String ticketId, String loggedInUser);
	String getNextSequenceOfTicketId(String projectCode);
	TicketDTO jsonToObjectConvertor(String ticket) throws JsonProcessingException;
	

}
