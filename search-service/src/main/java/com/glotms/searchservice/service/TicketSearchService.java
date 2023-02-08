package com.glotms.searchservice.service;

import java.util.List;

import com.glotms.searchservice.dto.SearchDTO;
import com.glotms.searchservice.model.Ticket;

public interface TicketSearchService {
	
	List<Ticket> searchCreatedSince(SearchDTO dto);

}
