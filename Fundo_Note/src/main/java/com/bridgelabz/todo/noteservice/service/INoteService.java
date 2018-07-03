package com.bridgelabz.todo.noteservice.service;

import java.util.List;

import com.bridgelabz.todo.noteservice.model.Note;


public interface INoteService 
{
	void addNote(Note note,String token);
	 
	 void update(Note note);
	 
	 Note getNoteById(long id);
	 
	 boolean deleteNoteById(long id);
	 
	 List<Note> getAllNotes(String token);
	 

}
