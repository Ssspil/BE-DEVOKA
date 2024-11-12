package com.jspp.devoka.term.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TermSearchResponse {

    private String categoryId;
    private String categoryName;
    private List<TermResponse> data;
}
