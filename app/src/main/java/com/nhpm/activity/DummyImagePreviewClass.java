package com.nhpm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nhpm.DocCamera.ImageUtil;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.nhpm.DocCamera.ImageUtil.rotateImage;

/**
 * Created by SUNAINA on 23-07-2018.
 */

public class DummyImagePreviewClass extends BaseActivity {
    private ImageView image;
    private Button clickBTN;
    private Context context;
    private int CAMERA_PIC_BACK_REQUEST = 101;
    private Bitmap captureImageBackBM;
    private String voterIdBackImg;
    private ImageView back;
    private RelativeLayout backLayout;
    private DummyImagePreviewClass activity;
    private Bitmap bitmap;
    private String purpose = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_dummy_layout);
        setupScreen();
    }

    private void setupScreen(){
        context=this;
        activity=this;
        back = (ImageView) findViewById(R.id.back);
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        AppUtility.navigateToHome(context,activity);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backLayout.performClick();
            }
        });
        image = (ImageView) findViewById(R.id.image);
        clickBTN = (Button) findViewById(R.id.clickBTN);
        clickBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUtility.openCamera(activity, AppConstant.BACK_CAMREA_OPEN, "ForStore", "DummyImagePreviewClass");

              /*  AppUtility.capturingType = AppConstant.capturingModeGovId;
              *//*  Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
              //  Uri imageUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH,context.getString(R.string.squarecamera__app_name)+"/govtIdPhoto" +".jpg"));
                Uri imageUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH,context.getString(R.string.squarecamera__app_name)+"/govtIdPhoto/IMG_12345.jpg"));
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, 2);*//*
                File mediaStorageDir = new File(
                        DatabaseHelpers.DELETE_FOLDER_PATH,
                        context.getString(R.string.squarecamera__app_name) + "/photoCapture"
                );

                if (mediaStorageDir.exists()) {
                    deleteDir(mediaStorageDir);
                }
                Intent startCustomCameraIntent = new Intent(context, CameraActivity.class);
                startActivityForResult(startCustomCameraIntent, CAMERA_PIC_BACK_REQUEST);
*/
            }
        });
    }

 /*   private void deleteDir(File file) {

        if (file.isDirectory()) {
            String[] children = file.list();
            for (String child : children) {
                if (child.endsWith(".jpg") || child.endsWith(".jpeg"))
                    new File(file, child).delete();
            }
            file.delete();
        }
        Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri fileContentUri = Uri.fromFile(file);
        mediaScannerIntent.setData(fileContentUri);
        context.sendBroadcast(mediaScannerIntent);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

     /*   if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == CAMERA_PIC_BACK_REQUEST) {
               *//* Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"govtIdPhoto" +".jpg"));
                try {
                    captureImageBM = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri); //(Bitmap)imageUri;//data.getExtras().get("data");
                } catch (IOException e) {
                    e.printStackTrace();
                }*//*
                Uri fileUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH,
                        context.getString(R.string.squarecamera__app_name) + "/photoCapture/IMG_12345.jpg"));
                Uri compressedUri = Uri.fromFile(new File(CommonUtilsImageCompression.compressImage(fileUri.getPath(), context, "/photoCapture")));
                //  captureImageBM=(Bitmap)data.getExtras().get("data");
                try {
                    captureImageBackBM = MediaStore.Images.Media.getBitmap(this.getContentResolver(), compressedUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Log.d(TAG," Bitmap Size : "+image.getAllocationByteCount());
                voterIdBackImg = AppUtility.convertBitmapToString(captureImageBackBM);
                updateBackImageScreen(voterIdBackImg);
            }
        }*/

        if (requestCode == AppConstant.REQ_CAMERA && resultCode == RESULT_OK) {
            final Intent intent = data;//new Intent();
            String path = intent.getStringExtra("response");
            Uri uri = Uri.fromFile(new File(path));
            if (uri == null) {
                Log.d("uri", "null");
            } else {
                bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File mediaFile = null;
                if (bitmap != null) {
                    byte[] imageBytes = ImageUtil.bitmapToByteArray(rotateImage(bitmap, 270));

                    File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), "");
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator + purpose + ".jpg");
                    if (mediaFile != null) {
                        try {
                            FileOutputStream fos = new FileOutputStream(mediaFile);
                            fos.write(imageBytes);
                            fos.close();
                        } catch (FileNotFoundException e) {
                            // Crashlytics.log(1, getClass().getName(), e.getMessage());
                            // Crashlytics.logException(e);
                        } catch (IOException e) {
                            //  Crashlytics.log(1, getClass().getName(), e.getMessage());
                            //  Crashlytics.logException(e);
                        }
                    }
                }
            }

            String base64 = AppUtility.converBitmapToBase64(bitmap);
            image.setImageBitmap(bitmap);
        }

    }

    private void updateBackImageScreen(String idImage) {
        try {
            if (idImage != null) {
                image.setImageBitmap(AppUtility.convertStringToBitmap(idImage));
            } else {
//                if (seccItem.getGovtIdPhoto() != null && !seccItem.getGovtIdPhoto().equalsIgnoreCase("")) {
//
//                } else {
                image.setImageBitmap(null);
//                }
            }
        } catch (Exception e) {

        }
    }

}
