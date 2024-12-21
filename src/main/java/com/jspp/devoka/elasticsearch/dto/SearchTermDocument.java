package com.jspp.devoka.elasticsearch.dto;

import com.jspp.devoka.elasticsearch.dto.request.DocumentCreateRequest;
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
}
