package com.bridgelabz.todo.label.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.bridgelabz.todo.noteservice.model.Note;
import com.bridgelabz.todo.userservice.model.User;

@Entity
@Cacheable
@Table(name = "User_Label")
public class Label {

	@Id
	@Column(name="Label_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String labelName;

	@ManyToOne
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name="User_id")
	private User userDetails;
	
	@ManyToMany(mappedBy="listOfLabels")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Note> listOfNotes=new ArrayList<Note>(); 


	public List<Note> getListOfNotes() {
		return listOfNotes;
	}

	public void setListOfNotes(List<Note> listOfNotes) {
		this.listOfNotes = listOfNotes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public User getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(User userDetails) {
		this.userDetails = userDetails;
	}
}