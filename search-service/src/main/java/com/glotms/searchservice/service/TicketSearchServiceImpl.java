package com.glotms.searchservice.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glotms.searchservice.dto.SearchDTO;
import com.glotms.searchservice.model.Ticket;
import com.glotms.searchservice.repository.TicketRepository;
import com.glotms.searchservice.utils.SearchConstants;
import com.glotms.searchservice.utils.SearchUtil;

@Service
public class TicketSearchServiceImpl implements TicketSearchService {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private TicketRepository ticketRepository;
	private final RestHighLevelClient client;

	private static final Logger log = LoggerFactory.getLogger(TicketSearchServiceImpl.class);

	@Autowired
	public TicketSearchServiceImpl(TicketRepository ticketRepository, RestHighLevelClient client) {
		this.ticketRepository = ticketRepository;
		this.client = client;
		MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	
	@Override
	public List<Ticket> searchCreatedSince(SearchDTO dto) {
		final SearchRequest request = SearchUtil.buildSearchRequest(SearchConstants.TICKET_INDEX, dto);

		return searchInternal(request);
	}

	private List<Ticket> searchInternal(SearchRequest request) {
		if (request == null) {
			log.error("Failed to build search request");
			return Collections.emptyList();
		}

		try {
			final SearchResponse response = client.search(request, RequestOptions.DEFAULT);

			final SearchHit[] searchHits = response.getHits().getHits();
			
			final List<Ticket> tickets = new ArrayList<>(searchHits.length);
			
			for (SearchHit hit : searchHits) {
				tickets.add(MAPPER.readValue(hit.getSourceAsString(), Ticket.class));
			}

			return tickets;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Collections.emptyList();
		}
	}
}
