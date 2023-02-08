package com.glotms.searchservice.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.glotms.searchservice.dto.SearchDTO;
import com.glotms.searchservice.model.Ticket;
import com.glotms.searchservice.service.ExcelService;
import com.glotms.searchservice.service.TicketSearchService;

@RestController
public class TicketSearchController {

	@Autowired
	private ExcelService fileService;

	private TicketSearchService ticketSearchService;

	@Autowired
	public TicketSearchController(TicketSearchService ticketSearchService) {
		this.ticketSearchService = ticketSearchService;
	}

	@PostMapping("/search")
	public List<Ticket> searchCreatedSince(@RequestBody SearchDTO dto) {
		return ticketSearchService.searchCreatedSince(dto);
	}

	@PostMapping("/download")
	public ResponseEntity<Resource> getFile(@RequestBody List<Ticket> tickets) throws IOException {
		String filename = "tickets.xlsx";
		InputStreamResource file = new InputStreamResource(fileService.load(tickets));

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}
}
