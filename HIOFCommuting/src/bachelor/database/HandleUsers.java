package bachelor.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import bachelor.objects.User;

public class HandleUsers {
	private static List<User> userList = new ArrayList<User>();

	public static List<User> getAllUsers(Context context, User userLoggedIn) {
		if (userList.isEmpty()) {
			int [] userid = {5,4,3,1,2};
			String[] firstName = { "Martin", "Patrick", "Arthur", "Chris", "Lars" };
			// double[] lat = {};
			// double[] lon = {};
			// double[] avstand = {};
			String institution = "HiØ";
			String campus = "Remmen";
			String department = "IT";
			String study = "Informatikk";
			int startingYear = 2011;
			boolean car = true;

			String address[] = { "Bodalsvei 1", "Nye tindlundvei 24b",
					"Likollveien 56", "Skjebergveien 122", "Nedre langgate 89" };
			int postalCode[] = { 1743, 1718, 1781, 1743, 1743 };
			double myLat = userLoggedIn.getLat();
			double myLon = userLoggedIn.getLon();
			for (int i = 0; i < address.length; i++) {
				double[] latLon = getLatLon(context, address[i], postalCode[i]);
				double lat = latLon[0];
				double lon = latLon[1];
				
				double distance = distFrom(myLat, myLon, lat, lon);
				userList.add(new User(userid[i],firstName[i], lat, lon, distance,
						institution, campus, department, study, startingYear, car));
			}

			Collections.sort(userList, new Comparator<User>() {
				public int compare(User s1, User s2) {
					return Double.compare(s1.getDistance(), s2.getDistance());
				}
			});
			userList.remove(0);
		} else {
			// Vurdere om lista bør oppdateres
			System.out
					.println("userList allerede fylt opp, bruker gammel arraylist");
		}
		return userList;
	}
	
	public static double[] getLatLon(Context context, String address, int postalCode){
		Geocoder coder = new Geocoder(context);
		List<Address> addressList;

		try {
			addressList = coder.getFromLocationName(address,1);
		    if (address == null) {
		        return null;
		    }
		    Address location = addressList.get(0);
		    double[] latLon = {location.getLatitude(), location.getLongitude() };
		    return latLon;
		}catch(Exception e){
			
		}
		return null;
	}

	public static double distFrom(double lat1, double lng1, double lat2,double lng2) {
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
