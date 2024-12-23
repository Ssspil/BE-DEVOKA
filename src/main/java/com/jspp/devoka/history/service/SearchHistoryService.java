package com.jspp.devoka.history.service;

import com.jspp.devoka.history.domain.SearchHistory;
import com.jspp.devoka.history.repository.SearchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    @Async
    @Transactional
    public void save(SearchHistory searchHistory){
        searchHistoryRepository.save(searchHistory);
    }
}
