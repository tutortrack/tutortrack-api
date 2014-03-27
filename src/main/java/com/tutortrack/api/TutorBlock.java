package com.tutortrack.api;

import java.util.List;

public class TutorBlock {

	String name;
	String startDate, endDate, startTime, endTime, location;
	List<String> subjects;
	
	public String getName() { return name; }
	public void setName(String newName) { name = newName; }
	
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the subjects
	 */
	public List<String> getSubjects() {
		return subjects;
	}

	/**
	 * @param subjects the subjects to set
	 */
	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}

	public TutorBlock() {}

	/**
	 * @param startDate
	 * @param endDate
	 * @param startTime
	 * @param endTime
	 * @param location
	 * @param subjects
	 */
	public TutorBlock(String name, String startDate, String endDate, String startTime,
			String endTime, String location, List<String> subjects) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.location = location;
		this.subjects = subjects;
	}
	
	
	
	
	
	
	
	
	
	
}

