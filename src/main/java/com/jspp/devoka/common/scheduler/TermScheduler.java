package com.jspp.devoka.common.scheduler;

import com.jspp.devoka.history.dto.RankData;
import com.jspp.devoka.history.service.SearchHistoryService;
import com.jspp.devoka.term.service.PopularSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TermScheduler {

    private final SearchHistoryService searchHistoryService;
    private final PopularSearchService popularSearchService;

    /**
     * 사용자가 하루에 검색 했던 것 가지고 인기검색어 추출
     */
    @Scheduled(cron = "0 0 2 * * *")    // 매일 오전 2시
    public void popularSearchTermScheduler() {
        // 인기 검색어 추출
        List<RankData> rankData = searchHistoryService.popularTermExtract();

        // 데이터 저장
        popularSearchService.save(rankData);
    }
}
