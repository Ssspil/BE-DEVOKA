package com.jspp.devoka.term.service;


import com.jspp.devoka.category.domain.Category;
import com.jspp.devoka.term.damain.Term;
import com.jspp.devoka.term.dto.CreateTermRequest;
import com.jspp.devoka.term.dto.TermResponse;
import com.jspp.devoka.term.dto.UpdateTermRequest;
import com.jspp.devoka.category.repository.CategoryRepository;
import com.jspp.devoka.term.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TermService {

    private final TermRepository termRepository;
    private final CategoryRepository termCategoryRepository;

    /**
     * 용어 생성
     * @param termRequest
     * @return
     */
    public TermResponse createTerm(CreateTermRequest termRequest){

        // 카테고리 조회
        Category findCategory = termCategoryRepository.findByCategoryId(termRequest.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        // 용어 저장
        Term term = new Term(termRequest.getKorName(), termRequest.getEngName(), termRequest.getAbbName(), termRequest.getDefinition(), findCategory);
        Term saveTerm = termRepository.save(term);

        return TermResponse.fromEntity(saveTerm);
    }

    /**
     * 용어 검색
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    public List<TermResponse> searchTerm(String keyword, int page, int size) {

        // 페이징 처리
        Pageable pageable = PageRequest.of(page, size);

        // LIKE 검색 + 페이징 처리된 결과를 가져옴
        Page<Term> findPageTerm = termRepository.findByKorNameContainingOrEngNameContainingOrAbbNameContaining(keyword, keyword, keyword, pageable);
        List<Term> content = findPageTerm.getContent();

        return content.stream().map(TermResponse::fromEntity).toList();
    }

    /**
     * 전체 용어 목록 조회
     * @param page
     * @param size
     * @return
     */
    public List<TermResponse> getTermList(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Term> findTermPage = termRepository.findAll(pageable);
        List<Term> content = findTermPage.getContent();

        // Term 리스트를 TermResponse로 변환하여 반환
        return content.stream().map(TermResponse::fromEntity).toList();
    }

    /**
     * 용어 수정
     * @param termNo
     * @param termRequest
     * @return
     */
    public TermResponse updateTerm(Long termNo, UpdateTermRequest termRequest) {

        Term findTerm = termRepository.findById(termNo).orElseThrow(() -> new IllegalArgumentException("존재하지 않은 용어 번호입니다."));

        // 카테고리 ID로 카테고리 조회
        Optional<Category> optionalCategory = termCategoryRepository.findByCategoryId(termRequest.getCategoryId());
        Category updatedCategory = optionalCategory.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));


        Term updatedTerm = new Term(
                findTerm.getTermNo(), // 기존 ID 유지
                termRequest.getKorName() != null && !termRequest.getKorName().isEmpty() ? termRequest.getKorName() : findTerm.getKorName(),
                termRequest.getEngName() != null && !termRequest.getEngName().isEmpty() ? termRequest.getEngName() : findTerm.getEngName(),
                termRequest.getAbbName() != null && !termRequest.getAbbName().isEmpty() ? termRequest.getAbbName() : findTerm.getAbbName(),
                termRequest.getDefinition() != null && !termRequest.getDefinition().isEmpty() ? termRequest.getDefinition() : findTerm.getDefinition(),
                updatedCategory // 카테고리 업데이트
        );

        Term saveTerm = termRepository.save(updatedTerm);

        return TermResponse.fromEntity(saveTerm);
    }

    /**
     * 용어 삭제
     * @param termNo
     * @return
     */
    public TermResponse deleteTerm(Long termNo){

        Term findTerm = termRepository.findById(termNo).orElseThrow(() -> new IllegalArgumentException("존재하지 않은 용어 번호입니다."));



        return TermResponse.fromEntity(findTerm);
    }
}
