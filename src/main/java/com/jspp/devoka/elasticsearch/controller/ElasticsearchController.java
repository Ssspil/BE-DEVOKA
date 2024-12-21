package com.jspp.devoka.elasticsearch.controller;


import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.jspp.devoka.common.response.CommonApiResponse;
import com.jspp.devoka.common.response.DataType;
import com.jspp.devoka.elasticsearch.dto.SearchTermDocument;
import com.jspp.devoka.elasticsearch.dto.request.DocumentCreateRequest;
import com.jspp.devoka.elasticsearch.dto.request.IndexCreateRequest;
import com.jspp.devoka.elasticsearch.dto.response.DocumentCreateResponse;
import com.jspp.devoka.elasticsearch.dto.response.IndexCreateResponse;
import com.jspp.devoka.elasticsearch.service.ElasticsearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/elasticsearch")
public class ElasticsearchController {

    private final ElasticsearchService elasticsearchService;

    @Operation(summary = "엘라스틱 서치 인덱스 생성 (=DB)", description = "ES 인덱스를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인덱스 생성 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/index")
    public ResponseEntity<CommonApiResponse<IndexCreateResponse>> createIndex(@RequestBody IndexCreateRequest indexCreateRequest) throws IOException {

        CreateIndexResponse esResponse = elasticsearchService.createIndex(indexCreateRequest.getIndexName());
        IndexCreateResponse indexResponse = IndexCreateResponse.of(esResponse);
        // 공통 응답 처리
        CommonApiResponse<IndexCreateResponse> response = CommonApiResponse.success(indexResponse, DataType.Object);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "엘라스틱 서치 인덱스 삭제 (=DB)", description = "ES 인덱스를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "인덱스 삭제 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/index")
    public ResponseEntity<Void> deleteIndex(@RequestBody IndexCreateRequest indexRequest) throws IOException {

        elasticsearchService.deleteIndex(indexRequest.getIndexName());

        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "엘라스틱 서치 데이터 생성 (=ROW)", description = "ES 데이터를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 생성 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/{indexName}/document")
    public ResponseEntity<CommonApiResponse<DocumentCreateResponse>> saveDocument(
                                                @RequestBody DocumentCreateRequest documentCreateRequest,
                                                @PathVariable String indexName) throws IOException {

        SearchTermDocument searchTermDocument = SearchTermDocument.of(documentCreateRequest);
        // es 생성 응답 값
        IndexResponse esResponse = elasticsearchService.saveDocument(indexName, searchTermDocument);
        // 사용자 응답 객체 생성
        DocumentCreateResponse createResponse = DocumentCreateResponse.create(esResponse.index(), searchTermDocument);
        // 공통 응답 처리
        CommonApiResponse<DocumentCreateResponse> response = CommonApiResponse.success(createResponse, DataType.Object);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/elasticsearch/" + esResponse.index() + "/document/" + esResponse.id()).toUriString());

        return ResponseEntity.created(uri).body(response);
    }
}
