package com.jspp.devoka.term.repository;

import com.jspp.devoka.term.damain.PopularSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopularSearchRepository extends JpaRepository<PopularSearch, Long>  {


    PopularSearch findTopByOrderByCreateDateDesc();
}