package com.jspp.devoka.category.controller;

import com.jspp.devoka.category.dto.CategoryListResponse;
import com.jspp.devoka.category.service.CategoryService;
import com.jspp.devoka.common.response.CommonApiResponse;

import com.jspp.devoka.common.response.DataType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "전체 카테고리 목록 조회", description = "용어의 카테고리 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/all")
    public ResponseEntity<CommonApiResponse<List<CategoryListResponse>>> getCategories(@RequestParam(name = "size", defaultValue = "10") int size,
                                                                @RequestParam(name = "page", defaultValue = "0") int page) {
        // 데이터
        List<CategoryListResponse> categoryList = categoryService.getCategoryList(page, size);
        // 공통 응답 코드 생성
        CommonApiResponse<List<CategoryListResponse>> response = CommonApiResponse.success(categoryList, DataType.Array);
        return ResponseEntity.ok().body(response);
    }

}
