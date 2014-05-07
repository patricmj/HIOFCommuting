package com.bachelor.hiofcommuting;

import java.text.DecimalFormat;

import bachelor.chat.ChatActivity;
import bachelor.database.HandleUsers;
import bachelor.objects.User;
import bachelor.tab.TabListenerActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class UserInformationActivity extends Activity {
	private TextView lv;
	private User selectedUser;
	private User userLoggedIn;
	private ImageView profilePic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_information);
		
		profilePic = (ImageView) findViewById(R.id.imageView_profilePicture);

		userLoggedIn = (User)getIntent().getSerializableExtra("CURRENT_USER");
		
		//Mottar valgt user-objekt fra forrige activity
		selectedUser = (User)getIntent().getSerializableExtra("SELECTED_USER");

        Bitmap bitmapImage = null;
        String imageID = "";

        // Checking if the selected user is a facebook user

        if (selectedUser.getFbId().equals("None")){
            imageID = selectedUser.getPhotoUrl();
            bitmapImage = HandleUsers.getProfilePicture(imageID);
        }
        else{
            imageID = selectedUser.getFbId();
            bitmapImage = HandleUsers.getProfilePicFromFb(imageID, true);
        }

        // Assigning a profile picture if it exists

		if(bitmapImage != null)
			profilePic.setImageBitmap(bitmapImage);
		else {
			profilePic.setImageResource(R.drawable.profile_picture_test);
		}
		
		//Setter tittel på activity til navnet på user-objekt
		setTitle(selectedUser.getFirstName());
		
		//Setter avstand til brukeren
		lv = (TextView)findViewById(R.id.textView_distance);
		DecimalFormat df = new DecimalFormat("0.0");
		String avstand = df.format(selectedUser.getDistance());
		lv.setText("Avstand: "+avstand+"km");
		
		//Setter avdeling til brukeren
		lv = (TextView)findViewById(R.id.textView_department);
		lv.setText("Avdeling: "+selectedUser.getDepartment());
	
		//Setter studie til brukeren
		lv = (TextView)findViewById(R.id.textView_study);
		lv.setText("Studie: "+selectedUser.getStudy());
		
		//Setter kull til brukeren
		lv = (TextView)findViewById(R.id.textView_startingYear);
		lv.setText("Kull: "+selectedUser.getStartingYear());
		
		//Setter om brukeren har bil
		lv = (TextView)findViewById(R.id.textView_car);
		if(selectedUser.hasCar()){
			lv.setText("Bil: Ja");
		}else{
			lv.setText("Bil: Nei");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);//sett inn R.menu.user_information, menu
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
	
	public void startChatClick(View view){
		Intent intent = new Intent(this, ChatActivity.class);
		intent.putExtra("SELECTED_USER", selectedUser);
		intent.putExtra("CURRENT_USER", userLoggedIn);
		startActivity(intent);
	}
}
