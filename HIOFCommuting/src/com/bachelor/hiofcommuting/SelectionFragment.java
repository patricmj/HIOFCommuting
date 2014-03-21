package com.bachelor.hiofcommuting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import bachelor.tab.TabListener.PlaceholderFragment;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SelectionFragment extends Fragment {

	private ProfilePictureView profilePictureView;
	private static final int REAUTH_ACTIVITY_CODE = 100;

	// private static final String TAG = "SelectionFragment";
	TextView t, name;
	private GoogleMap googleMap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.selection, container, false);

		// Find the user's profile picture custom view
		profilePictureView = (ProfilePictureView) view
				.findViewById(R.id.selection_profile_pic);
		profilePictureView.setCropped(true);

		// Check for an open session
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			// Get the user's data
			makeMeRequest(session);
		}

		try {
			// Loading map
			//initilizeMap();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return view;
	}

	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getFragmentManager()
					.findFragmentById(R.id.map2)).getMap();
			// googleMap = ((MapFragment)
			// getFragmentManager().findFragmentById(R.id.map)).getMap();
			// googleMap = ((SupportMapFragment)
			// getFragmentManager().findFragmentById(R.id.map)).getMap();
		}
		LatLng Hiÿ = new LatLng(59.129443, 11.352908);
		LatLng greaker = new LatLng(59.26789, 11.03205);

		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(greaker, 10));

		googleMap.addMarker(new MarkerOptions().title("GreÂker")
				.snippet("Et h¯l").position(greaker));

		googleMap.addMarker(new MarkerOptions().title("Hiÿ")
				.snippet("Hiÿ Halden").position(Hiÿ));

		// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		// check if map is created successfully or not
		if (googleMap == null) {
			Toast.makeText(getActivity().getApplicationContext(),
					"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
		}
	}

	private void makeMeRequest(final Session session) {
		// Make an API call to get user data and define a
		// new callback to handle the response.
		Request request = Request.newMeRequest(session,
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						// If the response is successful
						if (session == Session.getActiveSession()) {
							if (user != null) {
								// Set the id for the ProfilePictureView
								// view that in turn displays the profile
								// picture.
								profilePictureView.setProfileId(user.getId());
							}
						}
						if (response.getError() != null) {
							// Handle errors, will do so later.
						}
					}
				});
		request.executeAsync();
	}

	private void onSessionStateChange(final Session session,
			SessionState state, Exception exception) {
		if (session != null && session.isOpened()) {
			// Get the user's data.
			makeMeRequest(session);
		}
	}

	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REAUTH_ACTIVITY_CODE) {
			uiHelper.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		uiHelper.onSaveInstanceState(bundle);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

}
