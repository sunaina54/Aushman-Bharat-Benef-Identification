package com.nhpm.fragments;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.Models.request.FamilyDetailsItemModel;
import com.nhpm.Models.request.GetMemberDetail;
import com.nhpm.Models.request.PersonalDetailItem;
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.Models.response.FamilyDetailResponse;
import com.nhpm.Models.response.GenericResponse;
import com.nhpm.Models.response.PersonalDetailResponse;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.PrintCard.PrintCardMainActivity;
import com.nhpm.PrintCard.UsbHelper;
import com.nhpm.PrintCard.UsbPermissionRequestor;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.CollectDataActivity;
import com.nhpm.activity.FamilyMembersListActivity;
import com.nhpm.activity.LoginActivity;
import com.nhpm.activity.ViewMemberDataActivity;
import com.pointman.mobiledesigner.PointManJNI;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static com.nhpm.PrintCard.UsbHelper.ACTION_USB_PERMISSION;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewPrintCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPrintCardFragment extends Fragment implements UsbPermissionRequestor {
    private static final String LOG_TAG = PrintCardMainActivity.class.getName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    public final String ACTION_USB_PERMISSION1 = "com.android.scard.USB_PERMISSION";
    private boolean usbPermissionGranted = false;
    private boolean receivedUsbPermissionResponse = false;
    private String printerManufratureName;
    private UsbDevice udevice;
    private UsbManager mManager;
    private PointManJNI jni;
    private String formattedDate;
    private String nameString = "HOF: ";
    private Button previousBT, printCardBT, otherFamilyMemberBT;
    private FragmentTransaction fragmentTransection;
    private FragmentManager fragmentManager;
    private TextView nameTV, fatherNameTV, yobTV, genderTV, cardNumberTV, stateTV;
    //private CollectDataActivity activity;
    private ViewMemberDataActivity activity;
    private GetMemberDetail getMemberDetail;
    private DocsListItem beneficiaryListItem;
    private ImageView beneficiaryPhotoIV;
    private String familyResponse;
    private GenericResponse genericResponse;
    private CustomAsyncTask customAsyncTask;
    private VerifierLoginResponse verifierDetail;


    public ViewPrintCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrintCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPrintCardFragment newInstance(String param1, String param2) {
        ViewPrintCardFragment fragment = new ViewPrintCardFragment();
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

    private void setupScreen(View view) {
        context = getActivity();
        fragmentManager = getActivity().getSupportFragmentManager();
        verifierDetail = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.VERIFIER_CONTENT, context));
        nameTV = (TextView) view.findViewById(R.id.nameTV);
        cardNumberTV = (TextView) view.findViewById(R.id.cardNumberTV);
        fatherNameTV = (TextView) view.findViewById(R.id.fatherNameTV);
        genderTV = (TextView) view.findViewById(R.id.genderTV);
        yobTV = (TextView) view.findViewById(R.id.yobTV);

        previousBT = (Button) view.findViewById(R.id.previousBT);
        printCardBT = (Button) view.findViewById(R.id.printCardBT);
        otherFamilyMemberBT = (Button) view.findViewById(R.id.otherMemberBT);
        beneficiaryPhotoIV = (ImageView) view.findViewById(R.id.beneficiaryPhotoIV);
        stateTV = (TextView) view.findViewById(R.id.stateTV);

        if (getMemberDetail != null) {
            if (getMemberDetail.getAhl_tin() != null &&
                    !getMemberDetail.getAhl_tin().equalsIgnoreCase("")) {
                String ahltin = getMemberDetail.getAhl_tin();
                if(getMemberDetail.getDataSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE_NEW)){
                    cardNumberTV.setText(ahltin);
                }else {
                    if (ahltin != null && !ahltin.equalsIgnoreCase("")) {
                        Log.d("TAG", "AhlTine : " + ahltin);
                        String firstTwoChar = ahltin.substring(0, 2);
                        // 2 7 8 5 4 3
                        String nextSevenChar = ahltin.substring(2, 9);
                        String nextEightChar = ahltin.substring(9, 17);
                        String nextFiveChar = ahltin.substring(17, 22);
                        String nextFourChar = ahltin.substring(22, 26);
                        String lastThreeChar = ahltin.substring(26, 29);
                        Log.d("TAG", "AhlTine : " + ahltin);
                        Log.d("TAG", "First TTwo : " + firstTwoChar);
                        Log.d("TAG", "next seven : " + nextSevenChar);
                        Log.d("TAG", "next eight : " + nextEightChar);
                        Log.d("TAG", "next five : " + nextFiveChar);
                        Log.d("TAG", "next four : " + nextFourChar);
                        Log.d("TAG", "next three : " + lastThreeChar);
                        ahltin = firstTwoChar + " " + nextSevenChar + "  " + nextEightChar + "  " + nextFiveChar + " " + nextFourChar + " " + lastThreeChar;
                        Log.d("TAG", "Ayushman Id  : " + ahltin);
                        Log.d("TAG", "Ayushman Id  : " + ahltin);
                        cardNumberTV.setText(ahltin);
                    }
                }
                if(getMemberDetail.getPersonalDetail() != null) {

                    if (getMemberDetail.getPersonalDetail() != null &&
                            getMemberDetail.getPersonalDetail().getBenefPhoto() != null) {
                        beneficiaryPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(
                                getMemberDetail.getPersonalDetail().getBenefPhoto()));
                    }
                    if (getMemberDetail.getPersonalDetail() != null && getMemberDetail.getPersonalDetail().getBenefName() != null) {
                        nameTV.setText(getMemberDetail.getPersonalDetail().getBenefName());
                    }

                    if(getMemberDetail.getPersonalDetail().getFatherNameSecc()!=null){
                        fatherNameTV.setText(getMemberDetail.getPersonalDetail().getFatherNameSecc());
                    }

                    if (getMemberDetail.getPersonalDetail().getDobBen()!= null && getMemberDetail.getPersonalDetail().getDobBen().length() > 4) {

                        String currentYear = DateTimeUtil.currentDate(AppConstant.DATE_FORMAT);

                        currentYear = currentYear.substring(0, 4);
                        String date=DateTimeUtil.
                                convertTimeMillisIntoStringDate(DateTimeUtil.convertDateIntoTimeMillis(getMemberDetail.getPersonalDetail().getDobBen()),AppConstant.DATE_FORMAT);
                        String arr[];
                        String aadhaarYear=null;
                        if(getMemberDetail.getPersonalDetail().getDobBen().contains("-")){
                            arr=getMemberDetail.getPersonalDetail().getDobBen().split("-") ;
                            if(arr[0].length()==4){
                                aadhaarYear=arr[0];
                            }else if(arr[2].length()==4){
                                aadhaarYear=arr[2];
                            }
                        }else if(getMemberDetail.getPersonalDetail().getDobBen().contains("/")){
                            arr=getMemberDetail.getPersonalDetail().getDobBen().split("/") ;
                            if(arr[0].length()==4){
                                aadhaarYear=arr[0];
                            }else if(arr[2].length()==4){
                                aadhaarYear=arr[2];
                            }
                        }
                        if(aadhaarYear!=null) {
                            //int age = Integer.parseInt(currentYear) - Integer.parseInt(aadhaarYear);
                            yobTV.setText(aadhaarYear + "");
                        }

                    }else  if(getMemberDetail.getPersonalDetail().getDobBen() != null && getMemberDetail.getPersonalDetail().getDobBen().length() ==4){
                    /*    String currentYear = DateTimeUtil.currentDate("dd-mm-yyyy");
                        currentYear = currentYear.substring(6, 10);
                        int age = Integer.parseInt(currentYear) - Integer.parseInt(getMemberDetail.getPersonalDetail().getDobBen());
                        yobTV.setText(age + "");*/
                        yobTV.setText(getMemberDetail.getPersonalDetail().getDobBen());
                    }
/*
                    if(getMemberDetail.getPersonalDetail().getDobBen()!=null){
                        yobTV.setText(getMemberDetail.getPersonalDetail().getYobSecc());
                    }*/

                    if(getMemberDetail.getPersonalDetail().getStateNameBen()!=null){
                        stateTV.setText(getMemberDetail.getPersonalDetail().getStateNameBen());
                    }

                    if (getMemberDetail.getPersonalDetail().getGenderBen() != null &&
                            getMemberDetail.getPersonalDetail().getGenderBen().equalsIgnoreCase("1")
                            ||  getMemberDetail.getPersonalDetail().getGenderBen().substring(0, 1).toUpperCase().equalsIgnoreCase("M")) {
                        genderTV.setText("Male");
                    } else if (getMemberDetail.getPersonalDetail().getGenderBen() != null &&
                            getMemberDetail.getPersonalDetail().getGenderBen().equalsIgnoreCase("2")
                            ||  getMemberDetail.getPersonalDetail().getGenderBen().substring(0, 1).toUpperCase().equalsIgnoreCase("F") ) {
                        genderTV.setText("Female");
                    } else {
                        genderTV.setText("Other");
                    }
                }

                /*if (beneficiaryListItem != null) {
                    if (beneficiaryListItem.getFathername() != null) {
                        fatherNameTV.setText(beneficiaryListItem.getFathername());
                    }
                    if (beneficiaryListItem.getDob() != null && beneficiaryListItem.getDob().length() >= 4) {
                        yobTV.setText(beneficiaryListItem.getDob().substring(0, 4));
                    }
                    if (beneficiaryListItem.getState_name() != null) {
                        stateTV.setText(beneficiaryListItem.getState_name());
                    }

                    if (beneficiaryListItem.getGenderid() != null &&
                            beneficiaryListItem.getGenderid().equalsIgnoreCase("1")
                            ||  beneficiaryListItem.getGenderid().substring(0, 1).toUpperCase().equalsIgnoreCase("M")) {
                        genderTV.setText("Male");
                    } else if (beneficiaryListItem.getGenderid() != null &&
                            beneficiaryListItem.getGenderid().equalsIgnoreCase("2")
                            || beneficiaryListItem.getGenderid().substring(0, 1).toUpperCase().equalsIgnoreCase("F") ) {
                        genderTV.setText("Female");
                    } else if (beneficiaryListItem.getGenderid() != null &&
                            beneficiaryListItem.getGenderid().equalsIgnoreCase("3")) {
                        genderTV.setText("Other");
                    }
                }*/
            }
        }
       /* if(beneficiaryListItem!=null && beneficiaryListItem.getPrintCardDetail()!=null){
            activity.printEcardLL.setBackground(context.getResources().getDrawable(R.drawable.arrow));


            cardNumberTV.setText(beneficiaryListItem.getPrintCardDetail().getCardNo());
            nameTV.setText( beneficiaryListItem.getPrintCardDetail().getNameOnCard());
            fatherNameTV.setText( beneficiaryListItem.getPrintCardDetail().getFatherNameOnCard());
            yobTV.setText( beneficiaryListItem.getPrintCardDetail().getYobObCard());
            stateTV.setText(beneficiaryListItem.getState_name());
            beneficiaryPhotoIV.setImageBitmap(AppUtility.convertStringToBitmap(beneficiaryListItem.getPrintCardDetail().getBenefPhoto()));
            if(beneficiaryListItem.getPrintCardDetail().getGenderOnCard()!=null &&
                    beneficiaryListItem.getPrintCardDetail().getGenderOnCard().equalsIgnoreCase("1")) {
                genderTV.setText("Male");
            }else if(beneficiaryListItem.getPrintCardDetail().getGenderOnCard()!=null &&
                    beneficiaryListItem.getPrintCardDetail().getGenderOnCard().equalsIgnoreCase("2")) {
                genderTV.setText("Female");
            }else if(beneficiaryListItem.getPrintCardDetail().getGenderOnCard()!=null &&
                    beneficiaryListItem.getPrintCardDetail().getGenderOnCard().equalsIgnoreCase("3")){
                genderTV.setText("Other");
            }

        }*/

        otherFamilyMemberBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        printCardBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CustomAlert.alertWithOk(context, "Under Developement");
            }
        });

        previousBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FamilyDetailsFragment();
                fragmentTransection = fragmentManager.beginTransaction();
                fragmentTransection.add(R.id.fragContainer, fragment);
                fragmentTransection.commitAllowingStateLoss();
            }
        });
        mManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
        formattedDate = df.format(c.getTime());
        nameString = getActivity().getIntent().getStringExtra("NAMEONCARD");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_print_card, container, false);
        setupScreen(view);
        return view;

    }

    public final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, @NonNull Intent intent) {
            Log.i(LOG_TAG, "USB BroadcastReceiver onReceive");
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION1.equals(action)) {
                synchronized (this) {
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        usbPermissionGranted = true;
                        receivedUsbPermissionResponse = true;
                        if (printerManufratureName != null && !printerManufratureName.equalsIgnoreCase("")
                                && !printerManufratureName.equalsIgnoreCase("ZEBRA")) {
                            UsbDevice device = (UsbDevice) intent
                                    .getParcelableExtra(UsbManager.EXTRA_DEVICE);
                            if (intent.getBooleanExtra(
                                    UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                                if (device != null) {
                                    // pointman connect
                                    udevice = device;
                                    // FROM NATIVE
                                    String dname = udevice.getDeviceName();
                                    final UsbDeviceConnection conn = mManager.openDevice(udevice);
                                    int fd = conn.getFileDescriptor();
                                    if (fd == -1) {
                                        Log.d("Javelin", "Fails to open DeviceConnection");
                                    } else {
                                        Log.d("Javelin", "Opened DeviceConnection" + Integer.toString(conn.getFileDescriptor()));
                                    }
                                    //javelin interface 1
                                    int ret = jni.OpenDevice(dname, fd, 1);
                                    if (ret == 0) {
                                        // printtaskjavelin();
                                    }
                                }
                            } else {
                            }
                        }
                    } else {
                        Log.i(LOG_TAG, "USB BroadcastReceiver onReceive - EXTRA_PERMISSION_GRANTED = false");
                        usbPermissionGranted = false;
                        receivedUsbPermissionResponse = true;
                    }
                }
            } else {
                Log.i(LOG_TAG, "USB BroadcastReceiver onReceive - not ACTION_USB_PERMISSION");
            }
        }
    };


    @Override
    public void requestUsbPermission(UsbDevice usbDevice) {
        //Log.i(LOG_TAG, "requestUsbPermission");

        PendingIntent permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        UsbManager usbManager = UsbHelper.getUsbManager(context);
        usbManager.requestPermission(usbDevice, permissionIntent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (ViewMemberDataActivity) context;
        getMemberDetail = activity.getMemberDetailItem;
        beneficiaryListItem = activity.benefItem;
    }

    private void submitMemberData() {
        TaskListener taskListener = new TaskListener() {
            @Override
            public void execute() {
                try {
                    PersonalDetailItem personalDetailItem = beneficiaryListItem.getPersonalDetail();
                    FamilyDetailsItemModel familyMemberModel = beneficiaryListItem.getFamilyDetailsItemModel();

                    GetMemberDetail request = new GetMemberDetail();
                    request.setAhl_tin(beneficiaryListItem.getAhl_tin());
                    request.setHhd_no(beneficiaryListItem.getHhd_no());
                    if (beneficiaryListItem.getSource() != null && beneficiaryListItem.getSource().equalsIgnoreCase(AppConstant.RSBY_SOURCE_NEW)) {
                        request.setDataSource(AppConstant.RSBY_SOURCE_NEW);
                    } else {
                        request.setDataSource(AppConstant.SECC_SOURCE_NEW);

                    }
                    request.setStatecode(Integer.parseInt(beneficiaryListItem.getState_code()));

                    PersonalDetailResponse personalDetail = new PersonalDetailResponse();
                    personalDetail.setBenefName(personalDetailItem.getBenefName());
                    personalDetail.setBenefPhoto(personalDetailItem.getBenefPhoto());
                    personalDetail.setGovtIdNo(personalDetailItem.getGovtIdNo());
                    personalDetail.setGovtIdType(personalDetailItem.getGovtIdType());
                    personalDetail.setIdPhoto(personalDetailItem.getIdPhoto());
                    personalDetail.setIsAadhar(personalDetailItem.getIsAadhar());
                    personalDetail.setMobileNo(personalDetailItem.getMobileNo());
                    personalDetail.setName(personalDetailItem.getName());
                    personalDetail.setNameMatchScore(personalDetailItem.getNameMatchScore());
                    personalDetail.setIsMobileAuth(personalDetailItem.getIsMobileAuth());
                    personalDetail.setOpertaorid(personalDetailItem.getOpertaorid());
                    personalDetail.setFlowStatus(personalDetailItem.getFlowStatus());


                    FamilyDetailResponse familyDetail = new FamilyDetailResponse();
                    familyDetail.setFamilyMemberModels(familyMemberModel.getFamilyMemberModels());
                    familyDetail.setFamilyMatchScore(familyMemberModel.getFamilyMatchScore());
                    familyDetail.setIdImage(familyMemberModel.getIdImage());
                    familyDetail.setIdNumber(familyDetail.getIdNumber());
                    familyDetail.setIdType(familyDetail.getIdType());

                    request.setPersonalDetail(personalDetail);
                    request.setFamilyDetailsItem(familyDetail);


                    String syncRequest = request.serialize();
                    HashMap<String, String> apiResponse = CustomHttp.httpPostWithTokken(AppConstant.SUBMIT_MEMBER_ADDITIONAL_DATA, syncRequest, AppConstant.AUTHORIZATION, verifierDetail.getAuthToken());
                    familyResponse = apiResponse.get("response");

                    if (familyResponse != null) {
                        genericResponse = new GenericResponse().create(familyResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void updateUI() {
                if (genericResponse != null) {
                    if (genericResponse.isStatus()) {
                        Intent intent = new Intent(context, FamilyMembersListActivity.class);
                        CustomAlert.alertWithOk(context, getResources().getString(R.string.print_card_message), intent);
                    } else if (genericResponse != null && genericResponse.getErrorCode() != null &&
                            genericResponse.getErrorCode().equalsIgnoreCase(AppConstant.SESSION_EXPIRED)
                            || genericResponse.getErrorCode().equalsIgnoreCase(AppConstant.INVALID_TOKEN)) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        CustomAlert.alertWithOkLogout(context, genericResponse.getErrorMessage(), intent);

                    } else {
                        //server error
                        CustomAlert.alertWithOk(context, genericResponse.getErrorMessage());
                    }
                } else {
                    CustomAlert.alertWithOk(context, "Server Error");

                }
            }
        };
        if (customAsyncTask != null) {
            customAsyncTask.cancel(true);
            customAsyncTask = null;
        }

        customAsyncTask = new CustomAsyncTask(taskListener, "Please wait", context);
        customAsyncTask.execute();
    }
}
