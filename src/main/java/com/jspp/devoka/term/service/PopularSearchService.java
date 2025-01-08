package com.jspp.devoka.term.service;

import com.jspp.devoka.history.dto.RankData;
import com.jspp.devoka.term.damain.PopularSearch;
import com.jspp.devoka.term.repository.PopularSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PopularSearchService {

    private final PopularSearchRepository popularSearchRepository;

    public void save(List<RankData> list) {
        PopularSearch popularSearch = PopularSearch.create(list);
        popularSearchRepository.save(popularSearch);
    }

    public List<RankData> getRankData(){

        PopularSearch popularSearch = popularSearchRepository.findTopByOrderByCreateDateDesc();

        return popularSearch.getRankDataList();

    }
}