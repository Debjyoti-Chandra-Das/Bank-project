package com.assignment.response.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryResponse {
	private double amount;
	private int referenceNumber;
	private String transactionFlag;
}
