package com.jspp.devoka.term.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TermResponse {

    private Long termNo;
    private String korName;
    private String engName;
    private String abbName;
    private String definition;
    private String categoryId;
    private String categoryName;

}
