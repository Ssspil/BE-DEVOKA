package com.jspp.devoka.term.repository;

import com.jspp.devoka.term.damain.Term;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {

    // 용어 Like 검색 조회
    List<Term> findByKorNameContainingOrEngNameContainingOrAbbNameContainingAndDeleteYn(String keyword, String keyword1, String keyword2, String deleteYn);

    // 카테고리 별 용어 목록 조회
    Page<Term> findByCategory_CategoryIdAndDeleteYn(String categoryId, String deleteYn, Pageable pageable);
}
