package bachelor.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import bachelor.user.User;

public class HandleUsersInMapAndList {

	public static List<User> getAllUsers(Context context){
		List<User> userList = new ArrayList<User>();
		
		String[] fornavn = {"Martin", "Patrick", "Arthur", "Chris", "Lars"};
		//double[] lat = {};
		//double[] lon = {};
		//double[] avstand = {};
		String institusjon = "Hiÿ";
		String studiested = "Remmen";
		String avdeling = "IT";
		String studie = "Informatikk";
		int kull = 2011;
		boolean bil = true;
		
		
		Geocoder coder = new Geocoder(context);
		List<Address> address;
		String adresse[] = {"Bodalsvei 1", "Nye tindlundvei 24b", "Likollveien 56", "Skjebergveien 122", "Nedre langgate 89"};
		int postnr[] = {1743,1718,1781,1743,1743};
		double myLat = 59.129443;
		double myLon = 11.352908;
		for(int i = 0; i<adresse.length; i++){
			double lat = 0;
			double lon = 0;
			try {
				address = coder.getFromLocationName(adresse[i]+","+postnr[i], 1);
				
				Address location = address.get(0);
				lat = location.getLatitude();
				lon = location.getLongitude();
			} catch (IOException e) {
				e.printStackTrace();
			}
			double avstand = distFrom(myLat, myLon, lat, lon);
			userList.add(new User(fornavn[i], lat, lon, avstand, institusjon, studiested, avdeling, studie, kull, bil));
		}
		
		Collections.sort(userList, new Comparator<User>(){
		    public int compare(User s1, User s2) {
		        return Double.compare(s1.getAvstand(), s2.getAvstand());
		    }
		});
		return userList;
	}
	
	public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
	    double earthRadius = 3958.75;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    int meterConversion = 1609;

	    return (double) (dist * meterConversion)/1000;
	    }
}
