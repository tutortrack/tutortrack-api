package com.tutortrack.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;

import javax.inject.Named;

/**
 * Defines v1 of a helloworld API, which provides simple "greeting" methods.
 */
@Api(
    name = "tutortrack",
    version = "v1",
    scopes = {Constants.EMAIL_SCOPE})
public class TutorTrackAPI {

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


}
