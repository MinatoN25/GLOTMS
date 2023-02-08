package com.glotms.searchservice.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.glotms.searchservice.dto.SearchDTO;

public final class SearchUtil {
	public static final Logger logger = LoggerFactory.getLogger(SearchUtil.class);

	private SearchUtil() {
	}

	public static SearchRequest buildSearchRequest(String indexName, SearchDTO dto) {
		try {
			
			
			final QueryBuilder searchQuery = getQueryBuilder(dto);

			BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(searchQuery);

			if (dto.getGreatherThan() != null) {

				for (Map.Entry<String, String> entry : dto.getGreatherThan().entrySet()) {
					boolQuery.must(getQueryBuilderGreaterThan(entry.getKey(), entry.getValue()));

				}

			}
			if (dto.getLessThan() != null) {

				for (Map.Entry<String, String> entry : dto.getLessThan().entrySet()) {
					boolQuery.must(getQueryBuilderlessThan(entry.getKey(), entry.getValue()));

				}
			}
			
			

			final int page = dto.getPage();
            final int size = dto.getSize();
            final int from = page <= 0 ? 0 : page * size;
            
			SearchSourceBuilder builder = new SearchSourceBuilder() .from(from)
                    .size(size).postFilter(boolQuery);

			if (dto.getSortBy() != null) {
				builder = builder.sort(dto.getSortBy(), dto.getOrder() != null ? dto.getOrder() : SortOrder.ASC);
			}

			final SearchRequest request = new SearchRequest(indexName);
			request.source(builder);

			return request;
		} catch (final Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	private static QueryBuilder getQueryBuilder(SearchDTO dto) {
		if (dto == null || dto.getSearchTerm()==null) {
			
			return QueryBuilders.matchAllQuery();
		}

		final List<String> fields = dto.getFields();
		if (CollectionUtils.isEmpty(fields)) {
			return null;
		}

		if (fields.size() > 1) {
			final MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(dto.getSearchTerm())
					.type(MultiMatchQueryBuilder.Type.CROSS_FIELDS).operator(Operator.AND);

			fields.forEach(queryBuilder::field);

			return queryBuilder;
		}
		
		return fields.stream().findFirst()
				.map(field -> QueryBuilders.matchQuery(field, dto.getSearchTerm()).operator(Operator.AND)).orElse(null);
	}

	private static QueryBuilder getQueryBuilderGreaterThan(String field, String value) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

		return QueryBuilders.rangeQuery(field).gte(LocalDate.parse(value).atStartOfDay().format(dtf));
	}

	private static QueryBuilder getQueryBuilderlessThan(String field, String value) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

		return QueryBuilders.rangeQuery(field).lte(LocalDate.parse(value).atStartOfDay().format(dtf));
	}

}
