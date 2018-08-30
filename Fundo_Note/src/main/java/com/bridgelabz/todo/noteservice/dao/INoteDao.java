package com.bridgelabz.todo.noteservice.dao;

import java.util.List;

import com.bridgelabz.todo.noteservice.model.Links;
import com.bridgelabz.todo.noteservice.model.Note;
import com.bridgelabz.todo.userservice.model.User;

public interface INoteDao {
	void addNote(Note note, User user);

	void update(Note note);

	Note getNoteById(long id);

	boolean deleteNoteById(long id);

	List<Note> getAllNotes(long id);

	Links getByUrlId(long id);

	boolean deleteUrl(long id);

}
