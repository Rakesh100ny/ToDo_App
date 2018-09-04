package com.bridgelabz.todo.noteservice.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bridgelabz.todo.noteservice.model.Links;
import com.bridgelabz.todo.noteservice.model.Note;
import com.bridgelabz.todo.userservice.model.User;

@SuppressWarnings("deprecation")
@Repository
public class NoteDaoImpl implements INoteDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void addNote(Note note,User user)
	{
     Session session=sessionFactory.getCurrentSession();
     session.save(note);
	}

	@Override
	public void update(Note note)
	{
	 Session session=sessionFactory.getCurrentSession();
	 System.out.println("updated date" +note.getLastUpdatedDate());
	 session.update(note);
	 System.out.println("Note is successfully updated...!");
	}
	
	@Override
	public Note getNoteById(long id) 
	{
	 Session session=sessionFactory.getCurrentSession();
	  return session.get(Note.class, id);
	  
	}

	@Override
	public boolean deleteNoteById(long id)
	{
	 Session session=sessionFactory.getCurrentSession();
	 Note note=session.get(Note.class, id);
	 
	 if(note==null)return false;
	 
	 
	 session.delete(note);;
	 
	 return true;
	 
	}

	@Override
	public List<Note> getAllNotes(long id) 
	{
		Session session=sessionFactory.getCurrentSession();
		User user=session.get(User.class, id);
		
		return user.getListOfNotes();
	}

	@Override
	public Links getByUrlId(long id) {
		Criteria crt = sessionFactory.getCurrentSession().createCriteria(Links.class);

		crt.add(Restrictions.eq("id", id));
		
		Links url = (Links) crt.uniqueResult();

		return (url != null) ? url : null;
	}

	@Override
	public boolean deleteUrl(long id) {
//		String queryDelete = "delete from Links where id =: urlId";
//		Query<?> query = sessionFactory.getCurrentSession().createQuery(queryDelete);
//		query.setParameter("urlId", id);
//		long result = query.executeUpdate();
//		return result;
		
		Session session=sessionFactory.getCurrentSession();
		 Links link=session.get(Links.class, id);
		 
		 if(link==null)return false;
		 
		 
		 session.delete(link);;
		 
		 return true;
		 
	}

	@Override
	public void deleteAllTrashedNote() 
	{
		Date date=new Date(new Date().getTime()-(20*1000));
		Session session=sessionFactory.getCurrentSession();
	    
		
		Query query=session.createQuery("delete from Note where isTrashed=true AND lastUpdatedDate <:date");
		query.setParameter("date", date);
		

		int count=query.executeUpdate();
		if(count>0) {
			System.out.println("Number of notes delted " +count);
		}
		
	}
	
}
