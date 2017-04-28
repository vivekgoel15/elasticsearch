package com.test.contact.dao;

import java.util.List;

import com.test.contact.form.Contact;

public interface ContactDAO {
	
	public void addContact(Contact contact);
	public Contact getContact(Integer id);
	public void updateContact(Contact contact);
	public List<Contact> listContacts();
	public void removeContact(Integer id);
}
