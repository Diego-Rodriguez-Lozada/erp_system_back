package com.erp.entity;

import com.erp.auditable.Auditable;
import com.erp.enumeration.SaleStateEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ventas")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class SalesEntity extends Auditable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private CustomerEntity customer;
    @CreationTimestamp
    @Column(name = "fecha", nullable = false)
    private LocalDateTime date;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal total;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private SaleStateEnum saleState;
}
