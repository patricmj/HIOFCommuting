package bachelor.tab;

import com.bachelor.hiofcommuting.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabInbox extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_tab_inbox,
				container, false);
		return rootView;
	}

}