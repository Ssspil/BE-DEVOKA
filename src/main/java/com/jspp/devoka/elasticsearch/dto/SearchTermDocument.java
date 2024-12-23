package com.jspp.devoka.elasticsearch.dto;

import com.jspp.devoka.elasticsearch.dto.request.DocumentCreateRequest;
import com.jspp.devoka.elasticsearch.dto.request.DocumentUpdateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SearchTermDocument {
    private String id;
    private String korName;
    private String engName;
    private String abbName;
    private String definition;
    private String categoryId;
    private String categoryName;

    // 엘라스틱 서치 데이터 생성한 응답 값
    public static SearchTermDocument of(DocumentCreateRequest documentCreateRequest){
        SearchTermDocumentBuilder builder = SearchTermDocument.builder()
                .id(documentCreateRequest.getId())
                .korName(documentCreateRequest.getKorName())
                .engName(documentCreateRequest.getEngName())
                .abbName(documentCreateRequest.getAbbName())
                .definition(documentCreateRequest.getDefinition())
                .categoryId(documentCreateRequest.getCategoryId())
                .categoryName(documentCreateRequest.getCategoryName());

        return builder.build();
    }

    // 엘라스틱 서치 데이터 수정한 응답 값
    public static SearchTermDocument of(String termNo, DocumentUpdateRequest documentUpdateRequest){
        SearchTermDocumentBuilder builder = SearchTermDocument.builder()
                .id(termNo)
                .korName(documentUpdateRequest.getKorName())
                .engName(documentUpdateRequest.getEngName())
                .abbName(documentUpdateRequest.getAbbName())
                .definition(documentUpdateRequest.getDefinition())
                .categoryId(documentUpdateRequest.getCategoryId())
                .categoryName(documentUpdateRequest.getCategoryName());

        return builder.build();
    }

}
