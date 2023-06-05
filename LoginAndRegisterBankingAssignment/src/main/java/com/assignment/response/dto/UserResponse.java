package com.assignment.response.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
	private Integer ResponseCode;
	private Object data;
	private Object ResponseDescription;
}
