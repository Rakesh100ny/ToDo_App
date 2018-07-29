package com.bridgelabz.todo.noteservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.todo.noteservice.model.Note;
import com.bridgelabz.todo.noteservice.service.INoteService;
import com.bridgelabz.todo.utility.Response;

@RestController
public class NoteController {
	@Autowired
	INoteService noteService;

	// -------------------Create a User Note--------------------------------------------

	@RequestMapping(value = "/addnote", method = RequestMethod.POST, consumes={"application/json"})
	public ResponseEntity<?> addNote(@RequestBody Note note, @RequestHeader("userLoginToken") String token) {
		System.out.println("Creating User " + note.getTitle());

		noteService.addNote(note, token);

		return new ResponseEntity<>(new Response(true, "Note is created...!"), HttpStatus.CREATED);
	}

	// ------------------- Update a User Note ------------------------
	@RequestMapping(value = "/updatenote", method = RequestMethod.PUT)
	public ResponseEntity<?> updateNote(@RequestBody Note note, @RequestHeader("userLoginToken") String token) {
		System.out.println("Updating User Note : " + note.getTitle());

		noteService.update(note, token);

		return new ResponseEntity<>(new Response(true, "Note is successfully updated...!"), HttpStatus.OK);

	}

	// ------------------- Relation Between Note and Label ------------------------
	@RequestMapping(value = "/relationNoteLabel/{noteId}/{labelId}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateRelationNoteLabel(@PathVariable("noteId") long noteId,
			@PathVariable("labelId") long labelId) {
		System.out.println("noteId : " + noteId);
		System.out.println("labelId : " + labelId);

		noteService.relationBetweenNoteLabel(noteId,labelId);
		
		
		System.out.println("Rakesh Soni Baberwal");

		return new ResponseEntity<>(new Response(true, "Note is successfully updated...!"), HttpStatus.OK);

	}

	// -------------------Retrieve All Users Notes--------------------------------------

	@RequestMapping(value = "/note", method = RequestMethod.GET)
	public ResponseEntity<List<Note>> listAllNotes(@RequestHeader("userLoginToken") String token) {
		System.out.println("ranu1");
		System.out.println("token : " + token);
		List<Note> users = noteService.getAllNotes(token);

		if (users.isEmpty()) {
			return new ResponseEntity<List<Note>>(HttpStatus.NO_CONTENT);
		}

		System.out.println("ranu6");
		for (Note note1 : users) {
			System.out.println("Name : " + note1.getTitle());
		}

		return new ResponseEntity<List<Note>>(users, HttpStatus.OK);
	}

	// ------------------- Delete a User-----------------------------------------

	@RequestMapping(value = "/deletenote/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteNote(@PathVariable("id") long id, @RequestHeader("userLoginToken") String token) {
		System.out.println("Fetching & Deleting User Note with id " + id);

		if (noteService.deleteNoteById(id, token)) {
			return new ResponseEntity<>(new Response(true, "Note is successfully deleted...!"), HttpStatus.OK);
		}

		return new ResponseEntity<>(new Response(false, "Note is not deleted...!"), HttpStatus.NO_CONTENT);
	}

}
