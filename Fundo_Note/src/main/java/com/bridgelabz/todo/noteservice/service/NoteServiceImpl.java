package com.bridgelabz.todo.noteservice.service;

import java.security.SignatureException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.todo.label.dao.ILabelDao;
import com.bridgelabz.todo.label.exception.LabelAlreadyExistException;
import com.bridgelabz.todo.label.model.Label;
import com.bridgelabz.todo.noteservice.dao.INoteDao;
import com.bridgelabz.todo.noteservice.exception.NoteNotFoundException;
import com.bridgelabz.todo.noteservice.exception.UnauthorizedException;
import com.bridgelabz.todo.noteservice.model.Note;
import com.bridgelabz.todo.userservice.dao.IUserDao;
import com.bridgelabz.todo.userservice.model.User;
import com.bridgelabz.todo.utility.Token;

import net.bytebuddy.implementation.bytecode.Throw;

@Service
public class NoteServiceImpl implements INoteService {

	@Autowired
	INoteDao noteDao;

	@Autowired
	IUserDao userDao;
	
	@Autowired
	ILabelDao labelDao;

	@Transactional
	@Override
	public void addNote(Note note, String token) {
		try {
			User user = userDao.getUserById(Integer.parseInt(Token.getParseJWT(token)));
			/*note.setCreatedDate(new Date(System.currentTimeMillis()));
			note.setLastUpdatedDate(new Date(System.currentTimeMillis()));*/	
		     note.setUser(user);
			noteDao.addNote(note, user);
			System.out.println("Note is successfully created...!");
		} catch (NumberFormatException | SignatureException e) {
			e.printStackTrace();
		}

	}

	@Transactional
	@Override
	public Note getNoteById(long id) {
		return noteDao.getNoteById(id);
	}

	@Transactional
	@Override
	public void update(Note note,String token) {
	
		System.out.println("note id : "+note.getId());
        System.out.println("note id using user : "+note.getUser().getId());
		note.setLastUpdatedDate(new Date(System.currentTimeMillis()));

		//Note note2=noteDao.getNoteById(note.getId());
				
		
		try {
			
			long id=Long.parseLong(Token.getParseJWT(token));
					
			if(id==note.getUser().getId() && note.getUser().getId()!=0)
			{
			 noteDao.update(note);	
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
	public boolean deleteNoteById(long id,String token) {

		Note note=noteDao.getNoteById(id);
		
		User user;
		try {
			user = userDao.getUserById(Long.parseLong(Token.getParseJWT(token)));
			if(user.getId()==note.getUser().getId())
			{
			 if (!noteDao.deleteNoteById(id)) {
				System.out.println("Unable to delete. User with id " + id + " not found");
				throw new NoteNotFoundException("Note is not found...!");
			 }
		    }
			else
			{
			 throw new UnauthorizedException("This User is Not Allow to Delete Note...!");	
			}
		} catch (NumberFormatException | SignatureException e) {
			e.printStackTrace();
		}
	
		return true;
	}

	@Transactional
	@Override
	public List<Note> getAllNotes(String token) {
		List<Note> note = null;
		try {

			System.out.println("ranu2");
			note = noteDao.getAllNotes(Long.parseLong(Token.getParseJWT(token)));

			System.out.println("ranu5");
			
		} catch (NumberFormatException | SignatureException e) {
			e.printStackTrace();
		}
		return note;
	}

	@Transactional
	@Override
	public void addLabelOnNote(long noteId, long labelId) 
	{
     Note note=noteDao.getNoteById(noteId);
	 
     Label label=labelDao.getLabelById(labelId);

     if(note.getListOfLabels().contains(label))
     {
    	 System.out.println("A Label with Name " + label.getLabelName() + " Already Exist");
		 throw new LabelAlreadyExistException("Label already exists"); 	 
     }
     else
     {
    	 note.getListOfLabels().add(label);
     	 label.getListOfNotes().add(note);
    	 noteDao.update(note);
    	 labelDao.update(label);
    		 
     }
 	}

	@Transactional
	@Override
	public void removeLabelOnNote(long noteId, long labelId) {
		 Note note=noteDao.getNoteById(noteId);
		 System.out.println("Note title  : "+note.getTitle());
		 Label label=labelDao.getLabelById(labelId);
		 System.out.println("Label title : "+label.getLabelName());
		 
		 note.getListOfLabels().remove(label);
		 label.getListOfNotes().remove(note);

		 noteDao.update(note);
	     labelDao.update(label);
	}

}
