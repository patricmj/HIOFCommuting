package bachelor.tab;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bachelor.database.HandleUsers;
import bachelor.objects.User;

import com.bachelor.hiofcommuting.MainActivity;
import com.bachelor.hiofcommuting.R;
import com.facebook.Session;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class TabListenerActivity extends FragmentActivity implements
		ActionBar.TabListener {
	
	private List<User> users;
	private User userLoggedIn;
	Session session = null;
	//Bitmap profilePic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Mottar valgt user-objekt fra forrige activity
		setUserLoggedIn((User)getIntent().getSerializableExtra("CURRENT_USER"));
		//profilePic = (Bitmap)getIntent().getParcelableExtra("PROFILE_PIC");
		setContentView(R.layout.activity_tab_listener);
		
		
		try{
			session = (Session)getIntent().getSerializableExtra("FACEBOOK_SESSION");
			System.out.println(session.getState());
			System.out.println("Logget inn med facebook");
		}catch(NullPointerException e){
			System.out.println("Logget inn med epost");
		}
		
		

		System.out.println("User = "+getUserLoggedIn().getFirstName()+
				"\nStudy = "+getUserLoggedIn().getStudy()+
				"\nInstitution = "+getUserLoggedIn().getInstitution());
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction transaction = fm.beginTransaction();
			PlaceholderFragment pf = new PlaceholderFragment();
			transaction.add(R.id.container, pf);
		}

		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// for each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_map)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_list)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_inbox)
				.setTabListener(this));
		
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		try{
			new GetUsersFromDatabase(this).execute();
		}catch(NullPointerException e){
			System.out.println("ECEPITON"+e);
		}
	}
	
	@Override
	public void onBackPressed() {
		//Do nothing
	}

	/*public Bitmap getProfilePic() {
		return profilePic;
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//menu.add(R.string.settings);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId()){
		case R.id.action_settings:
			System.out.println("Settings trykka");
			return true;
		case R.id.newActivity:
			System.out.println("Logger ut");
			performLogout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void performLogout() {
		if(session != null){
			try{
				session = Session.getActiveSession();
				session.closeAndClearTokenInformation();
			}catch(NullPointerException e){
				System.out.println("Wooops! Var ikke logga inn med facebook?");
			}
		}
		userLoggedIn = null;
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
  	}
	
	public User getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(User userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}
	

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tab_listener,
					container, false);
			return rootView;
		}
	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {

		if (tab.getPosition() == 0){
			Fragment tm = new TabMapFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_tab_container, tm).commit();
			System.out.println("Map");
		}
		if (tab.getPosition() == 1) {
			Fragment tl = new TabListFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_tab_container, tl).commit();
			System.out.println("Liste");
		}
		if (tab.getPosition() == 2) {
			Fragment ti = new TabInboxFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_tab_container, ti).commit();
			System.out.println("Inbox");
		}
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}


	//GETTER
	public List<User> getUsers() {
		return users;
	}
	//SETTER
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	//PRIVATE CLASS TO GET ALL USERS IN DATABASE
	private class GetUsersFromDatabase extends AsyncTask<Void, Void, List<User>> {
		private Context mContext;
	    public GetUsersFromDatabase (Context context){
	         mContext = context;
	    }
		
		@Override
		protected List<User> doInBackground(Void... params) {
			try {
				List<User> users = HandleUsers.getAllUsers(mContext, getUserLoggedIn());
				return users;
			} catch (Exception e) {
				Log.e("ITCRssReader", e.getMessage());
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<User> result) {
			if(result != null){
				setUsers(result);
				System.out.println("Users initialized");
			}else{
				System.out.println("Users NOT initialized");
			}
		}
	}
}
