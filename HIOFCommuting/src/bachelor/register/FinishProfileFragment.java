package bachelor.register;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.AsyncTask;
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
import bachelor.database.JsonParser;
import bachelor.objects.Department;
import bachelor.objects.Institution;
import bachelor.objects.Study;
import bachelor.objects.User;

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
	private HashMap <Integer, Institution> hashMap = new HashMap <Integer, Institution>();
	private List<Institution> institutionObjects = new ArrayList<Institution>();
	private List<Department> departmentObjects = new ArrayList<Department>();
	private List<Study> studyObjects = new ArrayList<Study>();
	
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
		addDataToStartingYearSpinner();	
		new Read().execute();
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
		return new User(userid, firstName, "FIX(etternavn)", lat, lon, distance, institution, campus, department, study, startingYear, car);
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
	
	public void addItemsOnSpinner() {
		if(institutionObjects!=null){
			institutionSpinner = (Spinner) getView().findViewById(R.id.institutionSpinner);
			ArrayAdapter<Institution> adapter = new ArrayAdapter<Institution>(getActivity(), android.R.layout.simple_spinner_item, institutionObjects);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			institutionSpinner.setAdapter(adapter);
			addInstitutionSpinnerListener();
			addCampusSpinnerListener();
			addDepartmentSpinnerListener();
		}
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
						//Toast.makeText(getActivity().getApplicationContext(), "Du m� lese og godta betingelser for � fortsette", Toast.LENGTH_SHORT).show();
					}
				}
		});
	}
	
	public void addInstitutionSpinnerListener() {
		institutionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {

				institution = String.valueOf(institutionSpinner.getSelectedItem());
				int currentInstitutionId = 0;
				List<Department> currentList = new ArrayList<Department>();
				ArrayAdapter<Department> adapter = new ArrayAdapter<Department>(parentView.getContext(),android.R.layout.simple_spinner_item,currentList);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				for(int i = 0; i < institutionObjects.size(); i++) {
					if(institutionObjects.get(i).getInstitutionName()==institution){
						currentInstitutionId = institutionObjects.get(i).getInstitutionId();
					}
				}
				
				int institutionSize = institutionObjects.size();
				while (institutionSize > 0) {
					for(int i = 0; i < departmentObjects.size(); i++){
						if(departmentObjects.get(i).getInstitutionId()==currentInstitutionId){
							currentList.add(departmentObjects.get(i));
						}
					}
					institutionSize--;
				}
				
				campusSpinner.setAdapter(adapter);
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
				
				if(institutionSpinner.getSelectedItem().equals("Hi�")){
					switch (itemId) {
					case 0://Remmen
						departmentList.add("IT");
						departmentList.add("L�rerutdanning");
						departmentList.add("�konomi");
	
						departmentSpinner.setAdapter(adapter);
						break;
	
					case 1://Kraakeroy
						departmentList.add("Ingeni�r");
						departmentList.add("Helse- og sosialfag");
	
						departmentSpinner.setAdapter(adapter);
						break;
					}
				}
				else if(institutionSpinner.getSelectedItem().equals("HiSF")) {
					switch (itemId) {
					case 0://Songdal
						departmentList.add("Naturfag");
						departmentList.add("L�rerutdanning");
						departmentList.add("Samfunnsfag");
	
						departmentSpinner.setAdapter(adapter);
						break;
	
					case 1://F�rde
						departmentList.add("Helse- og sosialfag");
						departmentList.add("Ingeni�rfag");
	
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
						studyList.add("Dataingeni�r");
	
						studySpinner.setAdapter(adapter);
						break;
	
					case 1://LU
						studyList.add("Grunnskolel�rer 1-7");
						studyList.add("Grunnskolel�rer 5-10");
						studyList.add("Barnehagel�rer");
	
						studySpinner.setAdapter(adapter);
						break;
						
					case 2://�SS
						studyList.add("Revisjon og regnskap");
						studyList.add("�konomi og administrasjon");
	
						studySpinner.setAdapter(adapter);
						break;
					}
				}
				else if(campusSpinner.getSelectedItem().equals("Fredrikstad")) {
					switch (itemId) {
					case 0://Ingeni�r
						studyList.add("Byggingeni�r");
						studyList.add("Bioingeni�r");
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
	
					case 1://L�rerutdanning
						studyList.add("Grunnskolel�rer 1-7");
						studyList.add("Grunnskolel�rer 5-10");
						studyList.add("Barnehagel�rer");
	
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
				else if(campusSpinner.getSelectedItem().equals("F�rde")) {
					switch (itemId) {
					case 0://HS
						studyList.add("Barnevern");
						studyList.add("Sykepleie");
						studyList.add("Vernepleie");
	
						studySpinner.setAdapter(adapter);
						break;
	
					case 1://Ingeni�r
						studyList.add("Ingeni�r elektro - energi, elkraft og milj�");
						studyList.add("Ingeni�r elektro - automatiseringsteknikk");
	
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
	
	public class Read extends AsyncTask<Void, Integer, Boolean>{
		
		
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				JsonParser jp = new JsonParser();
				JSONArray jsonInstArr, jsonDepartmentArr, jsonStudArr;
				jsonInstArr = jp.getJsonArray("http://frigg.hiof.no/bo14-g23/py/institution.py");
				for(int i = 0; i < jsonInstArr.length(); i++) {
					institutionObjects.add(new Institution(jsonInstArr.getJSONObject(i)));
				}
				
				jsonDepartmentArr = jp.getJsonArray("http://frigg.hiof.no/bo14-g23/py/department.py");
				for(int i = 0; i < jsonDepartmentArr.length(); i++) {
					departmentObjects.add(new Department(jsonDepartmentArr.getJSONObject(i)));
				}
			
				jsonStudArr = jp.getJsonArray("http://frigg.hiof.no/bo14-g23/py/study.py?q=getAllStudies");
				for(int i = 0; i < jsonStudArr.length(); i++) {
					studyObjects.add(new Study(jsonStudArr.getJSONObject(i)));
				}
				
				return true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result){
			if(result){
				//TODO kj�r f�rste spinner herifra
				addItemsOnSpinner();
			}
		}
	}
}
