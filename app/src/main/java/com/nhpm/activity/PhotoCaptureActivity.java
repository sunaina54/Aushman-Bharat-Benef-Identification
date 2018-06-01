package com.nhpm.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.customComponent.CustomAlert;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.CameraUtils.CommonUtilsImageCompression;
import com.nhpm.CameraUtils.FaceCropper;
import com.nhpm.CameraUtils.squarecamera.CameraActivity;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
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

public class PhotoCaptureActivity extends BaseActivity implements ComponentCallbacks2 {
    private Button submitBT, takePhotoBT;
    private ImageView memberPhotoIV;
    private int CAMERA_PIC_REQUEST = 0;
    private static final String TAG = "PhotoCaptureActivity";
    private Button submitBt;
    private ImageView backIV;
    private int navigateType;
    private Context context;
    private SelectedMemberItem selectedMemItem;
    private SeccMemberItem seccItem;
    private Bitmap memberPhoto;
    private AlertDialog internetDiaolg;
    private Uri fileUri;
    private Picasso mPicasso;
    private FaceCropper mFaceCropper;
    private TextView headerTV, dashBoardNavBT;
    private boolean pinLockIsShown;


    private int wrongPinCount = 0;
    private long wrongPinSavedTime;
    private long currentTime;
    private TextView wrongAttempetCountValue, wrongAttempetCountText;
    private long millisecond24 = 86400000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_capture);
        setupScreen();


    }

    private void dashboardDropdown() {
        RelativeLayout menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
        menuLayout.setVisibility(View.VISIBLE);

        final ImageView settings = (ImageView) findViewById(R.id.settings);
        //  settings.setVisibility(View.GONE);
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.performClick();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_nav_dashboard, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.dashboard:
                                //   String searchText = searchFamilyMemberET.getText().toString();
                                Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                theIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(theIntent);
                                leftTransition();
                                break;

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.performClick();
            }
        });
    }


    private void setupScreen() {
        context = this;
        headerTV = (TextView) findViewById(R.id.centertext);
        showNotification();
        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemItem.getSeccMemberItem() != null) {
            seccItem = selectedMemItem.getSeccMemberItem();
            if (seccItem != null && seccItem.getDataSource() != null &&
                    seccItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                showRsbyDetail(seccItem);
            } else {
                showSeccDetail(seccItem);
            }
        }
        takePhotoBT = (Button) findViewById(R.id.takePhotoBT);
        memberPhotoIV = (ImageView) findViewById(R.id.memberPicIV);

        if (seccItem.getMemberPhoto1() != null) {
            try {
                memberPhoto = convertStringToBitmap(seccItem.getMemberPhoto1());
                memberPhotoIV.setImageBitmap(memberPhoto);
            } catch (Exception e) {

            }
        }
        submitBt = (Button) findViewById(R.id.submitBT);
        backIV = (ImageView) findViewById(R.id.back);
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
                // if (seccItem != null && seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                theIntent = new Intent(context, WithAadhaarActivity.class);
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
        if (seccItem.getMemberPhoto1() != null && !seccItem.getMemberPhoto1().equalsIgnoreCase("")) {

        } else {
            openCamera();
        }
        dashboardDropdown();
    }

    private void submitPhoto() {
        if (memberPhoto != null) {
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Member photo size1 : " + AppUtility.byteSizeOf(memberPhoto));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            memberPhoto.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            Bitmap photo = AppUtility.convertStringToBitmap(encoded);

            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Member photo size : " + AppUtility.byteSizeOf(photo));
            Log.d(TAG, "Image Byte : " + encoded);
            if (seccItem != null) {
                //seccItem.setMemberPhoto(encoded);
                seccItem.setMemberPhoto1(encoded);
                seccItem.setPhotoSurveyedStatus(AppConstant.SURVEYED + "");
                seccItem.setLockedSave(AppConstant.SAVE + "");
                //Log.d(TAG,"NHPS Relation : "+seccItem.getNhpsRelationCode());
               /* if(selectedMemItem.getOldHeadMember()!=null && selectedMemItem.getNewHeadMember()!=null){
                    SeccMemberItem oldHead=selectedMemItem.getOldHeadMember();
                    oldHead.setLockedSave(AppConstant.LOCKED+"");
                    AppUtility.showLog(AppConstant.LOG_STATUS,TAG," OLD Household name " +
                            ": "+oldHead.getName()+"" +
                            " Member Status "+oldHead.getMemStatus()+" House hold Status :" +
                            " "+oldHead.getHhStatus()+" Locked Save :"+oldHead.getLockedSave());
                    SeccDatabase.updateSeccMember(selectedMemItem.getOldHeadMember(),context);
                }
                SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(),context);
                SeccDatabase.updateSeccMember(seccItem,context);
                // SeccDatabase.getSeccMemberDetail(seccItem.getAhlTin(),context);
                seccItem=SeccDatabase.getSeccMemberDetail(seccItem.getNhpsMemId(),context);
                // Log.d(TAG, "Member photo string : " + SeccDatabase.getSeccMemberDetail(seccItem.getAhlTin(), context).getMemberPhoto1());
                // selectedMemItem.setSeccMemberItem(seccItem);
                selectedMemItem.setSeccMemberItem(seccItem);
                ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                        AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                Log.d(TAG," Aadhaar Status : "+seccItem.getAadhaarStatus());
                // if (seccItem != null && seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                theIntent = new Intent(context, WithAadhaarActivity.class);*/
               /* } else {
                    theIntent = new Intent(context, WithoutAadhaarVerificationActivity.class);

                }*/

                if (seccItem != null && seccItem.getDataSource() != null && seccItem.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                    if (selectedMemItem.getOldHeadMember() != null && selectedMemItem.getNewHeadMember() != null) {
                        SeccMemberItem oldHead = selectedMemItem.getOldHeadMember();
                        oldHead.setLockedSave(AppConstant.LOCKED + "");
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " OLD Household name " +
                                ": " + oldHead.getName() + "" +
                                " Member Status " + oldHead.getMemStatus() + " House hold Status :" +
                                " " + oldHead.getHhStatus() + " Locked Save :" + oldHead.getLockedSave());
                        SeccDatabase.updateRsbyMember(selectedMemItem.getOldHeadMember(), context);
                    }
                    SeccDatabase.updateRsbyMember(seccItem, context);
                    SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(), context);
                    seccItem = SeccDatabase.getSeccMemberDetail(seccItem, context);
                    selectedMemItem.setSeccMemberItem(seccItem);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);

                } else {
                    if (selectedMemItem.getOldHeadMember() != null && selectedMemItem.getNewHeadMember() != null) {
                        SeccMemberItem oldHead = selectedMemItem.getOldHeadMember();
                        oldHead.setLockedSave(AppConstant.LOCKED + "");
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, " OLD Household name " +
                                ": " + oldHead.getName() + "" +
                                " Member Status " + oldHead.getMemStatus() + " House hold Status :" +
                                " " + oldHead.getHhStatus() + " Locked Save :" + oldHead.getLockedSave());
                        SeccDatabase.updateSeccMember(selectedMemItem.getOldHeadMember(), context);
                    }
                    SeccDatabase.updateSeccMember(seccItem, context);
                    SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(), context);
                    seccItem = SeccDatabase.getSeccMemberDetail(seccItem, context);
                    selectedMemItem.setSeccMemberItem(seccItem);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
                    //  if (seccItem.getAadhaarStatus().equalsIgnoreCase("1")) {
                }
                Intent theIntent = new Intent(context, WithAadhaarActivity.class);
                startActivity(theIntent);
                finish();
                rightTransition();
            } else {
                // RSBY Yet to coded
            }

        } else {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pleaseTakeMemberPic));
        }
    }

    private void openCamera() {
        AppUtility.capturingType = AppConstant.capturingModePhoto;
        File mediaStorageDir = new File(
                DatabaseHelpers.DELETE_FOLDER_PATH,
                context.getString(R.string.squarecamera__app_name) + "/photoCapture"
        );

        if (mediaStorageDir.exists()) {
            deleteDir(mediaStorageDir);
        }

        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, CAMERA_PIC_REQUEST);

    /*    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);*/
    }

    void deleteDir(File file) {

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
                    Toast.makeText(PhotoCaptureActivity.this, context.getResources().getString(R.string.unableToCaptureImage), Toast.LENGTH_SHORT).show();
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
                            memberPhoto = ((BitmapDrawable) memberPhotoIV.getDrawable()).getBitmap();
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
            Toast.makeText(PhotoCaptureActivity.this, "Unable to capture image, Please provide necessary permission & Try Again", Toast.LENGTH_SHORT).show();
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

            return mFaceCropper.getCroppedImage(source, context);
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

    private String convertBitmapToString(Bitmap bitmap) {
        Bitmap selectedImage = bitmap;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String strBase64 = Base64.encodeToString(byteArray, 0);
        return strBase64;
    }

    private Bitmap convertStringToBitmap(String strBase64) {
        byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        //image.setImageBitmap(decodedByte);
        return decodedByte;
    }

    private void alertForValidateLater(String msg, SeccMemberItem item) {

        internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.internet_try_again_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        TextView tryGainMsgTV = (TextView) alertView.findViewById(R.id.deleteMsg);
        tryGainMsgTV.setText(msg);
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText(context.getResources().getString(R.string.OK));
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
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

    private void showRsbyDetail(SeccMemberItem item) {
        headerTV.setText(item.getRsbyName());
    }

    private void showSeccDetail(SeccMemberItem item) {
        headerTV.setText(item.getName());
    }

    public void showNotification() {

        LinearLayout notificationLayout = (LinearLayout) findViewById(R.id.notificationLayout);
        WebView notificationWebview = (WebView) findViewById(R.id.notificationWebview);
        String prePairedMessage = AppUtility.getNotificationData(context);
        if (prePairedMessage != null) {
            notificationLayout.setVisibility(View.VISIBLE);
            notificationWebview.loadData(prePairedMessage, "text/html", "utf-8"); // Set focus to textview
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (AppUtility.isAppIsInBackground(context)) {
            if (!pinLockIsShown) {
                askPinToLock();
            }
        }
    }

    private void askPinToLock() {
        pinLockIsShown = true;
        final AlertDialog askForPinDailog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.ask_pin_layout, null);
        askForPinDailog.setView(alertView);
        askForPinDailog.setCancelable(false);
        askForPinDailog.show();
        // Log.d(TAG,"delete status :"+deleteStatus);
        // dialog.setContentView(R.layout.opt_auth_layout);
        //    final TextView otpAuthMsg=(TextView)alertView.findViewById(R.id.otpAuthMsg);
        final VerifierLoginResponse verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        final EditText pinET = (EditText) alertView.findViewById(R.id.deletPinET);
        final TextView errorTV = (TextView) alertView.findViewById(R.id.invalidOtpTV);

        wrongAttempetCountText = (TextView) alertView.findViewById(R.id.wrongAttempetCountText);
        wrongAttempetCountValue = (TextView) alertView.findViewById(R.id.wrongAttempetCountValue);


        pinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //   errorTV.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Button proceedBT = (Button) alertView.findViewById(R.id.proceedBT);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        proceedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                currentTime = System.currentTimeMillis();
                try {

                    wrongPinSavedTime = Long.parseLong(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, context));
                } catch (Exception ex) {
                    wrongPinSavedTime = 0;
                }
                if (currentTime > (wrongPinSavedTime + millisecond24)) {


                    //  AppUtility.softKeyBoard(activity, 0);
                    String pin = pinET.getText().toString();
                    if (pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                        askForPinDailog.dismiss();
                        pinLockIsShown = false;
                    } else if (pin.equalsIgnoreCase("")) {
                        // CustomAlert.alertWithOk(context,"Please enter valid pin");
                        errorTV.setVisibility(View.VISIBLE);
                        errorTV.setText("Enter pin");
                        pinET.setText("");
                        //  pinET.setHint("");
                    } else if (!pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {

                        if (wrongPinCount >= 2) {
                            errorTV.setTextColor(context.getResources().getColor(R.color.red));
                            wrongAttempetCountValue.setTextColor(context.getResources().getColor(R.color.red));
                            wrongAttempetCountText.setTextColor(context.getResources().getColor(R.color.red));
                        }
                        wrongPinCount++;
                        wrongAttempetCountValue.setText((3 - wrongPinCount) + "");
                        if (wrongPinCount > 2) {
                            long time = System.currentTimeMillis();
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.WORNG_PIN_ENTERED_TIMESTAMP, time + "", context);
                            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.youHaveExceedPinLimit));
                        } else {
                            errorTV.setVisibility(View.VISIBLE);
                            errorTV.setText("Enter correct pin");
                            pinET.setText("");
//                        CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidPin));
//                        pinET.setText("");
                        }

//                    errorTV.setVisibility(View.VISIBLE);
//                    errorTV.setText("Enter correct pin");
//                    pinET.setText("");
                        // pinET.setHint("Enter 4-di");
                    }
                } else {

                    //alert  when pin login is diabled for 24 hrs
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.pinLoginDisabled));
                    errorTV.setText("Pin login disabled for 24 hrs.");
                    pinET.setText("");
                    return;
                }
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinLockIsShown = false;
                Intent intent_login = new Intent(context, LoginActivity.class);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_login);
                finish();
            }
        });
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTrimMemory(final int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            if (!pinLockIsShown) {
                askPinToLock();
            }
        }
    }
}
