package com.jspp.devoka.category.service;

import com.jspp.devoka.category.domain.Category;
import com.jspp.devoka.category.dto.CategoryResponse;
import com.jspp.devoka.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 목록 조회
     * @param page
     * @param size
     * @return
     */
    public List<CategoryResponse> getCategoryList(int page, int size){
        // 페이징 객체 생성
        Pageable pageable = PageRequest.of(page, size);

        Page<Category> findCategoriesPage = categoryRepository.findAll(pageable);
        List<Category> content = findCategoriesPage.getContent();

        return content.stream()
                .map(CategoryResponse::fromEntity)
                .collect(Collectors.toList());
    }


}
