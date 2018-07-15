package com.bridgelabz.todo.utility;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisUtility {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	

	public void saveToken(String id, String token) 
	{
		stringRedisTemplate.opsForValue().set(id, token);
		
	}

	public String getSaveToken(String id){
		return stringRedisTemplate.opsForValue().get(id);
	}

	public void expireSaveToken(String id) {
		stringRedisTemplate.expire(id, 24, TimeUnit.HOURS);
	}
}
