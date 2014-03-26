package bachelor.register;

import java.util.ArrayList;
import java.util.List;

import com.bachelor.hiofcommuting.MainActivity;
import com.bachelor.hiofcommuting.R;

import android.app.ActionBar.TabListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import bachelor.database.HandleLogin;
import bachelor.tab.TabList;


public class EmailLoginActivity extends FragmentActivity implements OnItemSelectedListener {
	
	protected int forsok = 5;
	FragmentManager fm = getSupportFragmentManager();
	FragmentTransaction transaction = fm.beginTransaction();
	private static final int LOGIN = 0;
	private static final int REGISTER = 1;
	private static final int FORGOTPW = 2;
	private static final int FINISH = 3;
	private Fragment[] fragments = new Fragment[4];
	TextView response;
	private Spinner institutionSpinner, campusSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		if (savedInstanceState == null) {
			fm = getSupportFragmentManager();
			transaction = fm.beginTransaction();
			//transaction.add(R.id.container, new PlaceholderFragment());

			fragments[LOGIN] = fm.findFragmentById(R.id.loginFragment);
			fragments[REGISTER] = fm.findFragmentById(R.id.registerFragment);
			fragments[FORGOTPW] = fm.findFragmentById(R.id.forgotPwFragment);
			fragments[FINISH] = fm.findFragmentById(R.id.finishProfileFragment);
			for (int i = 0; i < fragments.length; i++) {
				transaction.hide(fragments[i]);
			}
			transaction.commit();
			showFragment2(LOGIN, false);
			response = (TextView)findViewById(R.id.resetPasswordResponse);
			getInstitutionData();
			getCampusData();
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
		if (id == R.id.newActivity){
			Intent intent = new Intent(this, bachelor.tab.TabListener.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	//Overrider backbutton
	@Override
	public void onBackPressed() {
	    fm = getSupportFragmentManager();
	        
	    if(fragments[REGISTER].isVisible() || fragments[FORGOTPW].isVisible() ) {
	    	showFragment2(LOGIN, false);
	    } 
	    if(fragments[FINISH].isVisible()) {
	    	showFragment2(REGISTER, false);
	    } 
	    if(fragments[LOGIN].isVisible()) {
	    	super.onBackPressed(); //Oppf¿rer seg som normalt
	    } 
	}

	// BRUKEREN TRYKKER PÅ KNAPPEN: LOGG INN
	public void buttonLogin(View view) {
		// Henter brukerinput
		String epost = ((EditText) findViewById(R.id.login_editText_email))
				.getText().toString();
		String passord = ((EditText) findViewById(R.id.login_editText_passord))
				.getText().toString();

		// Forbereder toast-melding
		CharSequence toastMessage = null;

		// Sjekker om brukeren har fyllt inn data
		if (!epost.isEmpty() && !passord.isEmpty()) {
			// Sjekker om brukeren har prøvd å logge inn med feil passord for
			// mange ganger
			if (forsok > 0) {
				ArrayList<String> brukerInput = new ArrayList<String>();
				brukerInput.add(epost);
				brukerInput.add(passord);
				new ValiderBruker().execute(brukerInput);
			} else {
				toastMessage = "Glemt passord?";
			}
		} else {
			toastMessage = "Fyll inn brukernavn og passord";
		}

		// Skriver ut toast om noe gikk galt
		if (toastMessage != null) {
			Context context = getApplicationContext();
			int duration = Toast.LENGTH_SHORT;
			Toast.makeText(context, toastMessage, duration).show();
		}
	}

	// Når ValiderBruker-tråden er ferdig, blir denne metoden trigget
	private void ValiderBrukerFerdig(String results) {
		// Hvis brukernavn og passord stemte, logges brukeren inn
		if (results == null) {
			Intent intent = new Intent(this, bachelor.tab.TabListener.class);
			startActivity(intent);
			// avslutter denne aktiviteten, så den ikke ligger på stack
			finish();
		} else {
			Toast.makeText(getApplicationContext(), results, Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void nybruker(View view) {
		//showFragment(R.id.registerFragment);
		showFragment2(REGISTER, false);
	}

	public void glemtpassord(View view) {
		response.setText("");
		showFragment2(FORGOTPW, false);
		//showFragment(R.id.forgotPwFragment);
	}
	
	public void resetPasswordClicked(View view) {
		//todo : send mail til hiof bruker med tilbakestilling...
		response.setText("Du har faatt en mail for tilbakestilling av passord");
	}
	
	public void chooseProfilePic(View view) {
		//todo : velg profilbilde fra album
	}
	
	public void finishProfile(View view) {
		showFragment2(FINISH, false);
	}
	
	public void getInstitutionData() {
		List<String> institutionList = new ArrayList<String>();
		institutionList.add("HiØ");
		institutionList.add("HiB");
		institutionList.add("HiT");
		addItemsOnSpinners(institutionList); 
	}
	
	public void getCampusData() {
		List<String> campusList = new ArrayList<String>();
		campusList.add("R");
		campusList.add("T");
		campusSpinner = (Spinner) findViewById(R.id.campusSpinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, campusList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		campusSpinner.setAdapter(adapter);
	}
	
	public void addItemsOnSpinners(List institutionList) {
		institutionSpinner = (Spinner) findViewById(R.id.institutionSpinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, institutionList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		institutionSpinner.setAdapter(adapter);
		//
		addListener();
	}
	
	public void addListener() {
		institutionSpinner.setOnItemSelectedListener(this);
	}
	
	public void finishButtonClicked(View view) {
		Toast.makeText(EmailLoginActivity.this, 
				"OnClickListener : " + 
				"\n Spinner1 : " + String.valueOf(institutionSpinner.getSelectedItem()), Toast.LENGTH_SHORT).show();
	}
	
	private void showFragment2(int fragmentIndex, boolean addToBackStack) {
		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			if (i == fragmentIndex) {
				transaction.show(fragments[i]);
			} else {
				transaction.hide(fragments[i]);
			}
		}
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}
	
	private void showFragment(int fragmentId) {
		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();
		Fragment container = fm.findFragmentById(R.id.container);
		transaction.hide(container);
		Fragment fragment = fm.findFragmentById(fragmentId);
		transaction.show(fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	/**
	 * 
	 * @author Martin Validerer brukerinput opp mot database. Kjøres i AsyncTask
	 *         da dette er en tyngre oppgave.
	 */
	private class ValiderBruker extends
			AsyncTask<ArrayList<String>, Void, String> {

		private ProgressDialog Dialog = new ProgressDialog(
				EmailLoginActivity.this);

		@Override
		protected void onPreExecute() {
			Dialog.setMessage("Logger inn...");
			Dialog.show();
		}

		@Override
		protected String doInBackground(ArrayList<String>... params) {
			String epost = params[0].get(0);
			String passord = params[0].get(1);

			// Sjekker om eposten ligger i systemet
			if (HandleLogin.checkEmail(epost)) {
				// Sjekker om passordet matcher eposten
				if (HandleLogin.checkPassord(epost, passord)) {
					// Brukeren logget inn
					return null;
				} else {
					return "Feil brukernavn/passord. " + (--forsok)
							+ " forsøk igjen.";
				}
			} else {
				return "Fant ingen bruker med angitt epost i systemet";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			EmailLoginActivity.this.ValiderBrukerFerdig(result);
			Dialog.dismiss();
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		String item = parent.getItemAtPosition(pos).toString();
		int itemId = (int) parent.getItemIdAtPosition(pos);
		
		/*Toast.makeText(this, 
				"OnItemSelectedListener : " + itemId,
				Toast.LENGTH_SHORT).show();*/
		
		if(itemId == 0) {
			List<String> campusList = new ArrayList<String>();
			campusList.add("Remmen");
			campusList.add("Kraakeroy");
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_item, campusList);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			campusSpinner.setAdapter(adapter);
		}
		if(itemId == 1) {
			List<String> campusList = new ArrayList<String>();
			campusList.add("1 - H");
			campusList.add("2 - H");
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_item, campusList);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			campusSpinner.setAdapter(adapter);
		}
		if(itemId == 2) {
			List<String> campusList = new ArrayList<String>();
			campusList.add("1 - T");
			campusList.add("2 - T");
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_item, campusList);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			campusSpinner.setAdapter(adapter);
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	/*
	public static class PlaceholderFragment extends Fragment {



		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_login,
					container, false);
			return rootView;
		}
	}*/
}
