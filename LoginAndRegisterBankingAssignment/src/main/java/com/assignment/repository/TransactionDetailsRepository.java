package com.assignment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.model.TransactionDetails;
import com.assignment.model.TransactionDetailsKey;

public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, TransactionDetailsKey>{
	List<TransactionDetails> findByidAccountNo(int accountNo);
}
