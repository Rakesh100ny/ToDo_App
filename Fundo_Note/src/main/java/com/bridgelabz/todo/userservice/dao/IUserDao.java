package com.bridgelabz.todo.userservice.dao;

import java.util.List;

import com.bridgelabz.todo.userservice.model.User;



public interface IUserDao 
{
	 
	 void insert(User user);

	 long isUserExist(String email);

	 //User isCheckPassword(String email);
    
	 User getUserDetailsByEmail(String email);
 
	 User getUserById(long id);
 
     void updateUser(User user);

	List<User> getAllUsers();
}