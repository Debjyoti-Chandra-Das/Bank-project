package com.assignment.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.assignment.model.UserDetails;
import com.assignment.model.AccountDetails;
import com.assignment.model.PrimaryKeyUserId;
import com.assignment.model.TransactionDetails;
import com.assignment.model.TransactionDetailsKey;
import com.assignment.repository.AccountDetailsRepository;
import com.assignment.repository.TransactionDetailsRepository;
import com.assignment.repository.UserDetailsRepository;
import com.assignment.request.dto.AccountDetailsRequest;
import com.assignment.request.dto.CheckingBalance;
import com.assignment.request.dto.TransactionDetailsRequest;
import com.assignment.request.dto.UserRequest;
import com.assignment.response.dto.TransactionHistoryResponse;
import com.assignment.response.dto.TransferResponse;
import com.assignment.response.dto.UserResponse;
import com.assignment.response.dto.checkingBalanceResponse;
import com.assignment.service.UserService;
import com.assignment.utils.JwtUtils;
import java.util.*;
@Service
public class UserServiceImp implements UserService {

	@Autowired
	private UserDetailsRepository detailsRepository;
	@Autowired
	private AccountDetailsRepository accountDetailsRepository;
	@Autowired
	private TransactionDetailsRepository transactionDetailsRepository;
	@Autowired
	private JwtUtils jwtUtils;

	@Override
	public UserResponse register(UserRequest request) {
		UserDetails checkId = detailsRepository.findByprimaryKeyUserIdAccountNumber(request.getAccountNo());
		AccountDetails accountDetails=new AccountDetails();
		if (checkId == null) {
			UserDetails details = new UserDetails();
			PrimaryKeyUserId userId = new PrimaryKeyUserId();
			userId.setUserId(request.getUserId());
			userId.setAccountNumber(request.getAccountNo());
			details.setPrimaryKeyUserId(userId);
			details.setPassword(request.getPassword());
			details = detailsRepository.save(details);
			//set value in the account details 
			accountDetails.setAccountNo(request.getAccountNo());
			accountDetails.setAvailableBalance(0);
			accountDetailsRepository.save(accountDetails);
			return new UserResponse(HttpStatus.OK.value(), details, "Registration Successfully");
		} else {
			return new UserResponse(HttpStatus.OK.value(), new UserDetails(),
					"you entered ID and account number is already exist");
		}
	}

	@Override
	public UserResponse userLogin(UserRequest request) {
		UserDetails checkId = detailsRepository.findByprimaryKeyUserIdUserIdAndPassword(request.getUserId(),
				request.getPassword());
		UserResponse response = new UserResponse();
		if (checkId == null) {
			response.setData("User login faild");
			response.setResponseDescription("Bad Credentials");
			response.setResponseCode(HttpStatus.OK.value());
			return response;
		}
		String token = jwtUtils.generateJwt(checkId);
		response.setData(token);
		response.setResponseDescription("login successfully");
		response.setResponseCode(HttpStatus.OK.value());
		return response;
	}

	@Override
	public UserResponse getAccount(int accountNo) {
		UserDetails userDetails1 = detailsRepository.findByprimaryKeyUserIdAccountNumber(accountNo);
		return new UserResponse(HttpStatus.OK.value(), userDetails1, "");

	}

	public UserResponse addFund(AccountDetailsRequest accountDetailsRequest) {
		UserResponse response = new UserResponse();
		AccountDetails accountDetails = new AccountDetails();
		UserDetails checkAccount = detailsRepository
				.findByprimaryKeyUserIdAccountNumber(accountDetailsRequest.getAccountNo());
		if (checkAccount != null) {

			AccountDetails details = accountDetailsRepository.findByaccountNo(accountDetailsRequest.getAccountNo());
			accountDetails.setAccountNo(accountDetailsRequest.getAccountNo());
			accountDetails.setAvailableBalance(accountDetailsRequest.getAddBalance() + details.getAvailableBalance());
			accountDetailsRepository.save(accountDetails);
			response.setData(accountDetails);
			response.setResponseCode(200);
			response.setResponseDescription("Balance added successfully !");
		} else {
			response.setData("Sorry! Account Number is not exist");
			response.setResponseCode(401);
			response.setResponseDescription("Balance added failed!");
		}
		return response;
	}

