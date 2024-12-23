package com.jspp.devoka.elasticsearch.dto.response;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.jspp.devoka.elasticsearch.dto.SearchTermDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DocumentSearchResponse {

    private String indexName;
    private SearchTermDocument data;

    public static DocumentSearchResponse create(String indexName, SearchTermDocument searchTermDocument){
        return new DocumentSearchResponse(indexName, searchTermDocument);
    }

    public static List<DocumentSearchResponse> fromElasticResponse(List<Hit<SearchTermDocument>> hits){
        return hits.stream().map(hit -> create(hit.index(), hit.source())).toList();
    }
}
