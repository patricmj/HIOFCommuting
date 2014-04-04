package chat;

import bachelor.tab.TabListenerActivity;
import bachelor.user.User;

import com.bachelor.hiofcommuting.R;
import com.bachelor.hiofcommuting.R.id;
import com.bachelor.hiofcommuting.R.layout;
import com.bachelor.hiofcommuting.R.menu;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class ChatActivity extends Activity {

	private User userLoggedIn;
	private User userToChatWith;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		try{
			userLoggedIn = (User) getIntent().getSerializableExtra("CURRENT_USER");
			userToChatWith = (User) getIntent().getSerializableExtra("SELECTED_USER");
			System.out.println("Du heter "+userLoggedIn.getFirstName());
			System.out.println("Den du chatter med heter "+userToChatWith.getFirstName());
		}catch(NullPointerException e){
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_chat, container,
					false);
			return rootView;
		}
	}

}
