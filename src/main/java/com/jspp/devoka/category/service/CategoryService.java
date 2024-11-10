package com.jspp.devoka.category.service;

import com.jspp.devoka.category.domain.Category;
import com.jspp.devoka.category.dto.CategoryListResponse;
import com.jspp.devoka.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public List<CategoryListResponse> getCategoryList(int page, int size) {
        // 페이징 객체 생성 + sortOrder에 따라 정렬
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("sortOrder")));

        Page<Category> findCategoriesPage = categoryRepository.findAll(pageable);
        List<Category> content = findCategoriesPage.getContent();

        return content.stream()
                .map(CategoryListResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리 조회
     * @param categoryId
     * @return
     */
    public Category findByCategoryId(String categoryId){
        // 카테고리 조회
        return categoryRepository.findByCategoryId(categoryId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
    }
}
