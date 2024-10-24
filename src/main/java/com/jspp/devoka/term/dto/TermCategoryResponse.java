package com.jspp.devoka.term.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class TermCategoryResponse {

    private String categoryId;
    private String categoryName;
    private Integer sortOrder;
    private Integer depth;
    private List<TermCategoryResponse> childList;

}
