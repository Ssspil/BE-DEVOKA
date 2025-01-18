package com.jspp.devoka.term.dto.request;

import com.jspp.devoka.category.domain.Category;
import com.jspp.devoka.term.damain.Term;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TermCreateRequest {

    private String korName;
    private String engName;
    private String abbName;
    @Size(max = 255, message = "용어 설명하는 문자열을 255자 이하로 입력해 주세요.")
    private String definition;
    private String categoryId;

    /**
     * 엔티티로 변환, 값이 없으면 빈 문자열로
     * @param category
     * @return
     */
    public Term toEntity(Category category) {
        return Term.builder()
                .korName(isEmpty(korName) ? "" : korName)
                .engName(isEmpty(engName) ? "" : engName)
                .abbName(isEmpty(abbName) ? "" : abbName)
                .definition(isEmpty(definition) ? "" : definition)
                .category(category)
                .build();
    }

    /**
     * 빈 문자열인지 체크
     * @param val
     * @return
     */
    private boolean isEmpty(String val) {
        return val == null || val.trim().isEmpty();
    }
}
