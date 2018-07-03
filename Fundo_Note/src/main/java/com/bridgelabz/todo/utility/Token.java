package com.bridgelabz.todo.utility;

import java.security.SignatureException;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Token {
	
	
		private static String KEY="@Ron1900100ny#";
		
		public static String generateToken(long id)
		{
			long currentTime=System.currentTimeMillis();
			Date currentDate=new Date(currentTime);
			Date expireDate=new Date(currentTime+ 24*60*60*1000);
			
			String getToken=Jwts.builder()
					.setId(Long.toString(id))
					.setIssuedAt(currentDate)
					.setExpiration(expireDate)
					.signWith(SignatureAlgorithm.HS256,KEY)
					.compact();
			
			return getToken;
		}
		
		public static String getParseJWT(String token) throws SignatureException
		 {
			 Claims claim =  Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
			 	 
			 return claim.getId();
		 }

}