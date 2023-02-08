package com.glotms.searchservice.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glotms.searchservice.model.Ticket;
import com.glotms.searchservice.repository.TicketRepository;

@Component
public class KafkaConsumer {

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	public KafkaConsumer(TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
	}

	@KafkaListener(topics = "addTicketTopic")
	public void ticketListener(String ticketData) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Ticket ticket = mapper.readValue(ticketData, Ticket.class);
			System.out.println("Ticket received" + ticket);
			ticketRepository.save(ticket);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception occured in listner");
		}

	}

}
