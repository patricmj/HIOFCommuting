package bachelor.util;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HTTPClient {

    private static final String URL = "http://frigg.hiof.no/bo14-g23/py/hcserv.py?q=";
    public static boolean sent = false;

    public static void post(String operation, int sender, int receiver, String message){
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
}
