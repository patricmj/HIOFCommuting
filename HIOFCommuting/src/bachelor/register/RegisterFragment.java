package bachelor.register;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import bachelor.util.Util;

import com.bachelor.hiofcommuting.R;

public class RegisterFragment extends Fragment implements OnClickListener {
	
	ImageView cameraLogo, choosenPic;
	Button next;
	private static final int LOAD_IMAGE_RESULTS = 1;
	EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, repeatPasswordEditText;
	String firstName, lastName, email, password, repeatPassword;
	Bitmap rotatedBitmap;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
	        return inflater.inflate(R.layout.fragment_register, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		cameraLogo = (ImageView) getView().findViewById(R.id.register_cameraLogo);
		next = (Button) getView().findViewById(R.id.register_next);
		choosenPic = (ImageView) getView().findViewById(R.id.choosenPictureView);
		firstNameEditText = (EditText) getView().findViewById(R.id.register_firstName);
		lastNameEditText = (EditText) getView().findViewById(R.id.register_lastName);
		emailEditText = (EditText) getView().findViewById(R.id.register_email);
		passwordEditText = (EditText) getView().findViewById(R.id.register_password);
		repeatPasswordEditText = (EditText) getView().findViewById(R.id.register_repeatPassword);
		cameraLogo.setOnClickListener(this);
		next.setOnClickListener(this);
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
        case R.id.register_cameraLogo: 
			Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, LOAD_IMAGE_RESULTS);
        break;
        
        case R.id.register_next:
        	firstName = firstNameEditText.getText().toString();
        	lastName = lastNameEditText.getText().toString();
        	email = emailEditText.getText().toString();
        	password = passwordEditText.getText().toString();
        	repeatPassword = repeatPasswordEditText.getText().toString();
        	//boolean passwordEquals = ValidateRegistration.validatePasswords(password, repeatPassword);
        	//boolean lengthsAreOk = ValidateRegistration.validateLengths(firstName, lastName, email, password, repeatPassword);
        	boolean passwordEquals = true;
        	boolean lengthsAreOk = true;
        	if(passwordEquals && lengthsAreOk) {
        		Toast.makeText(getActivity().getApplicationContext(), "Passord er like og lengde ok" , Toast.LENGTH_SHORT).show();
        		((EmailLoginActivity)getActivity()).setRegistrationList(firstName, lastName, email, password, repeatPassword);
        	}
        	else {
        		Toast.makeText(getActivity().getApplicationContext(), "Passord er IKKE like og lengde ikke ok" , Toast.LENGTH_SHORT).show();
        	}
	        //Toast.makeText(getActivity().getApplicationContext(), firstName + " " + lastName + " " + email + " " + password + " " + repeatPassword, Toast.LENGTH_LONG).show();
	        //((EmailLoginActivity)getActivity()).changeFragment(3, "Fullfør profil");
        break;
		}
	}

	public void onActivityResult(int requestCode, int resultCode,
            Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == LOAD_IMAGE_RESULTS && resultCode == getActivity().RESULT_OK && data != null) {
        	Uri pickedPicture = data.getData();
        	String[] filePath = { MediaStore.Images.Media.DATA };
        	
        	Cursor cursor = getActivity().getContentResolver().query(pickedPicture, filePath, null, null ,null);
        	cursor.moveToFirst();
        	
        	String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
        	
        	Bitmap src = BitmapFactory.decodeFile(imagePath);
        	Bitmap scaledBitmap = Bitmap.createScaledBitmap(src, 850, 850, false);
        	rotatedBitmap = Util.rotateBitmap(imagePath, scaledBitmap);
        	
        	choosenPic.setImageBitmap(rotatedBitmap);
        	
        	cursor.close();
        }
    }
}
