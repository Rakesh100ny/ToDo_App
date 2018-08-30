package com.bridgelabz.todo.noteservice.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.bridgelabz.todo.label.model.Label;
import com.bridgelabz.todo.userservice.model.User;

@Entity
@Cacheable
@Table(name = "User_Note")
public class Note {
	@Id
	@Column(name="Note_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
		
	private String title;
	private String description;
	private String color = "white";
	private boolean isPined;
	private boolean isTrashed;
	private boolean isArchived;
	@Temporal(TemporalType.DATE)
	private Date createdDate = new Date();
	@Temporal(TemporalType.DATE)
	private Date lastUpdatedDate = new Date();
	
	private Date reminderDate;
   
	private String imageUrl;
	
	@Column
	@OneToMany (cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<Links> links;
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Date getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(Date reminderDate) {
		this.reminderDate = reminderDate;
	}

	@ManyToOne
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name="User_id")
	private User user;
	
		
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name="User_Note_Label",joinColumns=@JoinColumn(name="Note_id"),inverseJoinColumns=@JoinColumn(name="Label_id"))
	private List<Label> listOfLabels=new ArrayList<Label>();
	

	@ManyToMany(mappedBy = "collaboratorNotes")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<User> collaboratedUser;

	public List<User> getCollaboratedUser() {
		return collaboratedUser;
	}

	public void setCollaboratedUser(List<User> collaboratedUser) {
		this.collaboratedUser = collaboratedUser;
	}

	public Note() {
		super();
	}

	public User getUser() {
		return user;
	}

	public List<Label> getListOfLabels() {
		return listOfLabels;
	}

	public void setListOfLabels(List<Label> listOfLabels) {
		this.listOfLabels = listOfLabels;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isPined() {
		return isPined;
	}

	public void setPined(boolean isPined) {
		this.isPined = isPined;
	}

	public boolean isTrashed() {
		return isTrashed;
	}

	public void setTrashed(boolean isTrashed) {
		this.isTrashed = isTrashed;
	}

	public boolean isArchived() {
		return isArchived;
	}

	public void setArchived(boolean isArchived) {
		this.isArchived = isArchived;
	}

	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Set<Links> getLinks() {
		return links;
	}

	public void setLinks(Set<Links> links) {
		this.links = links;
	}

}
