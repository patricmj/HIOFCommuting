package bachelor.database;

import java.util.ArrayList;
import java.util.List;
import bachelor.util.HTTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import bachelor.objects.Conversation;
import bachelor.objects.Inbox;
import bachelor.objects.User;

public class HandleMessages {

    public static int myID;
    public static int partnerID;

	public static List<Conversation> getConversation(User sender, User receiver) {
		List<Conversation> chat = new ArrayList<Conversation>();
		int user_id_sender = 0;
		int user_id_receiver = 0;

		String url = "http://frigg.hiof.no/bo14-g23/py/hcserv.py?q=conversation&user_id_sender="
				+ sender.getUserid()
				+ "&user_id_receiver="
				+ receiver.getUserid();

		try {
			JSONArray chatArray = new JsonParser().getJsonArray(url);
			for (int i = 0; i < chatArray.length(); i++) {
				JSONObject obj;
				obj = chatArray.getJSONObject(i);
				user_id_sender = obj.getInt("user_id_sender");
				user_id_receiver = obj.getInt("user_id_receiver");
                String message = obj.getString("message");
                String sent = obj.getString("sent");

                System.out.println("myID = " + myID);
                System.out.println("user_id_sender = " + user_id_sender);
                System.out.println("sender.getUserid() = " + sender.getUserid());

				User sendr = null;
				User recvr = null;
				if (sender.getUserid() == user_id_sender
						&& receiver.getUserid() == user_id_receiver) {
					sendr = sender;
					recvr = receiver;
				} else {
					sendr = receiver;
					recvr = sender;
				}

                chat.add(new Conversation(sendr, recvr, message, sent));

                if (myID != user_id_sender) // If im not the sender..
                    setMessageAsRead(user_id_sender, user_id_receiver);
            }
		} catch (JSONException e) {
			return null;
		}

		return chat;
	}
	
	public static boolean newMessage(User userLoggedIn) {

		String url = "http://frigg.hiof.no/bo14-g23/py/hcserv.py?q=newMessages&user_id_receiver="
				+ userLoggedIn.getUserid();

		//try {
			JSONArray newMessages = new JsonParser().getJsonArray(url);
			if(newMessages == null) {
				return false;
			}
			else {
				return true;
			}
			
			/*
			for (int i = 0; i < newMessages.length(); i++) {
				JSONObject obj;
				obj = newMessages.getJSONObject(i);
				int user_id_sender = obj.getInt("user_id_sender");
				int user_id_receiver = obj.getInt("user_id_receiver");
                String message = obj.getString("message");
                String sent = obj.getString("sent");
            }
		} catch (JSONException e) {
			return null;
		}*/
	}

	private static void setMessageAsRead(final int sender, final int receiver) {

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				HTTPClient.post("read", sender, receiver, "");
			}
		});

		t.start();
	}

	public static void sendMessage(final User sender, final User receiver, final String message) {

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				HTTPClient.post("send", sender.getUserid(),
						receiver.getUserid(), message);
			}
		});

		t.start();
	}

	public static List<Inbox> getInbox(int userid_receiver, List<User> users) {
		List<Inbox> inbox = new ArrayList<Inbox>();

		String url = "http://frigg.hiof.no/bo14-g23/py/hcserv.py?q=inbox&user_id_receiver="
				+ userid_receiver;
		JSONArray inboxArray = new JsonParser().getJsonArray(url);
		System.out.println("WORK!!");
		System.out.println(inboxArray.toString());
		for (int i = 0; i < inboxArray.length(); i++) {
			JSONObject obj;
			try {
				obj = inboxArray.getJSONObject(i);
				System.out.println("Still works?" + obj.getString("message"));
				int userid = obj.getInt("user_id_sender");
				User sender = null;
				for (int ix = 0; ix < users.size(); ix++) {
					if (users.get(ix).getUserid() == userid) {
						sender = users.get(ix);
					}
				}
				//System.out.println(sender.getSurname());
				// System.out.println(obj.getInt("user_id_sender")+"");
				// System.out.println(users.get(obj.getInt("user_id_sender")).getFirstName()+"");
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
