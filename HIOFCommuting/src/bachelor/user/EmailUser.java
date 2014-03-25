package bachelor.user;

public class EmailUser extends User{
	private String epost;
	private String passord;
	
	
	public EmailUser(String fornavn, double lat, double lon, double avstand,
			String institusjon, String studiested, String avdeling,
			String studie, int kull, boolean bil, String epost, String passord) {
		super(fornavn, lat, lon, avstand, institusjon, studiested, avdeling, studie,
				kull, bil);
		this.epost = epost;
		this.passord = passord;
	}

	
	
	public String getEpost() {
		return epost;
	}
	public void setEpost(String epost) {
		this.epost = epost;
	}
	public String getPassord() {
		return passord;
	}
	public void setPassord(String passord) {
		this.passord = passord;
	}
	
	
}
