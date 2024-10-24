package com.jspp.devoka.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class Auditable {
    @Column(name = "create_by")
    private String createdBy;

    @Column(name = "create_date")
    private LocalDateTime createdDate;

    @Column(name = "update_by")
    private String updatedBy;

    @Column(name = "update_date")
    private LocalDateTime updatedDate;
}
