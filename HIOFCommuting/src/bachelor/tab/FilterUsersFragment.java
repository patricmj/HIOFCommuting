package bachelor.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.bachelor.hiofcommuting.R;

public class FilterUsersFragment extends Fragment {

	private Button finishButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_filter_users, container,
				false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		NumberPicker np = (NumberPicker)getView().findViewById(R.id.numberpicker_filter_distance);
		np.setMinValue(1);
		np.setMaxValue(100);
        np.setFocusable(true);
        np.setFocusableInTouchMode(true);
	}
}
