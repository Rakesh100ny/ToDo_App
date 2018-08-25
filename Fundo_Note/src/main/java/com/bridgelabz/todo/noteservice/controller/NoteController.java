package com.bridgelabz.todo.noteservice.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.todo.noteservice.model.Note;
import com.bridgelabz.todo.noteservice.service.INoteService;
import com.bridgelabz.todo.utility.Response;

@RestController
public class NoteController {
	@Autowired
	INoteService noteService;


	private static final Logger logger = LoggerFactory.getLogger(NoteController.class);

	// -------------------Create a User Note--------------------------------------------

	@RequestMapping(value = "/addnote", method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> addNote(@RequestBody Note note, @RequestHeader("userLoginToken") String token) {
		System.out.println("Creating User " + note.getTitle());
		noteService.addNote(note, token);

		return new ResponseEntity<>(new Response(true, "Note is created...!"), HttpStatus.CREATED);
	}

	// ------------------- Update a User Note ------------------------
	@RequestMapping(value = "/updatenote", method = RequestMethod.PUT)
	public ResponseEntity<?> updateNote(@RequestBody Note note, @RequestHeader("userLoginToken") String token) {
		System.out.println("Updating User Note : " + note.getTitle());
		System.out.println("Updating User Note Date : " + note.getReminderDate());
		noteService.update(note, token);

		return new ResponseEntity<>(new Response(true, "Note is successfully updated...!"), HttpStatus.OK);

	}

	// ------------------- Relation Between Note and Label ------------------------
	@RequestMapping(value = "/relationNoteLabel/{noteId}/{labelId}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateRelationNoteLabel(@PathVariable("noteId") long noteId,
			@PathVariable("labelId") long labelId) {

		noteService.relationBetweenNoteLabel(noteId, labelId);

		return new ResponseEntity<>(new Response(true, "Note is successfully updated...!"), HttpStatus.OK);

	}

	// -------------------Retrieve All Users Notes--------------------------------------

	@RequestMapping(value = "/note", method = RequestMethod.GET)
	public ResponseEntity<List<Note>> listAllNotes(@RequestHeader("userLoginToken") String token) {

		List<Note> users = noteService.getAllNotes(token);

		if (users.isEmpty()) {
			return new ResponseEntity<List<Note>>(HttpStatus.NO_CONTENT);
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

	// -----------------------------Upload Image--------------------------------

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public ResponseEntity<?> uploadFile(@RequestBody MultipartFile file) throws IOException 
	{
		String name = file.getOriginalFilename();
	
		if (!file.isEmpty()) 
		{
   		 String path=noteService.storeServerSideImage(file);
   
	 	 logger.info("Server File Location with Name=" + path);
      	 return new ResponseEntity<>(new Response(true, path),HttpStatus.OK);
		} 
		else 
		{
		 return new ResponseEntity<Response>(new Response(false, "You failed to upload " + name + " because the file was empty."),HttpStatus.CONFLICT);
    	}
	}
	
	// -----------------------------Retrive Image with help of src--------------------------------

	@RequestMapping(value = "/image/{name:.+}", method = RequestMethod.GET)
	public ResponseEntity<?> showFile(@PathVariable("name")String name) 
	{
		
		byte[] file=noteService.toGetImage(name);	
	
		for(byte str : file)
		{
	     System.out.println("image : "+str);		
		}
	
		System.out.println("file length : "+file.length);
		
		if(file.length==0)
		{
			return new ResponseEntity<Response>(new Response(false, "You failed to get Image" + name + " because the file was empty."),HttpStatus.CONFLICT);	
		}
		
		return new ResponseEntity<>(file,HttpStatus.OK);
		
	
		

	}
}
