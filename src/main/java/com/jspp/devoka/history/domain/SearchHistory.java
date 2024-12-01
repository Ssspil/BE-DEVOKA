package com.jspp.devoka.history.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "search_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "search_text")
    private String searchText;

    @Column(name = "search_date")
    private LocalDateTime searchDate;

    @Column(name = "response_data")
    private String responseData;

    public SearchHistory(String searchText, LocalDateTime searchDate, String responseData) {
        this.searchText = searchText;
        this.searchDate = searchDate;
        this.responseData = responseData;
    }

    public static SearchHistory create(String searchText, Object responseData)  {
        String jsonResponse;
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            jsonResponse = objectMapper.writeValueAsString(responseData);
        } catch (JsonProcessingException e) {
            jsonResponse = (String) responseData;
        }

        return new SearchHistory(searchText, LocalDateTime.now(), jsonResponse);
    }
}


