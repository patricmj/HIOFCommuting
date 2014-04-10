package bachelor.util;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.internal.fn;

import bachelor.objects.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HTTPClient {

    public static boolean sent = false;

    public static void post(String operation, int sender, int receiver, String message){
    	final String URL = "http://frigg.hiof.no/bo14-g23/py/hcserv.py?q=";
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);

        if (operation.equals("read")){
            final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("q", operation));
            nameValuePairs.add(new BasicNameValuePair("user_id_sender", String.valueOf(sender)));
            nameValuePairs.add(new BasicNameValuePair("user_id_receiver", String.valueOf(receiver)));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);

                if (httpResponse.getStatusLine().getStatusCode() == 200)
                    sent = true;

            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("q", operation));
            nameValuePairs.add(new BasicNameValuePair("user_id_sender", String.valueOf(sender)));
            nameValuePairs.add(new BasicNameValuePair("user_id_receiver", String.valueOf(receiver)));
            nameValuePairs.add(new BasicNameValuePair("message", message));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);

                if (httpResponse.getStatusLine().getStatusCode() == 200)
                    sent = true;

            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void insertUser(User user, boolean fbUser) {
    	final String URL = "http://frigg.hiof.no/bo14-g23/py/regusr.py?q=";
    	//http://frigg.hiof.no/bo14-g23/py/regusr.py?q=emailUser&sid=6&fname=finalfinal&sname=testtest&lon=59.2987673&lat=10.925502&car=true&email=testtest@frompytet.com&pw=password234ttpy
    	HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);
        
        String q = "emailUser";
      	int sid = user.getStudyid();
        String fname = user.getFirstName();
        String sname = user.getSurname();
        double lon = user.getLon();
        double lat = user.getLat();
        int car;
        if(user.hasCar()){
        	car = 1;
        }
        else {
        	car = 0;
        }
        if(!fbUser) {
	        String email = user.getEmail();
	        //password
        }
        else {
        	
        }
        
        /*
         * sid = args.getfirst("sid", "")
			fname = args.getfirst("fname", "")
			sname = args.getfirst("sname", "")
			lon = args.getfirst("lon", "")
			lat = args.getfirst("lat", "")
			car = args.getfirst("car", "")
			
			""" Email table """
			email = args.getfirst("email", "")
			pw = args.getfirst("pw", "")
         */

    	
        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("q", String.valueOf(q)));
        nameValuePairs.add(new BasicNameValuePair("sid", String.valueOf(sid)));
        nameValuePairs.add(new BasicNameValuePair("fname", String.valueOf(fname)));
        nameValuePairs.add(new BasicNameValuePair("sname", String.valueOf(sname)));
        nameValuePairs.add(new BasicNameValuePair("lon", String.valueOf(lon)));
        nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(lat)));
        nameValuePairs.add(new BasicNameValuePair("car", String.valueOf(car)));
        nameValuePairs.add(new BasicNameValuePair("email", String.valueOf("emailfrakode")));
        nameValuePairs.add(new BasicNameValuePair("pw", String.valueOf("passordfrakode")));
        
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse httpResponse = httpClient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() == 200)
                sent = true;

        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
