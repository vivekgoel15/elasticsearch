package com.test.contact.service;

import java.util.List;
import java.util.Map;

import com.test.contact.form.Contact;

public interface ESContactService {
	
	public void addContact(Contact contact);
	
	public List<Contact> listContacts();

	public Contact getContact(Integer contactId);
	
	public void updateContact(Contact contact);
	
	public void removeContact(Integer id);
	
	public List<Map<String, Object>> searchDocuments(String field, String value);
}