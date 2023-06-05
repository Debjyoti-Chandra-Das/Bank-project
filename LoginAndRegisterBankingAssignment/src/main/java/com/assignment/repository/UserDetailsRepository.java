package com.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.assignment.model.PrimaryKeyUserId;
import com.assignment.model.UserDetails;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, PrimaryKeyUserId> {

	//public UserDetails findByprimaryKeyUserIdUserId(String userId);

	public UserDetails findByprimaryKeyUserIdUserIdAndPassword(String userId, String password);
	
	public UserDetails findByprimaryKeyUserIdAccountNumber(int accountNumber);
	
	@Query(value = "select count(*) from user_details where user_id=:userId", nativeQuery = true)
	public Integer findByprimaryKeyUserIdUserId(@Param("userId") String userId);

}
