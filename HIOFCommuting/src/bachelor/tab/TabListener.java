package bachelor.tab;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bachelor.hiofcommuting.MainActivity;
import com.bachelor.hiofcommuting.R;
import com.facebook.Session;

public class TabListener extends FragmentActivity implements
		ActionBar.TabListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_listener);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// for each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_kart)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_liste)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.tab_inbox)
				.setTabListener(this));
		
		Intent i = getIntent();
		Session session = (Session)i.getSerializableExtra("FACEBOOK_SESSION");
		if(session != null && session.isOpened()){
			System.out.println("logget inn");
			FragmentManager fm = getSupportFragmentManager();
			fm.findFragmentById(R.id.userSettingsFragment);
			FragmentTransaction transaction = fm.beginTransaction();
			transaction.commit();
		}else{
			System.out.println("ikke logget inn");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.add(R.string.settings);
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
		Session session = Session.getActiveSession();
		session.closeAndClearTokenInformation();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
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
			Fragment tm = new TabMap();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_tab_container, tm).commit();
			System.out.println("Map");
		}
		if (tab.getPosition() == 1) {
			Fragment tl = new TabList();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_tab_container, tl).commit();
			System.out.println("Liste");
		}
		if (tab.getPosition() == 2) {
			Fragment ti = new TabInbox();
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

}
