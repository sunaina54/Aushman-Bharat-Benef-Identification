package com.nhpm.DocCamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by Manjunath on 12-04-2017.
 */

public class ImageUtil {
    private static final String TAG = ImageUtil.class.getName();

    public static Bitmap decodeImageToBitmap(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    public static Bitmap decodeBitmapToImage(String base64Str){
        byte[] decodedString = Base64.decode(base64Str, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static Bitmap decodeImageToBitmapAndResize(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Bitmap bmp = Bitmap.createScaledBitmap(bitmap,768,1024,false);
        return bmp;
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static Bitmap rotateBmpFront(Bitmap bitmap, Context context){
        Bitmap bmp = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(270);
        bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bmp;
    }

  /*  public static Bitmap compressBitmap(File file, Context context) {
        Bitmap bmp = null;

        Bitmap bitmap = new Compressor.Builder(context).setQuality(60)
                .setCompressFormat(Bitmap.CompressFormat.JPEG).setMaxWidth(480)
                .setMaxHeight(640).build().compressToBitmap(file);
        bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());

        return bmp;
    }*/

    public static byte[] compressBitmap(Bitmap bmp) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BitmapFactory.Options options = new BitmapFactory.Options();
       // options.inSampleSize = 8;
      //  Bitmap preview_bitmap = BitmapFactory.decodeStream(convertBitmapToInputStream(bmp), null, options);
        bmp.compress(Bitmap.CompressFormat.JPEG,50,os);
        return os.toByteArray();
    }

    public static InputStream convertBitmapToInputStream(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        return bs;
    }

    public static Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE=70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }


    public static String encodeImage(String path){
        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.NO_WRAP);
        //Base64.de
        return encImage;
    }

    @NonNull
    public static File getResponseImageFile(Context context, Bitmap imageBitmap , String purpose) {
        byte[] imageBytes = compressBitmap(imageBitmap);

        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), "");
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + purpose + ".jpg");
        if (mediaFile != null) {
            try {
                FileOutputStream fos = new FileOutputStream(mediaFile);
                fos.write(imageBytes);
                fos.close();
            } catch (FileNotFoundException e) {
             //   Crashlytics.log(1, TAG, e.getMessage());
              //  Crashlytics.logException(e);
            } catch (IOException e) {
             //   Crashlytics.log(1, TAG, e.getMessage());
             //   Crashlytics.logException(e);
            }
        }
        return mediaFile;
    }

}
