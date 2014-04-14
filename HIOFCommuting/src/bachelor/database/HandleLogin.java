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
			System.out.println("lager user");
			int userId, studyId, startingYear;
			String firstname, surname, institution, campus, department, study;
			double lat, lon, distance;
			boolean car;
			userId = Integer.parseInt(obj.getString("user_id"));
			studyId = Integer.parseInt(obj.getString("study_id"));
			startingYear = Integer.parseInt(obj.getString("starting_year"));
			firstname = obj.getString("firstname");
			surname = obj.getString("surname");
			institution = obj.getString("institution_name");
			campus = obj.getString("campus_name");
			department = obj.getString("department_name");
			study = obj.getString("name_of_study");
			String point = obj.getString("latlon").replace("POINT(", "").replace(")", "");
			String[] latlon = point.split(" ");
			lat = Double.parseDouble(latlon[0]);
			lon = Double.parseDouble(latlon[1]);
			System.out.println("lat2 : " + lat);
			System.out.println("lon2 : " + lon);
			distance = 0.0;
			String carString = obj.getString("car");
			if(carString.equals("1")) {
				car = true;
			}
			else {
				car = false;
			}
			userLoggedIn = new User(userId, studyId, firstname, surname, lat, lon, distance, institution, campus, department, study, startingYear, car);
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
