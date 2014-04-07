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
		
		String url = "http://frigg.hiof.no/bo14-g23/py/hcserv.py?q=conversation&user_id_sender="+sender.getUserid()+"&user_id_receiver="+receiver.getUserid();
		JSONArray chatArray = new JsonParser().getJsonArray(url);
		for (int i = 0; i < chatArray.length(); i++) {
			JSONObject obj;
			try {
				obj = chatArray.getJSONObject(i);
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
				
				//TODO: Set message as read
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

		String url = "http://frigg.hiof.no/bo14-g23/py/hcserv.py?q=inbox&user_id_receiver="+ userid_receiver;
		JSONArray inboxArray = new JsonParser().getJsonArray(url);
		System.out.println("WORK!!");
		System.out.println(inboxArray.toString());
		for (int i = 0; i < inboxArray.length(); i++) {
			JSONObject obj;
			try {
				obj = inboxArray.getJSONObject(i);
				System.out.println("Still works?"+obj.getString("message"));
				int userid = obj.getInt("user_id_sender");
				User sender = null;
				for (int ix = 0; ix < users.size(); ix++) {
					if (users.get(ix).getUserid() == userid) {
						sender = users.get(ix);
					}
				}
				System.out.println(sender.getFirstName());
				//System.out.println(obj.getInt("user_id_sender")+"");
				//System.out.println(users.get(obj.getInt("user_id_sender")).getFirstName()+"");
				String message = obj.getString("message");
				String sent = obj.getString("sent");
				inbox.add(new Inbox(sender, message, sent));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		return inbox;
	}

}
