package com.bridgelabz.todo.userservice.service;

import javax.servlet.http.HttpServletRequest;

import com.bridgelabz.todo.userservice.model.EmailModel;
import com.bridgelabz.todo.userservice.model.RegisterModel;
import com.bridgelabz.todo.userservice.model.User;

public interface IUserService {

	void insert(RegisterModel registerModel,HttpServletRequest request);

	boolean isUserExist(String email);

	boolean isCheckCredentials(String password, String email);

	User getUserDetailsByEmail(String email);
	
	 User getUserById(long id);
	 
	 void updateUser(User user);

	EmailModel getEmailModel(String token,HttpServletRequest request,User user);

	void isVerified(String token) ;

	void forgotPassword(String email,HttpServletRequest request);

	void restPassword(String token,String password);

	String isActivated(String email);

}