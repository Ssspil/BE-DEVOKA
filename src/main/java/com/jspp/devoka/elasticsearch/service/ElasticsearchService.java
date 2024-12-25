package com.jspp.devoka.elasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import com.jspp.devoka.elasticsearch.dto.SearchTermDocument;
import com.jspp.devoka.elasticsearch.dto.response.DocumentPopularResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    // https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/8.15/getting-started-java.html#_indexing_documents
    private final ElasticsearchClient elasticsearchClient;
    private final String SEARCH_HISTORY_INDEX_NAME= "search_history";

    /**
     * 엘라스틱 서치 인덱스 생성
     * @param indexName DB 명이랑 같은 것
     * @throws IOException
     */
    public CreateIndexResponse createIndex(String indexName) throws IOException {
        CreateIndexResponse response = elasticsearchClient.indices().create(c -> c
                .index(indexName)
        );

        return response;
        // CreateIndexResponse: {"index":"인덱스명","shards_acknowledged":true,"acknowledged":true}
    }

    /**
     * 엘라스틱 서치 인덱스 삭제
     * @param indexName DB 명이랑 같은 것
     * @throws IOException
     */
    public DeleteIndexResponse deleteIndex(String indexName) throws IOException {
        DeleteIndexResponse response = elasticsearchClient.indices().delete(d -> d
                .index(indexName)
        );

        return response;
        // DeleteIndexResponse: {"acknowledged":true}
    }

    /**
     * 엘라스틱 서치 데이터 저장
     * @param indexName     DB 명과 같은 것
     * @param searchTermDocument    저장할 데이터
     * @return
     * @throws IOException
     */
    public IndexResponse saveDocument(String indexName, SearchTermDocument searchTermDocument) throws IOException {

        // Elasticsearch에 인덱싱
        IndexResponse response = elasticsearchClient.index(i -> i
                .index(indexName) // 저장할 인덱스 이름
                .id(searchTermDocument.getId()) // 문서 ID
                .document(searchTermDocument) // 저장할 데이터
        );

        return response;
        // IndexResponse: {"_id":"1","_index":"인덱스 명","_primary_term":1,"result":"created","_seq_no":0,"_shards":{"failed":0.0,"successful":1.0,"total":2.0},"_version":1}
    }

    /**
     * 엘라스틱 서치 데이터 조회
     * @param indexName DB 명이랑 같은 것
     * @param id    데이터 ID
     * @throws IOException
     */
    public GetResponse<SearchTermDocument> getDocuments(String indexName, String id) throws IOException {
        GetResponse<SearchTermDocument> response = elasticsearchClient.get(g -> g
                        .index(indexName)
                        .id(id)
                ,SearchTermDocument.class
        );

        return response;
        // {"_index":"인덱스 명","found":true,"_id":"1","_primary_term":1,"_seq_no":4,"_source":"com.jspp.devoka.elasticsearch.dto.SearchTermDocument@1cee8117","_version":4}
    }

    /**
     * 엘라스틱 서치 데이터 검색
     * @param indexName DB 명이랑 같은 것
     * @param keyword   검색할 키워드
     * @throws IOException
     */
    public SearchResponse<SearchTermDocument> searchDocuments(String indexName, String keyword) throws IOException {

        // 검색
        SearchResponse<SearchTermDocument> response = elasticsearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .bool(b -> b
                                        .should(
                                                List.of(
                                                        QueryBuilders.wildcard(w -> w.field("korName").wildcard("*" + keyword + "*")),
                                                        QueryBuilders.wildcard(w -> w.field("engName").wildcard("*" + keyword + "*")),
                                                        QueryBuilders.wildcard(w -> w.field("abbName").wildcard("*" + keyword + "*")),
                                                        QueryBuilders.wildcard(w -> w.field("definition").wildcard("*" + keyword + "*"))
                                                )
                                        )
                                )
                        ),
                SearchTermDocument.class
        );

        // 데이터가 있으면 검색어를 히스토리에 저장
        if(!response.hits().hits().isEmpty()){
            saveSearchHistory(keyword);
        }

        /*
        // 검색할 필드들
        List<String> fields = List.of("korName", "engName", "abbName", "definition");

        SearchResponse<SearchTermDocument> response = elasticsearchClient.search(s -> s
                        .index(indexName)
                            .query(q -> q
                                .multiMatch(m -> m
                                    .query(keyword)  // 검색할 키워드
                                    .fields(fields)  // 검색할 필드
                                    .fuzziness("AUTO")  // 자동 철자 오류 허용
                                    //.maxExpansions(30)  // 후보 단어 최대 개수 제한
                                    //.prefixLength(1)    // 검색 접두어 일치해야 하는 길이
                                )
                            ),
                SearchTermDocument.class
        );


         */
        return response;
        // SearchResponse: {"took":17,"timed_out":false,"_shards":{"failed":0.0,"successful":1.0,"total":1.0,"skipped":0.0},"hits":{"total":{"relation":"eq","value":4},"hits":[{"_index":"인덱스 명","_id":"1","_score":4.0,"_source":"com.jspp.devoka.elasticsearch.dto.SearchTermDocument@4b4d7567"},{"_index":"인덱스명","_id":"2","_score":4.0,"_source":"com.jspp.devoka.elasticsearch.dto.SearchTermDocument@f133b88"},{"_index":"인덱스명","_id":"3","_score":4.0,"_source":"com.jspp.devoka.elasticsearch.dto.SearchTermDocument@48e81ced"},{"_index":"인덱스명","_id":"4","_score":4.0,"_source":"com.jspp.devoka.elasticsearch.dto.SearchTermDocument@38a17ec5"}],"max_score":4.0}}
    }

    /**
     * 엘라스틱 서치 데이터 수정
     * @param indexName DB 명이랑 같은 것
     * @param searchTermDocument 변경할 데이터
     */
    public UpdateResponse<SearchTermDocument> updateDocument(String indexName, String id, SearchTermDocument searchTermDocument) throws IOException {
        UpdateResponse<SearchTermDocument> response = elasticsearchClient.update(u -> u
                        .index(indexName)
                        .id(id)
                        .doc(searchTermDocument),
                        //.upsert(searchTermDocument),      // 해당 document가 없을 경우에 생셩하려고 할 때만 적용
                SearchTermDocument.class
        );

        return response;
        // UpdateResponse: {"_id":"1","_index":"인덱스 명","_primary_term":1,"result":"updated","_seq_no":8,"_shards":{"failed":0.0,"successful":1.0,"total":2.0},"_version":8}
    }

    /**
     * 엘라스틱 서치 데이터 삭제
     * @param indexName
     * @param id
     * @throws IOException
     */
    public void deleteDocument(String indexName, String id) throws IOException {
        DeleteResponse delete = elasticsearchClient.delete(d -> d.index(indexName).id(id));
        // DeleteResponse: {"_id": "1","_index": "testterms","_primary_term": 1,"result": "deleted","_seq_no": 9,"_shards": {"total": 2,"successful": 1,"failed": 0},"_version": 9}
    }


    /**
     * 엘라스틱 서치 검색 이력 저장
     * @param keyword
     * @throws IOException
     */
    public void saveSearchHistory(String keyword) throws IOException {
        if(keyword == null || keyword.isBlank() || keyword.isEmpty()){
            return;
        }

        elasticsearchClient.index(i -> i
                .index(SEARCH_HISTORY_INDEX_NAME)
                .document(Map.of(
                        "keyword", keyword,
                        "timestamp", Instant.now().toString()
                ))
        );
    }


    /**
     * 엘라스틱 서치 인기 검색어 조회
     * @return
     * @throws IOException
     */
    public List<DocumentPopularResponse> getPopularTerms() throws IOException {
        // 10분 전부터 현재까지 범위 쿼리 사용
        SearchResponse<Void> response = elasticsearchClient.search(s -> s
                        .index(SEARCH_HISTORY_INDEX_NAME)
                        .size(0) // 검색 결과는 반환하지 않음
                        .query(q -> q
                            .range(r -> r
                                .date(
                                    d -> d
                                        .field("timestamp")
                                        .gte("now-1h/h")   // 현재 시간 기준으로 한 시간 전
                                        .lt("now+1h/h")        // 현재 시간까지
                                )

                            )
                        )
                        .aggregations("popular_keywords", a -> a
                                .terms(t -> t
                                    .field("keyword") // 정확한 'keyword' 필드 사용
                                    .size(10)
                                )
                        ),
                Void.class
        );

        // 인기 검색어 추축
        List<StringTermsBucket> popularKeywords = response.aggregations()
                .get("popular_keywords")
                .sterms()
                .buckets()
                .array();

        return DocumentPopularResponse.fromElasticResponse(popularKeywords);
        // SearchResponse: {"took":1,"timed_out":false,"_shards":{"failed":0.0,"successful":1.0,"total":1.0,"skipped":0.0},"hits":{"total":{"relation":"eq","value":12},"hits":[],"max_score":null},"aggregations":{"sterms#popular_keywords":{"buckets":[{"doc_count":10,"key":"스크립트"},{"doc_count":1,"key":"서버"},{"doc_count":1,"key":"웹"}],"doc_count_error_upper_bound":0,"sum_other_doc_count":0}}}
    }

}
