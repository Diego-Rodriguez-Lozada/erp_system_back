package com.erp.entity;

import com.erp.auditable.Auditable;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ProductEntity extends Auditable {
    @Column(name = "nombre", nullable = false, length = 100)
    private String name;
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String description;
    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    @Column(name = "stock", nullable = false)
    private int stock;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor")
    private SupplierEntity supplier;
}
