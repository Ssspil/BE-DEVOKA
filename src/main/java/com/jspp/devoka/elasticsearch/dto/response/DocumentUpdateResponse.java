package com.jspp.devoka.elasticsearch.dto.response;

import com.jspp.devoka.elasticsearch.dto.SearchTermDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DocumentUpdateResponse {

    private String indexName;
    private SearchTermDocument data;

    public static DocumentUpdateResponse create(String indexName, SearchTermDocument searchTermDocument){
        return new DocumentUpdateResponse(indexName, searchTermDocument);
    }
}
