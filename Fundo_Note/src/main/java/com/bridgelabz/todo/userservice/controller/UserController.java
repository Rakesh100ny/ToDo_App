package com.bridgelabz.todo.userservice.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.todo.userservice.exception.EmailIdAlreadyExistException;
import com.bridgelabz.todo.userservice.exception.UserNotFoundException;
import com.bridgelabz.todo.userservice.model.ForgotModel;
import com.bridgelabz.todo.userservice.model.LoginModel;
import com.bridgelabz.todo.userservice.model.PasswordModel;
import com.bridgelabz.todo.userservice.model.RegisterModel;
import com.bridgelabz.todo.userservice.model.User;
import com.bridgelabz.todo.userservice.service.IUserService;
import com.bridgelabz.todo.utility.Response;
import com.bridgelabz.todo.utility.Token;
import com.bridgelabz.todo.validation.UserValidation;


@PropertySource("classpath:clientside.properties")
@RestController
public class UserController {
	@Autowired
	IUserService userService;

	@Autowired
	private UserValidation userValidation;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(userValidation);
	}

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

		if (!userService.isUserExist(loginModel.getEmail())) {
			throw new UserNotFoundException("User Not Found Exception...!");
		}

		if (!userService.isCheckCredentials(loginModel.getPassword(), loginModel.getEmail())) {
			System.out.println("A User with Email-Id " + loginModel.getEmail() + " and Password "
					+ loginModel.getPassword() + " is Invalid");
			return new ResponseEntity<>(new Response(false, "Invalid User And Password...!"), HttpStatus.UNAUTHORIZED);
		}

		if (!userService.isEmailActivated(loginModel.getEmail())) {
			System.out.println("A User with Email-Id " + loginModel.getEmail()
					+ " is not Actived please First Active Your Account");
			return new ResponseEntity<>(new Response(false," A User with Email-Id " + loginModel.getEmail()
					+ " is not Actived please First Active Your Account"),HttpStatus.UNAUTHORIZED);
		}

		User user = userService.getUserDetailsByEmail(loginModel.getEmail());

		String token = Token.generateToken(user.getId());

		return new ResponseEntity<>(new Response(true, token), HttpStatus.OK);
	}

	/*----------------------------------Verify Token--------------------------------------*/
	@RequestMapping(value = "/verifytoken/{token:.+}", method = RequestMethod.GET)
	public ResponseEntity<?> token(@PathVariable("token") String token, HttpServletResponse response,
			HttpServletRequest request) throws IOException {

		System.out.println("r1");
		userService.isVerifiedUser(token);

		System.out.println("r7");
/*		StringBuffer URL = request.getRequestURL();

		System.out.println("URL : "+URL);
		String url1 = URL.substring(0, URL.lastIndexOf("/"));

		System.out.println("url1 : "+url1);
		
		String url2 = url1.substring(0, url1.lastIndexOf("/"));

		System.out.println("url2 : "+url2);
		response.sendRedirect(url2 + "/#!/login");
		*/
		
		
		response.sendRedirect(login);
		
		return new ResponseEntity<>(new Response(true, "User is Successfully Activated...!"), HttpStatus.OK);

	}

	/*---------------------------------forgot Password------------------------------------*/
	@RequestMapping(value = "/forgotpassword", method = RequestMethod.POST)
	public ResponseEntity<?> forgotPassword(@Validated @RequestBody ForgotModel forgotModel, BindingResult result,
			HttpServletRequest request) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(new Response(false, "Check All Fileds"),HttpStatus.BAD_REQUEST);
		}

		String token = userService.forgotPassword(forgotModel.getEmail(), request);

		return new ResponseEntity<>(new Response(true, token), HttpStatus.OK);

	}

	/*---------------------------Send Reset API--------------------------*/
	@RequestMapping(value = "/resetpassword/{token:.+}", method = RequestMethod.GET)
	public ResponseEntity<?> sendResetPassword(@PathVariable("token") String token, HttpServletResponse response) throws IOException {

		response.sendRedirect(resetPassword);

		return new ResponseEntity<>(new Response(true, "Send Reset-Password API"), HttpStatus.OK);

	}

	/*---------------------------------reset Password------------------------------------*/
	@RequestMapping(value = "/resetpassword", method = RequestMethod.POST)
	public ResponseEntity<?> restPassword(@Validated @RequestBody PasswordModel passwordModel, BindingResult result,
			HttpServletRequest request) {

		String token = request.getHeader("tokenForgotPassword");

		System.out.println("Reset Token : "+token);
		
		if (result.hasErrors()) {
			return new ResponseEntity<>(new Response(false, "Check All Fileds"),HttpStatus.BAD_REQUEST);
		}

		if (!passwordModel.getNewPassword().equals(passwordModel.getConfirmPassword())) {
			return new ResponseEntity<>(new Response(false, "Both Password must be matched"), HttpStatus.UNAUTHORIZED);
		}

		userService.restPassword(token, passwordModel.getNewPassword());

		return new ResponseEntity<>(new Response(true, "Password is Successfully Updated...!"), HttpStatus.OK);

	}

}
