package com.jspp.devoka.history.repository;

import com.jspp.devoka.history.domain.SearchHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long>  {

    @Query(value = """
        SELECT 
            ROW_NUMBER() OVER (ORDER BY COUNT(search_text) DESC) AS rank,
            search_text AS termName,
            COUNT(search_text) AS count
        FROM 
            devoka.search_history
        WHERE 
            search_date >= :startDate
            AND search_date < :endDate
        GROUP BY 
            search_text
        ORDER BY 
            count DESC
        limit 10
        """, nativeQuery = true)
    List<Object[]> findSearchRankings(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
