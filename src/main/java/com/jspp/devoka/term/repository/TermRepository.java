package com.jspp.devoka.term.repository;

import com.jspp.devoka.term.damain.Term;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {

    // 용어 Like 검색 조회
    List<Term> findByKorNameContainingOrEngNameContainingOrAbbNameContainingOrDefinitionContainingAndDeleteYnAndApprovalYn(String korName, String engName, String abbName, String definition, String deleteYn, String approvalYn);

    // 카테고리 별 용어 목록 조회
    Page<Term> findByCategory_CategoryIdAndDeleteYn(String categoryId, String deleteYn, Pageable pageable);

    // 추천 용어 목록 조회
    // LIMIT과 random을 사용하기 위해 네이티브 쿼리 사용
    @Query(value = """
        SELECT * 
        FROM devoka.term_info 
        WHERE approval_yn = :approvalYn
          AND delete_yn = :deleteYn
        ORDER BY RANDOM()
        LIMIT 20
    """, nativeQuery = true)
    List<Term> findRandomByApprovalYnAndDeleteYn(@Param("approvalYn") String approvalYn, @Param("deleteYn") String deleteYn);
}
