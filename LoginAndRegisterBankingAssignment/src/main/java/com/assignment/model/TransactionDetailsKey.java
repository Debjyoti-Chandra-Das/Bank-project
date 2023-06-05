package com.assignment.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TransactionDetailsKey implements Serializable {

	private static final long serialVersionUID = 1L;
	@Column(name = "account_no", length = 10)
	private int accountNo;

	@Column(name = "reference_number", length = 10)
	private int referenceNumber;
}
