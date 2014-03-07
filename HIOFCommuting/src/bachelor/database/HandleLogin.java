package bachelor.database;

import java.util.Random;

public class HandleLogin {

	// SJEKK OM EPOST FINNES I BRUKERDATABASEN (tabell for alternative brukere)
	public static boolean checkEmail(String str) {
		String epost = "martino@hiof.no";
		if (epost.equals(str))
			return true;
		return false;
	}

	// SJEKK AT PASSORD STEMMER OVERENS MED EPOST (tabell for alternative
	// brukere)
	public static boolean checkPassord(String epost, String passord) {
		String un = "martino@hiof.no";
		String pw = "passord";
		if (passord.equals(pw) && epost.equals(un))
			return true;
		return false;
	}

	// SEND EPOST TIL BRUKEREN FOR TILBAKESTILLING AV PASSORD
	public static boolean resetPassword(String str) {
		// Random tall 0 eller 1 for å illustrere random feilmelding. Byttes ut
		// med fail/false for sending av epost
		Random r = new Random();
		int feilmelding;
		feilmelding = r.nextInt(1);

		if (feilmelding == 0) {
			return true;
		} else if (feilmelding == 1) {
			return false;
		}
		return false;
	}
}
