package bachelor.tab;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import bachelor.database.HandleUsersInMapAndList;
import bachelor.user.User;

import com.bachelor.hiofcommuting.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TabMap extends Fragment {

	private GoogleMap googleMap;
	private SupportMapFragment fragment;
	
	private MenuItem settings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_tab_map, container,
				false);
		return rootView;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		FragmentManager fm = getChildFragmentManager();
	    fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
	    if (fragment == null) {
	        fragment = SupportMapFragment.newInstance();
	        fm.beginTransaction().replace(R.id.map, fragment).commit();
	    }
	    else{
	    	initilizeMap();
	    	System.out.println("What");
	    	}
		/*try {
			initilizeMap();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if (googleMap == null){
			googleMap = fragment.getMap();
			googleMap.addMarker(new MarkerOptions().position(new LatLng(0,0)));
			initilizeMap();
		}
	}

	private void initilizeMap() {
		/*if (googleMap == null) {
			googleMap = ((SupportMapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
		}*/
		if (fragment == null) {
			System.out.println("fragment er null");
		}
		else {
			System.out.println("fragment er ikke null");
		}
		googleMap = fragment.getMap();
		
		LatLng Hiÿ = new LatLng(59.129443, 11.352908);
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Hiÿ, 8));
		new HentBrukere().execute();
		
		
		// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		// check if map is created successfully or not
		if (googleMap == null) {
			Toast.makeText(getActivity().getApplicationContext(),
					"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
		}
	}
	
	private class HentBrukere extends AsyncTask<Void, Void, List<User>> {
		private ProgressDialog Dialog = new ProgressDialog(getActivity());
		@Override
	    protected void onPreExecute(){
			Dialog.setMessage("Laster..");
	       Dialog.show();
	    }
		
		@Override
		protected List<User> doInBackground(Void... params) {
			try {
				List<User> userList = new ArrayList<User>();
				userList = HandleUsersInMapAndList.getAllUsers(getActivity());
				return userList;
			} catch (Exception e) {
				Log.e("ITCRssReader", e.getMessage());
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<User> result) {
			for(int i=0; i<result.size();i++){
				String fornavn = result.get(i).getFornavn();
				double lat = result.get(i).getLat();
				double lon = result.get(i).getLon();
				double avstand = result.get(i).getAvstand();
				LatLng pos = new LatLng(lat, lon);
				googleMap.addMarker(new MarkerOptions().title(fornavn)
					.snippet(avstand+"km unna").position(pos));
			}
			Dialog.dismiss();
		}
	}

}
