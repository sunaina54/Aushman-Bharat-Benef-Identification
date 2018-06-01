package com.nhpm.rsbyFieldValidation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.activity.BaseActivity;
import com.nhpm.CameraUtils.CommonUtilsImageCompression;
import com.nhpm.CameraUtils.FaceCropper;
import com.nhpm.CameraUtils.squarecamera.CameraActivity;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.File;

import pl.polidea.view.ZoomView;

/**
 * Created by DELL on 26-02-2017.
 */

public class RsbyPhotoCaptureActivity extends BaseActivity {
    private Button submitBT,takePhotoBT;
    private ImageView memberPhotoIV;
    private int CAMERA_PIC_REQUEST=0;
    private static final String TAG="PhotoCaptureActivity";
    private Button submitBt;
    private ImageView backIV;
    private int navigateType;
    private Context context;
    private SelectedMemberItem selectedMemItem;
    private RSBYItem rsbyItem;
    private Bitmap memberPhoto;
    private AlertDialog internetDiaolg;
    private Uri fileUri;
    private Picasso mPicasso;
    private FaceCropper mFaceCropper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_capture);
        setupScreen();
        setupHeader();
    }

    private void setupScreen(){
        context=this;
        TextView headerTV=(TextView)findViewById(R.id.centertext);

        selectedMemItem= SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if(selectedMemItem.getRsbyMemberItem()!=null) {
            rsbyItem = selectedMemItem.getRsbyMemberItem();
            headerTV.setText(rsbyItem.getName());
        }
        takePhotoBT=(Button)findViewById(R.id.takePhotoBT);
        memberPhotoIV=(ImageView)findViewById(R.id.memberPicIV);

        if(rsbyItem.getMemberPhoto1()!=null){
            try {
                memberPhoto = convertStringToBitmap(rsbyItem.getMemberPhoto1());
                memberPhotoIV.setImageBitmap(memberPhoto);
            }catch (Exception e){

            }
        }
        submitBt=(Button)findViewById(R.id.submitBT);
        backIV=(ImageView)findViewById(R.id.back);
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPhoto();
            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = null;
                // if (rsbyItem != null && rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                theIntent = new Intent(context, RsbyValidationWithAadharActivity.class);
               /* } else {
                    theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);
                }*/
                startActivity(theIntent);
                rightTransition();
                finish();
            }
        });
        takePhotoBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                //      alertForValidateLater(getResources().getString(R.string.photo_text),null);
            }
        });
        if(rsbyItem.getMemberPhoto1()!=null && !rsbyItem.getMemberPhoto1().equalsIgnoreCase("")){

        }else {
            openCamera();
        }
    }

    private void setupHeader(){
        context=this;

    }
    private void submitPhoto(){
        if(memberPhoto!=null){
            Intent theIntent = null;
            AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Member photo size1 : "+ AppUtility.byteSizeOf(memberPhoto));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            memberPhoto.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            Bitmap photo= AppUtility.convertStringToBitmap(encoded);

            AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Member photo size : "+ AppUtility.byteSizeOf(photo));
            Log.d(TAG, "Image Byte : " + encoded);
            if(rsbyItem!=null) {
                //rsbyItem.setMemberPhoto(encoded);
                rsbyItem.setMemberPhoto1(encoded);
                rsbyItem.setPhotoSurveyedStatus(AppConstant.SURVEYED+"");
                rsbyItem.setLockedSave(AppConstant.SAVE + "");
                //Log.d(TAG,"NHPS Relation : "+rsbyItem.getNhpsRelationCode());
                if(selectedMemItem.getOldHeadrsbyMemberItem()!=null && selectedMemItem.getNewHeadrsbyMemberItem()!=null){
                    RSBYItem oldHead=selectedMemItem.getOldHeadrsbyMemberItem();
                    oldHead.setLockedSave(AppConstant.LOCKED+"");
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG," OLD Household name " +
                            ": "+oldHead.getName()+"" +
                            " Member Status "+oldHead.getMemStatus()+" House hold Status :" +
                            " "+oldHead.getHhStatus()+" Locked Save :"+oldHead.getLockedSave());
                    SeccDatabase.updateRsbyMember(selectedMemItem.getOldHeadrsbyMemberItem(),context);
                }
                SeccDatabase.updateRSBYHouseHold(selectedMemItem.getRsbyHouseholdItem(),context);
                SeccDatabase.updateRsbyMember(rsbyItem,context);
                // SeccDatabase.getSeccMemberDetail(rsbyItem.getAhlTin(),context);
                rsbyItem= SeccDatabase.getRsbyMemberDetail(rsbyItem.getRsbyMemId(),context);
                // Log.d(TAG, "Member photo string : " + SeccDatabase.getSeccMemberDetail(rsbyItem.getAhlTin(), context).getMemberPhoto1());
                // selectedMemItem.setSeccMemberItem(rsbyItem);
                selectedMemItem.setRsbyMemberItem(rsbyItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
              //  Log.d(TAG," Aadhaar Status : "+rsbyItem.getAadhaarStatus());
                // if (rsbyItem != null && rsbyItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                theIntent = new Intent(context, RsbyValidationWithAadharActivity.class);
               /* } else {
                    theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);

                }*/
            }else{
                // RSBY Yet to coded
            }
            startActivity(theIntent);
            rightTransition();
            finish();

        }else{
            CustomAlert.alertWithOk(context,"Please take member photo");
        }
    }
    private void openCamera(){
        AppUtility.capturingType = AppConstant.capturingModePhoto;
        File mediaStorageDir = new File(
                DatabaseHelpers.DELETE_FOLDER_PATH,
                context.getString(R.string.squarecamera__app_name)+"/photoCapture"
        );

        if(mediaStorageDir.exists()){
            deleteDir(mediaStorageDir);
        }

        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, CAMERA_PIC_REQUEST);

    /*    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);*/
    }void deleteDir(File file) {

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
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                try {
                    //  fileUri = data.getData();
                    Uri fileUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH,
                            context.getString(R.string.squarecamera__app_name) + "/photoCapture/IMG_12345.jpg"));
                    Uri compressedUri = Uri.fromFile(new File(CommonUtilsImageCompression.compressImage(fileUri.getPath(), context, "/photoCapture")));
                    previewCapturedImage(compressedUri);

                } catch (Exception ee) {
                    Toast.makeText(context, "Error occurred, Please provide necessary permission & Try Again", Toast.LENGTH_SHORT).show();
                }

            }
        }

    }
    private void previewCapturedImage(Uri compressedUri) {
        try {
            mFaceCropper = new FaceCropper(1f);
            mFaceCropper.setFaceMinSize(0);
            mFaceCropper.setDebug(true);
            mPicasso = Picasso.with(this);

            // ImageView imageCropped = (ImageView) findViewById(R.id.finalRequiredImage);
//
            mPicasso.load(compressedUri)
                    .config(Bitmap.Config.RGB_565)
                    .transform(mCropTransformation).memoryPolicy(MemoryPolicy.NO_CACHE)//.rotate(270)
                    .into(memberPhotoIV, new Callback() {
                        @Override
                        public void onSuccess() {
                            memberPhoto = ((BitmapDrawable)memberPhotoIV.getDrawable()).getBitmap();
                            memberPhotoIV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ShowImageInPopUp(memberPhoto);
                                }
                            });
                        }

                        @Override
                        public void onError() {

                        }
                    });

        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(context, "Unable to capture image, Please provide necessary permission & Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    public void ShowImageInPopUp(Bitmap mIgameBitmap) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.layout_to_performzoom);
        LinearLayout mZoomLinearLayout;
        ZoomView zoomView;
        mZoomLinearLayout = (LinearLayout) dialog.findViewById(R.id.mZoomLinearLayoutPopUp);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.image_view_popup, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.0f);
        dialog.setCancelable(false);
        ImageView mImageView = (ImageView) v.findViewById(R.id.imageView);
        ImageView mCancelPopUp = (ImageView) v.findViewById(R.id.mCancelPopUp);
        mImageView.setImageBitmap(mIgameBitmap);
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mZoomLinearLayout.addView(zoomView);
        mCancelPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private Transformation mCropTransformation = new Transformation() {

        @Override
        public Bitmap transform(Bitmap source) {

            return mFaceCropper.getCroppedImage(source,context);
        }

        @Override
        public String key() {
            StringBuilder builder = new StringBuilder();

            builder.append("faceCrop(");
            builder.append("minSize=").append(mFaceCropper.getFaceMinSize());
            builder.append(",maxFaces=").append(mFaceCropper.getMaxFaces());

            FaceCropper.SizeMode mode = mFaceCropper.getSizeMode();
            if (FaceCropper.SizeMode.EyeDistanceFactorMargin.equals(mode)) {
                builder.append(",distFactor=").append(mFaceCropper.getEyeDistanceFactorMargin());
            } else if (FaceCropper.SizeMode.FaceMarginPx.equals(mode)) {
                builder.append(",margin=").append(mFaceCropper.getFaceMarginPx());
            }

            return builder.append(")").toString();
        }
    };
    private  String convertBitmapToString(Bitmap bitmap){
        Bitmap selectedImage =  bitmap;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String strBase64=Base64.encodeToString(byteArray, 0);
        return  strBase64;
    }

    private  Bitmap convertStringToBitmap(String strBase64){
        byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        //image.setImageBitmap(decodedByte);
        return decodedByte;
    }

    private void alertForValidateLater(String msg, SeccMemberItem item){

        internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.internet_try_again_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        TextView tryGainMsgTV=(TextView) alertView.findViewById(R.id.deleteMsg);
        tryGainMsgTV.setText(msg);
        Button tryAgainBT=(Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText("Ok");
        Button cancelBT=(Button)alertView.findViewById(R.id.cancelBT);
        //cancelBT.setVisibility(View.GONE);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
                openCamera();
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
    }

}
