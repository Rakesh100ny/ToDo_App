package com.bridgelabz.todo.userservice.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.todo.userservice.exception.EmailIdAlreadyExistException;
import com.bridgelabz.todo.userservice.model.ForgotModel;
import com.bridgelabz.todo.userservice.model.LoginModel;
import com.bridgelabz.todo.userservice.model.PasswordModel;
import com.bridgelabz.todo.userservice.model.RegisterModel;
import com.bridgelabz.todo.userservice.model.User;
import com.bridgelabz.todo.userservice.service.IUserService;
import com.bridgelabz.todo.utility.Response;

@PropertySource("classpath:clientside.properties")
@RestController
public class UserController {
	@Autowired
	IUserService userService;

	/*	@Autowired
	private UserValidation userValidation;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(userValidation);
	}*/

	@Value("${redirect.login}")
	String login;

	@Value("${redirect.resetpassword}")
	String resetPassword;

	/*-------------------------------Register a User-----------------------------------------------*/

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> registerUser(@Validated @RequestBody RegisterModel registerModel, BindingResult result,
			HttpServletRequest request) {
		System.out.println("Creating User with unique Email-Id " + registerModel.getEmail());

		if (result.hasErrors()) {

			return new ResponseEntity<>(new Response(false, "Check All Fileds"), HttpStatus.BAD_REQUEST);
		}

		if (userService.isUserExist(registerModel.getEmail())) {

			System.out.println("A User with Email-Id " + registerModel.getEmail() + " Already Exist");
			throw new EmailIdAlreadyExistException("A User with Email-Id  is Already Exist");
		}

		userService.insert(registerModel, request);

		return new ResponseEntity<>(new Response(true, "User is Successfully Registered...!"), HttpStatus.CREATED);

	}

	/*-------------------------------Login a User-----------------------------------------------*/

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> loginUser(@Validated @RequestBody LoginModel loginModel, BindingResult result) {
		System.out.println("Creating User with unique Email-Id " + loginModel.getEmail());

		if (result.hasErrors()) {
			return new ResponseEntity<>(new Response(false, "Check All Fileds"), HttpStatus.BAD_REQUEST);
		}

		
		String token=userService.verifyLogin(loginModel.getEmail(),loginModel.getPassword());
		
		if(token!="")
		{
			return new ResponseEntity<>(new Response(true, token), HttpStatus.OK);	
		}
			
		return new ResponseEntity<>(new Response(false,"You Can't successfully login...please check something was wrong"), HttpStatus.CONFLICT);


		
	}

	/*----------------------------------Verify Token--------------------------------------*/
	@RequestMapping(value = "/verifytoken/{token:.+}", method = RequestMethod.GET)
	public ResponseEntity<?> token(@PathVariable("token") String token, HttpServletResponse response,
			HttpServletRequest request) throws IOException {

		userService.isVerifiedUser(token);
	
		response.sendRedirect(login);

		return new ResponseEntity<>(new Response(true, "User is Successfully Activated...!"), HttpStatus.OK);

	}

	/*---------------------------------forgot Password------------------------------------*/
	@RequestMapping(value = "/forgotpassword", method = RequestMethod.POST)
	public ResponseEntity<?> forgotPassword(@Validated @RequestBody ForgotModel forgotModel, BindingResult result,
			HttpServletRequest request) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(new Response(false, "Check All Fileds"), HttpStatus.BAD_REQUEST);
		}

		String token = userService.forgotPassword(forgotModel.getEmail(), request);

		return new ResponseEntity<>(new Response(true, token), HttpStatus.OK);

	}

	/*---------------------------Send Reset API--------------------------*/
	@RequestMapping(value = "/resetpassword/{token:.+}", method = RequestMethod.GET)
	public ResponseEntity<?> sendResetPassword(@PathVariable("token") String token, HttpServletResponse response)
			throws IOException {

		response.sendRedirect(resetPassword);

		return new ResponseEntity<>(new Response(true, "Send Reset-Password API"), HttpStatus.OK);

	}

	/*---------------------------Get Login User--------------------------*/
	@RequestMapping(value = "/getCurrentUser", method = RequestMethod.GET)
	public ResponseEntity<User> getCurrentUser(@RequestHeader("userLoginToken") String token)
	{
       System.out.println("/user token : "+token);
       
       User user=userService.getCurrentUser(token);
       
       System.out.println("User Image : "+user.getProfilePic());
       
		
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	// ------------------- Update User  ------------------------
	@RequestMapping(value = "/updateUser", method = RequestMethod.PUT)
	public ResponseEntity<?> updateNote(@RequestBody User user, @RequestHeader("userLoginToken") String token) {
		System.out.println("Updating User id : " + user.getId());
		System.out.println("rakesh image upload");
		userService.update(user,token);


		return new ResponseEntity<>(new Response(true, "User is successfully updated...!"), HttpStatus.OK);

	}
	/*---------------------------------reset Password------------------------------------*/
	@RequestMapping(value = "/resetpassword", method = RequestMethod.POST)
	public ResponseEntity<?> restPassword(@Validated @RequestBody PasswordModel passwordModel, BindingResult result,
			HttpServletRequest request) {


		if (result.hasErrors()) {
			return new ResponseEntity<>(new Response(false, "Check All Fileds"), HttpStatus.BAD_REQUEST);
		}

		String token = request.getHeader("tokenForgotPassword");

		System.out.println("Reset Token : " + token);


		userService.restPassword(token, passwordModel.getNewPassword());

		return new ResponseEntity<>(new Response(true, "Password is Successfully Updated...!"), HttpStatus.OK);

	}
	
	//<====================================== Get All Users =======================================>	
	
		@RequestMapping(value="/getallUsers" ,method = RequestMethod.GET)
		  public ResponseEntity<List<User>> getAllUsers(@RequestHeader("userLoginToken") String token)
		  {
			  List<User> list=userService.getAllUsers();  
			 return new ResponseEntity<List<User>>( list,HttpStatus.CREATED); 
			  
		  }

}
