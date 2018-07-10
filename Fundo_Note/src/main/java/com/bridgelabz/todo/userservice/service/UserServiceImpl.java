package com.bridgelabz.todo.userservice.service;

import java.security.SignatureException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.todo.userservice.dao.IUserDao;
import com.bridgelabz.todo.userservice.exception.TokenExpireException;
import com.bridgelabz.todo.userservice.exception.UserNotFoundException;
import com.bridgelabz.todo.userservice.jms.MessageSender;
import com.bridgelabz.todo.userservice.model.EmailModel;
import com.bridgelabz.todo.userservice.model.RegisterModel;
import com.bridgelabz.todo.userservice.model.User;
import com.bridgelabz.todo.utility.RedisUtility;
import com.bridgelabz.todo.utility.Token;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserDao userDao;

	@Autowired
	private User user;

	@Autowired
	private EmailModel emailModel;

	@Autowired
	private MessageSender messageSender;

	@Autowired
	private RedisUtility redisUtility;

	@Transactional
	@Override
	public void insert(RegisterModel registerModel, HttpServletRequest request) {

		user.setFirstName(registerModel.getFirstName());
		user.setLastName(registerModel.getLastName());
		user.setEmail(registerModel.getEmail());
		user.setPassword(registerModel.getPassword());
		
		user.setMobileNo(registerModel.getMobileNo().substring(3));

		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));

		userDao.insert(user);

		String token = Token.generateToken(user.getId());

		StringBuffer URL = request.getRequestURL();

		String url = "<a href=" + URL.substring(0, URL.lastIndexOf("/")) + "/verifytoken/" + token + " ></a>";

		String subject = "link to activate your account";

		emailModel.setSubject(subject);
		emailModel.setTo(user.getEmail());
		emailModel.setUrl(url);

		messageSender.sendMessage(emailModel);

		redisUtility.saveToken(Long.toString(user.getId()), token);

	}

	@Transactional
	@Override
	public boolean isUserExist(String email) {
		long count = userDao.isUserExist(email);

		if (count >= 1) {
			return true;
		} else {
			return false;
		}

	}

	@Transactional
	@Override
	public boolean isCheckCredentials(String password, String email) {
		User user = userDao.getUserDetailsByEmail(email);

		if (user != null) {
			if (BCrypt.checkpw(password, user.getPassword())) {
				return true;
			} else {
				return false;
			}
		}
		return false;

	}

	@Transactional
	@Override
	public User getUserDetailsByEmail(String email) {
		return userDao.getUserDetailsByEmail(email);
	}

	@Transactional
	@Override
	public User getUserById(long id) {
		return userDao.getUserById(id);
	}

	@Transactional
	@Override
	public void updateUser(User user) {
		userDao.updateUser(user);
	}

	@Transactional
	@Override
	public EmailModel getEmailModel(String token, HttpServletRequest request, User user) {
		StringBuffer URL = request.getRequestURL();

		String url = "<a href=" + URL.substring(0, URL.lastIndexOf("/")) + "/resetpassword/" + token + " ></a>";

		String subject = "link to rest your password";

		emailModel.setTo(user.getEmail());
		emailModel.setSubject(subject);
		emailModel.setUrl(url);
		return emailModel;
	}

	@Transactional
	@Override
	public void isVerifiedUser(String token) {

		try {
			String storedToken = redisUtility.getSaveToken(Token.getParseJWT(token));

			redisUtility.expireSaveToken(Token.getParseJWT(token));
			
			if(storedToken==null)
				 throw new TokenExpireException("Token is Expired Please Again do Registration");
				
			
			if (storedToken.equals(token)) {

				User user = getUserById(Long.parseLong(Token.getParseJWT(token)));

				if (user != null) {
					user.setActivated(true);
					updateUser(user);

				} else {
					throw new UserNotFoundException("User Not Found...!");
				}
			}
		} catch (SignatureException e) {
			e.printStackTrace();
		}

	}

	@Transactional
	@Override
	public String forgotPassword(String email, HttpServletRequest request) {

		User user = getUserDetailsByEmail(email);
		if (user != null) {
			String token = Token.generateToken(user.getId());

			redisUtility.saveToken(Long.toString(user.getId()), token);

			EmailModel emailModel = getEmailModel(token, request, user);

			messageSender.sendMessage(emailModel);

			return token;
		} else {

			throw new UserNotFoundException("User Not Found...!");
		}
	}

	@Transactional
	@Override
	public void restPassword(String token, String newPassword) {
		try {
			String storedToken = redisUtility.getSaveToken(Token.getParseJWT(token));
			redisUtility.expireSaveToken(Token.getParseJWT(token));
			
			if(storedToken==null)
			 throw new TokenExpireException("Token is Expired Please Again do ForgotPassword");
			
			if (storedToken.equals(token)) {
				User user = getUserById(Long.parseLong(Token.getParseJWT(token)));

				if (user != null) {
					user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));

					updateUser(user);

				} else {
					throw new UserNotFoundException("User Not Found...!");
				}
			}
		} catch (SignatureException e) {
			e.printStackTrace();
		}

	}

	@Transactional
	@Override
	public boolean isEmailActivated(String email) {
		User user = userDao.getUserDetailsByEmail(email);

		if (user.isActivated() == true) {
			return true;
		} else {
			return false;
		}

	}
}
