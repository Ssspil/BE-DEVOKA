package com.jspp.devoka.term.repository;

import com.jspp.devoka.term.damain.PopularSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PopularSearchRepository extends JpaRepository<PopularSearch, Long>  {


    Optional<PopularSearch> findTopByOrderByCreateDateDesc();
}