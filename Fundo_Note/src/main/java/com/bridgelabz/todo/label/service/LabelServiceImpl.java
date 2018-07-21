package com.bridgelabz.todo.label.service;

import java.security.SignatureException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.todo.label.dao.ILabelDao;
import com.bridgelabz.todo.label.exception.LabelNotFoundException;
import com.bridgelabz.todo.label.exception.UnauthorizedLabelException;
import com.bridgelabz.todo.label.model.Label;
import com.bridgelabz.todo.userservice.dao.IUserDao;
import com.bridgelabz.todo.userservice.model.User;
import com.bridgelabz.todo.utility.Token;

@Transactional
@Service
public class LabelServiceImpl implements ILabelService
{
	@Autowired
	private ILabelDao labelDao;
	
	@Autowired
	private IUserDao userDao;

	@Override
	public void addLabel(Label label, String token) {
		try {
			label.setUser(userDao.getUserById(Long.parseLong(Token.getParseJWT(token))));
			labelDao.addLabel(label);

		} catch (NumberFormatException | SignatureException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public List<Label> getAllLabels(String token) {
		List <Label> labels = null;
        User user;
		try {
			user = userDao.getUserById(Long.parseLong(Token.getParseJWT(token)));
			System.out.println("User id : "+user.getId());
			
			if(user != null)
	        {
				System.out.println("hello");
	        	labels = labelDao.getAllLabels(user.getId());
	        	return labels;
	        }
		} catch (NumberFormatException | SignatureException e) {
			
			e.printStackTrace();
		}
             
		return labels;
	}

	@Override
	public boolean deleteLabel(long id, String token) 
	{
     Label label=labelDao.getLabelById(id);		
		
	 try 
	 {
		User user=userDao.getUserById(Long.parseLong(Token.getParseJWT(token)));
		
		if(user.getId() == label.getUser().getId())
		{
         
         if (!labelDao.deleteLabelById(label.getId())) {
				System.out.println("Unable to delete. User with id " + id + " not found");
				throw new LabelNotFoundException("Label is not found...!");
			 }
	    }
		else
		{
			throw new UnauthorizedLabelException("This User is Not Allow to Delete Note...!");
		}
        	
	 } catch (NumberFormatException | SignatureException e) {
		e.printStackTrace();
	  }
	 
		return true;
	}

	

}
