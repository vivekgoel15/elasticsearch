package com.test.contact.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.test.contact.form.Contact;

@Repository
@Scope("request")
public class ContactDAOImpl implements ContactDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void addContact(Contact contact) {
		sessionFactory.getCurrentSession().save(contact);
	}

	@Override
	public Contact getContact(Integer id) {
		return (Contact) sessionFactory.getCurrentSession().load(Contact.class, id);
	}
	
	@Override
	public void updateContact(Contact contact) {
		sessionFactory.getCurrentSession().update(contact);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Contact> listContacts() {
		return sessionFactory.getCurrentSession().createQuery("from Contact").list();
	}

	@Override
	public void removeContact(Integer id) {
		Contact contact = (Contact) sessionFactory.getCurrentSession().load(Contact.class, id);
		if (contact!=null) {
			sessionFactory.getCurrentSession().delete(contact);
		}
	}
}
