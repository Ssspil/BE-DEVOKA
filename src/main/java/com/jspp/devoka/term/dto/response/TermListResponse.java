package com.jspp.devoka.term.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TermListResponse {

    private String categoryId;
    private String categoryName;
    private List<TermResponse> data;

    /**
     * 데이터 -> ResponseDto로 변경
     * @param categoryId
     * @param categoryName
     * @param data
     * @return
     */
    public static TermListResponse of(String categoryId, String categoryName, List<TermResponse> data){
        return new TermListResponse(categoryId, categoryName, data);
    }
}
