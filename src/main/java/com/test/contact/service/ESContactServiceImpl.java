package com.test.contact.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.contact.form.Contact;

@Service("elasticService")
@Transactional
@Scope("session")
public class ESContactServiceImpl implements ESContactService {

	private final Client client;
	private static final String index = "contacts";
	private static final String type = "contact";
	
	public ESContactServiceImpl() {
		super();
		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "tc_cluster").build();
		client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
	}

	@Override
	public void addContact(Contact contact) {
		client.prepareIndex(index, type, String.valueOf(contact.getId())).setSource(buildDocument(contact)).execute().actionGet();
	}
	
	@Override
	public List<Contact> listContacts() {
		List<Contact> contacts = new ArrayList<Contact>();
		
		List<Map<String, Object>> results = searchDocuments(null, null);
		for (Map<String, Object> jsonDocument : results) {
			contacts.add(buildContact(jsonDocument));
		}
		return contacts;
	}
	
	@Override
	public Contact getContact(Integer contactId) {
		List<Map<String, Object>> results = searchDocuments("id", String.valueOf(contactId));
		return buildContact(results.get(0));
	}
	
	@Override
	public void updateContact(Contact contact) {
		Map<String, Object> jsonDocument = buildDocument(contact);
		client.prepareUpdate(index, type, jsonDocument.get("id").toString()).setDoc(jsonDocument).execute().actionGet();
	}
	
	@Override
	public void removeContact(Integer id) {
        client.prepareDelete(index, type, String.valueOf(id)).execute().actionGet();	 
    }

	@Override
	public List<Map<String, Object>> searchDocuments(String field, String value) {

		List<Map<String, Object>> arraylist = new ArrayList<Map<String, Object>>();
		try {
			// Wait for ES to refresh data for real time search
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SearchResponse response = null;
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index).setTypes(type);
		if (field != null) {
			searchRequestBuilder.setQuery(QueryBuilders.matchQuery(field, value));
		}
		
		System.out.println("ElasticSearch Query: \n" + searchRequestBuilder.toString());
		
		response = searchRequestBuilder.execute().actionGet();

		SearchHit[] results = response.getHits().getHits();
		System.out.println("Response:" + response.toString());
		for (SearchHit hit : results) {
			arraylist.add(hit.getSource());
		}
		return arraylist;
	}
	
	private Map<String, Object> buildDocument(Contact contact) {
		Map<String, Object> jsonDocument = new HashMap<String, Object>();
		jsonDocument.put("id", contact.getId());
		jsonDocument.put("firstname", contact.getFirstname());
		jsonDocument.put("lastname", contact.getLastname());
		jsonDocument.put("email", contact.getEmail());
		jsonDocument.put("telephone", contact.getTelephone());
		return jsonDocument;
	}

	private Contact buildContact(Map<String, Object> jsonDocument) {
		Contact contact = new Contact();
		contact.setId(Integer.valueOf(jsonDocument.get("id").toString()));
		contact.setFirstname(String.valueOf(jsonDocument.get("firstname")));
		contact.setLastname(String.valueOf(jsonDocument.get("lastname")));
		contact.setEmail(String.valueOf(jsonDocument.get("email")));
		contact.setTelephone(String.valueOf(jsonDocument.get("telephone")));		
		return contact;
	}
		
}
