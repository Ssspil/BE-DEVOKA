package com.jspp.devoka.common.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class ElasticsearchConfig {

    @Value("${spring.elasticsearch.uris}")
    private String serverUrl;

    // Elasticsearch 와 HTTP 연결을 관리하는 기본 클라이언트
    @Bean
    public RestClient restClient() {
        return RestClient.builder(HttpHost.create(serverUrl))
                .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder.setSocketTimeout(60000))  // 소켓 시간 초과 설정
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setMaxConnTotal(100).setMaxConnPerRoute(20))  // 연결 제한 설정
                .build();
    }

    // Elasticsearch API 호출 시 데이터를 전송하거나 수신하는데 사용
    @Bean
    public ElasticsearchTransport elasticsearchTransport(RestClient restClient) {
        return new RestClientTransport(restClient, new JacksonJsonpMapper());
    }

    // 최종 클라이언트 객체
    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport transport) {
        return new ElasticsearchClient(transport);
    }

}