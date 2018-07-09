package com.bridgelabz.todo.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.bridgelabz.todo.userservice.model.ForgotModel;
import com.bridgelabz.todo.userservice.model.LoginModel;
import com.bridgelabz.todo.userservice.model.PasswordModel;
import com.bridgelabz.todo.userservice.model.RegisterModel;

@Component
public class UserValidation implements Validator {

	@Autowired
	@Qualifier("emailValidator")
	EmailValidator emailValidator;

	public boolean supports(Class<?> clazz) {
		if (RegisterModel.class.equals(clazz) || LoginModel.class.equals(clazz)|| ForgotModel.class.equals(clazz) || PasswordModel.class.equals(clazz)) {
			return true;
		}

		return false;
	}

	public void validate(Object target, Errors errors) {

		System.out.println("Target : " + target);

		if (target instanceof RegisterModel) {
			RegisterModel user = (RegisterModel) target;

			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty.firstName");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty.lastName");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.email");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.password");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mobileNo", "NotEmpty.mobileNo");

			if (!emailValidator.valid(user.getEmail())) {
				errors.rejectValue("email", "Pattern.email");
			}
		} else if (target instanceof LoginModel) {
			LoginModel user = (LoginModel) target;
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.email");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.password");

			if (!emailValidator.valid(user.getEmail())) {
				errors.rejectValue("email", "Pattern.email");
			}
		}
		else if(target instanceof ForgotModel)
		{
		 ForgotModel user=(ForgotModel) target;	
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.email");
			if (!emailValidator.valid(user.getEmail())) {
				errors.rejectValue("email", "Pattern.email");
			}

		}
		else if(target instanceof PasswordModel)
		{
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "NotEmpty.newPassword");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "NotEmpty.confirmPassword");


		}

	}

}
