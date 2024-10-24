package com.jspp.devoka.tag.domain;


import com.jspp.devoka.term.damain.Term;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "mapping_term_tag")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MappingTermTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_no")
    private Term term;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teg_no")
    private Tag tag;

    @Column(name = "create_by")
    private String createdBy;

    @Column(name = "create_date")
    private LocalDateTime createdDate;
}
