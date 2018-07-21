package com.bridgelabz.todo.label.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.stereotype.Component;

import com.bridgelabz.todo.userservice.model.User;

@Component
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