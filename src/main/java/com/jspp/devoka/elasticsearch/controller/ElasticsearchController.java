package com.jspp.devoka.elasticsearch.controller;


import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.jspp.devoka.common.response.CommonApiResponse;
import com.jspp.devoka.common.response.DataType;
import com.jspp.devoka.elasticsearch.dto.SearchTermDocument;
import com.jspp.devoka.elasticsearch.dto.request.DocumentCreateRequest;
import com.jspp.devoka.elasticsearch.dto.request.DocumentUpdateRequest;
import com.jspp.devoka.elasticsearch.dto.request.IndexCreateRequest;
import com.jspp.devoka.elasticsearch.dto.response.*;
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
import java.util.List;

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

    @Operation(summary = "엘라스틱 서치 데이터 조회 (=ROW)", description = "ES 데이터를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 조회 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{indexName}/document/{termNo}")
    public ResponseEntity<CommonApiResponse<SearchTermDocument>> getDocument(@PathVariable String indexName,
                                                                             @PathVariable String termNo) throws IOException {
        
        // es 생성 응답 값
        GetResponse<SearchTermDocument> esResponse = elasticsearchService.getDocuments(indexName, termNo);

        // 공통 응답 처리
        CommonApiResponse<SearchTermDocument> response = CommonApiResponse.success(esResponse.source(), DataType.Object);

        return ResponseEntity.ok().body(response);
    }


    @Operation(summary = "엘라스틱 서치 데이터 검색 조회 (=ROW)", description = "ES 데이터를 검색 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 조회 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{indexName}/document/search")
    public ResponseEntity<CommonApiResponse<List<DocumentSearchResponse>>> searchDocument(@PathVariable String indexName,
                                                                                      @RequestParam(required = false, defaultValue = "") String keyword) throws IOException {

        // es 생성 응답 값
        SearchResponse<SearchTermDocument> esResponse = elasticsearchService.searchDocuments(indexName, keyword);
        // 검색한 값 추출
        List<Hit<SearchTermDocument>> hits = esResponse.hits().hits();
        // DTO로 변환
        List<DocumentSearchResponse> changeResponse = DocumentSearchResponse.fromElasticResponse(hits);

        // 공통 응답 처리
        CommonApiResponse<List<DocumentSearchResponse>> response = CommonApiResponse.success(changeResponse, DataType.Array);

        return ResponseEntity.ok().body(response);
    }


    @Operation(summary = "엘라스틱 서치 데이터 수정 (=ROW)", description = "ES 데이터를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 수정 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PatchMapping("/{indexName}/document/{termNo}")
    public ResponseEntity<CommonApiResponse<DocumentUpdateResponse>> updateDocument(@PathVariable String indexName,
                                                                                @PathVariable String termNo,
                                                                                @RequestBody DocumentUpdateRequest documentUpdateRequest) throws IOException {
        SearchTermDocument searchTermDocument = SearchTermDocument.of(termNo, documentUpdateRequest);
        // es 생성 응답 값
        UpdateResponse<SearchTermDocument> esResponse = elasticsearchService.updateDocument(indexName, termNo, searchTermDocument);
        // 사용자 응답 객체 생성
        DocumentUpdateResponse createResponse = DocumentUpdateResponse.create(esResponse.index(), searchTermDocument);
        // 공통 응답 처리
        CommonApiResponse<DocumentUpdateResponse> response = CommonApiResponse.success(createResponse, DataType.Object);

        return ResponseEntity.ok().body(response);
    }


    @Operation(summary = "엘라스틱 서치 데이터 삭제 (=ROW)", description = "ES 데이터를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "데이터 삭제 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{indexName}/document/{termNo}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String indexName,
                                            @PathVariable String termNo) throws IOException {

        elasticsearchService.deleteDocument(indexName, termNo);

        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "엘라스틱 서치 인기 검색 조회 (=ROW)", description = "ES 자주 검색한 데이터를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 조회 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{indexName}/document/popular")
    public ResponseEntity<CommonApiResponse<List<DocumentPopularResponse>>> getPopularDocument(@PathVariable String indexName) throws IOException {

        List<DocumentPopularResponse> list = elasticsearchService.getPopularTerms();

        // 공통 응답 처리
        CommonApiResponse<List<DocumentPopularResponse>> response = CommonApiResponse.success(list, DataType.Array);

        return ResponseEntity.ok().body(response);
    }
}