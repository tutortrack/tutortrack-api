package com.tutortrack.api;


public class StudentAppointmentPostObject {

	String studentEmail;
	String studentPassword;
	String date, time;
	String location;
	String subjects;
	Tutor tutor;
	
	public StudentAppointmentPostObject() {}
	
	public String getStudentEmail() {return studentEmail;}
	public String getStudentPassword() {return studentPassword;}
	public String getDate() {return date;}
	public String getTime() {return time;}
	public String getLocation() {return location;}
	public String getSubjects() {return subjects;}
	public Tutor getTutor() {return tutor;}

	public void setStudentEmail(String s) {studentEmail = s;}
	public void setStudentPassword(String s) {studentPassword = s;}
	public void setDate(String s) {date = s;}
	public void setTime(String s) {time = s;}
	public void setLocation(String s) {location = s;}
	public void setSubjects(String s) {subjects = s;}
	public void setTutor(Tutor tutor) {this.tutor = tutor;}

}