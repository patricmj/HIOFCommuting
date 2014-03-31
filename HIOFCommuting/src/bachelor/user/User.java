package bachelor.user;

import java.io.Serializable;

public class User implements Serializable{
	private String firstName;
	private double lat;
	private double lon;
	private double distance;
	private String institution;
	private String campus;
	private String department;
	private String study;
	private int startingYear;
	private boolean car;
	
	public User(String firstName, double lat, double lon, double distance,
			String institution, String campus, String department,
			String study, int startingYear, boolean car) {
		super();
		this.firstName = firstName;
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

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
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
	public boolean userHasCar() {
		return car;
	}
	public void setCar(boolean car) {
		this.car = car;
	}
}
