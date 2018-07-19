package com.bridgelabz.todo.label.dao;

import java.util.List;

import com.bridgelabz.todo.label.model.Label;
import com.bridgelabz.todo.userservice.model.User;

public interface ILabelDao
{

	void addLabel(Label label);

	List<Label> getAllLabels(long id);

	boolean deleteLabelById(long id);

	Label getLabelById(long id);

}
