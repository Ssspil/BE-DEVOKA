package com.jspp.devoka.term.damain;

import com.jspp.devoka.common.domain.Auditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "term_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Term {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "term_no")
    private Long termNo;

    @Column(name = "term_korean_name")
    private String korName;

    @Column(name = "term_english_name")
    private String engName;

    @Column(name = "term_abbreviation_name")
    private String abbName;

    @Column(name = "definition")
    private String definition;

    @Column(name = "approval_yn")
    private String approvalYn;

    @Column(name = "delete_yn")
    private String deleteYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Embedded
    private Auditable auditable;
}