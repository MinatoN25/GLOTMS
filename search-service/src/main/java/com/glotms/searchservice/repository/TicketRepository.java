package com.glotms.searchservice.repository;

import java.util.List;

import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.glotms.searchservice.model.Complexity;
import com.glotms.searchservice.model.IssueType;
import com.glotms.searchservice.model.Priority;
import com.glotms.searchservice.model.Ticket;
import com.glotms.searchservice.model.TicketStatus;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;

@Repository
public interface TicketRepository extends ElasticsearchRepository<Ticket, String> {

	List<Ticket> findTicketByTicketStatus(TicketStatus status);

	List<Ticket> findTicketByProjectCode(String projectCode);

	List<Ticket> findTicketByTicketSummary(String summary);

	List<Ticket> findTicketByIssueType(IssueType type);

	List<Ticket> findTicketByLabel(String label);

	List<Ticket> findTicketByPriority(Priority priority);

	List<Ticket> findTicketByAssignee(String assignee);

	List<Ticket> findTicketByReporter(String reporter);

	List<Ticket> findTicketByComplexity(Complexity complexity);

}
