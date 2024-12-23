package com.jspp.devoka.elasticsearch.dto.request;

import lombok.Getter;

@Getter
public class DocumentUpdateRequest {
    private String korName;
    private String engName;
    private String abbName;
    private String definition;
    private String categoryId;
    private String categoryName;
}
