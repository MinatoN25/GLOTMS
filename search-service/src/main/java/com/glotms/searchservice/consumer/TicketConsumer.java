package com.glotms.searchservice.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.glotms.searchservice.config.MQConfig;
import com.glotms.searchservice.model.Ticket;
import com.glotms.searchservice.repository.TicketRepository;

@Component
public class TicketConsumer {
	
	private TicketRepository ticketRepository;
	
	@Autowired
	public TicketConsumer(TicketRepository ticketRepository) {
		this.ticketRepository=ticketRepository;
	}
	
	@RabbitListener(queues = MQConfig.TICKET_QUEUE)
	public void ticketListener(Ticket ticket) {
		try {
			System.out.println("Ticket received"+ ticket);
			ticketRepository.save(ticket);
		}catch(Exception e){
			System.out.println("Exception occured in listner");
		}
		
	}
	

}
