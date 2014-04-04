package bachelor.chat;

import java.text.DecimalFormat;
import java.util.List;

import com.bachelor.hiofcommuting.R;

import bachelor.objects.Conversation;
import bachelor.objects.User;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatArrayAdapter extends ArrayAdapter<Conversation> {
	private final Context context;
	private final User userLoggedIn;
	private final User userToChatWith;
	private final List<Conversation> messages;
	private LinearLayout wrapper;
	
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
		
		TextView message = (TextView) row.findViewById(R.id.textview_chat_message);
		//TextView date  = (TextView) row.findViewById(R.id.textview_chat_sent);
		
		message.setText(messages.get(position).getMessage());
		//date.setText(messages.get(position).getSent());
		
		wrapper = (LinearLayout) row.findViewById(R.id.container_chat_message);
		if(messages.get(position).getReceiver().getUserid() == userLoggedIn.getUserid()){
			message.setBackgroundResource(R.drawable.bubble_green);
			wrapper.setGravity(Gravity.RIGHT);
		}else if(messages.get(position).getSender().getUserid() == userToChatWith.getUserid()){
			message.setBackgroundResource(R.drawable.bubble_yellow);
			wrapper.setGravity(Gravity.LEFT);
		}
		
		
		return row;
	}
}
