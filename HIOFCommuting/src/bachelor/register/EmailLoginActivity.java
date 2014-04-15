package bachelor.register;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
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
	Bitmap profilePic = null;
	
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
	
	//Trenger vi meny med settings etc når man skal logge inn?
	/*
	@Override
<<<<<<< HEAD
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
	*/
	
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
		Util.showFragment(FINISH, fm,fragments, "Fullf�r profil", weakActivity);
	}
	
	public ArrayList<String> getRegistrationList() {
		return registerData;
	}
	
	public void newUserClicked(View view) {
		Util.showFragment(REGISTER, fm, fragments, "Ny bruker", weakActivity);
	}

	public void forgotPasswordClicked(View view) {
		Util.showFragment(FORGOTPW, fm, fragments, "Glemt passord", weakActivity);
	}
}
