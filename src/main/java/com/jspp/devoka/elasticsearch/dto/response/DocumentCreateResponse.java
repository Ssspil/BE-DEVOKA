package com.jspp.devoka.elasticsearch.dto.response;

import com.jspp.devoka.elasticsearch.dto.SearchTermDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DocumentCreateResponse {

    private String indexName;
    private SearchTermDocument data;

    public static DocumentCreateResponse create(String indexName, SearchTermDocument searchTermDocument){
        return new DocumentCreateResponse(indexName, searchTermDocument);
    }
}
