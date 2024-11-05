package com.jspp.devoka.term.damain;

import com.jspp.devoka.category.domain.Category;
import com.jspp.devoka.common.domain.Auditable;
import com.jspp.devoka.term.dto.request.TermUpdateRequest;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Table(name = "term_info")
@Builder
@AllArgsConstructor
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
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @Embedded
    private Auditable auditable;

    @PrePersist
    public void onCreate(){
        this.approvalYn = "N";
        this.deleteYn = "N";
        if (auditable == null) {
            auditable = new Auditable(); // Auditable 객체 초기화
        }
    }


    public void updateTerm(TermUpdateRequest updateTermRequest, Category category){
        this.korName = updateTermRequest.getKorName();
        this.engName = updateTermRequest.getEngName();
        this.abbName = updateTermRequest.getAbbName();
        this.definition = updateTermRequest.getDefinition();
        this.category = category;
    }
}
