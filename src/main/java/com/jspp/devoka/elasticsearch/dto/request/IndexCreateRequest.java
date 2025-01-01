package com.jspp.devoka.elasticsearch.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class IndexCreateRequest {

    @NotBlank(message = "인덱스 이름이 비어있을 수 없습니다.")
    @Pattern(
            regexp = "^[a-z]+$",
            message = "인덱스 이름은 영문 소문자로만 가능합니다."
    )
    @Size(max = 255, message = "255자 이내로 작성해주세요")
    private String indexName;
}
