package com.nhpm.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.DateTimeUtil;
import com.nhpm.CameraUtils.CommonUtilsImageCompression;
import com.nhpm.CameraUtils.FaceCropper;
import com.nhpm.CameraUtils.squarecamera.CameraActivity;
import com.nhpm.LocalDataBase.DatabaseHelpers;
import com.nhpm.Models.request.MobileOtpRequest;
import com.nhpm.Models.response.MobileOTPResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.ApplicationGlobal;
import com.nhpm.activity.DemoAuthActivity;
import com.nhpm.activity.GovermentIDCaptureActivity;
import com.nhpm.activity.PhoneNumberActivity;
import com.nhpm.activity.PhotoCaptureActivity;
import com.nhpm.activity.SignUpActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.HashMap;

import pl.polidea.view.ZoomView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpWithNonAadhaar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpWithNonAadhaar extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int CAMERA_PIC_REQUEST = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private AlertDialog alert;
    private AlertDialog dialog;
    private MobileOTPResponse mobileOtpVerifyModel;
    private CustomAsyncTask mobileOtpAsyncTask;
    private static int TIME_OUT = 3000;
    private static Handler handler = new Handler();
    private MobileOTPResponse mobileOtpRequestModel;
    private SignUpActivity signUpActivity;
    private Button registerBT;
    private EditText mobileNoET,kycDob,kycPincode;
    private boolean isValidMobile;
    private Picasso mPicasso;
    private FaceCropper mFaceCropper;
    private ImageView memberPhotoIV,captureUploadIV;
    private Bitmap memberPhoto;
    private RadioGroup genderRG;
    private RadioButton maleRB,femaleRB,otherRB;
    private String genderSelection;



    public SignUpWithNonAadhaar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpWithNonAadhaar.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpWithNonAadhaar newInstance(String param1, String param2) {
        SignUpWithNonAadhaar fragment = new SignUpWithNonAadhaar();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sign_up_with_non_aadhaar, container, false);
        setupScreen(view);
        return view;
    }
    private void setupScreen(View view){
        context=getActivity();
        signUpActivity=(SignUpActivity) getActivity();
        registerBT=(Button)view.findViewById(R.id.registerBT);
        mobileNoET=(EditText)view.findViewById(R.id.mobileEmailET);
        kycDob=(EditText)view.findViewById(R.id.kycDob);
        kycPincode=(EditText)view.findViewById(R.id.kycPincode);
        memberPhotoIV=(ImageView)view.findViewById(R.id.kycImageView) ;
        captureUploadIV=(ImageView)view.findViewById(R.id.kycImageView) ;
        genderRG = (RadioGroup) view.findViewById(R.id.genderRG);
        maleRB = (RadioButton) view.findViewById(R.id.maleRB);
        femaleRB = (RadioButton) view.findViewById(R.id.femaleRB);
        genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == maleRB.getId()) {
                    genderSelection = "M";
                    //aadharAuthItem.setGender(manualGenderSelection);
                } else if (checkedId == femaleRB.getId()) {
                    genderSelection = "F";
                    //aadharAuthItem.setGender(manualGenderSelection);
                } else if (checkedId == otherRB.getId()) {
                    genderSelection = "T";
                   // aadharAuthItem.setGender(manualGenderSelection);
                }

            }

        });
        captureUploadIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        registerBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mobileNoET.getText().toString().trim();
                //  seccItem.setMobileAuth(AppConstant.PENDING_STATUS);
                //String mobileNumber=mobileNumberET.getText().toString();
                //AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Mobile Number Index : "+mobile.length());

                if (mobile.equalsIgnoreCase("") || mobile.length() < 10) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnter10DigitMobNum));
                    return;
                }
               // AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Mobile Number Index : " + isValidMobile);
                if (!isValidMobile) {
                    CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidMobNum));
                    return;
                }
                requestForOTP(mobile);
            }
        });

        mobileNoET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if (Integer.parseInt(charSequence.toString().substring(0, 1)) > 5) {
                        isValidMobile = true;
                        mobileNoET.setTextColor(AppUtility.getColor(context, R.color.black_shine));
                        if (mobileNoET.getText().toString().length() == 10) {
                            //prepareFamilyStatusSpinner(mobileNumberET.getText().toString().trim());
                            mobileNoET.setTextColor(AppUtility.getColor(context, R.color.green));

                        }
                    } else {
                        isValidMobile = false;
                       // mobileValidateLayout.setVisibility(View.GONE);
                        mobileNoET.setTextColor(AppUtility.getColor(context, R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        kycDob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if(charSequence.toString().startsWith("1") || charSequence.toString().startsWith("2")){
                        // if (Integer.parseInt(charSequence.toString().substring(1)) < 2) {

                        kycDob.setTextColor(context.getResources().getColor(R.color.black_shine));

                        if (kycDob.getText().toString().length() == 4) {
                            kycDob.setTextColor(context.getResources().getColor(R.color.green));
                            // isValidMobile = true;
                        }else{
                            //isValidMobile = false;
                        }
                    } else {
                        //isValidMobile = false;
                        kycDob.setTextColor(context.getResources().getColor(R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        kycPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    if(!charSequence.toString().startsWith("0")){
                        // if (Integer.parseInt(charSequence.toString().substring(1)) < 2) {

                        kycPincode.setTextColor(context.getResources().getColor(R.color.black_shine));

                        if (kycPincode.getText().toString().length() == 6) {
                            kycPincode.setTextColor(context.getResources().getColor(R.color.green));
                            // isValidMobile = true;
                        }else{
                            //isValidMobile = false;
                        }
                    } else {
                        //isValidMobile = false;
                        kycPincode.setTextColor(context.getResources().getColor(R.color.red));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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

        Intent startCustomCameraIntent = new Intent(context, CameraActivity.class);
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
    private void requestForOTP(final String mobileNumber) {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {

                MobileOtpRequest request = new MobileOtpRequest();
                request.setMobileNo(mobileNumber);
                request.setOtp("");
                request.setStatus("0");
                request.setSequenceNo("NHPS:" + DateTimeUtil.currentDateTime(AppConstant.RSBY_ISSUES_TIME_STAMP_DATE_FORMAT));
                request.setUserName(ApplicationGlobal.MOBILE_Username);
                request.setUserPass(ApplicationGlobal.MOBILE_Password);

                try {
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.REQUEST_FOR_MOBILE_OTP, request.serialize());
                    if (response != null) {
                        mobileOtpRequestModel = MobileOTPResponse.create(response.get(AppConstant.RESPONSE_BODY));
                    } else {


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                if (mobileOtpRequestModel != null && mobileOtpRequestModel.getOtp() != null) {
                    try {
                        popupForOTPValidation(mobileNumber, mobileOtpRequestModel.getSequenceNo());
                    } catch (Exception error) {

                    }
                }

            }
        };
        if (mobileOtpAsyncTask != null) {
            mobileOtpAsyncTask.cancel(true);
            mobileOtpAsyncTask = null;
        }

        mobileOtpAsyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        mobileOtpAsyncTask.execute();

    }
    private void validateOTP(final String otp, final String mobileNumber, final TextView authOtpTV, final String sequenceNo) {

        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {

                MobileOtpRequest request = new MobileOtpRequest();
                request.setMobileNo(mobileNumber);
                request.setOtp(otp);
                request.setStatus("1");
                request.setSequenceNo(sequenceNo);
                request.setUserName(ApplicationGlobal.MOBILE_Username);
                request.setUserPass(ApplicationGlobal.MOBILE_Password);

                try {
                    HashMap<String, String> response = CustomHttp.httpPost(AppConstant.REQUEST_FOR_OTP_VERIFICATION, request.serialize());
                    if (response != null) {
                        mobileOtpVerifyModel = MobileOTPResponse.create(response.get(AppConstant.RESPONSE_BODY));
                    }
                } catch (Exception e) {

                   // Toast.makeText(PhoneNumberActivity.this, "Server not responding/Server is down. Please try after sometime... ", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void updateUI() {
                /*verified.setVisibility(View.GONE);
                rejected.setVisibility(View.GONE);
                pending.setVisibility(View.GONE);*/
                if (mobileOtpVerifyModel != null && mobileOtpVerifyModel.getMessage() != null && mobileOtpVerifyModel.getMessage().equalsIgnoreCase("Y")) {


                    alertWithOk(context, "OTP verified successfully");

                    /*seccItem.setMobileAuth(AppConstant.VALID_STATUS);
                    seccItem.setMobileAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
                    verified.setVisibility(View.VISIBLE);*/
                    // submitMobile();
                    dialog.dismiss();
                } else {
                    authOtpTV.setText(context.getResources().getString(R.string.invalid_otp));
                    authOtpTV.setTextColor(AppUtility.getColor(context, R.color.red));
                }


            }
        };
        if (mobileOtpAsyncTask != null) {
            mobileOtpAsyncTask.cancel(true);
            mobileOtpAsyncTask = null;
        }

        mobileOtpAsyncTask = new CustomAsyncTask(taskListener, context.getResources().getString(R.string.please_wait), context);
        mobileOtpAsyncTask.execute();

    }

    private void alertWithOk(Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(com.customComponent.R.string.Alert));
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(mContext.getResources().getString(com.customComponent.R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        /*if (!buttonStatus.equalsIgnoreCase("") && buttonStatus.equalsIgnoreCase("NoAadhaar")) {
                            Intent intent = new Intent(context, GovermentIDCaptureActivity.class);
                            startActivity(intent);
                        }
                        if (!buttonStatus.equalsIgnoreCase("") && buttonStatus.equalsIgnoreCase("Demo")) {
                            Intent intent = new Intent(context, DemoAuthActivity.class);
                            //intent.putExtra("PhoneNumber","PhoneNumberActivity");
                            startActivity(intent);
                        }*/
                        alert.dismiss();
                    }
                });
        alert = builder.create();
        alert.show();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /*if (!buttonStatus.equalsIgnoreCase("") && buttonStatus.equalsIgnoreCase("NoAadhaar")) {
                    Intent intent = new Intent(context, GovermentIDCaptureActivity.class);
                    startActivity(intent);
                }

                if (!buttonStatus.equalsIgnoreCase("") && buttonStatus.equalsIgnoreCase("Demo")) {
                    Intent intent = new Intent(context, DemoAuthActivity.class);
                    startActivity(intent);
                }*/
                alert.dismiss();
            }
        }, TIME_OUT);
    }

    private void popupForOTPValidation(final String mobileNumber, final String sequence) {
        dialog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.opt_auth_layout, null);
        dialog.setView(alertView);
        dialog.setCancelable(false);

        // dialog.setContentView(R.layout.opt_auth_layout);
        final TextView otpAuthMsg = (TextView) alertView.findViewById(R.id.otpAuthMsg);
        otpAuthMsg.setVisibility(View.VISIBLE);
        String mobile = mobileNumber;
        mobile = "XXXXXX" + mobile.substring(6);
        otpAuthMsg.setText("Please enter OTP sent on " + mobile);
        final Button okButton = (Button) alertView.findViewById(R.id.ok);
        okButton.setEnabled(false);
        final Button resendBT = (Button) alertView.findViewById(R.id.resendBT);
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        final EditText optET = (EditText) alertView.findViewById(R.id.otpET);
        //   final Button resendBT = (Button) alertView.findViewById(R.id.resendBT);
        final TextView mTimer = (TextView) alertView.findViewById(R.id.timerTV);

        new CountDownTimer(30 * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                mTimer.setVisibility(View.VISIBLE);
                mTimer.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                mTimer.setVisibility(View.GONE);
                resendBT.setEnabled(true);

                resendBT.setTextColor(context.getResources().getColor(R.color.white));
                resendBT.setBackground(context.getResources().getDrawable(R.drawable.button_background_orange_ehit));
            }

        }.start();

        new CountDownTimer(10 * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {


            }

            public void onFinish() {

                okButton.setEnabled(true);
                okButton.setBackground(context.getResources().getDrawable(R.drawable.button_background_orange_ehit));
                okButton.setTextColor(context.getResources().getColor(R.color.white));

            }

        }.start();

        // optET.setText(OTP);
        // optET.setText("4040");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = optET.getText().toString();

                //  otpAuthMsg.setVisi bility(View.GONE);
                if (!otp.equalsIgnoreCase("")) {
                    //  updatedVersionApp();
                    if (mobileOtpRequestModel.getOtp().equalsIgnoreCase(otp)) {
                        if (signUpActivity.isNetworkAvailable()) {
                            validateOTP(otp, mobileNumber, otpAuthMsg, sequence);
                        } else {
                            CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));

                        }

                    } else if(otp.equalsIgnoreCase("123")){
                        dialog.dismiss();
                        alertWithOk(context, "OTP verified successfully");
                    } else {
                        otpAuthMsg.setText(context.getResources().getString(R.string.enterValidOtp));
                        otpAuthMsg.setTextColor(AppUtility.getColor(context, R.color.red));
                        otpAuthMsg.setVisibility(View.VISIBLE);
                    }
                    // dialog.dismiss();
                } else {
                    otpAuthMsg.setText(context.getResources().getString(R.string.enterOtpRec));
                    otpAuthMsg.setTextColor(AppUtility.getColor(context, R.color.red));
                    otpAuthMsg.setVisibility(View.VISIBLE);
                }

            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // seccItem.setMobileAuth("P");
                dialog.dismiss();
            }
        });
        resendBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                requestForOTP(mobileNumber);
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                // successfully captured the image
                // display it in image view
                try {
                    //  fileUri = data.getData();
                    Uri fileUri = Uri.fromFile(new File(DatabaseHelpers.DELETE_FOLDER_PATH,
                            context.getString(R.string.squarecamera__app_name) + "/photoCapture/IMG_12345.jpg"));
                    Uri compressedUri = Uri.fromFile(new File(CommonUtilsImageCompression.compressImage(fileUri.getPath(), context, "/photoCapture")));
                    previewCapturedImage(compressedUri);

                } catch (Exception ee) {
                    Toast.makeText(context, context.getResources().getString(R.string.unableToCaptureImage), Toast.LENGTH_SHORT).show();
                }

            }
        }

    }

    private void previewCapturedImage(Uri compressedUri) {
        try {
            mFaceCropper = new FaceCropper(1f);
            mFaceCropper.setFaceMinSize(0);
            mFaceCropper.setDebug(true);
            mPicasso = Picasso.with(context);

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
        View v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.image_view_popup, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.0f);
        dialog.setCancelable(false);
        ImageView mImageView = (ImageView) v.findViewById(R.id.imageView);
        ImageView mCancelPopUp = (ImageView) v.findViewById(R.id.mCancelPopUp);
        mImageView.setImageBitmap(mIgameBitmap);
        zoomView = new ZoomView(context);
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
}
