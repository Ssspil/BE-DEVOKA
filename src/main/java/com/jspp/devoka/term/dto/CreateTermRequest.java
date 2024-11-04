package com.jspp.devoka.term.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateTermRequest {

    private String korName;
    private String engName;
    private String abbName;
    @Size(max = 255)
    private String definition;
    private String categoryId;

}
