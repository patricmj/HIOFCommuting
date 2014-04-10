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
import bachelor.register.EmailLoginActivity;

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
    
    public static void insertEmailUser(User user, ArrayList<String> registerData) {
    	final String URL = "http://frigg.hiof.no/bo14-g23/py/regusr.py?q=";
    	HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);
                
        String q = "emailUser";
      	int sid = user.getStudyid();
        String fname = user.getFirstName();
        String sname = user.getSurname();
        double lon = user.getLon();
        double lat = user.getLat();
        String car;
    	String email = registerData.get(2);
    	String pw = registerData.get(3);
        if(user.hasCar()){
        	car = "true";
        }
        else {
        	car = "false";
        }

        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(9);
        nameValuePairs.add(new BasicNameValuePair("q", q));
        nameValuePairs.add(new BasicNameValuePair("sid", String.valueOf(sid)));
        nameValuePairs.add(new BasicNameValuePair("fname", String.valueOf(fname)));
        nameValuePairs.add(new BasicNameValuePair("sname", String.valueOf(sname)));
        nameValuePairs.add(new BasicNameValuePair("lon", String.valueOf(lon)));
        nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(lat)));
        nameValuePairs.add(new BasicNameValuePair("car", String.valueOf(car)));
        nameValuePairs.add(new BasicNameValuePair("email", "testfra@kode7"));
        nameValuePairs.add(new BasicNameValuePair("pw", "passordet7"));
        
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse httpResponse = httpClient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
            	sent = true;
            }
            System.out.println("sendt " + sent);

        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void insertFacebookUser(User user, ArrayList<String> fbData) {
    	final String URL = "";
    	HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);
                
        String q = "emailUser";
      	int sid = user.getStudyid();
        String fname = user.getFirstName();
        String sname = user.getSurname();
        double lon = user.getLon();
        double lat = user.getLat();
        String car;
    	String email;
    	String pw;
        if(user.hasCar()){
        	car = "true";
        }
        else {
        	car = "false";
        }

        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(9);
        nameValuePairs.add(new BasicNameValuePair("q", q));
        nameValuePairs.add(new BasicNameValuePair("sid", String.valueOf(sid)));
        nameValuePairs.add(new BasicNameValuePair("fname", String.valueOf(fname)));
        nameValuePairs.add(new BasicNameValuePair("sname", String.valueOf(sname)));
        nameValuePairs.add(new BasicNameValuePair("lon", String.valueOf(lon)));
        nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(lat)));
        nameValuePairs.add(new BasicNameValuePair("car", String.valueOf(car)));
        nameValuePairs.add(new BasicNameValuePair("email", "testfra@kode7"));
        nameValuePairs.add(new BasicNameValuePair("pw", "passordet7"));
        
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse httpResponse = httpClient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
            	sent = true;
            }
            System.out.println("sendt " + sent);

        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
