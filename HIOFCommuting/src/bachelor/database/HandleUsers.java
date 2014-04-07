package bachelor.database;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import bachelor.objects.Conversation;
import bachelor.objects.User;

public class HandleUsers {
	private static List<User> userList = new ArrayList<User>();

	public static List<User> getAllUsers(Context context, User userLoggedIn) {
		if (userList.isEmpty()) {
			String urlUser = "http://frigg.hiof.no/bo14-g23/py/usr.py?q=usr";
			String urlStudy = "http://frigg.hiof.no/bo14-g23/py/study.py?q=study";
			JSONArray arrayUser = new JsonParser().getJsonArray(urlUser);
			JSONArray arrayStudy = new JsonParser().getJsonArray(urlStudy);
			for (int i = 0; i < arrayUser.length(); i++) {
				JSONObject objectUser;
				JSONObject objectStudy;
				try {
					//USER OBJECT
					objectUser = arrayUser.getJSONObject(i);
					int user_id = objectUser.getInt("user_id");
					int study_id = objectUser.getInt("study_id");
					String firstname = objectUser.getString("firstname");
					String surname = objectUser.getString("surname");
					//String point = objectUser.getString("lonlat").replace("POINT(", "").replace(")", "");
					//System.out.println(point);
					//String[] latlon = point.split(" ");
					
					//double lat = Double.parseDouble(latlon[0]);
					//double lon = Double.parseDouble(latlon[1]);
					boolean car = objectUser.getBoolean("car");
					//STUDY OBJECT
					objectStudy = arrayStudy.getJSONObject(study_id);
					String institution = objectStudy.getString("institution_name");
					String campus = objectStudy.getString("campus_name");
					String department = objectStudy.getString("department_name");
					String study = objectStudy.getString("name_of_study");
					//double distance = distFrom(userLoggedIn.getLat(), userLoggedIn.getLon(),lat, lon);
					int startingYear = objectStudy.getInt("starting_year");
					
					//ADD USER OBJECT
					//userList.add(new User(user_id, firstname, surname, lon, lat, distance, institution, campus, department, study, startingYear, car));
					userList.add(new User(user_id, firstname, surname, 59.220537, 10.934701, 0.0, institution,campus,department,study,startingYear, car));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} else {
			// Vurdere om lista b�r oppdateres
			System.out.println("userList allerede fylt opp, bruker gammel arraylist");
		}
		return userList;
	}

	public static double[] getLatLon(Context context, String address,
			int postalCode) {
		Geocoder coder = new Geocoder(context);
		List<Address> addressList;

		try {
			addressList = coder.getFromLocationName(address, 1);
			if (address == null) {
				return null;
			}
			Address location = addressList.get(0);
			double[] latLon = { location.getLatitude(), location.getLongitude() };
			return latLon;
		} catch (Exception e) {

		}
		return null;
	}

	public static double distFrom(double lat1, double lng1, double lat2,
			double lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;

		return (double) (dist * meterConversion) / 1000;
	}
}
