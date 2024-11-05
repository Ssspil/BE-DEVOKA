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
    @Size(max = 255)
    private String definition;
    private String categoryId;

    public Term toEntity(Category category) {
        return Term.builder()
                .korName(korName)
                .engName(engName)
                .abbName(abbName)
                .definition(definition)
                .category(category)
                .build();
    }
}
