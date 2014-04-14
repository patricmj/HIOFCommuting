package bachelor.register;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import bachelor.util.UserInputValidator;
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
import bachelor.util.HTTPClient;

import com.bachelor.hiofcommuting.MainActivity;
import com.bachelor.hiofcommuting.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class FinishProfileFragment extends Fragment {
	
	ImageView choosenPic;
	private Spinner institutionSpinner, departmentSpinner, studySpinner, startingyearSpinner;
	private Button finishButton;
	boolean userHaveCar = false;
	boolean readConditions = false;
	boolean facebookUser = false;
	ToggleButton carQstButton, readConditionsToggleButton;
	EditText addressEditText, postalCodeEditText;
	String address, postalCode, institution, campus, department, study, startingYear;
	ArrayList<String> finishProfileData = new ArrayList<String>();
	String fbFirstName, fbSurName, fbId;
	private List<Institution> institutionObjects = new ArrayList<Institution>();
	private List<Department> departmentObjects = new ArrayList<Department>();
	private List<Study> studyObjects = new ArrayList<Study>();
    private FinishProfileFragment fragment = this;
    ArrayList<String>registerData;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_finish_profile, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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
								fbSurName = user.getLastName();
								fbId = user.getId();
								System.out.println("facebook id " + user.getId());
								System.out.println("facebook username " + user.getUsername());
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

		if(facebookUser) {
			HandleUsers.insertFacebookUserToDb(user, fbId);
		} 
		else {
			HandleUsers.insertEmailUserToDb(user, registerData);
		} 
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
		String firstName, surName;
		if(!facebookUser) {
			firstName = registerData.get(0);
			surName = registerData.get(1);
		}
		else {
			firstName = fbFirstName;
			surName = fbSurName;
		}
		int userid = 10;
		int postalCode = Integer.parseInt(finishProfileData.get(1));
		double[] latlon = HandleUsers.getLatLon(getActivity().getApplicationContext(), finishProfileData.get(0), postalCode);
		double lat = latlon[0];
		double lon = latlon[1];
		System.out.println("lat1 : " + lat);
		System.out.println("lon1 : " + lon);
		double distance = 0.0;
		String institution = finishProfileData.get(2);
		String campus = finishProfileData.get(3);
		String department = finishProfileData.get(4);
		String study = finishProfileData.get(5);
		int studyId = 0;
		System.out.println("studyid 1" + study);
		for(int i = 0; i < studyObjects.size(); i++){
			if(studyObjects.get(i).getStudyName().equals(study)){
				System.out.println("studyid 2" + studyObjects.get(i).getStudyName());
				studyId = studyObjects.get(i).getStudyId();
				System.out.println("studyid 3" + studyId);
			}
		}
		int startingYear = Integer.parseInt(finishProfileData.get(6));
		System.out.println("starting yr : " + startingYear);
		boolean car = false;
		if(finishProfileData.get(7).equals("Ja")){
			car = true;
		}
		return new User(userid, studyId, firstName, surName, lat, lon, distance, institution, campus, department, study, startingYear, car);
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
                address = addressEditText.getText().toString().trim();
                postalCode = postalCodeEditText.getText().toString().trim();
                institution = String.valueOf(institutionSpinner.getSelectedItem());
                department = String.valueOf(departmentSpinner.getSelectedItem());
                study = String.valueOf(studySpinner.getSelectedItem());
                startingYear = String.valueOf(startingyearSpinner.getSelectedItem());
                String activity = getActivity().toString();

                if(activity.startsWith("com.bachelor.hiofcommuting.MainActivity")){
                    facebookUser = true;
                    Session session;
                    session = ((MainActivity)getActivity()).getFacebookSession();
                    makeMeRequest(session);
                }
                if(activity.startsWith("bachelor.register.EmailLoginActivity")){
                    registerData = ((EmailLoginActivity)getActivity()).getRegistrationList();
                }

                UserInputValidator validator = new UserInputValidator();

                if (validator.isAddressValid(fragment, address, addressEditText)
                        && validator.isPostalCodeValid(fragment, postalCode, postalCodeEditText)
                        && validator.isConditionsRead(fragment, readConditions, readConditionsToggleButton))
                    setFinishProfileList(address, postalCode, institution, campus, department, study, startingYear, userHaveCar);

                if(!facebookUser)
                    navigateToMap();
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
				
				for(int i = 0; i < departmentObjects.size(); i++){
					if(departmentObjects.get(i).getInstitutionId()==currentInstitutionId){
						currentList.add(departmentObjects.get(i));
					}
				}

				departmentSpinner.setAdapter(adapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	public void addCampusSpinnerListener() {
		departmentSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
							
				campus = String.valueOf(departmentSpinner.getSelectedItem());
				int currentDepartmentId = 0;
				List<Study> currentList = new ArrayList<Study>();
				ArrayAdapter<Study> adapter = new ArrayAdapter<Study>(parentView.getContext(),android.R.layout.simple_spinner_item,currentList);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				
				for(int i = 0; i < departmentObjects.size(); i++) {
					if(departmentObjects.get(i).getDepartmentName()==campus){
						currentDepartmentId = departmentObjects.get(i).getDepartmentId();
					}
				}
				
				for(int i = 0; i < studyObjects.size(); i++){
					if(studyObjects.get(i).getDepartmentId()==currentDepartmentId){
						currentList.add(studyObjects.get(i));
					}
				}
				
				studySpinner.setAdapter(adapter);
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
