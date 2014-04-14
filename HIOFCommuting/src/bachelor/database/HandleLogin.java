package bachelor.database;

import java.util.Random;

import org.json.JSONObject;

import bachelor.objects.User;

public class HandleLogin {

	// SJEKK OM EPOST FINNES I BRUKERDATABASEN (tabell for alternative brukere) test
	public static boolean checkEmail(String str) {
		String email = "martino@hiof.no";
		if (email.equals(str))
			return true;
		return false;
	}

	// SJEKK AT PASSORD STEMMER OVERENS MED EPOST (tabell for alternative
	// brukere)test commment
	public static boolean checkPassword(String email, String password) {
		String un = "martino@hiof.no";
		String pw = "passord";
		if (password.equals(pw) && email.equals(un))
			return true;
		return false;
	}
	
	public static User getCurrentEmailUserLoggedIn(String epost){
		User userLoggedIn = null;
		try{
			//get user from database (email_user), where email = epost
			userLoggedIn = new User(4, 2, "Patrick", "Jansen", 59.272950, 11.046229, 0, "Hi�", "Remmen", "IT", "Informatikk", 2011, true);
		}catch(Exception e){
			userLoggedIn = null;
		}
		return userLoggedIn;
	}
	
	public static User getCurrentFacebookUserLoggedIn(String facebookid, JSONObject obj){
		User userLoggedIn;
		try{
			System.out.println("facebookid " + facebookid);/*
			public User(int userid, int studyid, String firstname, String surname, double lat, double lon, double distance,
					String institution, String campus, String department,
					String study, int startingYear, boolean car) {*/
			///if get userid from database (facebook_user), where facebookid = facebookid
			userLoggedIn = new User(100, 7, "Patrick", "Jansen", 59.272950, 11.046229, 0, "H�gskolen i �stfold", "Remmen", "IT", "Informatikk", 2011, true);
			//userLoggedIn = new User(1, "Martin", 59.249620, 11.183409, 0, "H�gskolen i �stfold", "Remmen", "IT", "Informatikk", 2011, true);
			//else if there is no user in database (not yet registered), return null
			//userLoggedIn = null;
			return userLoggedIn;
		}catch(Exception e){
			return null;
		}
	}

	// SEND EPOST TIL BRUKEREN FOR TILBAKESTILLING AV PASSORD
	public static boolean resetPassword(String str) {
		// Random tall 0 eller 1 for � illustrere random feilmelding. Byttes ut
		// med fail/false for sending av epost
		Random r = new Random();
		int error;
		error = r.nextInt(1);

		if (error == 0) {
			return true;
		} else if (error == 1) {
			return false;
		}
		return false;
	}
	
	
}
