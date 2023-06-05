package com.assignment.utils;

import java.util.Base64;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.assignment.model.UserDetails;
import com.assignment.repository.UserDetailsRepository;
import com.assignment.request.dto.UserRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
	@Autowired
	UserDetailsRepository detailsRepository;

	private static long expiryDuration = 60 * 60;
	static byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
	public static final String secret = Base64.getEncoder().encodeToString(keyBytes);

	public String generateJwt(UserDetails details) {
		long millitime = System.currentTimeMillis();
		long expiryTime = millitime + expiryDuration * 1000;
		Date issuedAt = new Date(millitime);
		Date expireAt = new Date(expiryTime);

		

		Claims claims = Jwts.claims().setIssuer(details.getPrimaryKeyUserId().getUserId()).setIssuedAt(issuedAt)
				.setExpiration(expireAt);
		// optional claims
		claims.put("user_id", details.getPrimaryKeyUserId().getUserId());
		claims.put("password", details.getPassword());
		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public int genrateRandomNumber() {

		Random random = new Random();
		int min = 100000000;
		int max = 999999999;
		
		int referenceNumber = random.nextInt(max - min + 1)+min;
		return referenceNumber;
		
	}

	public boolean verify(String authorization) throws Exception {
		try {

			Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authorization).getBody();
			// String userId = claims.getId();
			claims.getId();
			claims.get("user");
			
			Integer userCount = detailsRepository.findByprimaryKeyUserIdUserId(claims.get("user_id").toString());
			// If the user exists, return true
			if (userCount > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// Exception occurred, return false or handle it accordingly
			throw new Exception(e);
		}
	}
}
