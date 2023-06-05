package com.assignment.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class PrimaryKeyUserId implements Serializable {

	private static final long serialVersionUID = 1L;
	@Column(name = "user_id", nullable = false, unique = true,length = 5)
	private String userId;
	@Column(name = "account_no", nullable = false, unique = true,length = 10)
	private int accountNumber;

}
