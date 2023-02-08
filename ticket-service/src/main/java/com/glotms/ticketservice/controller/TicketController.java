package com.glotms.ticketservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.glotms.ticketservice.config.MQConfig;
import com.glotms.ticketservice.dto.TicketDTO;
import com.glotms.ticketservice.exception.TicketAlreadyExistsException;
import com.glotms.ticketservice.exception.TicketNotExistException;
import com.glotms.ticketservice.exception.UnauthorizedException;
import com.glotms.ticketservice.model.Ticket;
import com.glotms.ticketservice.service.TicketService;
import com.glotms.ticketservice.util.TicketConstants;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping(TicketConstants.ROOT_PATH)
public class TicketController {

	private TicketService ticketService;
	private RabbitTemplate rabbitTemplate;

	@Autowired
	public TicketController(TicketService ticketService, RabbitTemplate rabbitTemplate) {
		this.ticketService = ticketService;
		this.rabbitTemplate = rabbitTemplate;
	}

	@PostMapping(value = TicketConstants.RAISE_TICKET_PATH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> raiseTicket(@Valid @RequestPart String ticket, @RequestPart List<MultipartFile> files,
			@RequestParam String projectCode, @RequestAttribute Claims claims)
			throws JsonMappingException, JsonProcessingException {
		try {
			TicketDTO ticketDto = ticketService.jsonToObjectConvertor(ticket);
			Ticket ticketN = ticketService.raiseTicket(ticketDto, files, projectCode, claims.getSubject());
//			rabbitTemplate.convertAndSend(MQConfig.EXCHANGE, MQConfig.TICKET_ROUTING_KEY, ticketN);
			return new ResponseEntity<>(ticketN, HttpStatus.OK);
		} catch (TicketAlreadyExistsException t) {
			return new ResponseEntity<>(t.getMessage(), HttpStatus.CONFLICT);

		} catch (Exception e) {
			return new ResponseEntity<>(TicketConstants.CONFLICT_MSG, HttpStatus.CONFLICT);
		}
	}

	@PutMapping(TicketConstants.UPDATE_TICKET_PATH)
	public ResponseEntity<?> updateTicket(@Valid @RequestBody TicketDTO ticketDto, @PathVariable String ticketId,
			@RequestAttribute Claims claims) {
		try {
			Ticket ticketN = ticketService.updateTicket(ticketDto, ticketId, claims.getSubject());
//			rabbitTemplate.convertAndSend(MQConfig.EXCHANGE, MQConfig.TICKET_ROUTING_KEY, ticketN);
			return new ResponseEntity<>(ticketN, HttpStatus.OK);
		} catch (TicketNotExistException t) {
			return new ResponseEntity<>(t.getErrorMessage(), HttpStatus.CONFLICT);

		} catch (Exception e) {
			return new ResponseEntity<>(TicketConstants.CONFLICT_MSG, HttpStatus.CONFLICT);
		}
	}

	@GetMapping(TicketConstants.GET_TICKET_BY_ID_PATH)
	public ResponseEntity<?> getTicketById(@PathVariable String ticketId, @RequestAttribute Claims claims) {
		try {
			Ticket ticketN = ticketService.getTicket(ticketId, claims.getSubject());
			return new ResponseEntity<>(ticketN, HttpStatus.OK);
		} catch (TicketNotExistException t) {
			return new ResponseEntity<>(t.getMessage(), HttpStatus.CONFLICT);

		} catch (UnauthorizedException t) {
			return new ResponseEntity<>(t.getErrorMessage(), HttpStatus.CONFLICT);

		} catch (Exception e) {
			return new ResponseEntity<>(TicketConstants.CONFLICT_MSG, HttpStatus.CONFLICT);
		}
	}

}
