package com.jspp.devoka.tag.domain;

import com.jspp.devoka.common.entity.Auditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Table(name = "tag_info")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_no")
    private Long tagNo;

    @Column(name = "tag_nane")
    private String tagName;

    @Column(name = "delete_yn")
    private String deleteYn;

    @Embedded
    private Auditable auditable;
}
