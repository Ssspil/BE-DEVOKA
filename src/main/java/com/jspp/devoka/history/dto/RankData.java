package com.jspp.devoka.history.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankData {
    private Integer rank;
    private String termName;
    private long count;
}