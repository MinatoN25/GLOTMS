package com.glotms.ticketservice.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.glotms.ticketservice.enums.Complexity;
import com.glotms.ticketservice.enums.IssueType;
import com.glotms.ticketservice.enums.Priority;
import com.glotms.ticketservice.enums.TicketStatus;
import com.glotms.ticketservice.model.Ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {
	
	
	private String department;
	@NotNull(message = "Issue type can not be null")
	private IssueType issueType;
	private String ticketSummary;
	private String description;
	private String reporter;
	private String assignee;
	@NotNull(message = "Priority can not be null")
	private Priority priority;
	private String[] label;
	@NotNull(message = "Ticket status can not be null")
	private TicketStatus ticketStatus;
	private String resolution;
	private LocalDateTime sla;
	@NotNull(message = "Complexity can not be null")
	private Complexity complexity;
	
	public static Ticket convertToEntity(TicketDTO ticketDto) {
		Ticket ticket = new Ticket();
		ticket.setAssignee(ticketDto.getAssignee());
		ticket.setComplexity(ticketDto.getComplexity());
		ticket.setDepartment(ticketDto.getDepartment());
		ticket.setDescription(ticket.getDescription());
		ticket.setIssueType(ticketDto.getIssueType());
		ticket.setLabel(ticketDto.getLabel());
		ticket.setPriority(ticketDto.getPriority());
		ticket.setReporter(ticketDto.getReporter());
		ticket.setResolution(ticketDto.getResolution());
		ticket.setSla(ticketDto.getSla());
		ticket.setTicketStatus(ticketDto.getTicketStatus());
		ticket.setTicketSummary(ticketDto.getTicketSummary());
		return ticket;
	}

}
