package bachelor.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import bachelor.database.HandleUsers;
import bachelor.objects.User;
import com.bachelor.hiofcommuting.R;

import java.io.*;

/**
 * Created by alfaomega on 5/8/14.
 */
public class ImageHandler {

    // Thows NullPointers for test users
    public static String saveBitmapToCache(Activity activity, Bitmap bitmap, int userID) throws NullPointerException{
        String cachePath = activity.getCacheDir().getAbsolutePath() + "/";
        String fileName = userID + ".png";
        String imagePath = cachePath + fileName;

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return imagePath;
    }

    private static Bitmap loadBitmapFromCache(String imagePath){
        Bitmap bitmap = null;

        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(imagePath));
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static void setBitmapFromPath(ImageView imageView, String imagePath){
        Bitmap bitmapImage;

        bitmapImage = loadBitmapFromCache(imagePath);

        // Assigning a profile picture if it exists

        if(bitmapImage != null)
            imageView.setImageBitmap(bitmapImage);
        else {
            imageView.setImageResource(R.drawable.profile_picture_test);
        }
    }
}
