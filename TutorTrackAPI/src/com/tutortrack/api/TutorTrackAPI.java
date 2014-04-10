package com.tutortrack.api;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Named;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.DefaultValue;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

@Api(name = "tutortrack", version = "v1", scopes = { Constants.EMAIL_SCOPE })
public class TutorTrackAPI {

	public static DatastoreService datastore = DatastoreServiceFactory
			.getDatastoreService();

	@ApiMethod(name = "students.getinfo", httpMethod = "get", path = "students/myInfo")
	public Student getStudentInfo(@Named("email") String email,
			@Named("password") String password) {
		return Student.getStudentFromDatabase(email, password);
	}

	@ApiMethod(name = "students.create", httpMethod = "post", path = "students/register")
	public Student registerStudent(Student new_student_info) {
		if (Student.addStudentToDatabase(new_student_info)) {
			return new_student_info;
		} else {
			return new Student();
		}
	}

	@ApiMethod(name = "tutors.getinfo", httpMethod = "get", path = "tutors/myInfo")
	public Tutor getTutorInfo(@Named("email") String email,
			@Named("password") String password) {
		return Tutor.getTutorFromDatabase(email, password);
	}

	@ApiMethod(name = "tutors.create", httpMethod = "post", path = "tutors/register")
	public Tutor registerTutor(Tutor new_tutor_info) {
		if (Tutor.addTutorToDatabase(new_tutor_info)) {
			return new_tutor_info;
		} else {
			return new Tutor();
		}
	}

	@ApiMethod(name = "admins.getinfo", httpMethod = "get", path = "admins/myInfo")
	public Admin getAdminInfo(@Named("email") String email,
			@Named("password") String password) {
		return Admin.getAdminFromDatabase(email, password);
	}

	@ApiMethod(name = "admins.create", httpMethod = "post", path = "admins/register")
	public Admin registerAdmin(Admin new_admin_info) {
		if (Admin.addAdminToDatabase(new_admin_info)) {
			return new_admin_info;
		} else {
			return new Admin();
		}
	}

