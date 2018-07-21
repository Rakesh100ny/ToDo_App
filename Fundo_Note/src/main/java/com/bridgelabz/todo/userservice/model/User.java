package com.bridgelabz.todo.userservice.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.bridgelabz.todo.label.model.Label;
import com.bridgelabz.todo.noteservice.model.Note;
import com.bridgelabz.todo.validation.Phone;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Component
@Entity
@Cacheable
@Table(name = "User_Info")
public class User {

	@Id
	@Column(name = "User_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "User_fName")
	private String firstName;

	@Column(name = "User_lName")
	private String lastName;

	@Column(name = "User_mailId")
	private String email;

	@Column(name = "User_pass")
	private String password;

	@Column(name = "isUserActivate")
	private boolean isActivated;

	@Phone
	@Column(name = "User_mobileNo")
	private String mobileNo;

	@JsonIgnore
	@OneToMany(mappedBy="user",fetch=FetchType.EAGER,cascade=CascadeType.PERSIST)
	private List<Note> listOfNotes = new ArrayList<Note>();
	
	@JsonIgnore
	@OneToMany(mappedBy="userDetails",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private List<Label> listOfLabels = new ArrayList<Label>();
	
	public List<Label> getListOfLabels() {
		return listOfLabels;
	}

	public void setListOfLabels(List<Label> listOfLabels) {
		this.listOfLabels = listOfLabels;
	}

	public User(RegisterModel registerModel) {
		this.firstName = registerModel.getFirstName();
		this.lastName = registerModel.getLastName();
		this.email = registerModel.getEmail();
		this.mobileNo=registerModel.getMobileNo();
	}

	public List<Note> getListOfNotes() {
		return listOfNotes;
	}

	public void setListOfNotes(List<Note> listOfNotes) {
		this.listOfNotes = listOfNotes;
	}
	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public User() {
		isActivated = false;
	}

	public boolean isActivated() {
		return isActivated;
	}

	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}