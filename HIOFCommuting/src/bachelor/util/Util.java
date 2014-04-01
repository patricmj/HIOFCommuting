package bachelor.util;

import java.io.IOException;

import com.bachelor.hiofcommuting.R;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class Util {
	
	public static void showFragment(int fragmentIndex, FragmentManager fm, Fragment[] fragments) {
		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			if (i == fragmentIndex) {
				transaction.show(fragments[i]);
			} else {
				transaction.hide(fragments[i]);
			}
		}
		transaction.commit();
	}
	
	public static Bitmap rotateBitmap(String imagePath, Bitmap bitmap) {
		Bitmap rotatedBitmap = null;
		try {
			ExifInterface exif = new ExifInterface(imagePath);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			System.out.println("EXIF" + exif +  "orientation");
			
			Matrix matrix = new Matrix();
			if (orientation == 3) {
                matrix.postRotate(180);
            }
            else if (orientation == 6) {
                matrix.postRotate(90);
            }
            else if (orientation == 8) {
                matrix.postRotate(270);
            }
			/*
			 
			if (orientation == 3) {
                matrix.postRotate(180);
            }
            else if (orientation == 6) {
                matrix.postRotate(90);
            }
            else if (orientation == 8) {
                matrix.postRotate(270);
            }
			 
			 */
			rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); // rotating bitmap
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rotatedBitmap;
	}	
}
