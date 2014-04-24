package bachelor.register;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import bachelor.util.UserInputValidator;
import bachelor.util.Util;

import com.bachelor.hiofcommuting.R;

import java.io.*;

public class RegisterFragment extends Fragment implements OnClickListener {

    private Button next;
    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, repeatPasswordEditText;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String repeatPassword;
    private static String imagePath;
    private static String finalImagePath;
    private static Bitmap bitmap;
    private ImageView cameraLogo;
    private boolean logoIsChanged = false;
    private static final int LOAD_IMAGE_RESULTS = 1;
    public RegisterFragment fragment = this;


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

                UserInputValidator validator = new UserInputValidator();

                if (validator.isFirstNameValid(this, firstName, firstNameEditText)
                        && validator.isLastNameValid(this, lastName, lastNameEditText)
                        && validator.isEmailValid(this, email, emailEditText)
                        && validator.isPasswordValid(this, password, passwordEditText)
                        && validator.isPasswordMatch(this, password, repeatPassword, repeatPasswordEditText)
                        && validator.isProfilePictureChanged(this, logoIsChanged, cameraLogo)) {
                    ((EmailLoginActivity) getActivity()).setRegistrationList(firstName, lastName, email, password, repeatPassword);
                } else
                    return;
                break;
        }
    }

    //TODO: Debug img size, clean path strings
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == getActivity().RESULT_OK && data != null) {

            Uri pickedPicture = data.getData();

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(pickedPicture, filePath, null, null, null);
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            bitmap = BitmapFactory.decodeFile(imagePath);
            Bitmap scaledBitmap = createDynamicScaledBitmap(bitmap);
            Bitmap rotatedBitmap = Util.rotateBitmap(imagePath, scaledBitmap);

            cameraLogo.setImageBitmap(createDynamicScaledBitmap(rotatedBitmap));

            logoIsChanged = true;

            cursor.close();
        }
    }

    private static Bitmap createDynamicScaledBitmap(Bitmap bitmap){
        final int maxSize = 480;
        int outWidth;
        int outHeight;
        int inWidth = bitmap.getWidth();
        int inHeight = bitmap.getHeight();
        if(inWidth > inHeight){
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }

        return Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, false);
    }

    public static Bitmap getBitmap(){
        return bitmap;
    }

}
