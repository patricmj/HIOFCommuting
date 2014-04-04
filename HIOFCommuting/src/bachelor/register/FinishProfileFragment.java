package bachelor.register;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

import com.bachelor.hiofcommuting.R;

public class FinishProfileFragment extends Fragment {
	
	ImageView choosenPic;
	private Spinner institutionSpinner, campusSpinner, departmentSpinner, studySpinner, startingyearSpinner;
	private Button finishButton;
	boolean userHaveCar = false;
	boolean readConditions = false;
	ToggleButton carQstButton, readConditionsToggleButton;
	EditText addressEditText, postalCodeEditText;
	String address, postalCode, institution, campus, department, study, startingYear;
	
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
		institutionList.add("Hi�");
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
						((EmailLoginActivity)getActivity()).setFinishProfileList(address, postalCode, institution, campus, department, startingYear, userHaveCar);
					}
					else {
						Toast.makeText(getActivity().getApplicationContext(), "Du m� lese og godta betingelser for � fortsette", Toast.LENGTH_SHORT).show();
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
				case 0:// Hi�
					campusList.add("Halden");
					campusList.add("Fredrikstad");

					campusSpinner.setAdapter(adapter);
					break;

				case 1:// HiSF
					campusList.add("Sogndal");
					campusList.add("F�rde");

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

}
