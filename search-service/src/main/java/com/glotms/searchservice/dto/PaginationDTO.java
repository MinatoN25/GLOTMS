package com.glotms.searchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDTO {
	private static final int DEFAULT_SIZE = 20;

	private int page;
	private int size;

}
