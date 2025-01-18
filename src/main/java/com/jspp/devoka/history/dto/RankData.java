package com.jspp.devoka.history.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class RankData {
    private Integer rank;
    private String termName;
    private long count;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RankData rankData = (RankData) o;
        return count == rankData.count && Objects.equals(rank, rankData.rank) && Objects.equals(termName, rankData.termName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, termName, count);
    }
}