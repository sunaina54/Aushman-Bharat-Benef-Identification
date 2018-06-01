package com.customComponent.utility;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Anand on 04-05-2016.
 */
public class ImageProcessingUtil {

    public static String convertImageToBase64String(Bitmap bitmapImage){

        Bitmap bm = bitmapImage; //BitmapFactory.decodeFile("/path/to/image.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        return  Base64.encodeToString(b, Base64.DEFAULT);

    }
}
