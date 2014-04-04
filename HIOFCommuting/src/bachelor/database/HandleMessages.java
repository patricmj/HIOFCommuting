package bachelor.database;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import bachelor.user.Inbox;
import bachelor.user.Message;
import bachelor.user.User;

public class HandleMessages {
	
	public static List<Inbox> getInbox(int userid_receiver, List<User> users){
		List<Inbox> inbox = new ArrayList<Inbox>();
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		try {
			response = httpclient.execute(new HttpGet(
					"http://frigg.hiof.no/bo14-g23/py/hcserv.py?q=inbox&user_id_receiver=" + userid_receiver));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();

					JSONArray arr = new JSONArray(out.toString());
					for(int i=0; i<arr.length(); i++){
						JSONObject obj = arr.getJSONObject(i);
						int userid = obj.getInt("user_id_sender");
						User sender = null;
						for (int ix=0; ix<users.size();ix++){
							if(users.get(ix).getUserid() == userid){
								sender = users.get(ix);
							}
						}
						
						//System.out.println(obj.getInt("user_id_sender")+"");
						//System.out.println(users.get(obj.getInt("user_id_sender")).getFirstName()+"");
						String message = obj.getString("message");
						String sent = obj.getString("sent");
						inbox.add(new Inbox(sender, message, sent));
					}

			} else {
				// Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
			
			
		} catch (ClientProtocolException e) {
			// TODO Handle problems..
			System.out.println("TRØBBEL"+e);
			return null;
		} catch (IOException e) {
			// TODO Handle problems..
			System.out.println("TRØBBEL"+e);
			return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("TRØBBEL"+e);
			return null;
		}
		
		return inbox;
	}
	
}
