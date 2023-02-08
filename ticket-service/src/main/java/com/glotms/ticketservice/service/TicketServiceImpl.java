package com.glotms.ticketservice.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.glotms.ticketservice.dto.TicketDTO;
import com.glotms.ticketservice.enums.Role;
import com.glotms.ticketservice.enums.Status;
import com.glotms.ticketservice.exception.TicketNotExistException;
import com.glotms.ticketservice.exception.UnauthorizedException;
import com.glotms.ticketservice.model.Ticket;
import com.glotms.ticketservice.model.TicketSequence;
import com.glotms.ticketservice.repository.ProjectRepository;
import com.glotms.ticketservice.repository.SequenceRepository;
import com.glotms.ticketservice.repository.TicketRepository;
import com.glotms.ticketservice.repository.UserRepository;
import com.glotms.ticketservice.util.TicketConstants;

@Service
public class TicketServiceImpl implements TicketService {

	private TicketRepository ticketRepository;
	private ProjectRepository projectRepository;
	private SequenceRepository sequenceRepository;
	private UserRepository userRepository;
	private KafkaTemplate<String, String> kafkaTemplate;
	private ObjectMapper mapper;

	@Autowired
	public TicketServiceImpl(TicketRepository ticketRepository,
			SequenceRepository sequenceRepository,ProjectRepository projectRepository, UserRepository userRepository
			, KafkaTemplate<String, String> kafkaTemplate) {
		this.ticketRepository = ticketRepository;
		this.sequenceRepository = sequenceRepository;
		this.projectRepository = projectRepository;
		this.userRepository = userRepository;
		this.kafkaTemplate = kafkaTemplate;
		this.mapper = new ObjectMapper();
	}

	public <T> void sendMessage(String mailRequest, String topic) {
		Message<String> message = MessageBuilder.withPayload(mailRequest).setHeader(KafkaHeaders.TOPIC, topic).build();
		kafkaTemplate.send(message);

	}
	
	@Override
	public Ticket raiseTicket(TicketDTO ticketDto,List<MultipartFile> files, String projectCode, String userEmail) {
		Ticket ticket= null;
		if (ticketDto != null) {
			ticket = TicketDTO.convertToEntity(ticketDto);
			if(ticket.getSla()==null) {
				switch(ticket.getPriority()) {
				case HIGHEST: ticket.setSla(LocalDateTime.now().plusHours(10)); break;
				case HIGH: ticket.setSla(LocalDateTime.now().plusHours(20)); break;
				case MODERATE: ticket.setSla(LocalDateTime.now().plusHours(30)); break;
				case LOW: ticket.setSla(LocalDateTime.now().plusHours(40)); break;
				case LOWEST: ticket.setSla(LocalDateTime.now().plusHours(50)); break;
				}
			}
			
			List<Binary> attachments = files.stream().map(f -> {
				try {
					return new Binary(BsonBinarySubType.BINARY, f.getBytes());
				} catch (IOException e) {

					e.printStackTrace();
				}
				return null;
			}).collect(Collectors.toList());
			
			ticket.setAttachments(attachments);
			ticket.setProjectCode(projectCode);
			ticket.setTicketId(getNextSequenceOfTicketId(ticket.getProjectCode()));
			ticket.setCreatedDate(LocalDateTime.now());
			ticket.setReporter(userEmail);
			ticket.setCreatedBy(userEmail);
			ticketRepository.save(ticket);
			try {
				sendMessage(mapper.writeValueAsString(ticket), "addTicketTopic");
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return ticket;
	}

	@Override
	public TicketDTO jsonToObjectConvertor(String ticket) throws JsonProcessingException {
		TicketDTO jsonTicket = new TicketDTO();

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		jsonTicket = objectMapper.readValue(ticket, TicketDTO.class);
		return jsonTicket;
	}
	
	@Override
	public Ticket getTicket(String ticketId, String loggedInUser) {
		projectAccess(loggedInUser);
		Optional<Ticket> ticket = ticketRepository.findById(ticketId);
		if (!ticket.isPresent()) {
			throw new TicketNotExistException(TicketConstants.TICKET_DOES_NOT_EXIST_MSG);
		}

		return ticket.get();
	}
	
	private boolean projectAccess(String loggedInUser) {
		if(projectRepository.findAccessForUser(loggedInUser)!=null || userRepository.findByUserEmail(loggedInUser).getRole().equals(Role.SUPER_ADMIN)) {
			return true;
		}
		
		throw new UnauthorizedException("Unauth");
	}

	@Override
	public Ticket updateTicket(TicketDTO ticketDto, String ticketId, String loggedInUser) {
		Optional<Ticket> ticketO = ticketRepository.findById(ticketId);
		if (!ticketO.isPresent()) {
			throw new TicketNotExistException(TicketConstants.TICKET_DOES_NOT_EXIST_MSG);
		} else {
			Ticket ticket = TicketDTO.convertToEntity(ticketDto);
			ticket.setTicketId(ticketO.get().getTicketId());
			ticket.setCreatedDate(ticketO.get().getCreatedDate());
			ticket.setUpdatedDate(LocalDateTime.now());
			ticket.setUpdatedBy(loggedInUser);
			ticket.setProjectCode(ticketO.get().getProjectCode());
			ticketRepository.save(ticket);
			try {
				sendMessage(mapper.writeValueAsString(ticket), "addTicketTopic");
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return ticket;
		}
	}

	@Override
	public synchronized String getNextSequenceOfTicketId(String projectCode) {
		Optional<TicketSequence> seq = sequenceRepository.findById(projectCode);
		if (!seq.isPresent()) {
			TicketSequence ticketSeq = new TicketSequence();
			ticketSeq.setProjectCode(projectCode);
			ticketSeq.setSeq(1);
			sequenceRepository.save(ticketSeq);
			return ticketSeq.getProjectCode() + "-" + ticketSeq.getSeq();
		} else {
			TicketSequence nextSeq = seq.get();
			nextSeq.setSeq(nextSeq.getSeq() + 1);
			sequenceRepository.save(nextSeq);

			return nextSeq.getProjectCode() + "-" + nextSeq.getSeq();
		}
	}

}
