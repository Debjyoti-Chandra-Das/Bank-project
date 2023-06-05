package com.assignment.service;

import com.assignment.request.dto.UserRequest;
import com.assignment.response.dto.UserResponse;
public interface UserService {

	UserResponse register(UserRequest request);

	UserResponse userLogin(UserRequest request);

	public UserResponse getAccount(int accountNo);

	public UserResponse getTransactionHistory(int accountNo);
}
