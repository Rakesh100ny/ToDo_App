package com.bridgelabz.todo.label.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bridgelabz.todo.label.model.Label;
import com.bridgelabz.todo.userservice.model.User;

@Repository
public class LabelDaoImpl implements ILabelDao{

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void addLabel(Label label) {
		Session session=sessionFactory.getCurrentSession();
	     session.save(label);	
	}

	@Override
	public List<Label> getAllLabels(User user) {
		Session session=sessionFactory.getCurrentSession();
		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(Label.class);
		criteria.add(Restrictions.eq("user", user));
		@SuppressWarnings("unchecked")
		List<Label> labels = criteria.list();
		return labels;
	}

	@Override
	public boolean deleteLabelById(long id) {
		 Session session=sessionFactory.getCurrentSession();
		 Label label=session.get(Label.class, id);
		 
		 if(label==null)return false;
		 
		 
		 session.delete(label);;
		 
		 return true;
		 
	}

	@Override
	public Label getLabelById(long id) 
	{
		 Session session=sessionFactory.getCurrentSession();
		  return session.get(Label.class, id);
    }

}
