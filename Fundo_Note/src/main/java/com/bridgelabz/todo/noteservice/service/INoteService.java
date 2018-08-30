package com.bridgelabz.todo.noteservice.service;

import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.todo.label.model.Label;
import com.bridgelabz.todo.noteservice.model.Links;
import com.bridgelabz.todo.noteservice.model.Note;
import com.bridgelabz.todo.userservice.model.User;


public interface INoteService 
{
	void addNote(Note note,String token);
	 
	 void update(Note note,String token);
	 
	 Note getNoteById(long id);
	 
	 boolean deleteNoteById(long id,String token);
	 
	 List<Note> getAllNotes(String token);

	void addLabelOnNote(Note note,Label label);

	void removeLabelOnNote(Note note,Label label);

	void relationBetweenNoteLabel(long noteId, long labelId);

	String storeServerSideImage(MultipartFile file);

	byte[] toGetImage(String name);

	void addCollaboratorOnNote(long userid, long noteid);

	boolean removeCollaboratorOnNote(long userid, long noteid);

	List<User> getAllCollaboratedUsers(long id);

	List<Note> getAllCollaboratedNotes(String token);
	
	Set<Links> urlinfo(String description);

	void removeurlinfo(String token, Note note, long id);
}
