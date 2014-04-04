package bachelor.database;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

import android.net.Uri;
import android.net.Uri.Builder;
import bachelor.objects.Conversation;
import bachelor.objects.Inbox;
import bachelor.objects.User;

public class HandleMessages {
	
	public static List<Conversation> getConversation(User sender, User receiver) {
		List<Conversation> chat = new ArrayList<Conversation>();
		/*
		//THIS IS ONLY STATIC DATA FOR TESTING
		String[] message = {"Halla Martin, hva skjer? Dette er Arthur speaking.", "Halla Arthur! Nei, ikke mye du, trenger litt hjelp med scriptet for det slutta brått å fungere, kunne du sette på det?", "Seff, hva er problemet?", "Nei, du skjønner det at jeg satt i MySQL Workbench og skulle endre litt på SQL-setninga så man fikk ut ALLE meldinger, ikke bare de som ikke var lest, og det FUNGERTE i mysql workbench, men når jeg kopierte setninga inn i scriptet slutta det å fungere. WHATS UP?", "2 sec skal se", "trenger mer tekst for å sjekke om scroll fungerer...", "meeeer test", "meeeeeeeeeeeeeeeeeeeeeeeer", "GREEEEEEEEEEEESS"};
		String sent = "Dato + kl her";

		for(int i=0; i<message.length; i++){
			User sendr = null;
			User recev = null;
			if(i%2==0){
				sendr = sender;
				recev = receiver;
			}else{
				sendr = receiver;
				recev = sender;
			}
			String msg = message[i].replaceAll("%20", " ");
			chat.add(new Conversation(sendr, recev, msg, sent));
		}*/
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		try {
			response = httpclient.execute(new HttpGet(
					"http://frigg.hiof.no/bo14-g23/py/hcserv.py?q=conversation&user_id_sender="+sender.getUserid()+"&user_id_receiver="+receiver.getUserid()));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				
				JSONArray arr = new JSONArray(out.toString());
				for (int i = 0; i < arr.length(); i++) {
					JSONObject obj = arr.getJSONObject(i);
					int user_id_sender = obj.getInt("user_id_sender");
					int user_id_receiver = obj.getInt("user_id_receiver");
					User sendr = null;
					User recvr = null;
					if(sender.getUserid() == user_id_sender && receiver.getUserid() == user_id_receiver){
						sendr = sender;
						recvr = receiver;
					}else{
						sendr = receiver;
						recvr = sender;
					}
					String message = obj.getString("message");
					String sent = obj.getString("sent");
					chat.add(new Conversation(sendr, recvr, message, sent));
				}
				//TODO: Set message as read
				

			} else {
				// Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}

		} catch (ClientProtocolException e) {
			// TODO Handle problems..
			System.out.println("TRØBBEL" + e);
			return null;
		} catch (IOException e) {
			// TODO Handle problems..
			System.out.println("TRØBBEL" + e);
			return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("TRØBBEL" + e);
			return null;
		}
		return chat;
	}

	public static boolean sendMessage(User sender, User receiver, String message) {
		String msg = message.replace(" ", "%20").trim();
		String url = "http://frigg.hiof.no/bo14-g23/py/hcserv.py?q=send&user_id_sender="+sender.getUserid()+"&user_id_receiver="+receiver.getUserid()+"&message="+msg;
		try {
			System.out.println("Avsender:"+sender.getFirstName()+"\nMottaker:"+receiver.getFirstName()+"\nMelding:"+msg);
		} catch (Exception e) {

		}

		return false;
	}

	public static List<Inbox> getInbox(int userid_receiver, List<User> users) {
		List<Inbox> inbox = new ArrayList<Inbox>();

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		try {
			response = httpclient.execute(new HttpGet(
					"http://frigg.hiof.no/bo14-g23/py/hcserv.py?q=inbox&user_id_receiver="
							+ userid_receiver));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();

				JSONArray arr = new JSONArray(out.toString());
				for (int i = 0; i < arr.length(); i++) {
					JSONObject obj = arr.getJSONObject(i);
					int userid = obj.getInt("user_id_sender");
					User sender = null;
					for (int ix = 0; ix < users.size(); ix++) {
						if (users.get(ix).getUserid() == userid) {
							sender = users.get(ix);
						}
					}

					// System.out.println(obj.getInt("user_id_sender")+"");
					// System.out.println(users.get(obj.getInt("user_id_sender")).getFirstName()+"");
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
			System.out.println("TRØBBEL" + e);
			return null;
		} catch (IOException e) {
			// TODO Handle problems..
			System.out.println("TRØBBEL" + e);
			return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("TRØBBEL" + e);
			return null;
		}

		return inbox;
	}

}
