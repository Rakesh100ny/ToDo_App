package com.bridgelabz.todo.noteservice.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.todo.noteservice.model.Note;
import com.bridgelabz.todo.noteservice.service.INoteService;
import com.bridgelabz.todo.utility.Response;

@PropertySource("classpath:imageConfig.properties")
@RestController
public class NoteController {
	@Autowired
	INoteService noteService;
	
	@Value("${image.path}")
	private String path;

	private static final Logger logger = LoggerFactory.getLogger(NoteController.class);


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
		System.out.println("Updating User Note Date : " + note.getReminderDate());
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
			System.out.println("Name : " + note1.getTitle() +"Reminder Date : "+note1.getReminderDate());
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
	
	
	//-----------------------------Upload Image--------------------------------
	
	@RequestMapping(value = "/uploadFile",method = RequestMethod.POST)
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) 
	{
		System.out.println("rakesh");
		System.out.println("file : "+file);
       String name=file.getOriginalFilename();
       System.out.println("image name : "+file.getName());
       System.out.println("image name : "+name); 

       if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				
		       
				// Creating the directory to store file
				System.out.println("path : "+path);
				
				File dir = new File(path);
					
				System.out.println("dir : "+dir);

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + name);
				System.out.println("serverFile : "+serverFile);
				
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				logger.info("Server File Location="
						+ serverFile.getAbsolutePath());

				return new ResponseEntity<>(new Response(true, "You successfully uploaded file=" + name), HttpStatus.OK);
				
				
			} catch (Exception e) {
				return new ResponseEntity<>(new Response(false, "You failed to upload " + name + " => " + e.getMessage()), HttpStatus.CONFLICT);
				
			}
		} else {
			return new ResponseEntity<>(new Response(false, "You failed to upload " + name+ " because the file was empty."), HttpStatus.CONFLICT);
			
		}
	}

}
