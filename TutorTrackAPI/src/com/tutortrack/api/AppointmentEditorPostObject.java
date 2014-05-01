package com.tutortrack.api;

public class AppointmentEditorPostObject {
	
	StudentAppointmentPostObject orig;
	public StudentAppointmentPostObject getOrig() {
		return orig;
	}
	public StudentAppointmentPostObject getEdited() {
		return edited;
	}
	public void setOrig(StudentAppointmentPostObject orig) {
		this.orig = orig;
	}
	public void setEdited(StudentAppointmentPostObject edited) {
		this.edited = edited;
	}
	StudentAppointmentPostObject edited;
	
	AppointmentEditorPostObject() {}

}
