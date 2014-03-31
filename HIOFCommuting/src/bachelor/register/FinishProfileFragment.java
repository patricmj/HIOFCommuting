package bachelor.register;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bachelor.hiofcommuting.R;

public class FinishProfileFragment extends Fragment {
	
	ImageView choosenPic;
	private Spinner institutionSpinner, campusSpinner, departmentSpinner, studySpinner, startingyearSpinner;
	private CheckBox carQstCheckBox, readConditionsCheckBox;
	private Button finishButton;
	boolean userHaveCar = false;
	boolean readConditions = false;
	private static final int LOAD_IMAGE_RESULTS = 1;
	
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
		carQstCheckBox = (CheckBox) getView().findViewById(R.id.carqstCheckBox);
		readConditionsCheckBox = (CheckBox) getView().findViewById(R.id.readConditionsCheckBox);
		finishButton = (Button) getView().findViewById(R.id.finishbtn);
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
		institutionList.add("HiØ");
		institutionList.add("HiSF");
		addItemsOnSpinner(institutionList); 
	}
	
	public void addItemsOnSpinner(List institutionList) {
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
		carQstCheckBox.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
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
		});
		//ReadConditions
		readConditionsCheckBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
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
		});
		//FinishButton
		finishButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
					Toast.makeText(getActivity(), 
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
