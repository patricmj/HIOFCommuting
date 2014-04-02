package bachelor.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bachelor.user.EmailUser;
import bachelor.user.User;

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
	
	public static List<EmailUser> getCurrentUserLoggedIn(String epost){
		List<EmailUser> userLoggedIn = new ArrayList<EmailUser>();
		try{
			//get user from database (email_user), where email = epost
			userLoggedIn.add(new EmailUser(1, "Martin", 59.249620, 11.183409, 0, "Høgskolen i Østfold", "Remmen", "IT", "Informatikk", 2011, true, "martino@hiof.no", "password"));
		}catch(Exception e){
			userLoggedIn = null;
		}
		System.out.println(userLoggedIn.get(0).getFirstName());
		return userLoggedIn;
	}

	// SEND EPOST TIL BRUKEREN FOR TILBAKESTILLING AV PASSORD
	public static boolean resetPassword(String str) {
		// Random tall 0 eller 1 for å illustrere random feilmelding. Byttes ut
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
