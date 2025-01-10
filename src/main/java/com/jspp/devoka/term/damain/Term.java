package com.jspp.devoka.term.damain;

import com.jspp.devoka.category.domain.Category;
import com.jspp.devoka.common.entity.Auditable;
import com.jspp.devoka.term.dto.request.TermUpdateRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.concurrent.ThreadLocalRandom;


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

    @Column(name = "random_id")
    private int randomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @Column(name = "document_search", columnDefinition = "tsvector", insertable = false, updatable = false)
    private String documentSearch;

    @Embedded
    private Auditable auditable;

    // 엔티티 생성 때 Default 값
    @PrePersist
    private void onCreate(){
        this.approvalYn = "Y";
        this.deleteYn = "N";
        if (auditable == null) {
            auditable = new Auditable(); // Auditable 객체 초기화
        }
        // 1부터 1,000,000,000 까지 랜덤 생성
        this.randomId = ThreadLocalRandom.current().nextInt(1, 1_000_000_001);
    }

    // 엔티티 수정
    public void updateTerm(TermUpdateRequest updateTermRequest, Category category){
        this.korName = updateTermRequest.getKorName();
        this.engName = updateTermRequest.getEngName();
        this.abbName = updateTermRequest.getAbbName();
        this.definition = updateTermRequest.getDefinition();
        this.category = category;
    }

    // 엔티티 삭제
    public void delete() {
        this.deleteYn = "Y";
    }
}
