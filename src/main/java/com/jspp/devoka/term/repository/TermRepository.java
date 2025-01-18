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
    Page<Term> findByCategory_CategoryIdAndDeleteYnAndApprovalYn(String categoryId, String deleteYn, String approvalYn, Pageable pageable);

    // 추천 용어 목록 조회
    @Query(value = """
        SELECT t
        FROM Term t
        WHERE t.approvalYn = :approvalYn
          AND t.deleteYn = :deleteYn
          AND t.randomId >= :randomValue
        ORDER BY RANDOM()
        LIMIT 20
    """)
    List<Term> findRandomByApprovalYnAndDeleteYnAndRandomValueGreaterThan(@Param("approvalYn") String approvalYn, @Param("deleteYn") String deleteYn, @Param("randomValue") int randomValue);

    // 용어 검색 조회
    // TODO *로 조회 말고 칼럼 값으로 조회로 변경
    @Query(value = """
        SELECT ts_rank(t.document_search, to_tsquery(:keyword)) as rank,
                t.* 
        FROM devoka.term_info t
        WHERE t.document_search @@ to_tsquery(:keyword)
            AND t.approval_yn = :approvalYn
            AND t.delete_yn = :deleteYn
        ORDER BY rank desc, t.term_korean_name, t.term_english_name , t.term_abbreviation_name
    """, nativeQuery = true)
    List<Term> findSearchTerm(@Param("keyword") String keyword, @Param("approvalYn") String approvalYn, @Param("deleteYn") String deleteYn);

}
