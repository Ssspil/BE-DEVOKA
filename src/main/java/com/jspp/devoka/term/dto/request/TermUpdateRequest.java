package com.jspp.devoka.term.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TermUpdateRequest {

    private String korName;
    private String engName;
    private String abbName;
    @Size(max = 255)
    private String definition;
    private String categoryId;

}
