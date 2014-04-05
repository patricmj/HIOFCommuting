package com.bachelor.hiofcommuting;

import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import bachelor.database.HandleLogin;
import bachelor.objects.User;
import bachelor.register.EmailLoginActivity;
import bachelor.util.Util;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

public class MainActivity extends FragmentActivity {
	
	private static final int SPLASH = 0;
	private static final int FINISH = 1;
	private Fragment[] fragments = new Fragment[2];
	private boolean isResumed = false;
	private MenuItem settings;
	FragmentManager fm = getSupportFragmentManager();
	WeakReference<Activity> weakActivity = new WeakReference<Activity>(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		//FragmentManager fm = getSupportFragmentManager();
		fragments[SPLASH] = fm.findFragmentById(R.id.splashFragment);
		fragments[FINISH] = fm.findFragmentById(R.id.finishProfileFragment);

		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			transaction.hide(fragments[i]);
		}
		transaction.commit();
		
		/*
		//Add code to print out the key hash
	    try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	               getPackageName(), 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {
	    	System.out.println("name not found: "+e);
	    } catch (NoSuchAlgorithmException e) {
	    	System.out.println("no such algorithm: "+e);
	    }
	    */
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		//only add the menu when the selection fragment is showing
			if (menu.size() == 0) {
				settings = menu.add(R.string.settings);
			} else {
				menu.clear();
				settings = null;
			}
		return false;
	}
	
	@Override
	public void onBackPressed() {	        
	    if(fragments[FINISH].isVisible()) {
	    	Util.showFragment(SPLASH, fm, fragments, "HIOFCommuting", weakActivity);
	    	Session session = Session.getActiveSession();
			session.closeAndClearTokenInformation();
	    } 
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		isResumed = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		// Only make changes if the activity is visible
		if (isResumed) {
			FragmentManager manager = getSupportFragmentManager();
			// Get the number of entries in the back stack
			int backStackSize = manager.getBackStackEntryCount();
			// Clear the back stack
			for (int i = 0; i < backStackSize; i++) {
				manager.popBackStack();
			}
			System.out.println("Session: "+session + "\nState: "+state);
			/*
			//AUTHENTICATE USER IN OUR DATABASE
			int facebookid = 1; //get facebook id
			new AuthenticateUser().execute(facebookid);
			*/
			
			if (state.isOpened()) {
				// If the session state is open:
				// Show the authenticated fragment
				//Util.showFragment(SELECTION, fm, fragments, "", weakActivity);
				
				//AUTHENTICATE USER IN OUR DATABASE
				int facebookid = 1; //get facebook id
				new AuthenticateUser().execute(facebookid, session);
				/*Intent intent = new Intent(this, bachelor.tab.TabListenerActivity.class);
				intent.putExtra("FACEBOOK_SESSION", session);
				startActivity(intent);
				finish();
				*/
			} else if(state.isClosed()) {
				// If the session state is closed:
				// Show the login fragment
				//showFragment(SPLASH, false);
				Util.showFragment(SPLASH,fm,fragments, "HIOFCommuting" , weakActivity);
			}
		}
	}
	
	public Session getFacebookSession() {
		Session session = Session.getActiveSession();
		return session;
	}

	public void loggInnMedEpost(View view) {
		Intent intent = new Intent(this, EmailLoginActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		Session session = Session.getActiveSession();

		if (session != null && session.isOpened()) {
			// if the session is already open,

			//AUTHENTICATE USER IN OUR DATABASE
			int facebookid = 1; //get facebook id
			new AuthenticateUser().execute(facebookid, session);
			/*
			Intent intent = new Intent(this, bachelor.tab.TabListenerActivity.class);
			intent.putExtra("SESSION", session);
			startActivity(intent);
			*/
			
		} else {
			// otherwise present the splash screen
			// and ask the person to login.
			//showFragment(SPLASH, false);
			Util.showFragment(SPLASH, fm, fragments, "HIOFCommuting", weakActivity);
		}
	}

	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private class AuthenticateUser extends AsyncTask<Object, Void, User> {

		private int facebookid;
		private Session session;
		
		@Override
		protected User doInBackground(Object... params) {
			User userLoggedIn = null;
			facebookid = (Integer)params[0];
			session = (Session)params[1];
			try{
				userLoggedIn = HandleLogin.getCurrentFacebookUserLoggedIn(facebookid);
				return userLoggedIn;
			}catch(NullPointerException e){
				//User not yet registerred in app (first time user)
				return userLoggedIn;
			}
		}
		
		@Override
		protected void onPostExecute(User result){
			if(result==null){
				//user not yet registered
				System.out.println("User er ikke registrert i systemet fra før");
				// TODO: show FinishProfileFragment (hadde vært lettere om det var en activity?)
				Util.showFragment(FINISH, fm, fragments, "Fullfør profil", weakActivity);
			}else{
				//user IS registered, send user to map
				System.out.println("User ER registrert i systemet");
				Intent intent = new Intent(MainActivity.this, bachelor.tab.TabListenerActivity.class);
				intent.putExtra("CURRENT_USER", result);
				intent.putExtra("FACEBOOK_SESSION", session);
				startActivity(intent);
				finish();
			}
		}
		
	}
}
