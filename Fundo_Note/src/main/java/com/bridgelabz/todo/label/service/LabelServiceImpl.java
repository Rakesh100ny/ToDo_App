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
import com.bridgelabz.todo.noteservice.dao.INoteDao;
import com.bridgelabz.todo.noteservice.exception.UnauthorizedException;
import com.bridgelabz.todo.noteservice.model.Note;
import com.bridgelabz.todo.userservice.dao.IUserDao;
import com.bridgelabz.todo.userservice.model.User;
import com.bridgelabz.todo.utility.Token;

@Service
public class LabelServiceImpl implements ILabelService
{
	@Autowired
	private ILabelDao labelDao;
	
	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private INoteDao noteDao;

	@Transactional
	@Override
	public void addLabel(Label label, String token) {
		try {
			label.setUserDetails(userDao.getUserById(Long.parseLong(Token.getParseJWT(token))));
			labelDao.addLabel(label);

		} catch (NumberFormatException | SignatureException e) {
			e.printStackTrace();
		}
		
	}

	@Transactional
	@Override
	public List<Label> getAllLabels(String token) {
		List <Label> labels = null;
        
		try {
			
			long id=Long.parseLong(Token.getParseJWT(token));
			
		    	labels = labelDao.getAllLabels(id);
	        	return labels;
	       
		} catch (NumberFormatException | SignatureException e) {
			
			e.printStackTrace();
		}
             
		return labels;
	}

	@Transactional
	@Override
	public boolean deleteLabel(long id, String token) 
	{
		
	 try 
	 {
	     Label label=labelDao.getLabelById(id);		

		User user=userDao.getUserById(Long.parseLong(Token.getParseJWT(token)));
				
		if(user.getId() == label.getUserDetails().getId())
		{
		 List<Note> notes=user.getListOfNotes();
              
         for(Note note : notes)
         {
          List<Label> labels=note.getListOfLabels();
                  
          for(Label label1 : labels)
          {
          	if(label1.getId()==label.getId())
        	{
             note.getListOfLabels().remove(label1);
             noteDao.update(note);
            }
          }
         }
        
         if (!labelDao.deleteLabelById(id)) {
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

	@Transactional
	@Override
	public boolean isLabelExist(Label label)
	{
		long count = labelDao.isLabelExist(label);

		System.out.println("count : "+count);
		if (count >= 1) {
			return true;
		} else {
			return false;
		}

	}

	@Transactional
	@Override
	public void update(Label label, String token) 
	{		
	 try {
	  	long id=Long.parseLong(Token.getParseJWT(token));
					
			if(id==label.getUserDetails().getId() && label.getUserDetails().getId()!=0)
			{
			 labelDao.update(label);	
			}
			else
			{
			 throw new UnauthorizedException("This User is Not Allow to Update Note...!"); 	
			}
		} catch (NumberFormatException | SignatureException e) {
			e.printStackTrace();
		}	
	}

	@Transactional
	@Override
	public List<Note> getAllLabelNote(long id, String token)
	{
		List<Note> labelNotes = null;
		

		Label label=labelDao.getLabelById(id);
		
		labelNotes=label.getListOfNotes();
		
    	return labelNotes;
	}

	

}
