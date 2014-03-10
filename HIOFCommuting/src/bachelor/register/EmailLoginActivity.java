package bachelor.register;

import java.util.ArrayList;

import android.app.ActionBar.TabListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import bachelor.database.HandleLogin;
import bachelor.tab.TabList;

import com.bachelor.hiofcommuting.R;

public class EmailLoginActivity extends FragmentActivity {
	protected int forsok = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
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
			return true;
		}
		return super.onOptionsItemSelected(item);
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
		System.out.println("test nybruker text-click");
	}

	public void glemtpassord(View view) {
		System.out.println("test glemtpassord text-click");
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_login,
					container, false);
			return rootView;
		}
	}
}
