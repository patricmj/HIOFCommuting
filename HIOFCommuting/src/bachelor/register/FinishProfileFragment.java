package bachelor.register;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;
import bachelor.database.HandleUsers;
import bachelor.objects.User;
import bachelor.util.Util;

import com.bachelor.hiofcommuting.MainActivity;
import com.bachelor.hiofcommuting.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class FinishProfileFragment extends Fragment {
	
	ImageView choosenPic;
	private Spinner institutionSpinner, campusSpinner, departmentSpinner, studySpinner, startingyearSpinner;
	private Button finishButton;
	boolean userHaveCar = false;
	boolean readConditions = false;
	boolean facebookUser = false;
	ToggleButton carQstButton, readConditionsToggleButton;
	EditText addressEditText, postalCodeEditText;
	String address, postalCode, institution, campus, department, study, startingYear;
	ArrayList<String> finishProfileData = new ArrayList<String>();
	String fbFirstName;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_finish_profile, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		campusSpinner = (Spinner) getView().findViewById(R.id.campusSpinner);
		departmentSpinner = (Spinner) getView().findViewById(R.id.departmentSpinner);
		studySpinner = (Spinner) getView().findViewById(R.id.studySpinner);
		startingyearSpinner = (Spinner) getView().findViewById(R.id.startingyearSpinner);
		choosenPic = (ImageView) getView().findViewById(R.id.choosenPictureView);
		readConditionsToggleButton = (ToggleButton) getView().findViewById(R.id.readConditionsToggleButton);
		finishButton = (Button) getView().findViewById(R.id.finishbtn);
		carQstButton = (ToggleButton) getView().findViewById(R.id.carqstToggleButton);
		addressEditText = (EditText) getView().findViewById(R.id.address);
		postalCodeEditText = (EditText) getView().findViewById(R.id.postal);
		addOnClickListeners();
		getInstitutionData();
		addDataToStartingYearSpinner();	
	}
	
	public void makeMeRequest(final Session session) {
		// Make an API call to get user data and define a
		// new callback to handle the response.
		Request request = Request.newMeRequest(session,
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						// If the response is successful
						if (session == Session.getActiveSession()) {
							if (user != null) {
								fbFirstName = user.getFirstName();
								navigateToMap();
							}
						}
					}
				});
		request.executeAsync();
	}
	
	public ArrayList<String> getFinishProfileList() {
		return finishProfileData;
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
	}
	
	public void navigateToMap() {
		User user = createUserObject();
		Intent intent = new Intent(getActivity(), bachelor.tab.TabListenerActivity.class);
		intent.putExtra("CURRENT_USER", user);
		//if(profilePic != null)
			//intent.putExtra("PROFILE_PIC", profilePic);
		if(facebookUser) {
			Session session;
			session = ((MainActivity)getActivity()).getFacebookSession();
			intent.putExtra("FACEBOOK_SESSION", session);
		}
		startActivity(intent);
		getActivity().finish();
	}
	
	public User createUserObject() {
		String firstName;
		if(!facebookUser) {
			ArrayList<String>registerData = ((EmailLoginActivity)getActivity()).getRegistrationList();
			firstName = registerData.get(0);
		}
		else {
			firstName = fbFirstName;
		}
		int userid = 10;
		int postalCode = Integer.parseInt(finishProfileData.get(1));
		double[] latlon = HandleUsers.getLatLon(getActivity().getApplicationContext(), finishProfileData.get(0), postalCode);
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

	public void addDataToStartingYearSpinner() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		List<Integer> startingYearList = new ArrayList<Integer>();
		for(int i = 0; i < 6; i++) {
			startingYearList.add(year-i);
		}
		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, startingYearList);
		startingyearSpinner.setAdapter(adapter);
	}
	
	public void getInstitutionData() {
		List<String> institutionList = new ArrayList<String>();
		institutionList.add("HiØ");
		institutionList.add("HiSF");
		addItemsOnSpinner(institutionList); 
	}
	
	public void addItemsOnSpinner(List<String> institutionList) {
		institutionSpinner = (Spinner) getView().findViewById(R.id.institutionSpinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, institutionList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		institutionSpinner.setAdapter(adapter);
		addInstitutionSpinnerListener();
		addCampusSpinnerListener();
		addDepartmentSpinnerListener();
	}
	
	public void addOnClickListeners() {
		//CarQuestion
		carQstButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				boolean on = ((ToggleButton) view).isChecked();

				if (on) {
					userHaveCar = true;
				} else {
					userHaveCar = false;
				}
			}
		});
		//ReadConditions
		readConditionsToggleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				boolean on = ((ToggleButton) view).isChecked();

				if (on) {
					readConditions = true;
				} else {
					readConditions = false;
				}
			}
		});
		//FinishButton
		finishButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					String car;
					String conditions;
					address = addressEditText.getText().toString();
					postalCode = postalCodeEditText.getText().toString();
					institution = String.valueOf(institutionSpinner.getSelectedItem());
					campus = String.valueOf(campusSpinner.getSelectedItem());
					department = String.valueOf(departmentSpinner.getSelectedItem());
					study = String.valueOf(studySpinner.getSelectedItem());
					startingYear = String.valueOf(startingyearSpinner.getSelectedItem());
					String activity = getActivity().toString();
					if(activity.startsWith("com.bachelor.hiofcommuting.MainActivity")){
						Toast.makeText(getActivity(), "Facebook", Toast.LENGTH_LONG).show();
						facebookUser = true;
						Session session;
						session = ((MainActivity)getActivity()).getFacebookSession();
						makeMeRequest(session);
					}
					if(activity.startsWith("bachelor.register.EmailLoginActivity")){
						Toast.makeText(getActivity(), "Email", Toast.LENGTH_LONG).show();
					}
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
					
					Toast.makeText(getActivity(), 
							"OnClickListener : " + 
							"\n Institusjon : " + institution +
							"\n Studiested : " + campus + 
							"\n Avdeling : " + department +
							"\n Studie : " + study + 
							"\n Kull : " + startingYear +
							"\n Bil? : " + car +
							"\n Betingelser godkjent? : " + conditions 
							, Toast.LENGTH_SHORT).show();
					if(readConditions) {
						setFinishProfileList(address, postalCode, institution, campus, department, study, startingYear, userHaveCar);
						if(!facebookUser){
							navigateToMap();
						}
					}
					else {
						//Toast.makeText(getActivity().getApplicationContext(), "Du må lese og godta betingelser for å fortsette", Toast.LENGTH_SHORT).show();
					}
				}
		});
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

}
