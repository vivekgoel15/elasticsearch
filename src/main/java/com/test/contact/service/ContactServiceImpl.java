package com.test.contact.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.contact.dao.ContactDAO;
import com.test.contact.form.Contact;

@Service
@Transactional
@Scope("session")
public class ContactServiceImpl implements ContactService {

	@Autowired
	private ContactDAO contactDAO;
	
	@Override
	public void addContact(Contact contact) {
		contactDAO.addContact(contact);
	}

	@Override
	public Contact getContact(Integer id) {
		return contactDAO.getContact(id);
	}
	
	@Override
	public void updateContact(Contact contact) {
		contactDAO.updateContact(contact);
	}
	
	@Override
	public List<Contact> listContacts() {
		return contactDAO.listContacts();
	}

	@Override
	public void removeContact(Integer id) {
		contactDAO.removeContact(id);
	}
}
