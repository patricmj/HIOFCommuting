package bachelor.objects;

import java.io.Serializable;

public class User implements Serializable{
	private int userId;
	private int studyId;
	private String firstname;
	private String surname;
	private double lat;
	private double lon;
	private double distance;
	private String institution;
	private String campus;
	private String department;
	private String study;
	private int startingYear;
	private boolean car;
	
	public User(int userid, int studyid, String firstname, String surname, double lat, double lon, double distance,
			String institution, String campus, String department,
			String study, int startingYear, boolean car) {
		this.userId = userid;
		this.studyId = studyid;
		this.firstname = firstname;
		this.surname = surname;
		this.lat = lat;
		this.lon = lon;
		this.distance = distance;
		this.institution = institution;
		this.campus = campus;
		this.department = department;
		this.study = study;
		this.startingYear = startingYear;
		this.car = car;
	}
	
	public int getStudyid() {
		return studyId;
	}

	public void setStudyid(int studyid) {
		this.studyId = studyid;
	}

	public String getFirstName() {
		return firstname;
	}
	public void setFirstName(String firstName) {
		this.firstname = firstName;
	}
	
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institusjon) {
		this.institution = institusjon;
	}
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getStudy() {
		return study;
	}
	public void setStudy(String study) {
		this.study = study;
	}
	public int getStartingYear() {
		return startingYear;
	}
	public void setStartingYear(int startingYear) {
		this.startingYear = startingYear;
	}
	public boolean hasCar() {
		return car;
	}
	public void setCar(boolean car) {
		this.car = car;
	}

	public int getUserid() {
		return userId;
	}

	public void setUserid(int userid) {
		this.userId = userid;
	}
}
