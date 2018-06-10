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
import com.nhpm.Models.response.DocsListItem;
import com.nhpm.PrintCard.PrintCardMainActivity;
import com.nhpm.PrintCard.UsbHelper;
import com.nhpm.PrintCard.UsbPermissionRequestor;
import com.nhpm.R;
import com.nhpm.Utility.AppUtility;
import com.nhpm.activity.CollectDataActivity;
import com.pointman.mobiledesigner.PointManJNI;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.nhpm.PrintCard.UsbHelper.ACTION_USB_PERMISSION;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrintCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrintCardFragment extends Fragment implements UsbPermissionRequestor {
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
    private Button previousBT,printCardBT,otherFamilyMemberBT;
    private FragmentTransaction fragmentTransection;
    private FragmentManager fragmentManager;
    private TextView nameTV,fatherNameTV,yobTV,genderTV;
    private CollectDataActivity activity;
    private DocsListItem beneficiaryListItem;
    private ImageView beneficiaryPhotoIV;


    public PrintCardFragment() {
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
    public static PrintCardFragment newInstance(String param1, String param2) {
        PrintCardFragment fragment = new PrintCardFragment();
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

    private void setupScreen(View view){
        context=getActivity();
        fragmentManager = getActivity().getSupportFragmentManager();
        nameTV=(TextView)view.findViewById(R.id.nameTV) ;
        fatherNameTV=(TextView)view.findViewById(R.id.fatherNameTV) ;
        genderTV=(TextView)view.findViewById(R.id.genderTV) ;
        yobTV=(TextView)view.findViewById(R.id.yobTV) ;

        previousBT = (Button) view.findViewById(R.id.previousBT);
        printCardBT = (Button) view.findViewById(R.id.printCardBT);
        otherFamilyMemberBT = (Button) view.findViewById(R.id.otherMemberBT);
        beneficiaryPhotoIV=(ImageView)view.findViewById(R.id.beneficiaryPhotoIV);
        if(beneficiaryListItem!=null && beneficiaryListItem.getPrintCardDetail()!=null){
            activity.printEcardLL.setBackground(context.getResources().getDrawable(R.drawable.arrow));
            nameTV.setText( beneficiaryListItem.getPrintCardDetail().getNameOnCard());
            fatherNameTV.setText( beneficiaryListItem.getPrintCardDetail().getFatherNameOnCard());
            yobTV.setText( beneficiaryListItem.getPrintCardDetail().getYobObCard());
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
        }

        otherFamilyMemberBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        printCardBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlert.alertWithOk(context,"Under Developement");
            }
        });

        previousBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FamilyDetailsFragment();
                //Bundle args = new Bundle();

           /*     if(personalDetailItem!=null && personalDetailItem.getFamilyDetailsItem()!=null){
                    familyDetailsItemModel= personalDetailItem.getFamilyDetailsItem();
                    personalDetailItem.setFamilyDetailsItem(familyDetailsItemModel);

                } else {*/


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
        View view = inflater.inflate(R.layout.fragment_print_card, container, false);
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
        activity = (CollectDataActivity) context;

        beneficiaryListItem = activity.benefItem;
    }
}
