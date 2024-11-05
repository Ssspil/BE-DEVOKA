package com.jspp.devoka.term.service;


import com.jspp.devoka.category.domain.Category;
import com.jspp.devoka.term.damain.Term;
import com.jspp.devoka.term.dto.request.TermCreateRequest;
import com.jspp.devoka.term.dto.response.TermCreateResponse;
import com.jspp.devoka.term.dto.response.TermListResponse;
import com.jspp.devoka.term.dto.request.TermUpdateRequest;
import com.jspp.devoka.category.repository.CategoryRepository;
import com.jspp.devoka.term.dto.response.TermSearchResponse;
import com.jspp.devoka.term.dto.response.TermUpdateResponse;
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
public class TermService {

    private final TermRepository termRepository;
    private final CategoryRepository termCategoryRepository;

    /**
     * 용어 생성
     * @param termRequest
     * @return
     */
    public TermCreateResponse createTerm(TermCreateRequest termRequest){

        // 카테고리 조회
        Category findCategory = termCategoryRepository.findByCategoryId(termRequest.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

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
        Page<Term> findPageTerm = termRepository.findByKorNameContainingOrEngNameContainingOrAbbNameContaining(keyword, keyword, keyword, pageable);
        List<Term> content = findPageTerm.getContent();

        return content.stream().map(TermSearchResponse::fromEntity).toList();
    }

    /**
     * 전체 용어 목록 조회
     * @param page
     * @param size
     * @return
     */
    public List<TermListResponse> getTermList(int page, int size, String categoryId) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Term> findTermPage = (categoryId == null || categoryId.isEmpty())
                ? termRepository.findAll(pageable)
                : termRepository.findByCategory_CategoryId(categoryId, pageable);

        List<Term> content = findTermPage.getContent();

        // Term 리스트를 TermResponse로 변환하여 반환
        return content.stream().map(TermListResponse::fromEntity).toList();
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
        Optional<Category> optionalCategory = termCategoryRepository.findByCategoryId(termRequest.getCategoryId());
        Category updatedCategory = optionalCategory.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        findTerm.updateTerm(termRequest, updatedCategory);

        return TermUpdateResponse.fromEntity(findTerm);
    }

    /**
     * 용어 삭제
     * @param termNo
     * @return
     */
//    public TermResponse deleteTerm(Long termNo){
//
//        Term findTerm = termRepository.findById(termNo).orElseThrow(() -> new IllegalArgumentException("존재하지 않은 용어 번호입니다."));
//
//
//
//        return TermResponse.fromEntity(findTerm);
//    }
}
