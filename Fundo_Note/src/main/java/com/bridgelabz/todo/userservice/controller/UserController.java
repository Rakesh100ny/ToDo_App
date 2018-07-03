package com.bridgelabz.todo.userservice.controller;

 import java.security.SignatureException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.bridgelabz.todo.userservice.exception.UserNotFoundException;
import com.bridgelabz.todo.userservice.model.ForgotModel;
import com.bridgelabz.todo.userservice.model.LoginModel;
import com.bridgelabz.todo.userservice.model.PasswordModel;
import com.bridgelabz.todo.userservice.model.RegisterModel;
import com.bridgelabz.todo.userservice.model.User;
import com.bridgelabz.todo.userservice.service.IUserService;
import com.bridgelabz.todo.validation.UserValidation;

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

	/*-------------------------------Register a User-----------------------------------------------*/

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<Void> registerUser(@Validated @RequestBody RegisterModel registerModel, BindingResult result,
			HttpServletRequest request) {
		System.out.println("Creating User with unique Email-Id " + registerModel.getEmail());
	
		if (userService.isUserExist(registerModel.getEmail())) {
			
			System.out.println("A User with Email-Id " + registerModel.getEmail() + " Already Exist");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		
		if(result.hasErrors())
		{
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);	
		}
		
		userService.insert(registerModel,request);

		return new ResponseEntity<Void>(HttpStatus.CREATED);

	}

	/*-------------------------------Login a User-----------------------------------------------*/

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> loginUser(@Validated @RequestBody LoginModel loginModel, BindingResult result,HttpServletResponse response) throws UserNotFoundException {
		System.out.println("Creating User with unique Email-Id " + loginModel.getEmail());

		if(result.hasErrors())
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);	
		}


		if(!userService.isUserExist(loginModel.getEmail()))
		{
		 throw new UserNotFoundException("User Not Found Exception...!");	
		}
		
		System.out.println("mango");
		if (!userService.isCheckCredentials(loginModel.getPassword(), loginModel.getEmail())) {
			System.out.println("A User with Email-Id " + loginModel.getEmail() + " and Password " + loginModel.getPassword() + " is Invalid");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String token=userService.isActivated(loginModel.getEmail());
		
		if(token==null)
		{
			System.out.println("A User with Email-Id " + loginModel.getEmail() + " is not Actived please First Active Your Account");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	
		response.setHeader("token", token);
		response.setStatus(200);
		
		User user = userService.getUserDetailsByEmail(loginModel.getEmail());

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	
	/*----------------------------------Verify Token--------------------------------------*/
	@RequestMapping(value = "/verifytoken/{token:.+}", method = RequestMethod.GET)
	public ResponseEntity<String> token(@PathVariable("token") String token) throws UserNotFoundException, NumberFormatException, SignatureException {

		System.out.println("Token : " + token);
		System.out.println("Ranu");

		userService.isVerified(token);

		return new ResponseEntity<String>("User is Successfully Activated...!", HttpStatus.OK);

	}
	/*---------------------------------forgot Password------------------------------------*/
	@RequestMapping(value="/forgotpassword",method=RequestMethod.POST)
	public ResponseEntity<String> forgotPassword(@Validated @RequestBody ForgotModel forgotModel, BindingResult result,HttpServletRequest request) throws UserNotFoundException
	{
		if(result.hasErrors())
		{
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);	
		}	
		
		System.out.println("forgot Email : "+forgotModel.getEmail());
	
		
		userService.forgotPassword(forgotModel.getEmail(),request);
		
	return new ResponseEntity<String>("Reset Password...!", HttpStatus.OK);
	
	}
	
	
	/*---------------------------------reset Password------------------------------------*/
	@RequestMapping(value = "/resetpassword/{token:.+}", method = RequestMethod.POST)
	public ResponseEntity<String> restPassword(@Validated @RequestBody PasswordModel passwordModel,BindingResult result,@PathVariable("token") String token) throws SignatureException, UserNotFoundException {

		System.out.println("Token : " + token);

		System.out.println("password "+passwordModel.getPassword());
		
		if(result.hasErrors())
		{
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);	
		}
		
		System.out.println("rakesh1");
		userService.restPassword(token,passwordModel.getPassword());

		return new ResponseEntity<String>("Password is Successfully Updated...!", HttpStatus.OK);

	}

}
