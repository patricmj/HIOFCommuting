package bachelor.register;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import bachelor.database.HandleLogin;
import bachelor.database.JsonParser;
import bachelor.objects.User;

import com.bachelor.hiofcommuting.R;

public class LoginFragment extends Fragment {
	TextView response;
	protected int attempts = 5;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_login, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Button login = (Button)getView().findViewById(R.id.loginButton);
		login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// Henter brukerinput
        		String email = ((EditText) getView().findViewById(R.id.login_editText_email))
        						.getText().toString();
        		String password = ((EditText) getView().findViewById(R.id.login_editText_passord))
        						.getText().toString();
                validateUserInput(email, password);
            }
        });
	}

	public void validateUserInput(String email, String password) {
		//Forbereder toast-melding
		CharSequence toastMessage = null;

		// Sjekker om brukeren har fyllt inn data
		if (!email.isEmpty() && !password.isEmpty()) {
			// Sjekker om brukeren har pr�vd � logge inn med feil passord for
			// mange ganger
			if (attempts > 0) {
				String[] userInput = {email, password};
				new ValidateUser().execute(userInput);
			} else {
				toastMessage = "Glemt passord?";
			}
		} else {
			toastMessage = "Fyll inn brukernavn og passord";
		}

		// Skriver ut toast om noe gikk galt
		if (toastMessage != null) {
			Context context = getActivity().getApplicationContext();
			int duration = Toast.LENGTH_SHORT;
			Toast.makeText(context, toastMessage, duration).show();
		}
	}
	
	/**
	 * 
	 * @author Martin Validerer brukerinput opp mot database. Kj�res i AsyncTask
	 *         da dette er en tyngre oppgave.
	 */

	
	private class ValidateUser extends
			AsyncTask<String[], Void, Boolean> {

		private ProgressDialog Dialog = new ProgressDialog(getActivity());
		private String errorMessage;
		private User userLoggedIn;

		@Override
		protected void onPreExecute() {
			Dialog.setMessage("Logger inn...");
			Dialog.show();
		}

		@Override
		protected Boolean doInBackground(String[]... params) {
			String[] userInput = params[0];
			String email = userInput[0];
			String password = userInput[1];

			Boolean authenticated = HandleLogin.checkUnAndPw(email,password);
			if (authenticated) {
				userLoggedIn = HandleLogin.getCurrentEmailUserLoggedIn(email);
				return true;
			} else {
				errorMessage = "Feil brukernavn eller passord. " + (--attempts) + " fors�k igjen.";
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			//ValiderBrukerFerdig(result);
			Dialog.dismiss();
			if(result){
				Intent intent = new Intent(getActivity(), bachelor.tab.TabListenerActivity.class);
				intent.putExtra("CURRENT_USER", userLoggedIn);
				startActivity(intent);
				getActivity().finish();
			}else {
				Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
			}
		}
	}
}