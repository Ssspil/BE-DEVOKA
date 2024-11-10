package com.jspp.devoka.term.service;


import com.jspp.devoka.category.domain.Category;
import com.jspp.devoka.category.service.CategoryService;
import com.jspp.devoka.term.damain.Term;
import com.jspp.devoka.term.dto.request.TermCreateRequest;
import com.jspp.devoka.term.dto.response.*;
import com.jspp.devoka.term.dto.request.TermUpdateRequest;
import com.jspp.devoka.term.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;

    private final CategoryService categoryService;


    /**
     * 카테고리별 용어 목록 조회
     * @param page
     * @param size
     * @return
     */
    public TermListResponse getTermListByCategory(int page, int size, String categoryId) {

        Pageable pageable = PageRequest.of(page, size);

        Category category = categoryService.findByCategoryId(categoryId);
        Page<Term> findTermPage = termRepository.findByCategory_CategoryIdAndDeleteYn(categoryId, "N", pageable);

        List<Term> content = findTermPage.getContent();
        List<TermResponse> list = content.stream().map(TermResponse::fromEntity).toList();

        // Term 리스트를 TermResponse로 변환하여 반환
        return new TermListResponse(category.getCategoryId(), category.getCategoryName(), list);
    }


    /**
     * 용어 생성
     * @param termRequest
     * @return
     */
    public TermCreateResponse createTerm(TermCreateRequest termRequest){

        // 카테고리 조회
        Category findCategory = categoryService.findByCategoryId(termRequest.getCategoryId());

        // 용어 저장
        Term saveTerm = termRepository.save(termRequest.toEntity(findCategory));

        return TermCreateResponse.fromEntity(saveTerm);
    }

    /**
     * 용어 검색
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    public List<TermSearchResponse> searchTerm(String keyword, int page, int size) {

        // 페이징 처리
        Pageable pageable = PageRequest.of(page, size);

        // LIKE 검색 + 페이징 처리된 결과를 가져옴
        // TODO 관리자가 승인 한 것만 노출 되도록 수정
        Page<Term> findPageTerm = termRepository.findByKorNameContainingOrEngNameContainingOrAbbNameContainingAndDeleteYn(keyword, keyword, keyword, pageable, "N");
        List<Term> content = findPageTerm.getContent();

        return content.stream().map(TermSearchResponse::fromEntity).toList();
    }

    /**
     * 용어 수정
     * @param termNo
     * @param termRequest
     * @return
     */
    @Transactional
    public TermUpdateResponse updateTerm(Long termNo, TermUpdateRequest termRequest) {

        // 용어 조회
        Term findTerm = termRepository.findById(termNo).orElseThrow(() -> new IllegalArgumentException("존재하지 않은 용어 번호입니다."));

        // 카테고리 ID로 카테고리 조회
        Category updateCategory = categoryService.findByCategoryId(termRequest.getCategoryId());

        findTerm.updateTerm(termRequest, updateCategory);

        return TermUpdateResponse.fromEntity(findTerm);
    }

    /**
     * 용어 삭제
     * @param termNo
     * @return
     */
    @Transactional
    public void deleteTerm(Long termNo){

        // 용어 조회
        Term findTerm = termRepository.findById(termNo).orElseThrow(() -> new IllegalArgumentException("존재하지 않은 용어 번호입니다."));
        // 삭제
        findTerm.delete();
    }
}
