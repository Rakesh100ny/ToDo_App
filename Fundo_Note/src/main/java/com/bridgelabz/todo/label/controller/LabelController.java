package com.bridgelabz.todo.label.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.todo.label.model.Label;
import com.bridgelabz.todo.label.service.ILabelService;
import com.bridgelabz.todo.utility.Response;

@RestController
public class LabelController 
{
	@Autowired
	private ILabelService labelService;

	@RequestMapping(value = "/addlabel", method = RequestMethod.POST)
	public ResponseEntity<?> addLabel(@RequestBody Label label, @RequestHeader("userLoginToken")String token)
	{
		System.out.println("Creating label " + label.getLabelName());
		System.out.println("token in Note : "+token);
		System.out.println("rakesh");
		labelService.addLabel(label, token);
		
		return new ResponseEntity<>(new Response(true, "Label is created...!"),HttpStatus.OK);
			
	}

	@RequestMapping(value = "/labels", method = RequestMethod.GET)
	public ResponseEntity<List<Label>> listAllLabels(@RequestHeader("userLoginToken")String token)
	{
			List<Label> labels = labelService.getAllLabels(token);
			
			if(labels.isEmpty())
			{
			 return new ResponseEntity<List<Label>>(HttpStatus.NO_CONTENT); 
			}
			  
			return new ResponseEntity<List<Label>>(labels, HttpStatus.OK); 
			
	}

	@RequestMapping(value = "/deletelabel/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> deleteLabel(@PathVariable("id") long id,@RequestHeader("userLoginToken")String token)
	{
		System.out.println("id : "+id);
		  if(labelService.deleteLabel(id, token))
		   {
			   return new ResponseEntity<>(new Response(true, "Label is successfully deleted...!"),HttpStatus.OK);
		   }
		   
		    return new ResponseEntity<>(new Response(false, "Label is not deleted...!"),HttpStatus.NO_CONTENT); 
    }
}

