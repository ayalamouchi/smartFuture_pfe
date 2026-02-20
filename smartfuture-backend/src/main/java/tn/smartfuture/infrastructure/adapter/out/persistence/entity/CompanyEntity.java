package tn.smartfuture.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tn.smartfuture.domain.enums.CompanyType;

import java.math.BigDecimal;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "user_id")
public class CompanyEntity extends UserEntity {

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String contactName;

    @Column(nullable = false)
    private String address;

    private String ville;

    @Enumerated(EnumType.STRING)
    private CompanyType accountType = CompanyType.STANDARD;

    private BigDecimal budgetAllocated = BigDecimal.ZERO;

    private BigDecimal budgetConsumed = BigDecimal.ZERO;
}