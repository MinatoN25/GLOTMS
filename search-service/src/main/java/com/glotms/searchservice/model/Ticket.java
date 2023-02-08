package com.glotms.searchservice.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "ticket_data")
public class Ticket {

	@Id
	@Field(type = FieldType.Text)
	private String ticketId;
	@Field(type = FieldType.Text)
	private String department;
	@Field(type = FieldType.Text)
	private String projectCode;
	private IssueType issueType;
	@Field(type = FieldType.Text)
	private String ticketSummary;
	@Field(type = FieldType.Text)
	private String description;
	private String reporter;
	private String assignee;
	@Transient
	private Object[] attachments;
	private Priority priority;
	private String[] label;
	private TicketStatus ticketStatus;
	@Field(type = FieldType.Text)
	private String resolution;
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)  
	@JsonSerialize(using = LocalDateTimeSerializer.class) 
	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	private LocalDateTime sla;
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)  
	@JsonSerialize(using = LocalDateTimeSerializer.class) 
	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	private LocalDateTime createdDate;
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)  
	@JsonSerialize(using = LocalDateTimeSerializer.class) 
	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	private LocalDateTime updatedDate;
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)  
	@JsonSerialize(using = LocalDateTimeSerializer.class) 
	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	private LocalDateTime resolvedDate;
	private Complexity complexity;
	private String createdBy;
	private String updatedBy;
}
