package com.jspp.devoka.term.service;

import com.jspp.devoka.term.damain.Category;
import com.jspp.devoka.term.dto.TermCategoryResponse;
import com.jspp.devoka.term.repository.TermCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermCategoryService {

    private final TermCategoryRepository categoryRepository;

    public List<TermCategoryResponse> getCategoryList(){
        List<Category> categoryList = categoryRepository.findAll();

        return categoryList.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
    }

    private TermCategoryResponse convertToCategoryResponse(Category category) {
        TermCategoryResponse.TermCategoryResponseBuilder builder = TermCategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .sortOrder(category.getSortOrder())
                .depth(category.getDepth());

        // 자식 카테고리가 있을 경우 처리
        if (category.getChildCategories() != null) {
            List<TermCategoryResponse> childResponses = category.getChildCategories().stream()
                    .map(this::convertToCategoryResponse)
                    .collect(Collectors.toList());
            builder.childList(childResponses);
        }

        return builder.build();
    }
}
