package com.jspp.devoka.term.service;


import com.jspp.devoka.category.domain.Category;
import com.jspp.devoka.category.service.CategoryService;
import com.jspp.devoka.common.exception.ErrorCode;
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

import java.text.Collator;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TermService {

    // 용어 Repository
    private final TermRepository termRepository;


    // 카테고리 서비스
    private final CategoryService categoryService;
    // 검색 이력 서비스
    private final SearchHistoryService searchHistoryService;


    /**
     * 카테고리별 용어 목록 조회
     * @param page
     * @param size
     * @param categoryId
     * @return
     */
    public TermListResponse getTermListByCategory(int page, int size, String categoryId) {
        String approvalYn = "Y";
        String deleteYn = "N";

        Pageable pageable = PageRequest.of(page, size);

        // 카테고리 정보
        Category category = categoryService.findByCategoryId(categoryId);
        Page<Term> findTermPage = termRepository.findByCategory_CategoryIdAndDeleteYnAndApprovalYn(categoryId, deleteYn, approvalYn, pageable);

        List<Term> content = findTermPage.getContent();
        List<TermResponse> list = content.stream()
                .map(TermResponse::fromEntity)
                .sorted(Comparator.comparing(TermResponse::getKorName, Collator.getInstance(Locale.KOREAN)))
                .toList();

        log.info("{} 카테고리 용어 리스트 조회", category.getCategoryName());
        // Term 리스트를 TermResponse로 변환하여 반환
        return TermListResponse.of(category.getCategoryId(), category.getCategoryName(), list);
    }

    /**
     * TODO 반복문이 아닌 그룹별 용어로 한방에 가져오도록 수정
     * 용어 목록 전체 조회
     * @param page
     * @param size
     * @return
     */
    public List<TermListResponse> getTermAllList(int page, int size) {
        String approvalYn = "Y";
        String deleteYn = "N";

        Pageable pageable = PageRequest.of(page, size);

        // 카테고리 리스트 조회 : 카테고리 개수 10개로 우선 고정
        List<Category> categoryEntityList = categoryService.getCategoryEntityList(0, 10);

        List<TermListResponse> list = new ArrayList<>();

        for (Category category : categoryEntityList) {
            // 카테고리별 용어 조회
            Page<Term> findTermPage = termRepository.findByCategory_CategoryIdAndDeleteYnAndApprovalYn(category.getCategoryId(), deleteYn, approvalYn, pageable);
            // 진짜 데이터
            List<Term> content = findTermPage.getContent();
            // Dto로 변경
            List<TermResponse> listDto = content.stream()
                    .map(TermResponse::fromEntity)
                    .sorted(Comparator.comparing(TermResponse::getKorName, Collator.getInstance(Locale.KOREAN)))
                    .toList();

            // list화
            TermListResponse wrapListDto = TermListResponse.of(category.getCategoryId(), category.getCategoryName(), listDto);
            list.add(wrapListDto);
        }

        log.info("모든 카테고리 용어 리스트 조회");
        // Term 리스트를 List<TermResponse>로 변환하여 반환
        return list;
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

        // 생성된 Term TermCreateResponse로 변환하여 반환
        return TermCreateResponse.fromEntity(saveTerm);
    }


    /**
     * 용어 검색
     * @param keyword
     * @return
     */
    @Transactional
    public List<TermSearchResponse> searchTerm(String keyword) {
        String approvalYn = "Y";
        String deleteYn = "N";

        // 검색 문자열 유효성 검사
        if(!validationKeyword(keyword)){
            log.warn("사용자가 이상한 문자를 검색하였습니다. : {}", keyword);
            return Collections.EMPTY_LIST;
        }

        // 네이티브 쿼리 이용해서 검색 조회
        String searchKeyword = keyword.trim().replaceAll("\\s+", " & "); // 불필요한 공백 제거
        List<Term> findList = termRepository.findSearchTerm(searchKeyword, approvalYn, deleteYn);

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


        // 검색한 데이터 있을 떄, 검색 이력 추가(비동기)
        if(!findList.isEmpty()) {
            searchHistoryService.saveRequest(keyword, findList, responseData);
        }

        log.info("사용자가 검색한 용어 : {}", keyword);
        // 검색된 용어 TermSearchResponse로 변환하여 반환
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

        // 값 수정하여 업데이트
        findTerm.updateTerm(termRequest, updateCategory);

        // 수정된 Term TermUpdateResponse 변환하여 반환
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
     * 랜덤 추천 용어 10개
     * // TODO 랜덤 20개, 최신20개, 관리자 설정 등 할 수 있게 수정
     * @return
     */
    public List<TermResponse> recommendTermList(){
        String approvalYn = "Y";
        String deleteYn = "N";
        // 1부터 500,000,000까지 랜덤 생성
        int randomValue = ThreadLocalRandom.current().nextInt(1, 500_000_001);


        // 랜덤 숫자 보다 같거나 큰 Random ID 값들을 랜덤으로 조회
        List<Term> list = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            list = termRepository.findRandomByApprovalYnAndDeleteYnAndRandomValueGreaterThan(approvalYn, deleteYn, randomValue);
            if(list.size() == 10) break;
        }

        log.info("추천 용어 조회 개수 : {}", list.size());
        // Term 리스트를 TermResponse로 변환하여 반환
        return list.stream().map(TermResponse::fromEntity).toList();
    }


    /**
     * 검색 키워드 유효성 검사
     * @param keyword
     * @return
     */
    private boolean validationKeyword(String keyword){
        String regexp = "^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\s]*$";

        if (keyword == null || keyword.isEmpty()) {
            return false; // 키워드가 null이거나 빈 값이면 false 반환
        }
        return keyword.matches(regexp); // 정규식과 일치하면 true 반환
    }
}
