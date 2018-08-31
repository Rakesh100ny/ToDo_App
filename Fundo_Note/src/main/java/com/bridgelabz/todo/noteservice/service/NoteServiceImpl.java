package com.bridgelabz.todo.noteservice.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import com.bridgelabz.todo.noteservice.model.Links;
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
			Set<Links> listofurlinfo = urlinfo(note.getDescription());

			if (!listofurlinfo.isEmpty()) {
				note.setLinks(listofurlinfo);
			}

			note.setUser(user);
			noteDao.addNote(note, user);
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
			Set<Links> listofurlinfo = urlinfo(note.getDescription());

			if (id == note.getUser().getId() && note.getUser().getId() != 0) {
				note.setLinks(listofurlinfo);
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
		Note note = noteDao.getNoteById(noteId);
		Label label = labelDao.getLabelById(labelId);
		if (note.getListOfLabels().contains(label)) {
			removeLabelOnNote(note, label);
		} else {
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

		return responsePath + file.getOriginalFilename();
	}

	@Override
	public byte[] toGetImage(String name) {
		File serverFile = new File(path + File.separator + name);
		if (serverFile.exists()) {

			try {
				return Files.readAllBytes(serverFile.toPath());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override
	@Transactional
	public void addCollaboratorOnNote(long userid, long noteid) 
	{
	   
		Note note = noteDao.getNoteById(noteid);

		User user = userDao.getUserById(userid);

		  List<User> collaboratorUser = note.getCollaboratedUser();
		  collaboratorUser.add(user);
		  note.setCollaboratedUser(collaboratorUser);

		  List<Note> collaboratorNotes = user.getCollaboratorNotes();
		  collaboratorNotes.add(note);
		  user.setCollaboratorNotes(collaboratorNotes);

		  userDao.updateUser(user);
		  noteDao.update(note);
		
	}

	@Override
	@Transactional
	public boolean removeCollaboratorOnNote(long userid, long noteid) {
		Note note = noteDao.getNoteById(noteid);
		User user = userDao.getUserById(userid);

		List<User> collaboratorUser = note.getCollaboratedUser();
		for (User user2 : collaboratorUser) {
			if (userid == user2.getId()) {
				collaboratorUser.remove(user2);
				break;
			}
		}
		note.setCollaboratedUser(collaboratorUser);

		List<Note> collaboratorNotes = user.getCollaboratorNotes();
		for (Note note2 : collaboratorNotes) {
			if (noteid == note2.getId()) {
				collaboratorNotes.remove(note2);
				break;
			}
		}
		user.setCollaboratorNotes(collaboratorNotes);

		userDao.updateUser(user);
		noteDao.update(note);

		return true;
	}

	@Override
	@Transactional
	public List<User> getAllCollaboratedUsers(long id) {

		System.out.println("id : " + id);

		Note note = noteDao.getNoteById(id);

		return note.getCollaboratedUser();

	}

	@Transactional
	@Override
	public List<Note> getAllCollaboratedNotes(String token) {
		List<Note> listOfNotes = null;

		try {
			User user = userDao.getUserById(Long.parseLong(Token.getParseJWT(token)));
			listOfNotes = user.getCollaboratorNotes();

		} catch (NumberFormatException | SignatureException e) {
			e.printStackTrace();
		}

		return listOfNotes;
	}

	@Override
	public Set<Links> urlinfo(String description) {

		String urlregex = "^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)([).!';/?:,][[:blank:]])?$";
		description = description.replaceAll("(\r\n | \n)", "\\s");
		String[] descripArray = description.split("\\s+");

		Links urlinfo = null;
		Pattern p = Pattern.compile(urlregex);

		Set<String> listofurl = new HashSet<String>();
		Set<Links> listofurlinfo = new HashSet<Links>();

		for (int i = 0; i < descripArray.length; i++) {
			if (p.matcher(descripArray[i]).matches()) {
				listofurl.add(descripArray[i]);
			}
		}

		for (String url : listofurl) {

			if (url != null) {
				Document doc;
				try {
					doc = Jsoup.connect(url).get();
					String urlTitle = doc.title();

					String urlDescription = url.split("://")[1].split("/")[0];
					String urlImage = doc.select("meta[property=og:image]").first().attr("content");

					urlinfo = new Links();
					urlinfo.setUrl(url);
					urlinfo.setUrlTitle(urlTitle);
					urlinfo.setUrlDescription(urlDescription);
					urlinfo.setUrlImage(urlImage);

					listofurlinfo.add(urlinfo);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}
		return listofurlinfo;
	}

	@Override
	@Transactional
	public void removeurlinfo(String token, Note note, long id) {

		long userId;
		try {
			userId = Long.parseLong(Token.getParseJWT(token));
			Note updatingnote = noteDao.getNoteById(note.getId());

			if (updatingnote.getUser().getId() == userId) {
				Links url = noteDao.getByUrlId(id);

				if (updatingnote.getLinks().contains(url)) {
					updatingnote.getLinks().remove(url);
					boolean status = noteDao.deleteUrl(url.getId());
					if (status) {
						noteDao.update(updatingnote);
					}
				}
			}
		} catch (NumberFormatException | SignatureException e) {
			e.printStackTrace();
		}

	}

}
