package bachelor.register;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import bachelor.database.HandleLogin;
import bachelor.util.Util;

import com.bachelor.hiofcommuting.R;


public class EmailLoginActivity extends FragmentActivity {
	
	protected int attempts = 5;
	FragmentManager fm = getSupportFragmentManager();
	FragmentTransaction transaction = fm.beginTransaction();
	private static final int LOGIN = 0;
	private static final int REGISTER = 1;
	private static final int FORGOTPW = 2;
	private static final int FINISH = 3;
	private Fragment[] fragments = new Fragment[4];
	TextView response;

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
			Util.showFragment(LOGIN,fm,fragments);
			response = (TextView)findViewById(R.id.resetPasswordResponse);
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
	    fm = getSupportFragmentManager();
	        
	    if(fragments[REGISTER].isVisible() || fragments[FORGOTPW].isVisible() ) {
	    	Util.showFragment(LOGIN, fm, fragments);
	    } 
	    if(fragments[FINISH].isVisible()) {
	    	Util.showFragment(REGISTER, fm, fragments);
	    } 
	    if(fragments[LOGIN].isVisible()) {
	    	super.onBackPressed(); //Oppf¿rer seg som normalt
	    } 
	}

	// BRUKEREN TRYKKER PÅ KNAPPEN: LOGG INN
	public void buttonLogin(View view) {
		// Henter brukerinput
		String email = ((EditText) findViewById(R.id.login_editText_email))
				.getText().toString();
		String password = ((EditText) findViewById(R.id.login_editText_passord))
				.getText().toString();

		// Forbereder toast-melding
		CharSequence toastMessage = null;

		// Sjekker om brukeren har fyllt inn data
		if (!email.isEmpty() && !password.isEmpty()) {
			// Sjekker om brukeren har prøvd å logge inn med feil passord for
			// mange ganger
			if (attempts > 0) {
				ArrayList<String> userInput = new ArrayList<String>();
				userInput.add(email);
				userInput.add(password);
				new ValidateUser().execute(userInput);
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
			Intent intent = new Intent(this, bachelor.tab.TabListenerActivity.class);
			startActivity(intent);
			//avslutter denne aktiviteten, så den ikke ligger på stack
			finish();
		} else {
			Toast.makeText(getApplicationContext(), results, Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void newUserClicked(View view) {
		Util.showFragment(REGISTER, fm, fragments);
	}

	public void forgotPasswordClicked(View view) {
		response.setText("");
		Util.showFragment(FORGOTPW, fm, fragments);
	}
	
	public void resetPasswordClicked(View view) {
		//todo : send mail til hiof bruker med tilbakestilling...
		response.setText("Du har faatt en mail for tilbakestilling av passord");
	}
	
	public void finishProfileClicked(View view) {
		Util.showFragment(FINISH, fm,fragments);
	}
	

	/**
	 * 
	 * @author Martin Validerer brukerinput opp mot database. Kjøres i AsyncTask
	 *         da dette er en tyngre oppgave.
	 */
	private class ValidateUser extends
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
					return "Feil brukernavn/passord. " + (--attempts)
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
}
