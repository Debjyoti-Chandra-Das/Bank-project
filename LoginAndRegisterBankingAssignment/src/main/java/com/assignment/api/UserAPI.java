package com.assignment.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.request.dto.AccountDetailsRequest;
import com.assignment.request.dto.CheckingBalance;
import com.assignment.request.dto.TransactionDetailsRequest;
import com.assignment.request.dto.TransactionHistory;
import com.assignment.request.dto.UserRequest;
import com.assignment.response.dto.UserResponse;
import com.assignment.service.imp.UserServiceImp;
import com.assignment.utils.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UserAPI {

	@Autowired
	private UserServiceImp userService;
	@Autowired
	private JwtUtils jwtUtils;
	UserResponse response;

	@PostMapping("/register")
	public ResponseEntity<UserResponse> register(@RequestBody UserRequest request) {

		return ResponseEntity.ok().body(userService.register(request));
	}

	@PostMapping("/login")
	public UserResponse login(@RequestBody UserRequest request) {
		return userService.userLogin(request);
	}

	@PostMapping("/fundTransfer")
	public ResponseEntity<UserResponse> fundTransfer(HttpServletRequest req,
			@RequestBody TransactionDetailsRequest detailsRequest) {
		String value = req.getHeader("Authorization");

		boolean check = false;
		try {
			check = jwtUtils.verify(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (check) {
			response = userService.getTransfer(detailsRequest);
			return ResponseEntity.ok().body(response);
		} else {
			response = new UserResponse();
			response.setData("Unauthorized");
			response.setResponseCode(401);
			response.setResponseDescription("Please verify the token");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}

	}

	@PostMapping("/addMoney")
	public ResponseEntity<UserResponse> addMoney(@RequestBody AccountDetailsRequest accountDetailsRequest) {
		UserResponse response = userService.addFund(accountDetailsRequest);
		return ResponseEntity.ok().body(response);
	}

	// API for checking the balance
	@PostMapping("/checkingBalance")
	public ResponseEntity<UserResponse> checkBalance(HttpServletRequest req, @RequestBody CheckingBalance balance) {
		String value = req.getHeader("Authorization");

		boolean check = false;
		try {
			check = jwtUtils.verify(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (check) {
			response = userService.getAccBalance(balance);
			return ResponseEntity.ok().body(response);
		} else {
			response = new UserResponse();
			response.setData("Unauthorized");
			response.setResponseCode(401);
			response.setResponseDescription("Please verify the token");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
	}
	
	 @PostMapping("/transaction")
	    public ResponseEntity<UserResponse> getTransactionHistory(HttpServletRequest req ,@RequestBody TransactionHistory history) {
		 String value = req.getHeader("Authorization");

			boolean check = false;
			try {
				check = jwtUtils.verify(value);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (check) {
				response = userService.getTransactionHistory(history.getAccountNo());
				return ResponseEntity.ok().body(response);
			} else {
				response = new UserResponse();
				response.setData("Unauthorized");
				response.setResponseCode(401);
				response.setResponseDescription("Please verify the token");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
			}
	       
	    }
	   
}
