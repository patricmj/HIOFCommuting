package bachelor.chat;

import java.util.List;

import android.os.Handler;
import bachelor.database.HandleMessages;
import bachelor.objects.Conversation;
import bachelor.objects.User;
import bachelor.tab.TabListenerActivity;

import bachelor.util.HTTPClient;
import com.bachelor.hiofcommuting.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ChatFragment extends Fragment{

	private ListView chatView;
	private User userLoggedIn;
	private User userToChatWith;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_chat, container,
				false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		userLoggedIn = ((ChatActivity)getActivity()).getUserLoggedIn();
		userToChatWith = ((ChatActivity)getActivity()).getUserToChatWith();
		new GetMessages().execute();
		
		Button sendMsg = (Button)getView().findViewById(R.id.button_chat_sendmessage);
		sendMsg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText edittext = (EditText)getView().findViewById(R.id.edittext_chat_input);
				String message = edittext.getText().toString();
				new SendMessage().execute(message);

                Handler sleepHandler = new Handler();  // Sleeping thread so db can update, a callback should be implemented
                sleepHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new GetMessages().execute();
                    }
                }, 250);
			}
		});
	}
	

	
	private class GetMessages extends AsyncTask<Void, Void, List<Conversation>> {
		
		private ProgressDialog Dialog = new ProgressDialog(getActivity());
		@Override
	    protected void onPreExecute(){
			Dialog.setMessage("Laster..");
	       Dialog.show();
	    }
		
		@Override
		protected List<Conversation> doInBackground(Void... params) {
			List<Conversation> chat;
			try {
				chat = HandleMessages.getConversation(userLoggedIn, userToChatWith);
				return chat;
			} catch (Exception e) {
				Log.e("ITCRssReader", e.getMessage());
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<Conversation> result) {
			if(result!=null){
				// Get a ListView from main view
				chatView = (ListView)getView().findViewById(R.id.listview_chat);
				
				// Create a list adapter
				ChatArrayAdapter adapter = new ChatArrayAdapter(getActivity(), userLoggedIn, userToChatWith, result);
				
				// Set list adapter for the ListView
				chatView.setAdapter(adapter);
			}
			Dialog.dismiss();
		}
	}
	
	private class SendMessage extends AsyncTask<String, Void, Boolean> {
		
		@Override
		protected Boolean doInBackground(String... params) {
			String message = params[0].toString();
			try{
                HandleMessages.sendMessage(userLoggedIn, userToChatWith, message);

                if (HTTPClient.sent)
                    return true;
                else
                    return false;

			}catch(NullPointerException e){
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result){
			if(result){
				EditText et = (EditText)getView().findViewById(R.id.edittext_chat_input);
				et.setText("");
			}
		}

	}
}
