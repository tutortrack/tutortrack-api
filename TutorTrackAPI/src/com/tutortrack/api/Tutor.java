package com.tutortrack.api;

import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

public class Tutor {

	public static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public String name, email, password, umsid;
	
	public Tutor() {}
	
	public void setName(String newName) { name = newName; }
	public void setEmail(String newEmail) { email = newEmail; }
	public void setPassword(String newPass) { password = newPass; }
	public void setUMSID(String newID) { umsid = newID; }
	
	public String getName() { return name; }
	public String getEmail() { return email; }
	public String getPassword() { return password; }
	public String getUMSID() { return umsid; }

	public Tutor(String name, String email, String password, String umsid) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.umsid = umsid;
	}

	@SuppressWarnings("deprecation")
	public static Tutor getTutorFromDatabase(String email, String password) {
	
		Query q = new Query("Tutor")
					.addFilter("email",
						Query.FilterOperator.EQUAL,
						email)
					.addFilter("password",
						Query.FilterOperator.EQUAL,
						password);
		List<Entity> results = datastore.prepare(q)
                                      .asList(FetchOptions.Builder.withDefaults());
		Entity s = results.get(0);
		return new Tutor((String) s.getProperty("name"), (String) s.getProperty("email"), (String) s.getProperty("password"), (String) s.getProperty("umsid"));
	}
  
	private static boolean addTutorToDatabaseHelper(String name, String email, String password, String umsid) {
	
		Entity s = new Entity("Tutor");
		s.setProperty("name", name);
		s.setProperty("email", email);
		s.setProperty("password", password);
		s.setProperty("umsid", umsid);
		
		try {
		
			datastore.put(s);
			
		} catch (Exception e) {
			return false;
		}

		return true;
	}
	
	public static boolean addTutorToDatabase(Tutor s) {
		return addTutorToDatabaseHelper(s.name, s.email, s.password, s.umsid);
	}
	
	@SuppressWarnings("deprecation")
	public static Tutor searchDatabaseForName(String name) {
	
		Query q = new Query("Tutor").addFilter("name", Query.FilterOperator.EQUAL, name);
		
		List<Entity> results = datastore.prepare(q)
                                      .asList(FetchOptions.Builder.withDefaults());
		Entity s = results.get(0);
		return new Tutor((String) s.getProperty("name"), (String) s.getProperty("email"), (String) s.getProperty("password"), (String) s.getProperty("umsid"));
	
	}
	
}
