package com.bridgelabz.todo.label.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
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
	public List<Label> getAllLabels(long id) {

		Session session=sessionFactory.getCurrentSession();
		User user=session.get(User.class, id);
		
		
		return user.getListOfLabels();
	}

	@Override
	public boolean deleteLabelById(long id) {
		 Session session=sessionFactory.getCurrentSession();
		 Label label=session.get(Label.class, id);
		 
		 if(label==null)return false;
		 
		 
		 session.delete(label);
		 
		 return true;
		 
	}

	@Override
	public Label getLabelById(long id) 
	{
		 Session session=sessionFactory.getCurrentSession();
		  return session.get(Label.class, id);
    }

	@Override
	public long isUserExist(Label label) {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(Label.class).add(Restrictions.eq("labelName", label.getLabelName()))
				.setProjection(Projections.count("labelName"));

		long count = (long) criteria.uniqueResult();

		return count;		
	}

	@Override
	public void update(Label label)
	{
		Session session=sessionFactory.getCurrentSession();
		 session.update(label);
		 System.out.println("Label is successfully updated...!");	
	}

}
