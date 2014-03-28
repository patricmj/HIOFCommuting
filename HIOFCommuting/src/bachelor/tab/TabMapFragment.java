package bachelor.tab;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import bachelor.database.HandleUsers;
import bachelor.user.User;

import com.bachelor.hiofcommuting.R;
import com.bachelor.hiofcommuting.UserInformationActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class TabMapFragment extends Fragment implements OnInfoWindowClickListener{

	private GoogleMap googleMap;
	private SupportMapFragment fragment;
	private HashMap <String, User> hashMap = new HashMap <String, User>();
	private MenuItem settings;
	private LayoutInflater inflater;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
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
		googleMap.setOnInfoWindowClickListener(this);
		googleMap.setInfoWindowAdapter(new InfoWindowAdapter(){
			@Override
			public View getInfoWindow(Marker arg0){
				return null;
			}
			
			@Override
			public View getInfoContents(Marker arg0){
				View v = inflater.inflate(R.layout.tab_map_custominfowindow, null);
				String fornavn = hashMap.get(arg0.getId()).getFornavn();
				DecimalFormat df = new DecimalFormat("0.0");
				String avstand = df.format(hashMap.get(arg0.getId()).getAvstand());
				String avdeling = hashMap.get(arg0.getId()).getAvdeling();
				String institusjon = hashMap.get(arg0.getId()).getInstitusjon();
				
				ImageView profilbilde = (ImageView) v.findViewById(R.id.imageView_tabMap_profilbilde);
				TextView view1 = (TextView)v.findViewById(R.id.textView_tabMap_navn);
				TextView view2 = (TextView)v.findViewById(R.id.textView_tabMap_avstand);
				TextView view3 = (TextView)v.findViewById(R.id.textView_tabMap_avdeling);
				
				profilbilde.setImageResource(R.drawable.com_facebook_profile_default_icon);
				view1.setText(fornavn);
				
				view2.setText("Bor "+avstand+"km vekk fra din adresse");
				view3.setText("Studerer pÂ "+avdeling+" ved "+institusjon);
				return v;
			
			}
		});
		
		LatLng Hiÿ = new LatLng(59.129443, 11.352908);
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Hiÿ, 8));
		new HentBrukere().execute();
		
		
		
		// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		// check if map is created successfully or not hehe
		if (googleMap == null) {
			Toast.makeText(getActivity().getApplicationContext(),
					"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onInfoWindowClick(Marker arg0) {
		User valgtBruker = hashMap.get(arg0.getId());
		
		Intent intent = new Intent(getActivity(), UserInformationActivity.class);
		intent.putExtra("bruker", valgtBruker);
		startActivity(intent);
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
				userList = HandleUsers.getAllUsers(getActivity());
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
				DecimalFormat df = new DecimalFormat("0.0");
				String avstand = df.format(result.get(i).getAvstand());
				LatLng pos = new LatLng(lat, lon);
				Marker marker = googleMap.addMarker(new MarkerOptions().title(fornavn)
						.snippet("Bor "+avstand+"km fra din adresse").position(pos));
				hashMap.put(marker.getId(), result.get(i));
			}
			Dialog.dismiss();
		}
	}
}
