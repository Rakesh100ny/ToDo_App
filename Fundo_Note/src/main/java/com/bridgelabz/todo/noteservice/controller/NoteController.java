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

@RestController
public class NoteController {
	@Autowired
	INoteService noteService;

// -------------------Create a User Note--------------------------------------------------------

	@RequestMapping(value = "/addnote", method = RequestMethod.POST)
	public ResponseEntity<Void> createNote(@RequestBody Note note,@RequestHeader("token")String token)
	{
		System.out.println("Creating User " + note.getTitle());

		System.out.println("token in Note : "+token);
		System.out.println("rakesh");
	    
        noteService.addNote(note,token);

		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	
	//------------------- Update a User Note (Some Error is Occured)------------------------
	  @RequestMapping(value = "/updatenote", method = RequestMethod.PUT) 
	  public ResponseEntity<?> updateNote(@RequestBody Note note) 
	  { 
		  System.out.println("Updating User Note" + note.getTitle());
	  
	   noteService.update(note);
	  
	  return new ResponseEntity<>(HttpStatus.OK);
	  
	  }

	  

	  //-------------------Retrieve All Users Notes-----------------------------------------
	  
	  @RequestMapping(value = "/note", method = RequestMethod.GET) 
	  public ResponseEntity<List<Note>> listAllNotes(@RequestHeader("token")String token)
	  {
		  List<Note> users = noteService.getAllNotes(token);
	  
	  if(users.isEmpty())
	  {
		  System.out.println("r1"); 
		  return new ResponseEntity<List<Note>>(HttpStatus.NO_CONTENT); 
	  }
	  System.out.println("r2"); return new ResponseEntity<List<Note>>(users, HttpStatus.OK); 
	 }
	  
	  
	  //------------------- Delete a User-----------------------------------------
	  
	  @RequestMapping(value = "/deletenote/{id}", method = RequestMethod.DELETE)
	  public ResponseEntity<?> deleteNote(@PathVariable("id") long id)
	  {
	   System.out.println("Fetching & Deleting User Note with id " + id);
	  
	   if(noteService.deleteNoteById(id))
	   {
		   return new ResponseEntity<>(HttpStatus.OK);
	   }
	   
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
	   }
	  
	 
 
}