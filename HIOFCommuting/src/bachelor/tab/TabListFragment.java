package bachelor.tab;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import bachelor.database.HandleUsers;
import bachelor.user.User;
import bachelor.util.Util;

import com.bachelor.hiofcommuting.R;
import com.bachelor.hiofcommuting.UserInformationActivity;

public class TabListFragment extends Fragment {
	private ListView itcItems;
	private List<User> userList = new ArrayList<User>();
	private TextView testText;
	
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
		testText = (TextView) getView().findViewById(R.id.testText);
		testText.setText("text endret");
	}

	@Override
	public void onResume(){
		super.onResume();
		new HentBrukere().execute();
	}
	
	private void asyncTaskIsDone(final List<User> result) {
		itcItems.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				User selectedUser = result.get(position);
				Intent intent = new Intent(getActivity(), UserInformationActivity.class);
				intent.putExtra("bruker", selectedUser);
				startActivity(intent);
			}
		});
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
				userList = HandleUsers.getAllUsers(getActivity());
				return userList;
			} catch (Exception e) {
				Log.e("ITCRssReader", e.getMessage());
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<User> result) {
			if(result!=null){
				// Get a ListView from main view
				itcItems = (ListView) getView().findViewById(R.id.listview_list_users);
				
				// Create a list adapter
				CustomListView adapter = new CustomListView(getActivity(), result);
				
				// Set list adapter for the ListView
				itcItems.setAdapter(adapter);
				asyncTaskIsDone(result);
			}
			Dialog.dismiss();
		}
	}

}