	public UserResponse getTransfer(TransactionDetailsRequest request) {
	//	UserDetails userDetails1 = detailsRepository.findByprimaryKeyUserIdUserId(id);
		UserResponse response = new UserResponse();
		TransferResponse transferResponse = new TransferResponse();
		AccountDetails accountDetails = new AccountDetails();
		TransactionDetails transactionDetails = new TransactionDetails();

		double amount = request.getAmount();
		int fromAccount = request.getFromAccount();
		int toAccount = request.getToAccount();
         
		// getting reference number 
		int referenceNumber = jwtUtils.genrateRandomNumber();
		// setting value in transaction key class
		//TransactionDetailsKey detailsKeyDebit = new TransactionDetailsKey(fromAccount,referenceNumber );
		//TransactionDetailsKey detailsKeyCredit = new TransactionDetailsKey(toAccount,referenceNumber );

		// check client entered account number is present or not
		AccountDetails checkFromAccount = accountDetailsRepository.findByaccountNo(fromAccount);
		AccountDetails checkToAccount = accountDetailsRepository.findByaccountNo(toAccount);

		// Condition to check account numbers are null
		if (checkFromAccount != null && checkToAccount != null) {
			double fromAmount = checkFromAccount.getAvailableBalance();
			if (fromAmount < amount) {
				response.setData("Insufficient amount");
				response.setResponseCode(200);
				response.setResponseDescription("please check your balance, before transaction");
				return response;
			} else {
				double avlBalance = fromAmount - amount;
				double toAvlBal = amount + checkToAccount.getAvailableBalance();
				checkFromAccount.setAvailableBalance(avlBalance);
				checkToAccount.setAvailableBalance(toAvlBal);

				// Setting value in transaction details table for debited
				this.save(fromAccount, referenceNumber, "D", avlBalance);
				
				// Setting value in transaction details table for credited
				this.save(toAccount, referenceNumber, "C", toAvlBal);
		
			//	transferResponse.setAccountBalance(avlBalance);
				transferResponse.setReferenceNumber(referenceNumber);
				// set data in response
				response.setData(transferResponse);
				response.setResponseCode(200);
				response.setResponseDescription("Transaction has successully!");
			}
		}else {
		response.setResponseCode(401);
		response.setResponseDescription("Account Number are not correct!");
		}
		return response;
	}
    public void save(int accountNo , int referenceNumber, String transactionFlag , Double transactionAmount) {
    	 TransactionDetails transactionsDetails = new TransactionDetails();
	        TransactionDetailsKey transactionDetailsKey= new TransactionDetailsKey();
	        transactionDetailsKey.setAccountNo(accountNo);
	        transactionDetailsKey.setReferenceNumber(referenceNumber);
	        transactionsDetails.setId(transactionDetailsKey);
	        transactionsDetails.setTransactionAmount(transactionAmount);
	        transactionsDetails.setTransactionFlag(transactionFlag);
	        transactionDetailsRepository.save(transactionsDetails);
    }

	public UserResponse getAccBalance(CheckingBalance balance) {
		AccountDetails accountDetails = accountDetailsRepository.findByaccountNo(balance.getAccountNo());
        UserResponse response = new UserResponse();
		if(accountDetails == null) {
        	response.setData(null);
        	response.setResponseCode(401);
        	response.setResponseDescription("You enterd account number is not exist!");      
        	}else {
        	
        		response.setData(new checkingBalanceResponse(accountDetails.getAvailableBalance()));
        		response.setResponseCode(200);
            	response.setResponseDescription("Thank you");      
        	}
		return response;
	}
	
	 @Override
	    public UserResponse getTransactionHistory(int accountNo) {
		   UserResponse response = new UserResponse();
	        List<TransactionDetails> transactions = transactionDetailsRepository.findByidAccountNo(accountNo);
	        if(transactions == null) {
	        	response.setResponseDescription("Account number is not exist");
	        	response.setResponseCode(401);
	        }else {
	        	response.setData(transactions);
	        	response.setResponseCode(200);
	        	response.setResponseDescription("success!");
	        	 
	        }
	        return response;

	       
	    }
}
