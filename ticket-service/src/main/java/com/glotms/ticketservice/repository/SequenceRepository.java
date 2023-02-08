package com.glotms.ticketservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.glotms.ticketservice.model.TicketSequence;

public interface SequenceRepository extends MongoRepository<TicketSequence, String>{

}
