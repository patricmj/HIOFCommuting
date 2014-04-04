package bachelor.chat;

import java.util.List;

import bachelor.database.HandleMessages;
import bachelor.objects.Conversation;
import bachelor.objects.User;
import bachelor.tab.TabListenerActivity;

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
import android.widget.ListView;

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
		System.out.println("Fragment works!");
		userLoggedIn = ((ChatActivity)getActivity()).getUserLoggedIn();
		userToChatWith = ((ChatActivity)getActivity()).getUserToChatWith();
		new GetMessages().execute();
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
			System.out.println("DoInBackground triggered");
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
				chatView = (ListView)getView().findViewById(R.id.fragment_listview_chat);
				
				// Create a list adapter
				ChatArrayAdapter adapter = new ChatArrayAdapter(getActivity(), userLoggedIn, userToChatWith, result);
				
				// Set list adapter for the ListView
				chatView.setAdapter(adapter);
			}
			Dialog.dismiss();
		}
	}
}
