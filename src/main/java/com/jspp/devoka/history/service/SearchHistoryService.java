package com.jspp.devoka.history.service;

import com.jspp.devoka.history.domain.SearchHistory;
import com.jspp.devoka.history.dto.RankData;
import com.jspp.devoka.history.repository.SearchHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    @Async
    @Transactional
    public void save(SearchHistory searchHistory){
        searchHistoryRepository.save(searchHistory);
    }

    /**
     * 사용자가 검색한 인기 검색어 추출
     * @return
     */
    public List<RankData> popularTermExtract(){
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate startDay = yesterday.atStartOfDay().toLocalDate();  // 자정
        LocalDate endDay = yesterday.plusDays(1).atStartOfDay().toLocalDate();  // 자정 다음날

        // TODO QueryDSL로 변경 예정
        List<Object[]> results = searchHistoryRepository.findSearchRankings(startDay, endDay);

        // 검색한 데이터가 없을 경우 빈 리스트로 리턴
        if (results.isEmpty()) return List.of();

        // 로직 실행
        List<RankData> list = results.stream()
                .map(result ->
                        new RankData(
                        ((Number) result[0]).intValue(),  // rank
                        (String) result[1],               // search_text
                        ((Number) result[2]).longValue()  // search_count
                ))
                .toList();

        return list;
    }
}
