package com.jspp.devoka.term.repository;

import com.jspp.devoka.term.damain.Term;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {

    Page<Term> findByKorNameContainingOrEngNameContainingOrAbbNameContaining(String korName, String engName, String abbName, Pageable pageable);
}
