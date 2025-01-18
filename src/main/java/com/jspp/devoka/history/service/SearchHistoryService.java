package com.jspp.devoka.history.service;

import com.jspp.devoka.history.domain.SearchHistory;
import com.jspp.devoka.history.dto.RankData;
import com.jspp.devoka.history.repository.SearchHistoryRepository;
import com.jspp.devoka.term.damain.Term;
import com.jspp.devoka.term.dto.response.TermSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    /**
     * 검색 이력 저장
     * @param searchHistory
     */
    public void save(SearchHistory searchHistory){
        searchHistoryRepository.save(searchHistory);
    }


    /**
     * 검색이력에 저장할 수 있는지 검사하고 저장
     * @param keyword
     * @param findList
     * @param responseData
     */
    public void saveRequest(String keyword, List<Term> findList, List<TermSearchResponse> responseData) {
        String insertKeyword = keyword.trim().replaceAll("\\s+", " ").toLowerCase();  // 불필요한 공백 제거

        // 한글, 영어, 약어 중에 검색한 글자가 있으면 검색이력에 저장
        boolean isValid = findList.stream().anyMatch(term ->
            (term.getKorName() != null && term.getKorName().toLowerCase().contains(insertKeyword)) ||
            (term.getEngName() != null && term.getEngName().toLowerCase().contains(insertKeyword)) ||
            (term.getAbbName() != null && term.getAbbName().toLowerCase().contains(insertKeyword))
        );


        if(isValid){
            SearchHistory searchHistory = SearchHistory.create(keyword, responseData);
            this.save(searchHistory);
        }
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
