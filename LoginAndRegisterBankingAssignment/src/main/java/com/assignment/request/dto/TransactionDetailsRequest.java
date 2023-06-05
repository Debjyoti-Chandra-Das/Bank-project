package com.assignment.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetailsRequest {
	private double amount;
	private int fromAccount;
	private int toAccount;
	

}
