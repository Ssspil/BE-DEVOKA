package com.jspp.devoka.history.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jspp.devoka.history.dto.RankData;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class RankResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM.dd")
    private LocalDateTime dateTime;
    private List<RankData> rankDataList;

    /**
     * 데이터 -> ResponseDto로 변경
     * @param localDateTime
     * @param rankDataList
     * @return
     */
    public static RankResponse of(LocalDateTime localDateTime, List<RankData> rankDataList) {
        return RankResponse.builder().dateTime(localDateTime).rankDataList(rankDataList).build();
    }
}
