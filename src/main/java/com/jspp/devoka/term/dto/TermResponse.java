package com.jspp.devoka.term.dto;

import com.jspp.devoka.category.domain.Category;
import com.jspp.devoka.term.damain.Term;
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

    /**
     * 엔티티 -> ResponseDto로 변경
     * @param termEntity
     * @return
     */
    public static TermResponse fromEntity(Term termEntity){

        Category category = termEntity.getCategory();

        TermResponseBuilder builder = TermResponse.builder()
                .termNo(termEntity.getTermNo())
                .korName(termEntity.getKorName())
                .engName(termEntity.getEngName())
                .abbName(termEntity.getAbbName())
                .definition(termEntity.getDefinition())
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName());

        return builder.build();
    }
}
