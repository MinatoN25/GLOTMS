package com.glotms.ticketservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "ticket_sequences")
public class TicketSequence {

	@Id
	private String projectCode;
	private int seq;
}
