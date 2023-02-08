package com.glotms.ticketservice.model;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.glotms.ticketservice.enums.Complexity;
import com.glotms.ticketservice.enums.IssueType;
import com.glotms.ticketservice.enums.Priority;
import com.glotms.ticketservice.enums.TicketStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Ticket")
public class Ticket {

	@Id
	private String ticketId;
	private String department;
	private String projectCode;
	private IssueType issueType;
	private String ticketSummary;
	private String description;
	private String reporter;
	private String assignee;
	private Priority priority;
	private String[] label;
	private TicketStatus ticketStatus;
	private String resolution;
	private List<Binary> attachments;
	private Complexity complexity;
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)  
	@JsonSerialize(using = LocalDateTimeSerializer.class) 
	private LocalDateTime sla;
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)  
	@JsonSerialize(using = LocalDateTimeSerializer.class) 
	private LocalDateTime createdDate;
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)  
	@JsonSerialize(using = LocalDateTimeSerializer.class) 
	private LocalDateTime updatedDate;
	private String createdBy;
	private String updatedBy;
	
}
