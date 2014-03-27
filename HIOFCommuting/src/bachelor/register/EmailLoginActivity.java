package bachelor.register;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import bachelor.database.HandleLogin;

import com.bachelor.hiofcommuting.R;


public class EmailLoginActivity extends FragmentActivity {
	
	protected int forsok = 5;
	FragmentManager fm = getSupportFragmentManager();
	FragmentTransaction transaction = fm.beginTransaction();
	private static final int LOGIN = 0;
	private static final int REGISTER = 1;
	private static final int FORGOTPW = 2;
	private static final int FINISH = 3;
	private static final int LOAD_IMAGE_RESULTS = 1;
	private Fragment[] fragments = new Fragment[4];
	TextView response;
	ImageView choosenPic;
	private Spinner institutionSpinner, campusSpinner, departmentSpinner, studySpinner, startingyearSpinner;
	boolean userHaveCar = false;
	boolean readConditions = false;

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
			campusSpinner = (Spinner) findViewById(R.id.campusSpinner);
			departmentSpinner = (Spinner) findViewById(R.id.departmentSpinner);
			studySpinner = (Spinner) findViewById(R.id.studySpinner);
			startingyearSpinner = (Spinner) findViewById(R.id.startingyearSpinner);
			choosenPic = (ImageView) findViewById(R.id.choosenPictureView);
			getInstitutionData();
			addDataToStartingYearSpinner();
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
			Intent intent = new Intent(this, bachelor.tab.TabListenerActivity.class);
			startActivity(intent);
			//avslutter denne aktiviteten, så den ikke ligger på stack
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
		//Starter imagegalleriet
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		
		startActivityForResult(intent, LOAD_IMAGE_RESULTS);
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {
        	Uri pickedPicture = data.getData();
        	String[] filePath = { MediaStore.Images.Media.DATA };
        	
        	Cursor cursor = getContentResolver().query(pickedPicture, filePath, null, null ,null);
        	cursor.moveToFirst();
        	
        	String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
        	//choosenPic.setImageBitmap(BitmapFactory.decodeFile(imagePath));works
        	
        	
        	cursor.close();
        }
    }
	
	public void finishProfile(View view) {
		showFragment2(FINISH, false);
	}
	
	//Finishprofilefragment start
	
	public void addDataToStartingYearSpinner() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		List<Integer> startingYearList = new ArrayList<Integer>();
		for(int i = 0; i < 6; i++) {
			startingYearList.add(year-i);
		}
		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, startingYearList);
		startingyearSpinner.setAdapter(adapter);
	}
	
	public void getInstitutionData() {
		List<String> institutionList = new ArrayList<String>();
		institutionList.add("HiØ");
		institutionList.add("HiSF");
		addItemsOnSpinner(institutionList); 
	}
	
	public void addItemsOnSpinner(List institutionList) {
		institutionSpinner = (Spinner) findViewById(R.id.institutionSpinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, institutionList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		institutionSpinner.setAdapter(adapter);
		addInstitutionSpinnerListener();
		addCampusSpinnerListener();
		addDepartmentSpinnerListener();
	}
	
	public void addInstitutionSpinnerListener() {
		institutionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				int itemId = (int) parentView.getItemIdAtPosition(position);
				List<String> campusList = new ArrayList<String>();
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(parentView.getContext(),android.R.layout.simple_spinner_item,campusList);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				switch (itemId) {
				case 0:// HiØ
					campusList.add("Halden");
					campusList.add("Fredrikstad");

					campusSpinner.setAdapter(adapter);
					break;

				case 1:// HiSF
					campusList.add("Sogndal");
					campusList.add("Førde");

					campusSpinner.setAdapter(adapter);
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	public void addCampusSpinnerListener() {
		campusSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				int itemId = (int) parentView.getItemIdAtPosition(position);
				List<String> departmentList = new ArrayList<String>();
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(parentView.getContext(),android.R.layout.simple_spinner_item, departmentList);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				
				if(institutionSpinner.getSelectedItem().equals("HiØ")){
					switch (itemId) {
					case 0://Remmen
						departmentList.add("IT");
						departmentList.add("Lærerutdanning");
						departmentList.add("Økonomi");
	
						departmentSpinner.setAdapter(adapter);
						break;
	
					case 1://Kraakeroy
						departmentList.add("Ingeniør");
						departmentList.add("Helse- og sosialfag");
	
						departmentSpinner.setAdapter(adapter);
						break;
					}
				}
				else if(institutionSpinner.getSelectedItem().equals("HiSF")) {
					switch (itemId) {
					case 0://Songdal
						departmentList.add("Naturfag");
						departmentList.add("Lærerutdanning");
						departmentList.add("Samfunnsfag");
	
						departmentSpinner.setAdapter(adapter);
						break;
	
					case 1://Førde
						departmentList.add("Helse- og sosialfag");
						departmentList.add("Ingeniørfag");
	
						departmentSpinner.setAdapter(adapter);
						break;
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	public void addDepartmentSpinnerListener() {
		departmentSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				int itemId = (int) parentView.getItemIdAtPosition(position);
				List<String> studyList = new ArrayList<String>();
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(parentView.getContext(),android.R.layout.simple_spinner_item, studyList);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				
				if(campusSpinner.getSelectedItem().equals("Halden")){
					switch (itemId) {
					case 0://IT
						studyList.add("Informatikk");
						studyList.add("Digital medieproduksjon");
						studyList.add("Dataingeniør");
	
						studySpinner.setAdapter(adapter);
						break;
	
					case 1://LU
						studyList.add("Grunnskolelærer 1-7");
						studyList.add("Grunnskolelærer 5-10");
						studyList.add("Barnehagelærer");
	
						studySpinner.setAdapter(adapter);
						break;
						
					case 2://ØSS
						studyList.add("Revisjon og regnskap");
						studyList.add("Økonomi og administrasjon");
	
						studySpinner.setAdapter(adapter);
						break;
					}
				}
				else if(campusSpinner.getSelectedItem().equals("Fredrikstad")) {
					switch (itemId) {
					case 0://Ingeniør
						studyList.add("Byggingeniør");
						studyList.add("Bioingeniør");
						studyList.add("Innovasjon og prosjektledelse");
	
						studySpinner.setAdapter(adapter);
						break;
	
					case 1://HS
						studyList.add("Barnevern");
						studyList.add("Vernepleie");
	
						studySpinner.setAdapter(adapter);
						break;
					}
				}
				else if(campusSpinner.getSelectedItem().equals("Sogndal")){
					switch (itemId) {
					case 0://Naturfag
						studyList.add("Fornybar energi");
						studyList.add("Geologi og geofare");
	
						studySpinner.setAdapter(adapter);
						break;
	
					case 1://Lærerutdanning
						studyList.add("Grunnskolelærer 1-7");
						studyList.add("Grunnskolelærer 5-10");
						studyList.add("Barnehagelærer");
	
						studySpinner.setAdapter(adapter);
						break;
						
					case 2://Samfunnsfag
						studyList.add("Historie");
						studyList.add("Sosiologi");
						studyList.add("Samfunnsfag");
	
						studySpinner.setAdapter(adapter);
						break;
					}
				}
				else if(campusSpinner.getSelectedItem().equals("Førde")) {
					switch (itemId) {
					case 0://HS
						studyList.add("Barnevern");
						studyList.add("Sykepleie");
						studyList.add("Vernepleie");
	
						studySpinner.setAdapter(adapter);
						break;
	
					case 1://Ingeniør
						studyList.add("Ingeniør elektro - energi, elkraft og miljø");
						studyList.add("Ingeniør elektro - automatiseringsteknikk");
	
						studySpinner.setAdapter(adapter);
						break;
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	public void carQuestionChecked(View view) {
	    boolean checked = ((CheckBox) view).isChecked();

	    switch(view.getId()) {
	        case R.id.carqstCheckBox:
	            if (checked){
	            	userHaveCar = true;
	            }
	            else{
	            	userHaveCar = false;
	            }
	            break;
	    }
	}
	
	public void readConditionsCheckBox(View view) {
		boolean checked = ((CheckBox) view).isChecked();
		
		switch(view.getId()) {
        case R.id.readConditionsCheckBox:
            if (checked){
            	readConditions = true;
            }
            else{
            	readConditions = false;
            }
            break;
    }
	}
	
	public void finishButtonClicked(View view) {
		String car;
		String conditions;
		if(userHaveCar){
			car = "Ja";
		}
		else{
			car = "Nei";
		}
		if(readConditions){
			conditions = "Ja";
		}
		else {
			conditions = "Nei";
		}
		Toast.makeText(EmailLoginActivity.this, 
				"OnClickListener : " + 
				"\n Institusjon : " + String.valueOf(institutionSpinner.getSelectedItem()) +
				"\n Studiested : " + String.valueOf(campusSpinner.getSelectedItem()) + 
				"\n Avdeling : " + String.valueOf(departmentSpinner.getSelectedItem()) +
				"\n Studie : " + String.valueOf(studySpinner.getSelectedItem()) + 
				"\n Kull : " + String.valueOf(startingyearSpinner.getSelectedItem()) +
				"\n Bil? : " + car +
				"\n Betingelser godkjent? : " + conditions
				, Toast.LENGTH_SHORT).show();
	}
	
	//Finishprofilefragment end
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