	@ApiMethod(name = "tutors.searchbyname", httpMethod = "get", path = "tutors/searchByName")
	public Tutor getTutor(@Named("tutor_name") String tutor_name) {

		return Tutor.searchDatabaseForName(tutor_name);

	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@ApiMethod(name = "tutors.search", httpMethod = "get", path = "tutors/search")
	public List<TutorBlockPostObject> searchTutors(
			@Named("location") @DefaultValue String location,
			@Named("subject") @DefaultValue String subject) {

		List<TutorBlockPostObject> res = new LinkedList<TutorBlockPostObject>();

		Query q = new Query("TutorBlock");

		if (!location.equals("")) {
			q.addFilter("location", Query.FilterOperator.EQUAL, location);
		}

		if (!subject.equals("")) {
			q.addFilter("subject", Query.FilterOperator.EQUAL, subject);
		}

		q.addSort("tutor_email", SortDirection.ASCENDING);

		for (Entity e : datastore.prepare(q).asIterable()) {

			List<String> subjects = (List<String>) e.getProperty("subject");

			TutorBlockPostObject block = new TutorBlockPostObject();
			block.setTutor(getTutorInfo((String) e.getProperty("tutor_email"), (String) e.getProperty("tutor_password")));
			block.setStartDate((String) e.getProperty("startDate"));
			block.setEndDate((String) e.getProperty("endDate"));
			block.setStartTime((String) e.getProperty("startTime"));
			block.setEndTime((String) e.getProperty("endTime"));
			block.setLocation((String) e.getProperty("location"));

			String sub = "";

			for (String s : subjects) {
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
		en.setProperty("tutor_email", tutor.getTutor().getEmail());
		en.setProperty("tutor_password", tutor.getTutor().getPassword());
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
		}

		AddTutorBlockResponse res = new AddTutorBlockResponse();
		res.setMessage("Operation Succeeded!");
		return res;

	}
	
	/**
	 * Make appointment with tutor. Must be logged in.
	 * @param apptPost
	 * @return
	 */

	@ApiMethod(name = "appointments.make_appointment", httpMethod = "post", path = "appointments/makeAppointmentWithTutor")
	public AddTutorBlockResponse makeAppointmentWithTutor(
			StudentAppointmentPostObject apptPost) {
		String email = apptPost.getStudentEmail();
		String pass = apptPost.getStudentPassword();

		Entity en = new Entity("StudentAppointment");
		en.setProperty("email", email);
		en.setProperty("password", pass);
		en.setProperty("appointment_date", apptPost.getDate());
		en.setProperty("appointment_time", apptPost.getTime());
		en.setProperty("location", apptPost.getLocation());
		String[] parts = apptPost.getSubjects().split(", ");

		en.setProperty("subject", Arrays.asList(parts));
		en.setProperty("tutor_email", apptPost.getTutor().getEmail());
		en.setProperty("tutor_password", apptPost.getTutor().getPassword());

		try {

			datastore.put(en);

		} catch (Exception e) {

			AddTutorBlockResponse res = new AddTutorBlockResponse();
			res.setMessage("Operation Failed!");
			return res;

		}

		AddTutorBlockResponse res = new AddTutorBlockResponse();
		res.setMessage("Operation Succeeded!");
		return res;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@ApiMethod(name = "appointments.getappointments.student", httpMethod = "get", path = "appointments/getStudentAppointments")
	public List<StudentAppointmentPostObject> getStudentAppointments(
			@Named("email") String email, @Named("password") String password) {

		List<StudentAppointmentPostObject> res = new LinkedList<StudentAppointmentPostObject>();

		Query q = new Query("StudentAppointment").addFilter("email",
				Query.FilterOperator.EQUAL, email).addFilter("password",
				Query.FilterOperator.EQUAL, password);

		q.addSort("tutor_email", SortDirection.ASCENDING);

		for (Entity e : datastore.prepare(q).asIterable()) {

			List<String> subjects = (List<String>) e.getProperty("subject");

			StudentAppointmentPostObject block = new StudentAppointmentPostObject();
			block.setTutor(this.getTutorInfo((String) e.getProperty("tutor_email"), (String) e.getProperty("tutor_password"))); 
			block.setLocation((String) e.getProperty("location"));
			block.setStudentEmail(email);
			block.setStudentPassword(password);

			String sub = "";

			for (String s : subjects) {
				sub += s + ", ";
			}

			sub = sub.substring(0, sub.lastIndexOf(", "));

			block.setSubjects(sub);

			block.setDate((String) e.getProperty("appointment_date"));
			block.setTime((String) e.getProperty("appointment_time"));

			res.add(block);
		}

		return res;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@ApiMethod(name = "appointments.getappointments.tutor", httpMethod = "get", path = "appointments/getTutorAppointments")
	public List<StudentAppointmentPostObject> getTutorAppointments(
			@Named("tutor_email") String tutor_email, @Named("tutor_password") String tutor_password) {

		List<StudentAppointmentPostObject> res = new LinkedList<StudentAppointmentPostObject>();

		Query q = new Query("StudentAppointment").addFilter("tutor_email",
				Query.FilterOperator.EQUAL, tutor_email).addFilter("tutor_password",
				Query.FilterOperator.EQUAL, tutor_password);

		q.addSort("email", SortDirection.ASCENDING);

		for (Entity e : datastore.prepare(q).asIterable()) {

			List<String> subjects = (List<String>) e.getProperty("subject");

			StudentAppointmentPostObject block = new StudentAppointmentPostObject();
			block.setTutor(this.getTutorInfo((String) e.getProperty("tutor_email"), (String) e.getProperty("tutor_password"))); 
			block.setLocation((String) e.getProperty("location"));
			block.setStudentEmail((String) e.getProperty("email"));
			block.setStudentPassword((String) e.getProperty("password"));

			String sub = "";

			for (String s : subjects) {
				sub += s + ", ";
			}

			sub = sub.substring(0, sub.lastIndexOf(", "));

			block.setSubjects(sub);

			block.setDate((String) e.getProperty("appointment_date"));
			block.setTime((String) e.getProperty("appointment_time"));

			res.add(block);
		}

		return res;
	}

}
