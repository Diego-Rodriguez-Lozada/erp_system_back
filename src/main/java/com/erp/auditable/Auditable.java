package com.erp.auditable;

import com.erp.auth.domain.RequestContext;
import com.erp.exception.ApiException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.AlternativeJdkIdGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public abstract class Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;
    private String referenceId = new AlternativeJdkIdGenerator().generateId().toString();

    @NotNull
    private Long createdBy;

    @NotNull
    private Long updatedBy;

    @NotNull
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void beforePersist() {
        var userId = RequestContext.getUserId();
        if (userId == null) {
            throw new ApiException("No se puede persistir en entidad sin ID de usuario en requestContext para este hilo.");
        }
        setCreatedAt(LocalDateTime.now());
        setCreatedBy(userId);
        setUpdatedBy(userId);
        setUpdatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void beforeUpdate() {
        var userId = RequestContext.getUserId();
        if (userId == null) {
            throw new ApiException("No se puede actualizar la entidad sin ID de usuario en requestContext para este hilo.");
        }
        setUpdatedAt(LocalDateTime.now());
        setUpdatedBy(userId);
    }
}
