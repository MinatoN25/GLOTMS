package com.glotms.searchservice.dto;
import java.util.List;
import java.util.Map;

import org.elasticsearch.search.sort.SortOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO extends PaginationDTO {
    private List<String> fields;
    private String searchTerm;
    private String sortBy;
    private SortOrder order;
    private Map<String,String> greatherThan;
    private Map<String,String> lessThan;
}