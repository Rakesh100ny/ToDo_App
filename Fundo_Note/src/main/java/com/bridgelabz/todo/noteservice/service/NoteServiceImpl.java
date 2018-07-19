package com.bridgelabz.todo.noteservice.service;

import java.security.SignatureException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.todo.noteservice.dao.INoteDao;
import com.bridgelabz.todo.noteservice.exception.NoteNotFoundException;
import com.bridgelabz.todo.noteservice.exception.UnauthorizedException;
import com.bridgelabz.todo.noteservice.model.Note;
import com.bridgelabz.todo.userservice.dao.IUserDao;
import com.bridgelabz.todo.userservice.model.User;
import com.bridgelabz.todo.utility.Token;

@Service
public class NoteServiceImpl implements INoteService {

	@Autowired
	INoteDao noteDao;

	@Autowired
	IUserDao userDao;

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
		System.out.println("rakesh");
		return noteDao.getNoteById(id);
	}

	@Transactional
	@Override
	public void update(Note note,String token) {
	
		System.out.println("note id : "+note.getId());

		note.setLastUpdatedDate(new Date(System.currentTimeMillis()));

		//Note note2=noteDao.getNoteById(note.getId());
				
		System.out.println("note2 : "+note.getId());

		try {
			
			long id=Long.parseLong(Token.getParseJWT(token));
			
			System.out.println("User id : "+id);
			
			if(id==note.getUser().getId())
			{
			 System.out.println("sonu");	
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

			note = noteDao.getAllNotes(Long.parseLong(Token.getParseJWT(token)));

		} catch (NumberFormatException | SignatureException e) {
			e.printStackTrace();
		}
		return note;
	}

}
