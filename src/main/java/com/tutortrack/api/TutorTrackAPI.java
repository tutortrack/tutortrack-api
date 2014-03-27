package com.tutortrack.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.DefaultValue;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;

import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

import javax.inject.Named;

/**
 * Defines v1 of a helloworld API, which provides simple "greeting" methods.
 */
@Api(
    name = "tutortrack",
    version = "v1",
    scopes = {Constants.EMAIL_SCOPE})
public class TutorTrackAPI {

	public static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	@ApiMethod(name = "students.getinfo", httpMethod = "get", path="students/myInfo")
	public Student getStudentInfo(@Named("email") String email, @Named("password") String password) {
		return Student.getStudentFromDatabase(email, password);
	}

	@ApiMethod(name = "students.create", httpMethod = "post", path="students/register")
	public Student registerStudent(Student new_student_info) {
		if (Student.addStudentToDatabase(new_student_info)) {
			return new_student_info;
		} else {
			return new Student();
		}
	}
	
	@ApiMethod(name = "tutors.getinfo", httpMethod = "get", path="tutors/myInfo")
	public Tutor getTutorInfo(@Named("email") String email, @Named("password") String password) {
		return Tutor.getTutorFromDatabase(email, password);
	}

	@ApiMethod(name = "tutors.create", httpMethod = "post", path="tutors/register")
	public Tutor registerTutor(Tutor new_tutor_info) {
		if (Tutor.addTutorToDatabase(new_tutor_info)) {
			return new_tutor_info;
		} else {
			return new Tutor();
		}
	}
	
	@ApiMethod(name = "admins.getinfo", httpMethod = "get", path="admins/myInfo")
	public Admin getAdminInfo(@Named("email") String email, @Named("password") String password) {
		return Admin.getAdminFromDatabase(email, password);
	}

	@ApiMethod(name = "admins.create", httpMethod = "post", path="admins/register")
	public Admin registerAdmin(Admin new_admin_info) {
		if (Admin.addAdminToDatabase(new_admin_info)) {
			return new_admin_info;
		} else {
			return new Admin();
		}
	}
	
	@ApiMethod(name= "tutors.search", httpMethod = "get", path="tutors/search")
	public List<TutorBlockPostObject> searchTutors(@Named("location") @DefaultValue String location , @Named("subject") @DefaultValue String subject ) {
	
		List<TutorBlockPostObject> res = new LinkedList<TutorBlockPostObject>();
	
		Query q = new Query("TutorBlock");
		
		if (!location.equals("")) {
			q.addFilter("location", Query.FilterOperator.EQUAL,
						location);
		}
		
		if (!subject.equals("")) {
			q.addFilter("subject", Query.FilterOperator.EQUAL,
						subject);
		}
		
		q.addSort("name", SortDirection.ASCENDING);
									  
		for ( Entity e : datastore.prepare(q).asIterable() ) {
		
			List<String> subjects = (List<String>) e.getProperty("subject");
		
			TutorBlockPostObject block = new TutorBlockPostObject();
			block.setName((String) e.getProperty("name"));
			block.setStartDate((String) e.getProperty("startDate"));
			block.setEndDate((String) e.getProperty("endDate"));
			block.setStartTime((String) e.getProperty("startTime"));
			block.setEndTime((String) e.getProperty("endTime"));
			block.setLocation((String) e.getProperty("location"));
			
			String sub = "";
			
			for ( String s : subjects ) {
				sub += s + ", ";
			}
			
			sub = sub.substring(0, sub.lastIndexOf(", "));
			
			block.setSubjects(sub);
		
			res.add(block);
		}			  
									  
		return res;
	
	}
	
	public AddTutorBlockResponse addTutorBlock(TutorBlockPostObject tutor) {
	
		Entity en = new Entity("TutorBlock");
		en.setProperty("name", tutor.getName());
		en.setProperty("startDate", tutor.getStartDate());
		en.setProperty("endDate", tutor.getEndDate());
		en.setProperty("startTime", tutor.getStartTime());
		en.setProperty("endTime", tutor.getEndTime());
		en.setProperty("location", tutor.getLocation());
		String[] parts = tutor.getSubjects().split(", ");
		
		en.setProperty("subject", Arrays.asList(parts));
		
		try {
		
			datastore.put(en);
			
		} catch (Exception e) {
			 AddTutorBlockResponse res = new AddTutorBlockResponse();
			 res.setMessage("Operation Failed!");
			 return res;
		} finally {
			AddTutorBlockResponse res = new AddTutorBlockResponse();
			 res.setMessage("Operation Succeeded!");
			 return res;
		}
		
		
	
	
	
	}


}
