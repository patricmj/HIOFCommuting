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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import bachelor.util.Util;

import com.bachelor.hiofcommuting.R;

public class RegisterFragment extends Fragment {
	
	ImageView cameraLogo, choosenPic;
	Button next;
	private static final int LOAD_IMAGE_RESULTS = 1;
	
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
		addOnClickListeners();
	}
	
	public void addOnClickListeners() {
		cameraLogo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				//Starter imagegalleriet
				Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				
				startActivityForResult(intent, LOAD_IMAGE_RESULTS);
			}
		});
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
        	Bitmap rotatedBitmap = Util.rotateBitmap(imagePath, scaledBitmap);
        	
        	choosenPic.setImageBitmap(rotatedBitmap);
        	
        	cursor.close();
        }
    }
}
