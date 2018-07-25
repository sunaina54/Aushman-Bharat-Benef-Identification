package com.nhpm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import com.nhpm.DocCamera.Camera2BasicFragment;
import com.nhpm.DocCamera.ImageUtil;
import com.nhpm.DocCamera.Preferences;
import com.nhpm.DocCamera.RetakeFragment;
import com.nhpm.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.nhpm.DocCamera.ImageUtil.rotateImage;


/**
 * Created by allsmartlt218 on 13-12-2016.
 */

public class DocCameraActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Preferences preferences;
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private String purpose = "",screenName="";
    public static boolean forBelowLollipop = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Fabric.with(this, new Crashlytics());
        setContentView(R.layout.doc_camera_activity);
   //     toolbar = (Toolbar) findViewById(R.id.toolbar);
        preferences = new Preferences(getApplicationContext());
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        try {

            screenName=getIntent().getStringExtra("screen");
            purpose = getIntent().getStringExtra("purpose");
        } catch (Exception e) {
            e.printStackTrace();
        }



        String bgColor = preferences.getString(Preferences.HEADER_BG_COLOR, "#d9020d");
        String textColor = preferences.getString(Preferences.HEADER_TEXT_COLOR, "#ffffff");

      //  toolbar.setTitleTextColor(Color.parseColor(textColor));
      //  toolbar.setBackgroundColor(Color.parseColor(bgColor));

        if (Build.VERSION.SDK_INT >= 22) {
            forBelowLollipop = false;
            Bundle bundle = new Bundle();
            bundle.putString("image_purpose", purpose);
            Fragment fragment = new Camera2BasicFragment();
            FragmentManager fm = getSupportFragmentManager();
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.flCapture, fragment).commit();
        } else {
            openDefualtCamera();
        }
    }

    public void openDefualtCamera() {
        forBelowLollipop = true;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public Fragment getTopFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.flCapture);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if(imageBitmap != null) {
                byte[] imageBytes = ImageUtil.bitmapToByteArray(rotateImage(imageBitmap, 270));

                File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), "");
                File mediaFile = new File(mediaStorageDir.getPath() + File.separator + purpose + ".jpg");
                if (mediaFile != null) {
                    try {
                        FileOutputStream fos = new FileOutputStream(mediaFile);
                        fos.write(imageBytes);
                        fos.close();
                    } catch (FileNotFoundException e) {
                      //  Crashlytics.log(1, getClass().getName(), e.getMessage());
                      //  Crashlytics.logException(e);
                    } catch (IOException e) {
                      //  Crashlytics.log(1, getClass().getName(), e.getMessage());
                      //  Crashlytics.logException(e);
                    }
                }
                    RetakeFragment fragment = new RetakeFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("image_taken", mediaFile.getAbsolutePath());
                    bundle.putString("image_purpose", purpose);
                    bundle.putString("screen",screenName);
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.flCapture, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commitAllowingStateLoss();
            }
        }
    }
}
