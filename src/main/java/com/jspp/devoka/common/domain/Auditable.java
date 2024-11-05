package com.jspp.devoka.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

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

    @PrePersist
    public void onCreate(){
        this.createdBy = (createdBy != null) ? createdBy : "SYSTEM";
        this.createdDate = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedBy = (updatedBy != null) ? updatedBy : "SYSTEM";
        this.updatedDate = LocalDateTime.now();
    }

}
