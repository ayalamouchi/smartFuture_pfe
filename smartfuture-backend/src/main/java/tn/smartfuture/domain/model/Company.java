package tn.smartfuture.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tn.smartfuture.domain.enums.CompanyType;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Company extends User {
    private String companyName;
    private String contactName;
    private String address;
    private String ville;  // ← AJOUTER CECI
    private CompanyType accountType;
    private BigDecimal budgetAllocated;
    private BigDecimal budgetConsumed;
}