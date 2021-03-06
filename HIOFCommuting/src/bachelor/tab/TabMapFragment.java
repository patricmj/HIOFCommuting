package bachelor.tab;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import bachelor.database.HandleUsers;
import bachelor.objects.Filter;
import bachelor.objects.User;

import bachelor.util.HTTPClient;
import bachelor.util.ImageHandler;
import com.bachelor.hiofcommuting.R;
import com.bachelor.hiofcommuting.UserInformationActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class TabMapFragment extends Fragment implements OnInfoWindowClickListener {

    private GoogleMap googleMap;
    private SupportMapFragment fragment;
    private HashMap<String, User> hashMap = new HashMap<String, User>();
    private LayoutInflater inflater;
    private User userLoggedIn;
    private Filter filter;


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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userLoggedIn = ((TabListenerActivity) getActivity()).getUserLoggedIn();
        filter = ((TabListenerActivity) getActivity()).getFilter();
        if (fragment == null) {
            FragmentManager fm = getChildFragmentManager();
            fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        } else {
            initilizeMap();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (googleMap == null) {
            googleMap = fragment.getMap();
            initilizeMap();
            System.out.println("Done, tabmap resumed");
        }
    }

    public void initilizeMap() {
        if (fragment == null) {
            System.out.println("fragment er null");
        } else {
            System.out.println("fragment er ikke null");
        }
        googleMap = fragment.getMap();
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker arg0) {
                if (!(hashMap.get(arg0.getId()).getUserid() == userLoggedIn.getUserid())) {
                    View view = inflater.inflate(R.layout.tab_map_custominfowindow, null);
                    String firstName = hashMap.get(arg0.getId()).getFirstName();
                    DecimalFormat df = new DecimalFormat("0.0");
                    String distance = df.format(hashMap.get(arg0.getId()).getDistance());
                    String department = hashMap.get(arg0.getId()).getDepartment();
                    String institution = hashMap.get(arg0.getId()).getInstitution();

                    ImageView profilePic = (ImageView) view.findViewById(R.id.imageView_tabMap_profilePic);
                    TextView nameTxt = (TextView) view.findViewById(R.id.textView_tabMap_name);
                    TextView distanceTxt = (TextView) view.findViewById(R.id.textView_tabMap_distance);
                    TextView departmentTxt = (TextView) view.findViewById(R.id.textView_tabMap_department);

                    final User user = hashMap.get(arg0.getId());
                    ImageHandler.setBitmapFromPath(profilePic, user.getImagePath());

                    nameTxt.setText(firstName);

                    distanceTxt.setText("Bor " + distance + " km unna din adresse");
                    departmentTxt.setText("Studerer " + department);
                    return view;
                }
                return null;

            }
        });

        LatLng user = new LatLng(userLoggedIn.getLat(), userLoggedIn.getLon());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user, 11));
        Marker userLoggedInMarker = googleMap.addMarker(new MarkerOptions()
                .title(userLoggedIn.getFirstName())
                .snippet("Her bor du!")
                .position(user)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        hashMap.put(userLoggedInMarker.getId(), userLoggedIn);

        //Fill map with users
        new GetUsers().execute();

        // check if map is created successfully
        if (googleMap == null) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {
        User selectedUser = hashMap.get(arg0.getId());
        if (!(selectedUser.getUserid() == userLoggedIn.getUserid())) {
            Intent intent = new Intent(getActivity(), UserInformationActivity.class);
            intent.putExtra("SELECTED_USER", selectedUser);
            intent.putExtra("CURRENT_USER", userLoggedIn);
            startActivity(intent);
        }
    }

    private class GetUsers extends AsyncTask<Void, Void, List<User>> {
        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Laster brukere..");
            dialog.show();
        }

        @Override
        protected List<User> doInBackground(Void... params) {
            try {

                if ((User.userList != null && ImageHandler.isUserProfilePictureSet() && !Filter.isFilterSet && !User.isUserListFiltered) ||
                        (User.userList != null && ImageHandler.isUserProfilePictureSet() && Filter.isFilterSet && User.isUserListFiltered && Filter.currentFilter == filter))
                    return User.userList;
                else {
                    User.userList = HandleUsers.getAllUsers(getActivity(), userLoggedIn, filter);

                    // Saving images to cache, setting path to user objects
                    for (final User user : User.userList) {

                        Bitmap bitmap;

                        if (user.getFbId().equals("None"))
                            bitmap = HTTPClient.getProfilePicturesFromServer("email", user.getPhotoUrl(), false);
                        else
                            bitmap = HTTPClient.getProfilePicturesFromServer("facebook", user.getFbId(), true);

                        String imagePath = ImageHandler.saveBitmapToCache(getActivity(), bitmap, user.getUserid());
                        user.setImagePath(imagePath);
                    }

                    if (Filter.isFilterSet) {
                        User.isUserListFiltered = true;
                        Filter.currentFilter = filter;
                    }
                    if (User.isUserListFiltered && !Filter.isFilterSet)
                        User.isUserListFiltered = false;

                    return User.userList;
                }
            } catch (NullPointerException e) {
                System.out.println("Returns null in doInBackground: TabMapFragment.java");
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<User> result) {
            if (result != null) {
                //Create hashmap containing markers as key and users as value
                for (int i = 0; i < result.size(); i++) {
                    String firstName = result.get(i).getFirstName();
                    double lat = result.get(i).getLat();
                    double lon = result.get(i).getLon();
                    DecimalFormat df = new DecimalFormat("0.0");
                    String distance = df.format(result.get(i).getDistance());
                    LatLng pos = new LatLng(lat, lon);
                    BitmapDescriptor icon = null;
                    if (result.get(i).hasCar()) {
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_user_car);
                    } else {
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_user_nocar);
                    }
                    Marker marker = googleMap.addMarker(new MarkerOptions().title(firstName).icon(icon)
                            .snippet("Bor " + distance + "km fra din adresse").position(pos));
                    hashMap.put(marker.getId(), result.get(i));
                }
            }
            dialog.dismiss();
        }
    }

}
