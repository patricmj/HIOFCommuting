package bachelor.tab;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import bachelor.chat.ChatActivity;
import bachelor.database.HandleMessages;
import bachelor.database.HandleUsers;
import bachelor.objects.Filter;
import bachelor.objects.Inbox;
import bachelor.objects.User;


import bachelor.util.HTTPClient;
import bachelor.util.ImageHandler;
import com.bachelor.hiofcommuting.R;

public class TabInboxFragment extends Fragment {

	private User userLoggedIn;
	public ListView inboxItems;
    private Filter filter;
	
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
        filter = ((TabListenerActivity)getActivity()).getFilter();
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
			try{
                if ((User.userList != null && ImageHandler.isUserProfilePictureSet() && !Filter.isFilterSet && !User.isUserListFiltered) ||
                        (User.userList != null && ImageHandler.isUserProfilePictureSet() && Filter.isFilterSet && User.isUserListFiltered)) {
                	Filter f = null;
                    User.userList = HandleUsers.getAllUsers(getActivity(), userLoggedIn, f);        
                	newMessage = HandleMessages.getInbox(userLoggedIn.getUserid(), User.userList);
                }
                else {
                	Filter f = null;
                    User.userList = HandleUsers.getAllUsers(getActivity(), userLoggedIn, f);
                    
                    // Saving images to cache, setting path to user objects
                    for (final User user : User.userList) {

                        Bitmap bitmap;

                        if (user.getFbId().equals("None"))
                            bitmap = HTTPClient.getProfilePicturesFromServer("email", user.getPhotoUrl(), false);
                        else
                            bitmap = HTTPClient.getProfilePicturesFromServer("facebook", user.getFbId(), true);

                        String imagePath = ImageHandler.saveBitmapToCache(getActivity(), bitmap, user.getUserid());
                        user.setImagePath(imagePath);
                    }
                    newMessage = HandleMessages.getInbox(userLoggedIn.getUserid(), User.userList);
                }

                if (Filter.isFilterSet)
                    User.isUserListFiltered = true;
                if (User.isUserListFiltered && !Filter.isFilterSet)
                    User.isUserListFiltered = false;
                resetFilter();
                
                return newMessage;
			}catch(NullPointerException e){
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<Inbox> result) {
			if(result != null){
				// Get a ListView from main view
                if (getView() != null) {
                    inboxItems = (ListView) getView().findViewById(R.id.listView_inbox_message);

                    // Create a list adapter
                    //CustomListView adapter = new CustomListView(getActivity(), result);
                    CustomInboxListView adapter = new CustomInboxListView(getActivity(), result);

                    // Set list adapter for the ListView
                    inboxItems.setAdapter(adapter);
                    asyncTaskIsDone(result);
                    Dialog.dismiss();
                }
			}
            else {
                Toast.makeText(getActivity().getApplicationContext(), "Tom innboks", Toast.LENGTH_SHORT).show();
            }
		}

		private void resetFilter() {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					User.userList = HandleUsers.getAllUsers(getActivity(), userLoggedIn, filter);
				}
			});

			t.start();
		}
	}

}