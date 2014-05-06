package bachelor.tab;

import java.text.DecimalFormat;
import java.util.List;

import com.bachelor.hiofcommuting.R;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;

import bachelor.database.HandleUsers;
import bachelor.objects.User;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListListView extends ArrayAdapter<User>{
	private final Context context;
	private final List<User> userObjects;
	Session session = null;
	private String fbId = "";

	public CustomListListView(Context context, List<User> userObjects) {
		super(context, R.layout.tab_list_customrow, userObjects);
		this.context = context;
		this.userObjects = userObjects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.tab_list_customrow, parent, false);
		
		ImageView profilePic = (ImageView) rowView.findViewById(R.id.imageView_tabList_profilePic);
		TextView nameTxt = (TextView) rowView.findViewById(R.id.textView_tabList_name);
		TextView distanceTxt = (TextView) rowView.findViewById(R.id.textView_tabList_distance);
		TextView departmentTxt = (TextView) rowView.findViewById(R.id.textView_tabList_department);
		
		//String urlExtension = userObjects.get(position).getFirstName() + userObjects.get(position).getLat() + userObjects.get(position).getLon();
		String urlExtension = userObjects.get(position).getPhotoUrl();
		System.out.println("Extension : " + urlExtension);
		Bitmap profilePicture = HandleUsers.getProfilePicture(urlExtension);
		if(profilePicture != null)
			profilePic.setImageBitmap(profilePicture);
		else {
			//urlExtension = userObjects.get(position).getFbId();
			urlExtension = "730075401";
			profilePicture = HandleUsers.getProfilePicFromFb(urlExtension);
			profilePic.setImageBitmap(profilePicture);
		}
		if (profilePicture == null) {
			profilePic.setImageResource(R.drawable.profile_picture_test);
		}
		
		nameTxt.setText(userObjects.get(position).getFirstName());
		DecimalFormat df = new DecimalFormat("0.0");
		String formattedDistance = df.format(userObjects.get(position).getDistance());
		distanceTxt.setText("Bor "+formattedDistance+"km fra din adresse");
		departmentTxt.setText("Studerer "+userObjects.get(position).getDepartment()+" på "+userObjects.get(position).getInstitution());
	
		return rowView;
	}
}
