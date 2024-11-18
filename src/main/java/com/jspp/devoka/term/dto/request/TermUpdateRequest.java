package com.jspp.devoka.term.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TermUpdateRequest {

    private String korName;
    private String engName;
    private String abbName;
    @Size(max = 255, message = "용어 설명하는 문자열을 255자 이하로 입력해 주세요.")
    private String definition;
    private String categoryId;

}
