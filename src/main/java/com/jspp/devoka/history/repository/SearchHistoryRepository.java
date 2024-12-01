package com.jspp.devoka.history.repository;

import com.jspp.devoka.history.domain.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long>  {

}
