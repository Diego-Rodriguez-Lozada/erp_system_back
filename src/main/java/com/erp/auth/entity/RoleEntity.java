package com.erp.auth.entity;

import com.erp.auditable.Auditable;
import com.erp.auth.enumeration.Authority;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RoleEntity extends Auditable {
    private String name;
    private Authority authorities;
}
