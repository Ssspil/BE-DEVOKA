package com.jspp.devoka.category.domain;

import com.jspp.devoka.common.entity.Auditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Table(name = "category_info")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_no")
    private Long categoryNo;

    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id", referencedColumnName = "category_id")
    private Category parentCategory;

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> subCategories = new ArrayList<>();

    @Column(name = "depth")
    private Integer depth;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "delete_yn")
    private String deleteYn;

    @Embedded
    private Auditable auditable;
}

