package com.bridgelabz.todo.noteservice.service;

import java.util.List;

import com.bridgelabz.todo.noteservice.model.Note;


public interface INoteService 
{
	void addNote(Note note,String token);
	 
	 void update(Note note,String token);
	 
	 Note getNoteById(long id);
	 
	 boolean deleteNoteById(long id,String token);
	 
	 List<Note> getAllNotes(String token);

	void addLabelOnNote(long noteId, long labelId);

	void removeLabelOnNote(long noteId, long labelId);
	 

}
