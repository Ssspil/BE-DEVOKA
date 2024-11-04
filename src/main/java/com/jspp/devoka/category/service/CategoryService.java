package com.jspp.devoka.category.service;

import com.jspp.devoka.category.domain.Category;
import com.jspp.devoka.category.dto.CategoryResponse;
import com.jspp.devoka.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 목록 조회
     * @param pageable
     * @return
     */
    public List<CategoryResponse> getCategoryList(Pageable pageable){
        Page<Category> pageCategories = categoryRepository.findAll(pageable);
        List<Category> content = pageCategories.getContent();

        return content.stream()
                .map(CategoryResponse::fromEntity)
                .collect(Collectors.toList());
    }


}
