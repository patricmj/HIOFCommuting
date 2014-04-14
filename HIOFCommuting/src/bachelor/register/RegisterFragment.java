package bachelor.register;

import android.content.Intent;
import android.database.Cursor;
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
import bachelor.util.FileUploader;

import bachelor.util.UserInputValidator;
import com.bachelor.hiofcommuting.R;

public class RegisterFragment extends Fragment implements OnClickListener {

    private Button next;
    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, repeatPasswordEditText;
    private String firstName, lastName, email, password, repeatPassword, imagePath;
	private ImageView cameraLogo;
    private boolean logoIsChanged = false;
	private static final int LOAD_IMAGE_RESULTS = 1;
    private RegisterFragment fragment = this;

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
	        return inflater.inflate(R.layout.fragment_register, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		cameraLogo = (ImageView) getView().findViewById(R.id.register_cameraLogo);
        cameraLogo.setOnClickListener(this);

		next = (Button) getView().findViewById(R.id.register_next);
        next.setOnClickListener(this);

		firstNameEditText = (EditText) getView().findViewById(R.id.register_firstName);
		lastNameEditText = (EditText) getView().findViewById(R.id.register_lastName);
		emailEditText = (EditText) getView().findViewById(R.id.register_email);
		passwordEditText = (EditText) getView().findViewById(R.id.register_password);
		repeatPasswordEditText = (EditText) getView().findViewById(R.id.register_repeatPassword);
	}

	public void onClick(View view) {
		switch (view.getId()) {
            case R.id.register_cameraLogo:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, LOAD_IMAGE_RESULTS);
            break;
            case R.id.register_next:
                firstName = firstNameEditText.getText().toString().trim();
                lastName = lastNameEditText.getText().toString().trim();
                email = emailEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();
                repeatPassword = repeatPasswordEditText.getText().toString().trim();
/*
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Laster opp bilde", Toast.LENGTH_SHORT).show();
                        }
                    });
                        FileUploader.upload(fragment, imagePath);
                    }

                });
                t.start();
*/

                UserInputValidator validator = new UserInputValidator();

                if (validator.isFirstNameValid(this, firstName, firstNameEditText)
                        && validator.isLastNameValid(this, lastName, lastNameEditText)
                        && validator.isEmailValid(this, email, emailEditText)
                        && validator.isPasswordValid(this, password, passwordEditText)
                        && validator.isPasswordMatch(this, password, repeatPassword, repeatPasswordEditText)
                        && validator.isProfilePictureChanged(this, logoIsChanged, cameraLogo)) {

                    ((EmailLoginActivity) getActivity()).setRegistrationList(firstName, lastName, email, password, repeatPassword);
                }
                else
                  return;
            break;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == getActivity().RESULT_OK && data != null) {

        	Uri pickedPicture = data.getData();

        	String[] filePath = { MediaStore.Images.Media.DATA };
        	Cursor cursor = getActivity().getContentResolver().query(pickedPicture, filePath, null, null ,null);
        	cursor.moveToFirst();
        	imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            cameraLogo.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            logoIsChanged = true;
        	
        	cursor.close();
        }
    }

    public String getImagePath(){
        return imagePath;
    }
}
