package bachelor.util;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;
import bachelor.register.FinishProfileFragment;
import bachelor.register.RegisterFragment;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Insert check methods here
public class UserInputValidator {

    public UserInputValidator(){}

    public boolean isFirstNameValid(RegisterFragment fragment, String firstname, EditText fnText){
        if (firstname.length() > 0 && firstname.matches("[a-zA-Z]+"))
            return true;
        else {
            Toast.makeText(fragment.getActivity().getApplicationContext(), "Tast inn fornavn", Toast.LENGTH_SHORT).show();
            fnText.requestFocus();
            return false;
        }
    }

    public boolean isLastNameValid(RegisterFragment fragment, String lastname, EditText lnText){
        if (lastname.length() > 0 && lastname.matches("[a-zA-Z]+"))
            return true;
        else{
            Toast.makeText(fragment.getActivity().getApplicationContext(), "Tast inn etternavn" , Toast.LENGTH_SHORT).show();
            lnText.requestFocus();
            return false;
        }
    }

    // Thanks to stackoverflow.com
    public boolean isEmailValid(RegisterFragment fragment, String email, EditText emailText) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            return true;
        }
        else{
            Toast.makeText(fragment.getActivity().getApplicationContext(), "Ugyldig epost" , Toast.LENGTH_SHORT).show();
            emailText.requestFocus();
            return false;
        }
    }

    public boolean isPasswordValid(RegisterFragment fragment, String pw, EditText pwText){
        boolean isPwPresent = false;
        boolean isPwLengthOk = false;

        if (pw.length() > 0)
            isPwPresent = true;
        else{
            Toast.makeText(fragment.getActivity().getApplicationContext(), "Tast inn passord" , Toast.LENGTH_SHORT).show();
            pwText.requestFocus();
        }

        if (!(pw.length() < 7))
            isPwLengthOk = true;
        else{
            Toast.makeText(fragment.getActivity().getApplicationContext(), "Passordet må bestå av minst 7 tegn" , Toast.LENGTH_SHORT).show();
            pwText.requestFocus();
        }

        if (isPwPresent && isPwLengthOk)
            return true;
        else
            return false;
    }

    public boolean isPasswordMatch(RegisterFragment fragment, String firstPW, String secondPW, EditText secondPWText){
        if (firstPW.length() == secondPW.length() && secondPW.equals(firstPW))
            return true;
        else{
            Toast.makeText(fragment.getActivity().getApplicationContext(), "Gjennta passord" , Toast.LENGTH_SHORT).show();
            secondPWText.requestFocus();
            return false;
        }
    }

    public boolean isProfilePictureChanged(RegisterFragment fragment, Boolean picture, ImageView view){
        if (picture)
            return true;
        else{
            Toast.makeText(fragment.getActivity().getApplicationContext(), "Velg profilbilde" , Toast.LENGTH_SHORT).show();
            view.requestFocus();
            return false;
        }
    }

    public boolean isAddressValid(FinishProfileFragment fragment, String address, EditText adrText){
        if (address.length() > 0)
            return true;
        else{
            Toast.makeText(fragment.getActivity().getApplicationContext(), "Tast inn adresse" , Toast.LENGTH_SHORT).show();
            adrText.requestFocus();
            return false;
        }
    }

    public boolean isPostalCodeValid(FinishProfileFragment fragment, String postalCode, EditText postalCodeText){
        if (postalCode.length() == 4 && postalCode.matches("[0-9]+"))
            return true;
        else{
            Toast.makeText(fragment.getActivity().getApplicationContext(), "Tast inn postnummer" , Toast.LENGTH_SHORT).show();
            postalCodeText.requestFocus();
            return false;
        }
    }

    public boolean isConditionsRead(FinishProfileFragment fragment, boolean readConditions, ToggleButton button){
        if (readConditions)
            return true;
        else{
            Toast.makeText(fragment.getActivity().getApplicationContext(), "Du må godkjenne betingelser for å fortsette" , Toast.LENGTH_SHORT).show();
            button.requestFocus();
            return false;
        }
    }
}
