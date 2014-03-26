package bachelor.register;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bachelor.hiofcommuting.R;

public class FinishProfileFragment extends Fragment {
	
	private Spinner institutionSpinner;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_finish_profile, container, false);
	}
	
	/*@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		List<String> institutionList = new ArrayList<String>();
		institutionList.add("Hiÿ");
		institutionList.add("HiB");
		institutionList.add("HiT");
		addItemsOnSpinners(institutionList); //error inflating class fragment
		super.onActivityCreated(savedInstanceState);
	}

	public void addItemsOnSpinners(List list) {
		institutionSpinner = (Spinner) getView().findViewById(R.id.institutionSpinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		institutionSpinner.setAdapter(adapter);
	}
	
	public void finishButtonClicked(View view) {
		Toast.makeText(getActivity(), 
				"OnClickListener : " + 
				"\n Spinner1 : " + String.valueOf(institutionSpinner.getSelectedItem()), Toast.LENGTH_SHORT);
	}
	*/
}
