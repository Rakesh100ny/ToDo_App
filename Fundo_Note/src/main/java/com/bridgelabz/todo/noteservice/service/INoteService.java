package com.bridgelabz.todo.noteservice.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.todo.label.model.Label;
import com.bridgelabz.todo.noteservice.model.Note;


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
	 

}
