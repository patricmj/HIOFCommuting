package com.bachelor.hiofcommuting;

import bachelor.user.User;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;


public class UserInformationActivity extends Activity {
	private TextView lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_information);

		//Mottar valgt user-objekt fra forrige activity
		User valgtBruker = (User)getIntent().getSerializableExtra("bruker");
		
		//Setter tittel på activity til navnet på user-objekt
		setTitle(valgtBruker.getFornavn());
		
		//Setter avstand til brukeren
		lv = (TextView)findViewById(R.id.textView_avstand);
		lv.setText("Avstand: "+valgtBruker.getAvstand()+"");
		
		//Setter avdeling til brukeren
		lv = (TextView)findViewById(R.id.textView_avdeling);
		lv.setText("Avdeling: "+valgtBruker.getAvdeling());
	
		//Setter studie til brukeren
		lv = (TextView)findViewById(R.id.textView_studie);
		lv.setText("Studie: "+valgtBruker.getStudie());
		
		//Setter kull til brukeren
		lv = (TextView)findViewById(R.id.textView_kull);
		lv.setText("Kull: "+valgtBruker.getKull());
		
		//Setter om brukeren har bil
		lv = (TextView)findViewById(R.id.textView_bil);
		if(valgtBruker.isBil()){
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
}
