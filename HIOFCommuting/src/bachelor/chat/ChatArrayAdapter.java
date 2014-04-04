package bachelor.chat;

import java.text.DecimalFormat;
import java.util.List;

import com.bachelor.hiofcommuting.R;

import bachelor.objects.Conversation;
import bachelor.objects.User;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatArrayAdapter extends ArrayAdapter<Conversation> {
	private final Context context;
	private final User userLoggedIn;
	private final User userToChatWith;
	private final List<Conversation> messages;
	
	public ChatArrayAdapter(Context context, User userLoggedIn, User userToChatWith, List<Conversation> messages) {
		super(context, R.layout.chat_customrow, messages);
		this.context = context;
		this.userLoggedIn = userLoggedIn;
		this.userToChatWith = userToChatWith;
		this.messages = messages;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.chat_customrow, parent, false);
		}
		
		ImageView profilePic = (ImageView) row.findViewById(R.id.imageView_chat_profilePic);
		TextView message = (TextView) row.findViewById(R.id.textView_chat_message);
		TextView date  = (TextView) row.findViewById(R.id.textView_chat_date);
		
		profilePic.setImageResource(R.drawable.profile_picture_test);
		message.setText(messages.get(position).getMessage());
		date.setText(messages.get(position).getSent());
		
		return row;
	}
}
