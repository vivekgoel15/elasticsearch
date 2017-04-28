package com.test.contact.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.test.contact.form.Contact;
import com.test.contact.service.ContactService;
import com.test.contact.service.ESContactService;

@Controller
@Scope("session")
public class ContactController {

	@Autowired
	private ContactService contactService;
	
	@Autowired
	private ESContactService esContactService;
	
	@RequestMapping("/")
	public String listContacts(Map<String, Object> map) {

		map.put("contact", new Contact());
		map.put("contactList", esContactService.listContacts());
		return "contact";
	}

	@RequestMapping(value = {"/add"}, method = RequestMethod.POST)
	public String addContact(@ModelAttribute("contact") Contact contact, BindingResult result) {

		contactService.addContact(contact);
		esContactService.addContact(contact);
		return "redirect:/";
	}

	@RequestMapping(value = "/edit/{contactId}")
	public String editContact(Map<String, Object> map, @PathVariable("contactId") Integer contactId) {
		
		map.put("contact", esContactService.getContact(contactId));
		map.put("contactList", esContactService.listContacts());
		
		return "contact";
	}
	
	@RequestMapping(value = "/edit/add", method = RequestMethod.POST)
	public String updateContact(@ModelAttribute("contact") Contact contact, BindingResult result) {

		contactService.updateContact(contact);
		esContactService.updateContact(contact);
		return "redirect:/";
	}
	
	@RequestMapping("/delete/{contactId}")
	public String deleteContact(@PathVariable("contactId") Integer contactId) {

		contactService.removeContact(contactId);
		esContactService.removeContact(contactId);
		return "redirect:/";
	}
}
