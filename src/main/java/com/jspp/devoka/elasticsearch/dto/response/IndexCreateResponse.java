package com.jspp.devoka.elasticsearch.dto.response;

import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IndexCreateResponse {
    private String indexName;

    public static IndexCreateResponse of(CreateIndexResponse createIndexResponse){
        // CreateIndexResponse: {"index":"인덱스명","shards_acknowledged":true,"acknowledged":true}
        return new IndexCreateResponse(createIndexResponse.index());
    }
}
