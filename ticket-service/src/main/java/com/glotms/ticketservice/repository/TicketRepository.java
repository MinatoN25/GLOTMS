package com.glotms.ticketservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.glotms.ticketservice.model.Ticket;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {


}
