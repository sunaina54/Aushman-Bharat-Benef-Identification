package com.nhpm.DocCamera;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;


import com.nhpm.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


/**
 * Created by allsmartlt218 on 13-12-2016.
 */

public class CameraFragment extends Fragment implements Camera.PictureCallback{

    private static final String TAG = CameraFragment.class.getName();
    private Camera camera;
    private CameraPreview previewClass;

    private ImageButton imageButton;
    private static final int MEDIA_TYPE_IMAGE_FRONT = 1;
    private static final int MEDIA_TYPE_IMAGE_BACK = 2;
    private File pic;
    FrameLayout surfaceView;
 //   private boolean canTakePhoto = false;
    static final int REQUEST_IMAGE_CAPTURE = 100;
    String purpose = "" ;
    int cameraForB = BACK_CAMREA_OPEN;
    String fieldCode = "";
    public static final int FRONT_CAMREA_OPEN = 1;
    public static final int BACK_CAMREA_OPEN = 2;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraForB = getActivity().getIntent().getIntExtra("camera_key",BACK_CAMREA_OPEN);
        purpose = getActivity().getIntent().getStringExtra("purpose");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_fragment,container,false);
        surfaceView = (FrameLayout) view.findViewById(R.id.flLivePreview);
        imageButton = (ImageButton) view.findViewById(R.id.ibPhotoCapture);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturePhoto();
            }
        });

        Log.d(CameraFragment.TAG,"checking camera checking camera feature " + hasCamera(getContext()));
        if (hasCamera(getContext())) {
            Log.d(CameraFragment.TAG,"checking camera checking camera feature " + hasCamera(getContext()));
            if(cameraForB == FRONT_CAMREA_OPEN) {
                camera = getCameraInstace();
            } else {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }

            previewClass = new CameraPreview(getContext(),camera);
            surfaceView.addView(previewClass);
        }

        return view;
    }

    private void capturePhoto() {
        Log.d(CameraFragment.TAG,"checking camera onClick");
        try {
            camera.takePicture(null,null,this);
        } catch (RuntimeException e) {
          //  Crashlytics.log(e.getMessage());
            Toast.makeText(getContext(),"Please use default camera", Toast.LENGTH_SHORT).show();
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            onPictureTaken(ImageUtil.bitmapToByteArray(ImageUtil.rotateImage(imageBitmap,270)),camera);
        }
    }

    public boolean hasCamera(Context context) {
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            return true;
        } else {
            return false;
        }
    }

    private Camera getCameraInstace() {
        Camera c = null;
        try {
            if (Camera.getNumberOfCameras() >= 2) {
                c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            }
            else{
                c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }
        } catch (Exception e) {
          //  Crashlytics.log(1,getClass().getName(),"Error in CameraFragment");
          //  Crashlytics.logException(e);
        }
        return c;
    }


    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM), "");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("CAMERA", "failed to create directory");
                return null;
            }
        }
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + purpose + ".jpg");

        return mediaFile;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        pic = getOutputMediaFile(MEDIA_TYPE_IMAGE_FRONT);
        confirmPicture(data);
    }

    public void confirmPicture(byte[] data) {
        if (pic != null) {
            try {
                FileOutputStream fos = new FileOutputStream(pic);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
            //    Crashlytics.log(1,getClass().getName(),"Error in CameraFragment");
            //    Crashlytics.logException(e);
            } catch (IOException e) {
            //    Crashlytics.log(1,getClass().getName(),"Error in CameraFragment");
             //   Crashlytics.logException(e);
            }
        }


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RetakeFragment fragment = new RetakeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("image_taken",pic.getAbsolutePath());
        bundle.putString("image_purpose",purpose);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.flCapture, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }
}
