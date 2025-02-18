package com.erp.entity;

import com.erp.auditable.Auditable;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CustomerEntity extends Auditable {
    @Column(name = "nombre", nullable = false, length = 100)
    private String name;
    @Column(name = "email", unique = true, length = 100)
    private String email;
    @Column(name = "telefono", length = 20)
    private String phone;
    @Column(name = "direccion", columnDefinition = "TEXT")
    private String address;
}
