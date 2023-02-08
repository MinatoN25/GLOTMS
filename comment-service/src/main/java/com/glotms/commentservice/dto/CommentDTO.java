package com.glotms.commentservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.glotms.commentservice.model.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

	@NotNull(message = "Text can not be null")
	@NotBlank(message = "Text can not be blank")
	private String text;
	
	public static Comment converToEntity(CommentDTO commentDto) {
		Comment comment = new Comment();
		comment.setText(commentDto.getText());
		return comment;
	}
}
