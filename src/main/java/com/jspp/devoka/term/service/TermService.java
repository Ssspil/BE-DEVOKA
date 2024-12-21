package com.jspp.devoka.term.service;


import com.jspp.devoka.category.domain.Category;
import com.jspp.devoka.category.service.CategoryService;
import com.jspp.devoka.common.exception.ErrorCode;
import com.jspp.devoka.history.domain.SearchHistory;
import com.jspp.devoka.history.service.SearchHistoryService;
import com.jspp.devoka.term.damain.Term;
import com.jspp.devoka.term.dto.request.TermCreateRequest;
import com.jspp.devoka.term.dto.response.*;
import com.jspp.devoka.term.dto.request.TermUpdateRequest;
import com.jspp.devoka.term.exception.TermNotFoundException;
import com.jspp.devoka.term.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;

    private final CategoryService categoryService;

    private final SearchHistoryService searchHistoryService;


    /**
     * 카테고리별 용어 목록 조회
     * @param page
     * @param size
     * @param categoryId
     * @return
     */
    public TermListResponse getTermListByCategory(int page, int size, String categoryId) {

        Pageable pageable = PageRequest.of(page, size);

        Category category = categoryService.findByCategoryId(categoryId);
        Page<Term> findTermPage = termRepository.findByCategory_CategoryIdAndDeleteYn(categoryId, "N", pageable);

        List<Term> content = findTermPage.getContent();
        List<TermResponse> list = content.stream().map(TermResponse::fromEntity).toList();

        // Term 리스트를 TermResponse로 변환하여 반환
        return TermListResponse.of(category.getCategoryId(), category.getCategoryName(), list);
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
     * @return
     */
    @Transactional
    public List<TermSearchResponse> searchTerm(String keyword) {

        // LIKE 검색 결과를 가져옴
        // TODO 엘라스틱 서치로 변경
        List<Term> findList = termRepository.findByKorNameContainingOrEngNameContainingOrAbbNameContainingOrDefinitionContainingAndDeleteYnAndApprovalYn(keyword, keyword, keyword, keyword, "N", "Y");

        // 카테고리 별로 그룹화
        Map<Category, List<Term>> termListGroupByCategoryId = findList.stream().collect(Collectors.groupingBy(Term::getCategory));

        // TermSearchResponse 리스트 생성 및 정렬
        List<TermSearchResponse> responseData =  termListGroupByCategoryId.entrySet().stream()
                .map(group -> {
                    Category category = group.getKey();
                    List<Term> termList = group.getValue();

                    List<TermResponse> list = termList.stream().map(TermResponse::fromEntity).toList();
                    return TermSearchResponse.of(category.getCategoryId(), category.getCategoryName(), list);
                })
                .sorted((e1, e2) -> e2.getData().size() - e1.getData().size()) // 데이터 수에 따른 내림차순 정렬
                .toList();


        // 검색 이력
        searchHistoryService.save(SearchHistory.create(keyword, responseData));

        return responseData;
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
        Term findTerm = termRepository.findById(termNo).orElseThrow(() -> new TermNotFoundException(ErrorCode.NOT_FOUND_TERM));

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
        Term findTerm = termRepository.findById(termNo).orElseThrow(() -> new TermNotFoundException(ErrorCode.NOT_FOUND_TERM));
        // 삭제
        findTerm.delete();
    }


    /**
     * 랜덤 추천 용어 20개
     * // TODO 랜덤 20개, 최신20개, 관리자 설정 등 할 수 있게 수정
     * @return
     */
    public List<TermResponse> recommendTerm(){
        String approval = "Y";
        String deleteYn = "N";
        List<Term> list = termRepository.findRandomByApprovalYnAndDeleteYn(approval, deleteYn);

        return list.stream().map(TermResponse::fromEntity).toList();
    }
}
