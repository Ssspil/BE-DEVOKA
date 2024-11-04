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
public class CategoryResponse {

    private Long categoryNo;
    private String categoryId;
    private String categoryName;
    private Integer sortOrder;
    private Integer depth;
    private List<CategoryResponse> childList;


    /**
     * 엔티티 -> ResponseDto로 변경
     * @param categoryEntity
     * @return
     */
    public static CategoryResponse fromEntity(Category categoryEntity) {
        CategoryResponseBuilder builder = CategoryResponse.builder()
                .categoryNo(categoryEntity.getCategoryNo())
                .categoryId(categoryEntity.getCategoryId())
                .categoryName(categoryEntity.getCategoryName())
                .sortOrder(categoryEntity.getSortOrder())
                .depth(categoryEntity.getDepth());

        if(categoryEntity.getChildCategories() != null) {
            List<CategoryResponse> childList = categoryEntity.getChildCategories().stream()
                    .map(CategoryResponse::fromEntity)
                    .collect(Collectors.toList());
            builder.childList(childList);
        }

        return builder.build();
    }
}
