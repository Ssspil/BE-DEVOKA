package com.jspp.devoka.elasticsearch.dto.response;

import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Builder
@AllArgsConstructor
public class DocumentPopularResponse {
    private int rank;
    private String keyword;
    private long count;

    public static List<DocumentPopularResponse> fromElasticResponse(List<StringTermsBucket> buckets){
        // 람다에서 rank 값을 추적할 수 있도록 AtomicInteger 사용
        AtomicInteger sort = new AtomicInteger(1);  // 순위가 1부터 시작
        return buckets.stream().map(bucket ->
                DocumentPopularResponse.builder()
                        .rank(sort.getAndIncrement())
                        .keyword(bucket.key().stringValue())
                        .count(bucket.docCount())
                        .build()
        ).toList();
    }
}
