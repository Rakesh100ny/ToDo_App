package com.bridgelabz.todo.label.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.bridgelabz.todo.userservice.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Label {

	@Id
	@Column(name="Label_Id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String labelName;

	@JsonIgnore
	@ManyToOne
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name = "User_Id")
	private User user;

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}