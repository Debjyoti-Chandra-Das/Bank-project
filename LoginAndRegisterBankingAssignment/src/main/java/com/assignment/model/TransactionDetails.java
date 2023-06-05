package com.assignment.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction_details")
public class TransactionDetails {

	@EmbeddedId
	private TransactionDetailsKey id;
	@Column(name = "transaction_flag", nullable = false, length = 1, columnDefinition = "CHAR(1) CHECK (transaction_flag IN ('C', 'D'))")
	private String transactionFlag;
	@Column(name = "transaction_amount", nullable = false, length = 12, scale = 2)
	private double transactionAmount;

}
