package com.assignment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account_details")
public class AccountDetails {
	@Id
    @Column(name = "account_no",length = 10)
    private int accountNo;

    @Column(name = "available_balance",length = 12)
    private double availableBalance;
}
