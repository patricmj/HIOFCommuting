package bachelor.user;

import java.io.Serializable;

public class EmailUser extends User implements Serializable{
	private String email;
	private String password;
	
	
	public EmailUser(int userid, String firstName, double lat, double lon, double distance,
			String institution, String campus, String department,
			String study, int startingYear, boolean car, String email, String password) {
		super(userid, firstName, lat, lon, distance, institution, campus, department, study,
				startingYear, car);
		this.email = email;
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
