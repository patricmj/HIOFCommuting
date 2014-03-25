package bachelor.user;

public class FacebookUser extends User {

	private String facebookid;

	public FacebookUser(String fornavn, double lat, double lon, double avstand,
			String institusjon, String studiested, String avdeling,
			String studie, int kull, boolean bil, String facebookid) {
		super(fornavn, lat, lon, avstand, institusjon, studiested, avdeling, studie,
				kull, bil);
		this.facebookid = facebookid;
	}

	public String getFacebookid() {
		return facebookid;
	}

	public void setFacebookid(String facebookid) {
		this.facebookid = facebookid;
	}
	
}
