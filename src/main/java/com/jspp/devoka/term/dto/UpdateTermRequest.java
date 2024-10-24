package com.jspp.devoka.term.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateTermRequest {

    private Long termNo;
    private String korName;
    private String engName;
    private String abbName;
    private String definition;
    private String categoryId;

}
