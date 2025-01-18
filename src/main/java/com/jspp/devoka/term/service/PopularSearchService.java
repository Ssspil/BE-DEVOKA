package com.jspp.devoka.term.service;

import com.jspp.devoka.history.dto.RankData;
import com.jspp.devoka.history.dto.response.RankResponse;
import com.jspp.devoka.term.damain.PopularSearch;
import com.jspp.devoka.term.repository.PopularSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopularSearchService {

    private final PopularSearchRepository popularSearchRepository;

    /**
     * 인기 검색어 추출 한 것 저장
     * @param list
     */
    public void save(List<RankData> list) {
        PopularSearch popularSearch = PopularSearch.create(list);
        popularSearchRepository.save(popularSearch);

    }


    /**
     * 검색검색어 DP 테이블로 저장된것 데이터 가져오기
     * TODO 조회 로직 변경
     * @return
     */
    public RankResponse getRankData(){

        Optional<PopularSearch> popularSearchOptional = popularSearchRepository.findTopByOrderByCreateDateDesc();

        List<RankData> rankData = new ArrayList<>();
        LocalDateTime dateTime = LocalDateTime.now();
        if(popularSearchOptional.isPresent()){
            rankData = popularSearchOptional.get().getRankDataList();
            dateTime = popularSearchOptional.get().getCreateDate();
        }

        log.info("인기 검색어 조회");
        return RankResponse.of(dateTime, rankData);
    }
}