package bachelor.user;

public class User {
	private String fornavn;
	private double lat;
	private double lon;
	private double avstand;
	private String institusjon;
	private String studiested;
	private String avdeling;
	private String studie;
	private int kull;
	private boolean bil;
	
	public User(String fornavn, double lat, double lon, double avstand,
			String institusjon, String studiested, String avdeling,
			String studie, int kull, boolean bil) {
		super();
		this.fornavn = fornavn;
		this.lat = lat;
		this.lon = lon;
		this.avstand = avstand;
		this.institusjon = institusjon;
		this.studiested = studiested;
		this.avdeling = avdeling;
		this.studie = studie;
		this.kull = kull;
		this.bil = bil;
	}
	
	
	
	
	@Override
	public String toString() {
		return fornavn + ", lat=" + lat + ", lon=" + lon
				+ ", avstand=" + avstand + ", institusjon=" + institusjon
				+ ", studiested=" + studiested + ", avdeling=" + avdeling
				+ ", studie=" + studie + ", kull=" + kull + ", bil=" + bil;
	}




	public String getFornavn() {
		return fornavn;
	}
	public void setFornavn(String fornavn) {
		this.fornavn = fornavn;
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
	public double getAvstand() {
		return avstand;
	}
	public void setAvstand(double avstand) {
		this.avstand = avstand;
	}
	public String getInstitusjon() {
		return institusjon;
	}
	public void setInstitusjon(String institusjon) {
		this.institusjon = institusjon;
	}
	public String getStudiested() {
		return studiested;
	}
	public void setStudiested(String studiested) {
		this.studiested = studiested;
	}
	public String getAvdeling() {
		return avdeling;
	}
	public void setAvdeling(String avdeling) {
		this.avdeling = avdeling;
	}
	public String getStudie() {
		return studie;
	}
	public void setStudie(String studie) {
		this.studie = studie;
	}
	public int getKull() {
		return kull;
	}
	public void setKull(int kull) {
		this.kull = kull;
	}
	public boolean isBil() {
		return bil;
	}
	public void setBil(boolean bil) {
		this.bil = bil;
	}
	
}
