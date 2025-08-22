package com.hnh.enterprise.core.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

/**
 * AuditingDTO
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class AuditingDTO {

    private String createdBy;

    @Builder.Default
    private Instant createdDate = Instant.now();

    private String lastModifiedBy;

    @Builder.Default
    private Instant lastModifiedDate = Instant.now();
}
