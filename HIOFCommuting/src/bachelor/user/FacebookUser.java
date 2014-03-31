package bachelor.user;

public class FacebookUser extends User {

	private String facebookid;

	public FacebookUser(String firstName, double lat, double lon, double distance,
			String institution, String campus, String department,
			String study, int startingYear, boolean car, String facebookid) {
		super(firstName, lat, lon, distance, institution, campus, department, study,
				startingYear, car);
		this.facebookid = facebookid;
	}

	public String getFacebookid() {
		return facebookid;
	}

	public void setFacebookid(String facebookid) {
		this.facebookid = facebookid;
	}
	
}
