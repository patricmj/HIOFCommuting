package bachelor.tab;

import java.util.ArrayList;
import java.util.List;

import bachelor.database.HandleUsersInMapAndList;
import bachelor.user.User;

import com.bachelor.hiofcommuting.R;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TabList extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_tab_list, container,
				false);
		return rootView;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		new HentBrukere().execute();
	}
	
	
	private class HentBrukere extends AsyncTask<Void, Void, List<User>> {
		private ProgressDialog Dialog = new ProgressDialog(getActivity());
		@Override
	    protected void onPreExecute(){
			Dialog.setMessage("Laster..");
	       Dialog.show();
	    }
		
		@Override
		protected List<User> doInBackground(Void... params) {
			try {
				List<User> userList = new ArrayList<User>();
				userList = HandleUsersInMapAndList.getAllUsers(getActivity());
				return userList;
			} catch (Exception e) {
				Log.e("ITCRssReader", e.getMessage());
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<User> result) {
			// Get a ListView from main view
			ListView itcItems = (ListView) getView().findViewById(R.id.listview_list_users);

			// Create a list adapter
			ArrayAdapter<User> adapter = new ArrayAdapter<User>(getActivity(),
					android.R.layout.simple_list_item_1, result);
			// Set list adapter for the ListView
			itcItems.setAdapter(adapter);
			Dialog.dismiss();
		}
	}

}
