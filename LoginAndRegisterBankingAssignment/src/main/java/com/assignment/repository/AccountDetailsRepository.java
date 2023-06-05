package com.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.model.AccountDetails;

public interface AccountDetailsRepository extends JpaRepository<AccountDetails, Integer>{

	AccountDetails findByaccountNo(int accountNo);

}
