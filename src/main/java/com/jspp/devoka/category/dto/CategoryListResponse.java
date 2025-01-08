package com.jspp.devoka.category.dto;

import com.jspp.devoka.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CategoryListResponse {

    private Long categoryNo;
    private String categoryId;
    private String categoryName;
    private Integer sortOrder;
    private Integer depth;
    private List<CategoryListResponse> subCategoryList;


    /**
     * 엔티티 -> ResponseDto로 변경
     * @param categoryEntity
     * @return
     */
    public static CategoryListResponse fromEntity(Category categoryEntity) {
        CategoryListResponseBuilder builder = CategoryListResponse.builder()
                .categoryNo(categoryEntity.getCategoryNo())
                .categoryId(categoryEntity.getCategoryId())
                .categoryName(categoryEntity.getCategoryName())
                .sortOrder(categoryEntity.getSortOrder())
                .depth(categoryEntity.getDepth());

        // 하위 카테고리
        if(categoryEntity.getSubCategories() != null) {
            List<CategoryListResponse> childList = categoryEntity.getSubCategories().stream()
                    .map(CategoryListResponse::fromEntity)
                    .collect(Collectors.toList());
            builder.subCategoryList(childList);
        }

        return builder.build();
    }
}
