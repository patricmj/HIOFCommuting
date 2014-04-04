package bachelor.chat;

import java.util.List;

import bachelor.database.HandleMessages;
import bachelor.database.HandleUsers;
import bachelor.objects.Conversation;
import bachelor.objects.User;
import bachelor.tab.CustomListListView;
import bachelor.tab.TabListenerActivity;
import bachelor.tab.TabListenerActivity.PlaceholderFragment;

import com.bachelor.hiofcommuting.R;
import com.bachelor.hiofcommuting.R.id;
import com.bachelor.hiofcommuting.R.layout;
import com.bachelor.hiofcommuting.R.menu;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class ChatActivity extends FragmentActivity {

	private User userLoggedIn;
	private User userToChatWith;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new ChatFragment()).commit();
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction transaction = fm.beginTransaction();
			ChatFragment pf = new ChatFragment();
			transaction.add(R.id.container, pf);
		}
		
		try{
			setUserLoggedIn((User) getIntent().getSerializableExtra("CURRENT_USER"));
			setUserToChatWith((User) getIntent().getSerializableExtra("SELECTED_USER"));
			System.out.println("Du heter "+userLoggedIn.getFirstName());
			System.out.println("Du chatter med "+userToChatWith.getFirstName());
			setTitle(userToChatWith.getFirstName()+"");
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

	public User getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(User userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

	public User getUserToChatWith() {
		return userToChatWith;
	}

	public void setUserToChatWith(User userToChatWith) {
		this.userToChatWith = userToChatWith;
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
