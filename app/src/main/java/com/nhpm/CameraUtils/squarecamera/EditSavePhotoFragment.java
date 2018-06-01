package com.nhpm.CameraUtils.squarecamera;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nhpm.CameraUtils.FaceCropper;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


/**
 *
 */
public class EditSavePhotoFragment extends Fragment {

    public static final String TAG = EditSavePhotoFragment.class.getSimpleName();
    public static final String BITMAP_KEY = "bitmap_byte_array";
    public static final String ROTATION_KEY = "rotation";
    public static final String IMAGE_INFO = "image_info";
    private Picasso mPicasso;
    private FaceCropper mFaceCropper;
    private static final int REQUEST_STORAGE = 1;
    private ImageView photoImageView;
    private Uri photoUri;
    private boolean isFaceDetected=true;
    private LinearLayout saveLayout;
    private FragmentManager mFragmentManager;
    public static Fragment newInstance(byte[] bitmapByteArray, int rotation,
                                       @NonNull ImageParameters parameters) {
        Fragment fragment = new EditSavePhotoFragment();

        Bundle args = new Bundle();
        args.putByteArray(BITMAP_KEY, bitmapByteArray);
        args.putInt(ROTATION_KEY, rotation);
        args.putParcelable(IMAGE_INFO, parameters);

        fragment.setArguments(args);
        return fragment;
    }

    public EditSavePhotoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentManager= getActivity().getSupportFragmentManager();
        return inflater.inflate(R.layout.squarecamera__fragment_edit_save_photo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int rotation = getArguments().getInt(ROTATION_KEY);
        byte[] data = getArguments().getByteArray(BITMAP_KEY);
        ImageParameters imageParameters = getArguments().getParcelable(IMAGE_INFO);

        if (imageParameters == null) {
            return;
        }

        photoImageView = (ImageView) view.findViewById(R.id.photo);
        saveLayout=(LinearLayout)view.findViewById(R.id.saveLayout);
        saveLayout.setVisibility(View.GONE);


        imageParameters.mIsPortrait =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        final View topView = view.findViewById(R.id.topView);
        if (imageParameters.mIsPortrait) {
            topView.getLayoutParams().height = imageParameters.mCoverHeight;
        } else {
            topView.getLayoutParams().width = imageParameters.mCoverWidth;
        }

        rotatePicture(rotation, data, photoImageView);

        view.findViewById(R.id.save_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePicture();
            }
        });
    }

    private void rotatePicture(int rotation, byte[] data, ImageView photoImageView) {
        Bitmap bitmap = ImageUtility.decodeSampledBitmapFromByte(getActivity(), data);
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap .getWidth(), bitmap.getHeight(), matrix, true);
        photoUri = ImageUtility.savePicture(getActivity(), rotatedBitmap);
        //photoImageView.setImageBitmap(bitmap);
        previewCapturedImage(rotation);
    }

    private void savePicture() {
        requestForPermission();
    }

    private void requestForPermission() {
        Bitmap bitmap = ((BitmapDrawable) photoImageView.getDrawable()).getBitmap();
        ((CameraActivity) getActivity()).returnPhotoUri(photoUri);
        /*RuntimePermissionActivity.startActivity(EditSavePhotoFragment.this,
                REQUEST_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK != resultCode) return;

        if (REQUEST_STORAGE == requestCode && data != null) {
            final boolean isGranted = data.getBooleanExtra(RuntimePermissionActivity.REQUESTED_PERMISSION, false);
            final View view = getView();
            if (isGranted && view != null) {
                ImageView photoImageView = (ImageView) view.findViewById(R.id.photo);



                //   ((CameraActivity) getActivity()).returnPhotoUri(photoUri);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void previewCapturedImage(int Rotate) {
        try {

            photoImageView.setImageBitmap(null);
            mFaceCropper = new FaceCropper(1.2f);
            mFaceCropper.setFaceMinSize(0);
            mFaceCropper.setDebug(true);

            mPicasso = Picasso.with(getActivity());

            // ImageView imageCropped = (ImageView) findViewById(R.id.finalRequiredImage);
//if
            /*if (Rotate != 0) {
                mPicasso.load(photoUri)
                        .config(Bitmap.Config.RGB_565)
                        .transform(mCropTransformation).rotate(Rotate).memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(photoImageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                // memberPhoto = ((BitmapDrawable)memberPhotoIV.getDrawable()).getBitmap();
                                saveLayout.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError() {

                            }
                        });
            }else{*/
            if(AppUtility.capturingType == AppConstant.capturingModeGovId){
                mPicasso.load(photoUri)
                        .config(Bitmap.Config.RGB_565)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(photoImageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                // memberPhoto = ((BitmapDrawable)memberPhotoIV.getDrawable()).getBitmap();
                                saveLayout.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onError() {

                            }
                        });
            }else {
                try {
                    mPicasso.load(photoUri)
                            .config(Bitmap.Config.RGB_565)
                            .transform(mCropTransformation).memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(photoImageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    // memberPhoto = ((BitmapDrawable)memberPhotoIV.getDrawable()).getBitmap();
                                    saveLayout.setVisibility(View.VISIBLE);
                                    if (!isFaceDetected) {
                                        //  Toast.makeText(getActivity(), "No face detection performed", Toast.LENGTH_SHORT).show();
                                        alertWithOk(getActivity());
                                    }
                                }

                                @Override
                                public void onError() {
                                    //  AppUtility.alertWithOk(getActivity(),"No face detected please try again");
                                    alertWithOk(getActivity());
                                }
                            });
                }catch (Exception e){
                    alertWithOk(getActivity());
                }
            }
            //}
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "Unable to capture image, Please provide necessary permission & Try Again", Toast.LENGTH_SHORT).show();
        }

    }
    private Transformation mCropTransformation = new Transformation() {

        @Override
        public Bitmap transform(Bitmap source) {
            try {
                Bitmap convertedBitmap = mFaceCropper.getCroppedImage(source, getActivity());
                if (convertedBitmap != null) {
                    return convertedBitmap;
                } else {
                    isFaceDetected = false;
                    Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.qr_wrong_button);
                    return icon;
                }
            }catch (Exception ex){
                isFaceDetected = false;
                Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.qr_wrong_button);
                return icon;
            }
            // return mFaceCropper.getCroppedImage(source,getActivity());
        }


        @Override
        public String key() {
            StringBuilder builder = new StringBuilder();
            try {
                builder.append("faceCrop(");
                builder.append("minSize=").append(mFaceCropper.getFaceMinSize());
                builder.append(",maxFaces=").append(mFaceCropper.getMaxFaces());

                FaceCropper.SizeMode mode = mFaceCropper.getSizeMode();
                if (FaceCropper.SizeMode.EyeDistanceFactorMargin.equals(mode)) {
                    builder.append(",distFactor=").append(mFaceCropper.getEyeDistanceFactorMargin());
                } else if (FaceCropper.SizeMode.FaceMarginPx.equals(mode)) {
                    builder.append(",margin=").append(mFaceCropper.getFaceMarginPx());
                }
            }catch (Exception ex){

            }
            return builder.append(")").toString();
        }
    };
    public  void alertWithOk(Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Alert");
        builder.setMessage(mContext.getResources().getString(R.string.photo_error_text))
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        mFragmentManager.popBackStack();
                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
             getActivity().finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
