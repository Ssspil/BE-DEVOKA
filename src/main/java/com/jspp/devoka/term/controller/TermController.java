package com.jspp.devoka.term.controller;

import com.jspp.devoka.term.dto.CreateTermRequest;
import com.jspp.devoka.term.dto.TermResponse;
import com.jspp.devoka.term.dto.UpdateTermRequest;
import com.jspp.devoka.term.service.TermService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<TermResponse>> getTermList(@RequestParam(name = "page", defaultValue = "0") int page,
                                                          @RequestParam(name = "size", defaultValue = "10") int size){

        return new ResponseEntity<>(termService.getTermList(page, size), HttpStatus.OK); // 200 OK 응답
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
    public ResponseEntity<TermResponse> createTerm(@RequestBody @Validated CreateTermRequest termRequest){

        return new ResponseEntity<>(termService.createTerm(termRequest), HttpStatus.OK);
    }


    @Operation(summary = "용어 검색 목록 조회", description = "검색한 용어들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<TermResponse>> searchTerm(@Parameter(description = "검색할 용어", required = true) @RequestParam(name = "keyword") String keyword,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size){

        return new ResponseEntity<>(termService.searchTerm(keyword, page, size), HttpStatus.OK);
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
    public ResponseEntity<TermResponse> updateTerm(@RequestBody @Validated UpdateTermRequest termRequest,
                                                   @PathVariable Long termNo){

        return new ResponseEntity<>(termService.updateTerm(termNo, termRequest), HttpStatus.OK);
    }



}
