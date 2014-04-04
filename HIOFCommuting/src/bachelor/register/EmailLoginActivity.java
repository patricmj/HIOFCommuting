package bachelor.register;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import bachelor.database.HandleUsers;
import bachelor.user.User;
import bachelor.util.Util;

import com.bachelor.hiofcommuting.R;


public class EmailLoginActivity extends FragmentActivity {
	FragmentManager fm = getSupportFragmentManager();
	FragmentTransaction transaction = fm.beginTransaction();
	private static final int LOGIN = 0;
	private static final int REGISTER = 1;
	private static final int FORGOTPW = 2;
	private static final int FINISH = 3;
	private Fragment[] fragments = new Fragment[4];
	WeakReference<Activity> weakActivity = new WeakReference<Activity>(this);
	ArrayList<String> registerData = new ArrayList<String>();
	ArrayList<String> finishProfileData = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		if (savedInstanceState == null) {
			fm = getSupportFragmentManager();
			transaction = fm.beginTransaction();

			fragments[LOGIN] = fm.findFragmentById(R.id.loginFragment);
			fragments[REGISTER] = fm.findFragmentById(R.id.registerFragment);
			fragments[FORGOTPW] = fm.findFragmentById(R.id.forgotPwFragment);
			fragments[FINISH] = fm.findFragmentById(R.id.finishProfileFragment);
			for (int i = 0; i < fragments.length; i++) {
				transaction.hide(fragments[i]);
			}
			transaction.commit();
			Util.showFragment(LOGIN,fm,fragments, "Logg inn", weakActivity);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			System.out.print("Settings clicked");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
	    if(fragments[REGISTER].isVisible() || fragments[FORGOTPW].isVisible() ) {
	    	Util.showFragment(LOGIN, fm, fragments, "Logg inn", weakActivity);
	    } 
	    if(fragments[FINISH].isVisible()) {
	    	Util.showFragment(REGISTER, fm, fragments, "Ny bruker", weakActivity);
	    } 
	    if(fragments[LOGIN].isVisible()) {
	    	super.onBackPressed();
	    } 
	}
	
	public void setRegistrationList(String firstName, String lastName, String email, String password, String repeatPassword) {
		registerData.add(firstName);
		registerData.add(lastName);
		registerData.add(email);
		registerData.add(password);
		registerData.add(repeatPassword);
		Util.showFragment(FINISH, fm,fragments, "Fullfør profil", weakActivity);
	}
	
	public void setFinishProfileList(String address, String postalCode, String institution, String campus, String department, String study, String startingYear, boolean hasCar) {
		finishProfileData.add(address);
		finishProfileData.add(postalCode);
		finishProfileData.add(institution);
		finishProfileData.add(campus);
		finishProfileData.add(department);
		finishProfileData.add(study);
		finishProfileData.add(startingYear);
		if(hasCar)
			finishProfileData.add("Ja");
		else
			finishProfileData.add("Nei");
		User user = createUserObject();
		Intent intent = new Intent(this, bachelor.tab.TabListenerActivity.class);
		intent.putExtra("CURRENT_USER", user);
		startActivity(intent);
		finish();
	}
	
	public User createUserObject() {
		//etternavn?
		//Hente userid fra database?
		int userid = 10;
		String firstName = registerData.get(0);
		int postalCode = Integer.parseInt(finishProfileData.get(1));
		double[] latlon = HandleUsers.getLatLon(this, finishProfileData.get(0), postalCode);
		double lat = latlon[0];
		double lon = latlon[1];
		double distance = 0.0;
		String institution = finishProfileData.get(2);
		String campus = finishProfileData.get(3);
		String department = finishProfileData.get(4);
		String study = finishProfileData.get(5);
		int startingYear = Integer.parseInt(finishProfileData.get(6));
		boolean car = false;
		if(finishProfileData.get(7).equals("Ja")){
			car = true;
		}
		return new User(userid, firstName, lat, lon, distance, institution, campus, department, study, startingYear, car);
	}
	
	public void newUserClicked(View view) {
		Util.showFragment(REGISTER, fm, fragments, "Ny bruker", weakActivity);
	}

	public void forgotPasswordClicked(View view) {
		Util.showFragment(FORGOTPW, fm, fragments, "Glemt passord", weakActivity);
	}
}
