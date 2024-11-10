package com.jspp.devoka.term.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TermListResponse {

    private String categoryId;
    private String categoryName;
    private List<TermResponse> data;
}
