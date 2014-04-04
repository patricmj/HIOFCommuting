package bachelor.tab;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import bachelor.database.HandleMessages;
import bachelor.user.Inbox;
import bachelor.user.Message;
import bachelor.user.User;

import chat.ChatActivity;

import com.bachelor.hiofcommuting.R;
import com.bachelor.hiofcommuting.UserInformationActivity;

public class TabInboxFragment extends Fragment {

	private User userLoggedIn;
	public ListView inboxItems;
	private List<User> users;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_tab_inbox,
				container, false);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		userLoggedIn = ((TabListenerActivity)getActivity()).getUserLoggedIn();
		users = ((TabListenerActivity)getActivity()).getUsers();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		new getMessages().execute();
	}
	
	private void asyncTaskIsDone(final List<Inbox> result) {
		inboxItems.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Inbox selectedMessage = result.get(position);
				Intent intent = new Intent(getActivity(), ChatActivity.class);
				intent.putExtra("CURRENT_USER", userLoggedIn);
				intent.putExtra("SELECTED_USER", selectedMessage.getSender());
				startActivity(intent);
			}
		});
	}
	
	private class getMessages extends AsyncTask<Void, Void, List<Inbox>> {
		private ProgressDialog Dialog = new ProgressDialog(getActivity());
		private List<Inbox> inbox = null;
		private List<Inbox> newMessage = null;;
		
		@Override
	    protected void onPreExecute(){
			Dialog.setMessage("Laster..");
	       Dialog.show();
	    }
		
		@Override
		protected List<Inbox> doInBackground(Void... params) {
			newMessage = HandleMessages.getInbox(userLoggedIn.getUserid(), users);
			return newMessage;
		}

		@Override
		protected void onPostExecute(List<Inbox> result) {
			if(result!=null){
				// Get a ListView from main view
				inboxItems = (ListView) getView().findViewById(R.id.listView_inbox_message);
				
				// Create a list adapter
				//CustomListView adapter = new CustomListView(getActivity(), result);
				CustomInboxListView adapter = new CustomInboxListView(getActivity(), result);
				
				// Set list adapter for the ListView
				inboxItems.setAdapter(adapter);
				asyncTaskIsDone(result);
			}else{
				System.out.println("RETURNERER NULL?");
			}
			Dialog.dismiss();
		}
	}

}