package com.jspp.devoka.term.controller;

import com.jspp.devoka.term.dto.request.TermCreateRequest;
import com.jspp.devoka.term.dto.response.TermCreateResponse;
import com.jspp.devoka.term.dto.response.TermListResponse;
import com.jspp.devoka.term.dto.request.TermUpdateRequest;
import com.jspp.devoka.term.dto.response.TermSearchResponse;
import com.jspp.devoka.term.dto.response.TermUpdateResponse;
import com.jspp.devoka.term.service.TermService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/terms")
public class TermController {

    private final TermService termService;

    @Operation(summary = "전체 용어 목록 조회", description = "모든 용어들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/all")
    public ResponseEntity<List<TermListResponse>> getTermList(@RequestParam(name = "page", defaultValue = "0") int page,
                                                              @RequestParam(name = "size", defaultValue = "10") int size,
        @Parameter(description = "해당 카테고리에 있는 목록조회") @RequestParam(name = "categoryId", required = false) String categoryId){
        List<TermListResponse> termList = termService.getTermList(page, size, categoryId);

        return ResponseEntity.ok().body(termList);
    }


    @Operation(summary = "용어 생성", description = "새로운 용어을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "용어 생성 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    public ResponseEntity<TermCreateResponse> createTerm(@RequestBody @Validated TermCreateRequest termRequest){
        TermCreateResponse term = termService.createTerm(termRequest);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/terms/" + term.getTermNo()).toUriString());
        return ResponseEntity.created(uri).body(term);
    }


    @Operation(summary = "용어 검색 목록 조회", description = "검색한 용어들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<TermSearchResponse>> searchTerm(@Parameter(description = "검색할 용어", required = true) @RequestParam(name = "keyword") String keyword,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size){

        List<TermSearchResponse> termList = termService.searchTerm(keyword, page, size);
        return ResponseEntity.ok().body(termList);
    }


    @Operation(summary = "용어 수정", description = "용어의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "용어 수정 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PatchMapping("/{termNo}")
    public ResponseEntity<TermUpdateResponse> updateTerm(@RequestBody @Validated TermUpdateRequest termRequest,
                                                         @PathVariable Long termNo){

        TermUpdateResponse term = termService.updateTerm(termNo, termRequest);
        return ResponseEntity.ok().body(term);
    }


    @Operation(summary = "용어 삭제", description = "용어를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "용어 삭제 성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{termNo}")
    public ResponseEntity<Void> deleteTerm(@PathVariable Long termNo){

        termService.deleteTerm(termNo);

        return ResponseEntity.noContent().build();
    }

}
