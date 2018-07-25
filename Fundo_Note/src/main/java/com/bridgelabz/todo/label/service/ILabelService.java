package com.bridgelabz.todo.label.service;

import java.util.List;

import com.bridgelabz.todo.label.model.Label;

public interface ILabelService 
{
	void addLabel(Label label,String token);

	List<Label> getAllLabels(String token);

	boolean deleteLabel(long id, String token);

	boolean isLabelExist(Label label);

	void update(Label label, String token);

}
