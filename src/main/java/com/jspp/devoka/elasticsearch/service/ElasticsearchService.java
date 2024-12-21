package com.jspp.devoka.elasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import com.jspp.devoka.elasticsearch.dto.SearchTermDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    // https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/8.15/getting-started-java.html#_indexing_documents
    private final ElasticsearchClient elasticsearchClient;

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
    public void getDocuments(String indexName, String id) throws IOException {
        GetResponse<SearchTermDocument> searchTermDocumentGetResponse = elasticsearchClient.get(g ->
                            g.index(indexName)
                                    .id(id),
                    SearchTermDocument.class
            );

    }

    /**
     * 엘라스틱 서치 데이터 검색
     * @param indexName DB 명이랑 같은 것
     * @param keyword   검색할 키워드
     * @throws IOException
     */
    public void searchDocuments(String indexName, String keyword) throws IOException {
        elasticsearchClient.search(s ->
                s.index(indexName)
                        .query(q -> q
                                .match(t -> t
                                        .field("korName")
                                        .query(keyword)
                                )
                        ),
                SearchTermDocument.class
        );
    }

    /**
     * 엘라스틱 서치 데이터 수정
     * @param indexName DB 명이랑 같은 것
     * @param searchTermDocument 변경할 데이터
     */
    public void updateDocument(String indexName, SearchTermDocument searchTermDocument) throws IOException {
        elasticsearchClient.update(u ->
                u.index(indexName)
                        .id(searchTermDocument.getId())
                        .upsert(searchTermDocument),
                SearchTermDocument.class
        );
    }

    /**
     * 엘라스틱 서치 데이터 삭제
     * @param indexName
     * @param id
     * @throws IOException
     */
    public void deleteDocument(String indexName, String id) throws IOException {
        elasticsearchClient.delete(d -> d.index(indexName).id(id));
    }

}
