package com.bridgelabz.todo.noteservice.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SignatureException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.todo.label.dao.ILabelDao;
import com.bridgelabz.todo.label.model.Label;
import com.bridgelabz.todo.noteservice.dao.INoteDao;
import com.bridgelabz.todo.noteservice.exception.NoteNotFoundException;
import com.bridgelabz.todo.noteservice.exception.UnauthorizedException;
import com.bridgelabz.todo.noteservice.model.Note;
import com.bridgelabz.todo.userservice.dao.IUserDao;
import com.bridgelabz.todo.userservice.model.User;
import com.bridgelabz.todo.utility.Token;

@Service
@PropertySource("classpath:imageConfig.properties")
public class NoteServiceImpl implements INoteService {

	@Autowired
	INoteDao noteDao;

	@Autowired
	IUserDao userDao;

	@Autowired
	ILabelDao labelDao;

	@Value("${image.path}")
	private String path;

	@Value("${response.path}")
	private String responsePath;
	
	
	@Transactional
	@Override
	public void addNote(Note note, String token) {
		try {
			User user = userDao.getUserById(Integer.parseInt(Token.getParseJWT(token)));
			/*
			 * note.setCreatedDate(new Date(System.currentTimeMillis()));
			 * note.setLastUpdatedDate(new Date(System.currentTimeMillis()));
			 */
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
	public void update(Note note, String token) {

		System.out.println("note id : " + note.getId());
		System.out.println("note id using user : " + note.getUser().getId());
		note.setLastUpdatedDate(new Date(System.currentTimeMillis()));

		try {

			long id = Long.parseLong(Token.getParseJWT(token));

			if (id == note.getUser().getId() && note.getUser().getId() != 0) {
				noteDao.update(note);
			} else {
				throw new UnauthorizedException("This User is Not Allow to Update Note...!");
			}
		} catch (NumberFormatException | SignatureException e) {
			e.printStackTrace();
		}

	}

	@Transactional
	@Override
	public boolean deleteNoteById(long id, String token) {

		Note note = noteDao.getNoteById(id);

		User user;
		try {
			user = userDao.getUserById(Long.parseLong(Token.getParseJWT(token)));
			if (user.getId() == note.getUser().getId()) {
				if (!noteDao.deleteNoteById(id)) {
					System.out.println("Unable to delete. User with id " + id + " not found");
					throw new NoteNotFoundException("Note is not found...!");
				}
			} else {
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

	@Transactional
	@Override
	public void addLabelOnNote(Note note, Label label) {

		note.getListOfLabels().add(label);
		label.getListOfNotes().add(note);
		noteDao.update(note);
		labelDao.update(label);

	}

	@Transactional
	@Override
	public void removeLabelOnNote(Note note, Label label) {

		note.getListOfLabels().remove(label);
		label.getListOfNotes().remove(note);

		noteDao.update(note);
		labelDao.update(label);
	}

	@Transactional
	@Override
	public void relationBetweenNoteLabel(long noteId, long labelId) {
		System.out.println("r1");
		Note note = noteDao.getNoteById(noteId);

		Label label = labelDao.getLabelById(labelId);

		if (note.getListOfLabels().contains(label)) {
			System.out.println("r2");
			removeLabelOnNote(note, label);
		} else {
			System.out.println("r3");
			addLabelOnNote(note, label);
		}
	}

	@Override
	public String storeServerSideImage(MultipartFile file) {

		try {

			byte[] bytes = file.getBytes();
			System.out.println("path : " + path + File.separator + file.getOriginalFilename());

			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(path + File.separator + file.getOriginalFilename()));
			stream.write(bytes);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return responsePath+file.getOriginalFilename();
	}

	@Override
	public byte[] toGetImage(String name) 
	{
		File serverFile = new File(path+File.separator+name);
		
		if (serverFile.exists()) {
            
		 System.out.println("r1");
		try {
		 return Files.readAllBytes(serverFile.toPath());
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	//<==================================== Add Collaborator On Note ==============================>	 
	 
	 
		@Transactional
		public void addCollaboratorOnNote(int userid,int noteid) {
			
			System.out.println("Entering in to the note label service");
			Note note = noteDao.getNoteById(noteid);
			System.out.println("Note in collaborator:"+note);

			User user = userDao.getUserById(userid);
			System.out.println("user in collaborator:"+user);
			
			List<User> collaboratorUser =  note.getCollaboratedUser();
			collaboratorUser.add(user);
			note.setCollaboratedUser(collaboratorUser);
			
			List<Note> collaboratorNotes = user.getCollaboratorNotes();
			collaboratorNotes.add(note);
			user.setCollaboratorNotes(collaboratorNotes);
			
			userDao.updateUser(user);
			noteDao.update(note);

	}

	//<================================== Remove Collaborator On Note ===========================>	
		
		@Transactional
		public boolean removeCollaboratorOnNote(int userid, int noteid) {
			System.out.println("Entering in to the note label service");
			Note note = noteDao.getNoteById(noteid);
			System.out.println("Note in collaborator:"+note);

			User user = userDao.getUserById(userid);
			System.out.println("user in collaborator:"+user);
			
			List<User> collaboratorUser =  note.getCollaboratedUser();
			for(User user2:collaboratorUser) {
				if(userid == user2.getId()) {
					collaboratorUser.remove(user2);
					break;
				}
			}
		    note.setCollaboratedUser(collaboratorUser);
		
			List<Note> collaboratorNotes = user.getCollaboratorNotes();
			for(Note note2 :collaboratorNotes) {
				if(noteid == note2.getId()) {
					collaboratorNotes.remove(note2);
					break;
				}
			}
			user.setCollaboratorNotes(collaboratorNotes);
			
			userDao.updateUser(user);
			noteDao.update(note);
			return true;
		}
		
		
	//<================================== Get All Collaborators ==============================>	
		
		
		@Transactional
		public List<Note> getAllCollaboratedNotes(String token) {
			System.out.println(token);
			long id;
			List<Note> listofCollaboratedNotes = null;
			try {
				id = Long.parseLong(Token.getParseJWT(token));
		
				User user = userDao.getUserById(id);

				listofCollaboratedNotes = user.getCollaboratorNotes();
				System.out.println(listofCollaboratedNotes);
			
			} catch (NumberFormatException | SignatureException e) {
				e.printStackTrace();
			}
			return listofCollaboratedNotes;

		}

}
