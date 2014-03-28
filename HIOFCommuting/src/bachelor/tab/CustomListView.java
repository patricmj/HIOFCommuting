package bachelor.tab;

import java.text.DecimalFormat;
import java.util.List;

import com.bachelor.hiofcommuting.R;

import bachelor.user.User;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListView extends ArrayAdapter<User>{
	private final Context context;
	private final List<User> objects;

	public CustomListView(Context context, List<User> objects) {
		super(context, R.layout.tab_list_customrow, objects);
		this.context = context;
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.tab_list_customrow, parent, false);
		
		ImageView profilbilde = (ImageView) rowView.findViewById(R.id.imageView_tabList_profilbilde);
		TextView navn = (TextView) rowView.findViewById(R.id.textView_tabList_navn);
		TextView avstand = (TextView) rowView.findViewById(R.id.textView_tabList_avstand);
		TextView avdeling = (TextView) rowView.findViewById(R.id.textView_tabList_avdeling);
		
		profilbilde.setImageResource(R.drawable.com_facebook_profile_default_icon);
		navn.setText(objects.get(position).getFornavn());
		DecimalFormat df = new DecimalFormat("0.0");
		String avstandFormatert = df.format(objects.get(position).getAvstand());
		avstand.setText("Bor "+avstandFormatert+"km fra din adresse");
		avdeling.setText("Studerer "+objects.get(position).getAvdeling()+" på "+objects.get(position).getInstitusjon());
	
		return rowView;
	}

}
