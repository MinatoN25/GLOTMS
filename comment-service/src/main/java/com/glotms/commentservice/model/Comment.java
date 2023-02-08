package com.glotms.commentservice.model;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

	@Id
	private String id;
	private String ticketId;
	private String email;
	private String text;
	private List<Binary> attachments;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

}
