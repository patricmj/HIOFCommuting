package bachelor.tab;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import bachelor.database.HandleUsers;
import bachelor.objects.Filter;
import bachelor.objects.User;

import bachelor.util.HTTPClient;
import bachelor.util.ImageHandler;
import com.bachelor.hiofcommuting.R;
import com.bachelor.hiofcommuting.UserInformationActivity;

public class TabListFragment extends Fragment {
	private ListView itcItems;
	private User userLoggedIn;
	private Filter filter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_tab_list, container,
				false);
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
		new getUsers().execute();
	}

	private void setOnItemClickOnList(final List<User> result) {
		itcItems.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				User selectedUser = result.get(position);
				Intent intent = new Intent(getActivity(), UserInformationActivity.class);
				intent.putExtra("SELECTED_USER", selectedUser);
				intent.putExtra("CURRENT_USER", userLoggedIn);
				startActivity(intent);
			}
		});
	}


	private class getUsers extends AsyncTask<Void, Void, List<User>> {
		private ProgressDialog dialog = new ProgressDialog(getActivity());
		@Override
	    protected void onPreExecute(){
		    dialog.setMessage("Laster brukere..");
	        dialog.show();
	    }

		@Override
		protected List<User> doInBackground(Void... params) {
			try {
                if ((User.userList != null && ImageHandler.isUserProfilePictureSet() && !Filter.isFilterSet && !User.isUserListFiltered) ||
                        (User.userList != null && ImageHandler.isUserProfilePictureSet() && Filter.isFilterSet && User.isUserListFiltered && Filter.currentFilter == filter))
                    return User.userList;
                else {
                    User.userList = HandleUsers.getAllUsers(getActivity(), userLoggedIn, filter);

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

                    if (Filter.isFilterSet)
                        User.isUserListFiltered = true;
                    if (User.isUserListFiltered && !Filter.isFilterSet)
                        User.isUserListFiltered = false;

                    return User.userList;
                }
			} catch (NullPointerException e) {
				System.out.println("Returns null in doInBackground: TabListFragment.java");
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<User> result) {
			if(result != null){
                // Get a ListView from main view
                if (getView() != null) {
                    itcItems = (ListView) getView().findViewById(R.id.listview_list_users);

                    // Create a list adapter
                    final CustomListListView adapter = new CustomListListView(getActivity(), result);

                    // Set list adapter for the ListView
                    itcItems.setAdapter(adapter);
                    setOnItemClickOnList(result);
                    update(adapter);
                    dialog.dismiss();
                }
			}
            else {
                Toast.makeText(getActivity().getApplicationContext(), "Fant ingen brukere", Toast.LENGTH_SHORT).show();
            }
		}
	}

    private void update(final CustomListListView adapter) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        }, 2000);
    }

}
