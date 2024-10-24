package com.jspp.devoka.term.service;


import com.jspp.devoka.term.damain.Category;
import com.jspp.devoka.term.damain.Term;
import com.jspp.devoka.term.dto.CreateTermRequest;
import com.jspp.devoka.term.dto.TermResponse;
import com.jspp.devoka.term.dto.UpdateTermRequest;
import com.jspp.devoka.term.repository.TermCategoryRepository;
import com.jspp.devoka.term.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;
    private final TermCategoryRepository termCategoryRepository;

    public TermResponse createTerm(CreateTermRequest termRequest){
        Optional<Category> optionalCategory = termCategoryRepository.findByCategoryId(termRequest.getCategoryId());
        Category findCategory = null;

        if(optionalCategory.isPresent()) {
            findCategory = optionalCategory.get();
        }
        Term term = new Term(termRequest.getKorName(), termRequest.getEngName(), termRequest.getAbbName(), termRequest.getDefinition(), findCategory);
        Term saveTerm = termRepository.save(term);

        TermResponse termResponse = new TermResponse(saveTerm.getTermNo(), saveTerm.getKorName(), saveTerm.getEngName(), saveTerm.getAbbName(), saveTerm.getDefinition(), saveTerm.getCategory().getCategoryId(), saveTerm.getCategory().getCategoryName());

        return termResponse;
    }

    public Page<TermResponse> searchTerm(String keyword, int page, int size) {

        // PageRequest 객체 생성 (페이징 처리용)
        Pageable pageable = PageRequest.of(page, size);

        // LIKE 검색 + 페이징 처리된 결과를 가져옴
        Page<Term> findListTerm = termRepository.findByKorNameContainingOrEngNameContainingOrAbbNameContaining(keyword, keyword, keyword, pageable);

        // Page<Term>을 Page<TermResponse>로 변환
        return findListTerm.map(this::convertToTermResponse);

    }

    private TermResponse convertToTermResponse(Term term){
        TermResponse.TermResponseBuilder builder = TermResponse.builder()
                .termNo(term.getTermNo())
                .korName(term.getKorName())
                .engName(term.getEngName())
                .abbName(term.getAbbName())
                .definition(term.getDefinition())
                .categoryId(term.getCategory().getCategoryId())
                .categoryName(term.getCategory().getCategoryName());

        return builder.build();
    }


    public Page<TermResponse> getTermList(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Term> termPage = termRepository.findAll(pageable);

        // Term 리스트를 TermResponse로 변환하여 반환
        return termPage.map(this::convertToTermResponse);
    }

    public TermResponse updateTerm(UpdateTermRequest termRequest) {

        Optional<Term> findTerm = termRepository.findById(termRequest.getTermNo());
        Term existingTerm = findTerm.get();

        // 카테고리 ID로 카테고리 조회
        Optional<Category> optionalCategory = termCategoryRepository.findByCategoryId(termRequest.getCategoryId());
        Category updatedCategory = optionalCategory.orElse(null); // 카테고리가 없으면 null로 설정


        Term updatedTerm = new Term(
                existingTerm.getTermNo(), // 기존 ID 유지
                termRequest.getKorName() != null && !termRequest.getKorName().isEmpty() ? termRequest.getKorName() : existingTerm.getKorName(),
                termRequest.getEngName() != null && !termRequest.getEngName().isEmpty() ? termRequest.getEngName() : existingTerm.getEngName(),
                termRequest.getAbbName() != null && !termRequest.getAbbName().isEmpty() ? termRequest.getAbbName() : existingTerm.getAbbName(),
                termRequest.getDefinition() != null && !termRequest.getDefinition().isEmpty() ? termRequest.getDefinition() : existingTerm.getDefinition(),
                updatedCategory // 카테고리 업데이트
        );

        Term saveTerm = termRepository.save(updatedTerm);

        TermResponse termResponse = new TermResponse(saveTerm.getTermNo(), saveTerm.getKorName(), saveTerm.getEngName(), saveTerm.getAbbName(), saveTerm.getDefinition(), saveTerm.getCategory().getCategoryId(), saveTerm.getCategory().getCategoryName());

        return termResponse;
    }
}
