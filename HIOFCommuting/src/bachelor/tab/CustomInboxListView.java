package bachelor.tab;

import java.util.List;

import bachelor.util.ImageHandler;
import com.bachelor.hiofcommuting.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import bachelor.objects.Inbox;
import bachelor.objects.User;


public class CustomInboxListView extends ArrayAdapter<Inbox>{

	private final Context context;
	private final List<Inbox> objects;

	public CustomInboxListView(Context context, List<Inbox> objects) {
		super(context, R.layout.tab_inbox_customrow, objects);
		this.context = context;
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.tab_inbox_customrow, parent, false);
		
		ImageView profilePic = (ImageView) rowView.findViewById(R.id.imageView_inbox_profilePic);
		TextView name = (TextView) rowView.findViewById(R.id.textView_inbox_name);
		TextView message = (TextView) rowView.findViewById(R.id.textView_inbox_message);
		TextView date = (TextView) rowView.findViewById(R.id.textView_inbox_date);

        final User sender = objects.get(position).getSender();
        if(sender != null) {
	        System.out.println("name " + sender.getFirstName());
	        
	        ImageHandler.setBitmapFromPath(profilePic, sender.getImagePath());
	
			name.setText(objects.get(position).getSender().getFirstName());
			message.setText(objects.get(position).getMessage());
			date.setText(objects.get(position).getSent());
        }
        else 
        	System.out.println("Sender er null");
		
		return rowView;
	}

}
