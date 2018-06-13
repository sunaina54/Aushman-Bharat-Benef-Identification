package com.nhpm.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.customComponent.CustomAlert;
import com.customComponent.utility.CustomHttp;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.AadhaarUtils.CheckConnection;
import com.nhpm.AadhaarUtils.Global;
import com.nhpm.AadhaarUtils.VerhoeffAadhar;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.request.PersonalDetailItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.AadhaarResponseItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.nhpm.Utility.ApplicationGlobal;
import com.nhpm.activity.CaptureAadharDetailActivity;
import com.nhpm.activity.EkycActivity;
import com.nhpm.activity.FingerprintResultActivity;
import com.nhpm.activity.WithAadhaarActivity;
import com.sec.biometric.license.SecBiometricLicenseManager;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Saurabh on 27-09-2017.
 */

public class AadhaarIrisViaRDServices extends Fragment implements View.OnClickListener {

    private TextView kycName, kycDob, kycGender, kycEmail, kycPhone, kycCareOf, kycAddr, kycTs, kycTxn, kycRespTs, kycErrorTextView;
    private TextView nameAsAadhar, dobAsAadhar, genderAsAadhar, aadharNum;
    private EkycActivity ekycActivity;
    private ProgressBar progressBar;
    private Boolean isKycEnabled = AppConstant.isKyCEnabled;
    private EditText edtxt_Aadhaar;
    private CheckBox aadharConsetCB;
    private boolean validAadhaar = false;
    private View view;
    private String consent = "Y";
    private CaptureAadharDetailActivity activity;
    private Button cancelEkyc, auth_demo_go;
    private TextView aadharNumResetEditText;
    private Context context;
    // RD Service values
    private String deviceType = "I";
    boolean e_KYC = true;
    private String mywadh = "";
    String cert_type = "PP";
    private String pidFormate = "0";
    private String final_XML = "";
    private Boolean demographic = false;
    private String ra = "";
    private String rc = "Y";
    private String lr = "N";
    private String de = "N";
    private String pfr = "N";
    private String dpid, Skey, Hmac, Data, Datatype;
    private String ci;
    private String deviceMake = "";
    private String serialNumber = "";
    private String deviceModel = "";
    private String rdsId;
    private String rdsVer;
    private String dc;
    private String mc;
    public String DeviceInfoXml = "";
    public String pidDataXML = "";
    private String IPaddress = "";
    private TextView txtView_info;
    private String kyc_ver = "2.1";
    private Intent intentCapture;
    private Intent intentInfo;
    private static int capture_finger = 1;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private CheckConnection checkConnection;
    private boolean checkNetwork;
    private long startTime;
    private AadhaarResponseItem aadhaarKycResponse;
    private LinearLayout kycDetailLayoutNew, kycErrorLayout;
    private long endTime;
    private Button updateKycButtonNew, cancelButtonNew, errorCancelButton;
    private long totalTime;
    private ImageView kycImageView;
    private CardView card_view1;
    private SeccMemberItem seccItem;
    private SelectedMemberItem selectedMemItem;
    private AlertDialog internetDiaolg;
    private VerifierLoginResponse loginResponse;
    private boolean check;
    private String aadharNumber;
    private int iCount = 1;
    private RadioGroup aadharAuthRG;
    private RadioButton singleEye, doubleEye;
    private PersonalDetailItem personalDetailItem;

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for context fragment
        view = inflater.inflate(R.layout.aadhaar_fingerprint_rdsevices_layout, container, false);
        context = getActivity();
        checkConnection = new CheckConnection(context);
        if (checkConnection.isConnectingToInternet()) {
            // toast("Connected to Internet Connection.");
            NetwordDetect();
        } else {
            showMessageDialogue("Please check your Internet Connection .");
            // toast("Please check your Internet Connection .");
        }
        String aadharNo = ekycActivity.serachItem.getAadhaarNo();// ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.AadharNumber, context);
        String validatorAadhar = null;
        VerifierLoginResponse storedLoginResponse = VerifierLoginResponse.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context));
        if (storedLoginResponse != null) {
            validatorAadhar = storedLoginResponse.getAadhaarNumber();
        }
        if (validatorAadhar != null && !validatorAadhar.equalsIgnoreCase("")) {

            Global.VALIDATORAADHAR = validatorAadhar;
        } else {
            Global.VALIDATORAADHAR = "352624429973";
        }
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.bringToFront();
        txtView_info = (TextView) view.findViewById(R.id.textview_info);
        edtxt_Aadhaar = (EditText) view.findViewById(R.id.aadhaarNoEdittext);
        edtxt_Aadhaar.addTextChangedListener(inputTextWatcher);
        if (aadharNo != null) {
            edtxt_Aadhaar.setText(aadharNo);
            edtxt_Aadhaar.setEnabled(false);

        }

        String aadharNo1 = ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.AadharNumber, context);

        if (aadharNo1 != null) {
            edtxt_Aadhaar.setText(aadharNo1);
            edtxt_Aadhaar.setEnabled(false);
        }
        aadharAuthRG = (RadioGroup) view.findViewById(R.id.aadharAuthRG);
        aadharAuthRG.setVisibility(View.VISIBLE);
        singleEye = (RadioButton) view.findViewById(R.id.singleRadioButton);
        doubleEye = (RadioButton) view.findViewById(R.id.doubleRadioButton);
        kycErrorTextView = (TextView) view.findViewById(R.id.errorTextView);
        aadharConsetCB = (CheckBox) view.findViewById(R.id.aadharConsetCB);
        auth_demo_go = (Button) view.findViewById(R.id.auth_demo_go);
        auth_demo_go.setOnClickListener(this);
        cancelEkyc = (Button) view.findViewById(R.id.cancelEkyc);
        cancelEkyc.setOnClickListener(this);
        card_view1 = (CardView) view.findViewById(R.id.card_view1);
        aadharNum = (TextView) view.findViewById(R.id.aadharNum);
        nameAsAadhar = (TextView) view.findViewById(R.id.nameAsAadhar);
        dobAsAadhar = (TextView) view.findViewById(R.id.dobAsAadhar);
        genderAsAadhar = (TextView) view.findViewById(R.id.genderAsAadhar);
        kycName = (TextView) view.findViewById(R.id.kycName);
        kycDob = (TextView) view.findViewById(R.id.kycDob);
        kycGender = (TextView) view.findViewById(R.id.kycGender);
        kycEmail = (TextView) view.findViewById(R.id.kycEmail);
        kycPhone = (TextView) view.findViewById(R.id.kycPhone);
        kycCareOf = (TextView) view.findViewById(R.id.kycCareOf);
        kycAddr = (TextView) view.findViewById(R.id.kycAddr);
        kycTs = (TextView) view.findViewById(R.id.kycTs);
        kycTxn = (TextView) view.findViewById(R.id.kycTxn);
        kycRespTs = (TextView) view.findViewById(R.id.kycRespTs);
        //   kycErrorTextView = (TextView) view.findViewById(R.id.errorTextView);
        kycErrorLayout = (LinearLayout) view.findViewById(R.id.kycErrorLayout);
        updateKycButtonNew = (Button) view.findViewById(R.id.updateKycButton);
        cancelButtonNew = (Button) view.findViewById(R.id.cancelButton);
        errorCancelButton = (Button) view.findViewById(R.id.errorCancelButton);
        kycImageView = (ImageView) view.findViewById(R.id.kycImageView);
        kycDetailLayoutNew = (LinearLayout) view.findViewById(R.id.kycDetailLayout);
        aadharNumResetEditText = (TextView) view.findViewById(R.id.aadharNumResetEditText);
        aadharNumResetEditText.setOnClickListener(this);
        if (!hasPermission()) {
            check = activateIrisLicense();
        } else {
            check = true;
        }


        aadharAuthRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == singleEye.getId()) {
                    iCount = 1;
                } else if (checkedId == doubleEye.getId()) {
                    iCount = 2;
                }
            }
        });
        setupData();
        return view;
    }

    private boolean hasPermission() {
        try {
            String BIOMETRIC_LICENSE_PERMISSION = "com.sec.enterprise.biometric.permission.IRIS_RECOGNITION";
            PackageManager packageManager = this.getActivity().getApplicationContext()
                    .getPackageManager();
            if (packageManager.checkPermission(BIOMETRIC_LICENSE_PERMISSION,
                    this.getActivity().getApplicationContext().getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                //  Print.d(TAG + ">>> hasPermission : true");
                return true;
            }
        } catch (Exception e) {
        }
        //  Print.d(TAG + ">>> hasPermission : false");
        return false;
    }

    private TextWatcher inputTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (edtxt_Aadhaar.length() == 12) {
                try {
                    if (VerhoeffAadhar.validateVerhoeff(edtxt_Aadhaar.getEditableText().toString())) {
                        edtxt_Aadhaar.setTextColor(Color.parseColor("#0B610B"));
                        validAadhaar = true;
                    } else {
                        edtxt_Aadhaar.setTextColor(Color.parseColor("#ff0000"));
                        validAadhaar = false;
                    }

                } catch (Exception e) {

                }
            } else {
                edtxt_Aadhaar.setTextColor(Color.parseColor("#000000"));
                validAadhaar = false;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.aadharNumResetEditText:
                edtxt_Aadhaar.setText("");
                break;
            case R.id.auth_demo_go:
                if (!hasPermission()) {
                    check = activateIrisLicense();
                } else {

                    check = true;
                    startActivity();
                    if (ekycActivity.isNetworkAvailable()) {
                        performAction();
                    }else {
                        CustomAlert.alertWithOk(context, getResources().getString(R.string.internet_connection_msg));

                    }
                }
                /*if (check) {

                }*/
                break;
            case R.id.cancelEkyc:
                ekycActivity.finish();
                break;
        }
    }


    private void performAction() {
        if (!aadharConsetCB.isChecked()) {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.adhar_consent_msg));
            return;
        }
        if (edtxt_Aadhaar.getText().toString().equalsIgnoreCase("")) {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.enterValidAadhaar));
            return;
        }
        if (validAadhaar) {

            try {
                kycErrorLayout.setVisibility(View.GONE);
                if (e_KYC) {
                    Log.e("kyc", "----------->");
                    mywadh("");
                } else {
                    Log.e("AUTH", "----------->");
                    captureFinger(generatePidOptXml(capture_finger));
                }

            } catch (Exception activityNotFound) {
                Toast.makeText(context, "Actvity Not Found : " + activityNotFound.getMessage(), Toast.LENGTH_LONG).show();
                //Toast.makeText(" " + activityNotFound.getMessage());
            }

        } else {
            CustomAlert.alertWithOk(context, context.getResources().getString(R.string.plzEnterValidAadhaar));
        }

    }


    private void startActivity() {
        try {
            int resultCode = 1;
            intentInfo = new Intent("in.gov.uidai.rdservice.iris.INFO");
            startActivityForResult(intentInfo, resultCode);
            //Global.DEVICE_TYPE = "I";
            deviceType = "I";
        } catch (ActivityNotFoundException activityNotFound) {

            showMessageDialogue("NO RD service found");
        } catch (Exception ex) {
            Log.d("TAG", "Error : " + ex.toString());
            //showMessageDialogue(ex.getMessage());
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        ekycActivity = (EkycActivity) context;
        //activity = (CaptureAadharDetailActivity) context;

    }


    //  functions for RD sevices

    public String mywadh(String myval) {
        //rhVuL7SnJi2W2UmsyukVqY7c93JWyL9O/kVKgdNMfv8=

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        ra = deviceType;

        String text = kyc_ver + ra + rc + lr + de + pfr;
//        String text = "2.1FYNNN";

        try {
            md.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] digest = md.digest();
        myval = Base64.encodeToString(digest, Base64.NO_WRAP);
        Log.e("myval", "-->" + myval);
        mywadh = myval;
        captureFinger(generatePidOptXml(capture_finger));
        return myval;
    }

    private void captureFinger(String pidOptXml) {
        if (pidOptXml.startsWith("ERROR")) {
            showMessageDialogue(pidOptXml + " occurred while generating PID Option XML");
//            disableAuth();
        } else {
            //CALL CAPTURE FUNCTION USING INTENT REQUEST.....
            //intentInfo.putExtra("Interface id", "CAPTURE");
            //intentCapture = new Intent("in.gov.uidai.rdservice.Iris.CAPTURE");

            intentCapture = new Intent("in.gov.uidai.rdservice.iris.CAPTURE");

/*            PackageManager manager = context.getPackageManager();
            List<ResolveInfo> infos = manager.queryIntentActivities(intentCapture, 0);
            if (infos.size() > 0) {
                Toast.makeText(ekycActivity, "Activity Found" + infos.get(0).toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ekycActivity, "RD service not found", Toast.LENGTH_SHORT).show();
            }*/


            // showMessageDialogue(pidOptXml);
            intentCapture.putExtra("PID_OPTIONS", pidOptXml);
            int resultCode = 2;
            startActivityForResult(intentCapture, resultCode);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        try {

            super.onActivityResult(requestCode, resultCode, data);
/*

            if (requestCode == REQUEST_PERMISSION_SETTING) {
                if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //Got Permission
                    proceedAfterPermission();
                }
            }
            Log.e("requestCode", "--->" + requestCode);
            Log.e("resultCode", "--->" + resultCode);
            Log.e("data", "--->" + data);
*/

            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == 1) {
                    String rd_info = data.getStringExtra("RD_SERVICE_INFO");
                    //  showMessageDialogue("1111-->" + rd_info);
                    // toast("11111");
                    if (rd_info != null && rd_info.contains("NOTREADY")) {
                        showMessageDialogue("Device is not Ready/Connected \n\n" + rd_info);
                        enableInfo();
                    } else {
                        enableInfo();
                        DeviceInfoXml = data.getStringExtra("DEVICE_INFO");
                        if (DeviceInfoXml != null) {
                            if (DeviceInfoXml.equals("") || DeviceInfoXml.isEmpty()) {
                                showMessageDialogue("Error occurred in DeviceInfo DATA XML");
                                return;
                            }
                            if (DeviceInfoXml.startsWith("ERROR:-")) {
                                showMessageDialogue("32222" + DeviceInfoXml);
                                return;
                            }
                        }
                        // toast("22222");
                        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = null;
                        builder = builderFactory.newDocumentBuilder();
                        Document inputDocument = null;
                        inputDocument = builder.parse(new InputSource(new StringReader(rd_info)));
                        String Stat = inputDocument.getElementsByTagName("RDService").item(0).getAttributes().getNamedItem("status").getNodeValue();
                        if (DeviceInfoXml != null && !DeviceInfoXml.equals("") && !DeviceInfoXml.isEmpty()) {
                            //  showMessageDialogue("DEVICE INFO XML :- \n" + DeviceInfoXml);
                        }
                        if (Stat != null) {
                            if (Stat.equals("READY")) {
                                //  toast("33333");
                                enableCapture();
                                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                                Document doc2 = dBuilder.parse(new InputSource(new StringReader(DeviceInfoXml)));
                                //optional, but recommended
                                // XPath to retrieve the content of the <FamilyAnnualDeductibleAmount> tag
                                XPath xpath2 = XPathFactory.newInstance().newXPath();
                                //  toast("4444");
                                XPathExpression expImei = xpath2.compile("/DeviceInfo/@dpId");
                                deviceMake = (String) expImei.evaluate(doc2, XPathConstants.STRING);
                                // toast("5555");
                                XPathExpression exprdsId = xpath2.compile("/DeviceInfo/@rdsId");
                                rdsId = (String) exprdsId.evaluate(doc2, XPathConstants.STRING);
                                // toast("6666");
                                XPathExpression exprdsVer = xpath2.compile("/DeviceInfo/@rdsVer");
                                rdsVer = (String) exprdsVer.evaluate(doc2, XPathConstants.STRING);
                                //  toast("7777");
                                XPathExpression expdc = xpath2.compile("/DeviceInfo/@dc");
                                dc = (String) expdc.evaluate(doc2, XPathConstants.STRING);
                                //  toast("8888");
                                XPathExpression expmc = xpath2.compile("/DeviceInfo/@mc");
                                mc = (String) expmc.evaluate(doc2, XPathConstants.STRING);
                                // toast("99999");
                                XPathExpression expImi = xpath2.compile("/DeviceInfo/@mi");
                                deviceModel = (String) expImi.evaluate(doc2, XPathConstants.STRING);
                                XPathExpression expsr = xpath2.compile("/DeviceInfo/@srno");
                                serialNumber = (String) expsr.evaluate(doc2, XPathConstants.STRING);
                                String connecteddevicevalues = deviceMake.trim() + deviceModel.trim() + deviceMake.trim() + serialNumber.trim();
                                String connectedDevice = connecteddevicevalues;
                                txtView_info.setText(deviceMake + " & " + deviceModel + " & " + serialNumber);
                            } else {
                                disableCapture();
                            }
                        } else {

                        }

                    }

                } else if (requestCode == 2) {
                    pidDataXML = data.getStringExtra("PID_DATA");
                    Log.e("pidDataXML", "-->" + pidDataXML);
                    if (pidDataXML != null) {
                        if (pidDataXML.equals("") || pidDataXML.isEmpty()) {
                            showMessageDialogue("Error occurred in PID DATA XML");
                            return;
                        }
                        if (pidDataXML.startsWith("ERROR:-")) {
                            showMessageDialogue(pidDataXML);
                            return;
                        }
                        DocumentBuilderFactory db = DocumentBuilderFactory.newInstance();
                        Document inputDocument = db.newDocumentBuilder().parse(new InputSource(new StringReader(pidDataXML)));
                        NodeList nodes = inputDocument.getElementsByTagName("PidData");
                        if (nodes != null) {
                            Element element = (Element) nodes.item(0);
                            NodeList respNode = inputDocument.getElementsByTagName("Resp");
                            if (respNode != null) {
                                Node respData = respNode.item(0);
                                NamedNodeMap attsResp = respData.getAttributes();
                                String errCodeStr = "";
                                String errInfoStr = "";
                                Node errCode = attsResp.getNamedItem("errCode");
                                if (errCode != null) {
                                    errCodeStr = errCode.getNodeValue();
                                } else errCodeStr = "0";
                                Node errInfo = attsResp.getNamedItem("errInfo");
                                if (errInfo != null) {
                                    errInfoStr = errInfo.getNodeValue();
                                }
                                if (Integer.parseInt(errCodeStr) > 0) {
                                    showMessageDialogue("Capture error :- " + errCodeStr + " , " + errInfoStr);
                                    return;
                                }
                            }
                        }
                    }
                    if (pidDataXML != null) {   //deviceInfoXML != null &&
                        // appendLogForResXML("pidDataXML-->" + pidDataXML);
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc2 = dBuilder.parse(new InputSource(new StringReader(pidDataXML)));

                        //optional, but recommended

                        XPath xpath2 = XPathFactory.newInstance().newXPath();
                        XPathExpression expResp = xpath2.compile("/PidData/Resp/@errCode");
                        String resp = (String) expResp.evaluate(doc2, XPathConstants.STRING);
                        Log.e("errorinfo", "-->" + resp);

                        if (resp.equalsIgnoreCase("0")) {
                            XPathExpression expImei = xpath2.compile("/PidData/DeviceInfo/@dpId");
                            dpid = (String) expImei.evaluate(doc2, XPathConstants.STRING);
                            Log.e("dpId", "-->" + dpid);
                            XPathExpression expmc = xpath2.compile("/PidData/DeviceInfo/@mc");
                            mc = (String) expmc.evaluate(doc2, XPathConstants.STRING);
                            XPathExpression exprdsId = xpath2.compile("/PidData/DeviceInfo/@rdsId");
                            rdsId = (String) exprdsId.evaluate(doc2, XPathConstants.STRING);
                            XPathExpression exprdsVer = xpath2.compile("/PidData/DeviceInfo/@rdsVer");
                            rdsVer = (String) exprdsVer.evaluate(doc2, XPathConstants.STRING);
                            XPathExpression expdc = xpath2.compile("/PidData/DeviceInfo/@dc");
                            dc = (String) expdc.evaluate(doc2, XPathConstants.STRING);
                            XPathExpression expImi = xpath2.compile("/PidData/DeviceInfo/@mi");
                            deviceModel = (String) expImi.evaluate(doc2, XPathConstants.STRING);
                            XPathExpression expsr = xpath2.compile("/PidData/DeviceInfo/@srno");
                            serialNumber = (String) expsr.evaluate(doc2, XPathConstants.STRING);
                            XPathExpression expci = xpath2.compile("/PidData/Skey/@ci");
                            ci = (String) expci.evaluate(doc2, XPathConstants.STRING);
                            Log.e("ci", "-->" + ci);
                            XPathExpression expSkey = xpath2.compile("/PidData/Skey/text()");
                            Skey = (String) expSkey.evaluate(doc2, XPathConstants.STRING);
                            Log.e("Skey", "-->" + Skey);
                            XPathExpression expHmac = xpath2.compile("/PidData/Hmac/text()");
                            Hmac = (String) expHmac.evaluate(doc2, XPathConstants.STRING);
                            Log.e("Hmac", "-->" + Hmac);
                            XPathExpression expData = xpath2.compile("/PidData/Data/text()");
                            Data = (String) expData.evaluate(doc2, XPathConstants.STRING);
                            Log.e("Data", "-->" + Data);
                            XPathExpression expDatatype = xpath2.compile("/PidData/Data/@type");
                            Datatype = (String) expDatatype.evaluate(doc2, XPathConstants.STRING);
                            Log.e("Datatype", "-->" + Datatype);
                            XPathExpression experror = xpath2.compile("/PidData/Resp/@errCode");
                            String errorCode = (String) experror.evaluate(doc2, XPathConstants.STRING);
                            Log.e("errorCode", "-->" + errorCode);
                            final_XML = createXmlForAuth(deviceType, edtxt_Aadhaar.getText().toString().trim());
                            //   showMessageDialogue("Auth->" + final_XML);
                            if (e_KYC) {
                                if (final_XML != null && !final_XML.equalsIgnoreCase("")) {
                                    hitToServerforFINALRequest();
                                }
                                // enter your code here

                            } else {
                                // enter your code here
                            }
                        } else {
                            XPathExpression experror = xpath2.compile("/PidData/Resp/@errCode");
                            String errorCode = (String) experror.evaluate(doc2, XPathConstants.STRING);
                            Log.e("errorCode", "-->" + errorCode);
                            showMessageDialogue("Error Info->" + resp + " & Error Code-> " + errorCode);
                        }
                    } else {
                        showMessageDialogue("Scan Failure");
                    }
                } else if (requestCode == 3) {
                } else if (requestCode == 13) {
                    String value = data.getStringExtra("CALIM");
                    if (value != null) {
                        showMessageDialogue(value);
                    }
                } else if (requestCode == 14) {
                    String value = data.getStringExtra("RELEASE");
                    if (value != null) {
                        showMessageDialogue(value);
                    }
                } else if (requestCode == 15) {
                    String value = data.getStringExtra("SET_REG");
                    if (value != null) {
                        showMessageDialogue(value);
                    }
                } else if (requestCode == 16) {
                    String value = data.getStringExtra("GET_REG");
                    if (value != null) {
                        showMessageDialogue(value);
                    }
                } else if (requestCode == 17) {
                    String value = data.getStringExtra("RESET_REG");
                    if (value != null) {
                        showMessageDialogue(value);
                    }
                } else {
                    String str = data.getStringExtra("Setting");
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                showMessageDialogue("Scan Failed/Aborted!");
            }
        } catch (Exception ex) {
            //showMessageDialogue("Error:-" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private String generatePidOptXml(int fingsToCap) {
        String tmpOptXml = "";
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = null;
            docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);
            Element rootElement = doc.createElement("PidOptions");
            doc.appendChild(rootElement);
            Attr attrVer = doc.createAttribute("ver");
            attrVer.setValue("1.0");
            rootElement.setAttributeNode(attrVer);
            Element opts = doc.createElement("Opts");
            rootElement.appendChild(opts);
            Attr attr = doc.createAttribute("fCount");
            if (deviceType.equalsIgnoreCase("F")) {
                attr.setValue(String.valueOf(fingsToCap));
            } else if (deviceType.equalsIgnoreCase("I")) {
                attr.setValue("0");
            }
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("fType");
            attr.setValue("0");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("iCount");
            if (deviceType.equalsIgnoreCase("F")) {
                attr.setValue("0");
            } else if (deviceType.equalsIgnoreCase("I")) {
                attr.setValue(iCount + "");
            }
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("iType");
            attr.setValue("0");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("pCount");
            attr.setValue("0");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("pType");
            attr.setValue("0");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("format");
            attr.setValue(pidFormate);
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("pidVer");
            attr.setValue("2.0");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("timeout");
            attr.setValue("5000");
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("env");
            attr.setValue(cert_type);
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("wadh");
            if (e_KYC) {
                attr.setValue(mywadh);
            } else {
                attr.setValue("");
            }
            opts.setAttributeNode(attr);
            attr = doc.createAttribute("posh");
            if (deviceType.equalsIgnoreCase("F")) {
                attr.setValue("UNKNOWN");
            } else if (deviceType.equalsIgnoreCase("I")) {
                attr.setValue("UNKNOWN");
            }
            opts.setAttributeNode(attr);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            tmpOptXml = writer.getBuffer().toString().replaceAll("\n|\r", "");
            Log.d("TAG", "Original XML : " + tmpOptXml);

            tmpOptXml = tmpOptXml.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
            Log.d("TAG", "Replaced  XML : " + tmpOptXml);

            return tmpOptXml;
        } catch (ParserConfigurationException e) {
            return "ERROR :- " + e.getMessage();
        } catch (TransformerConfigurationException e) {
            return "ERROR :- " + e.getMessage();
        } catch (TransformerException e) {
            Log.d("TAG", "Error : " + e.getMessage());
            Toast.makeText(ekycActivity, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return "ERROR :- " + e.getMessage();
        }
        // return null;

    }

    /*public String createXmlForAuth(String deviceType, String aadhaarNo) {

        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        Calendar localCalendar = GregorianCalendar
                .getInstance();
        localCalendar.setTime(date);
        String pidTimeStamp = String.valueOf(localCalendar
                .get(Calendar.YEAR)) + "-" +
                (String.valueOf(localCalendar.get(Calendar.MONTH) + 1).length() < 2 ? "0" + String.valueOf(localCalendar
                        .get(Calendar.MONTH) + 1) : String.valueOf(localCalendar
                        .get(Calendar.MONTH) + 1))
                + "-" +
                (String.valueOf(localCalendar.get(Calendar.DATE)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.DATE)) : String.valueOf(localCalendar.get(Calendar.DATE)))
                + "T" +
                (String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)) : String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)))
                + ":" +
                (String.valueOf(localCalendar.get(Calendar.MINUTE)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.MINUTE)) : String.valueOf(localCalendar.get(Calendar.MINUTE)))
                + ":" +
                (String.valueOf(localCalendar.get(Calendar.SECOND)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.SECOND)) : String.valueOf(localCalendar.get(Calendar.SECOND)));


        Log.e("pidTimeStamp", "==" + pidTimeStamp);

        String KycAuthXml = "";
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        org.w3c.dom.Document document = documentBuilder.newDocument();

        boolean bio, pi;
        if (deviceType.equalsIgnoreCase("F")) {
            bio = true;
            pi = false;
        } else if (deviceType.equalsIgnoreCase("I")) {
            bio = true;
            pi = false;
        } else {
            bio = false;
            pi = false;
        }
        boolean otp_check = false;
        String txn = "";
        if (e_KYC) {
            txn = "UKC:" + aadhaarNo + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "");
        } else {
            txn = "AuthDemo:" + aadhaarNo + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "");
        }
        Element rootElement = document.createElement("Auth");
        document.appendChild(rootElement);
        Attr rootUid = document.createAttribute("uid");
        rootUid.setValue(aadhaarNo);
        Attr rootTid = document.createAttribute("tid");
        rootTid.setValue("registered");
        Attr rootAc = document.createAttribute("ac");
        rootAc.setValue("public");
        Attr rootSa = document.createAttribute("sa");
        rootSa.setValue("");

        Attr rootLk = document.createAttribute("lk");
        rootLk.setValue("");

        Attr rootTxn = document.createAttribute("txn");
        rootTxn.setValue(txn);
        Attr rootVer = document.createAttribute("ver");
        rootVer.setValue("2.0");

        Attr rootrc = document.createAttribute("rc");
        rootrc.setValue("Y");

        Attr metapip = document.createAttribute("mypip");
        metapip.setValue(IPaddress);
        rootElement.setAttributeNode(metapip);

        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/2.0");

        rootElement.setAttributeNode(rootrc);
        rootElement.setAttributeNode(rootTid);
        rootElement.setAttributeNode(rootAc);
        rootElement.setAttributeNode(rootSa);
        rootElement.setAttributeNode(rootLk);
        rootElement.setAttributeNode(rootTxn);
        rootElement.setAttributeNode(rootVer);
        rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootUid);

        //		<Uses bio="y"  bt="'.$data['bt'].'" pi="n" pa="n" pfa="n" pin="n" otp="n" />

        Element usesElement = document.createElement("Uses");

        Attr usesBio = document.createAttribute("bio");
        if (bio) {
            usesBio.setValue("y");
        } else {
            usesBio.setValue("n");
        }

        Attr usesBt = document.createAttribute("bt");
        if (deviceType.equalsIgnoreCase("F")) {
            usesBt.setValue("FMR");
        } else if (deviceType.equalsIgnoreCase("I")) {
            usesBt.setValue("IIR");
        }

        Attr usesPi = document.createAttribute("pi");
        if (demographic) {
            usesPi.setValue("y");
        } else {
            usesPi.setValue("n");
        }

        Attr usesPa = document.createAttribute("pa");
        usesPa.setValue("n");
        Attr usesPfa = document.createAttribute("pfa");
        usesPfa.setValue("n");
        Attr usesPin = document.createAttribute("pin");
        usesPin.setValue("n");
        Attr usesOtp = document.createAttribute("otp");
        if (otp_check) {
            usesOtp.setValue("y");
        } else {
            usesOtp.setValue("n");
        }

        usesElement.setAttributeNode(usesBio);
//        if (deviceType.equalsIgnoreCase("F") | deviceType.equalsIgnoreCase("I")) {
        if (bio) {
            usesElement.setAttributeNode(usesBt);
        }

        usesElement.setAttributeNode(usesPi);
        usesElement.setAttributeNode(usesPa);
        usesElement.setAttributeNode(usesPfa);
        usesElement.setAttributeNode(usesPin);
        usesElement.setAttributeNode(usesOtp);

        rootElement.appendChild(usesElement);

        //		<Meta udc="AIIMSTEST" fdc="'.$data['fdc'].'" idc="'.$data['idc'].'" pip="127.0.0.1" lot="P" lov="110092" />

        Element metaElement = document.createElement("Meta");

        Attr metaUdc = document.createAttribute("udc");
        metaUdc.setValue(deviceModel + serialNumber);

        metaElement.setAttributeNode(metaUdc);

        Attr metardsId = document.createAttribute("rdsId");
        metardsId.setValue(rdsId);

        Attr metardsVer = document.createAttribute("rdsVer");
        metardsVer.setValue(rdsVer);

        Attr metadpId = document.createAttribute("dpId");
        metadpId.setValue(dpid);

        Attr metadc = document.createAttribute("dc");
        metadc.setValue(dc);

        Attr metami = document.createAttribute("mi");
        metami.setValue(deviceModel);

        Attr metamc = document.createAttribute("mc");
        metamc.setValue(mc);


        metaElement.setAttributeNode(metardsId);
        metaElement.setAttributeNode(metardsVer);
        metaElement.setAttributeNode(metadpId);
        metaElement.setAttributeNode(metadc);
        metaElement.setAttributeNode(metami);
        metaElement.setAttributeNode(metamc);

        rootElement.appendChild(metaElement);


        Element skeyElement = document.createElement("Skey");

        Attr skeyCi = document.createAttribute("ci");
        skeyCi.setValue(ci);

        skeyElement.setAttributeNode(skeyCi);
        skeyElement.appendChild(document.createTextNode(Skey));

        rootElement.appendChild(skeyElement);

        Element dataElement = document.createElement("Data");

        Attr dataType = document.createAttribute("type");
        dataType.setValue("X");
        dataElement.appendChild(document.createTextNode(Data));
        dataElement.setAttributeNode(dataType);

        rootElement.appendChild(dataElement);

        Element hmcaElement = document.createElement("Hmac");
        hmcaElement.appendChild(document.createTextNode(Hmac));

        rootElement.appendChild(hmcaElement);

        Element signatureElement = document.createElement("Signature");
        signatureElement.appendChild(document.createTextNode(""));


        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        //			OutputStream output = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(writer);
        try {
            transformer.transform(domSource, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        KycAuthXml = writer.getBuffer().toString();
        Log.e("kyc auth ", "==" + KycAuthXml);
//        showMessageDialogue(KycAuthXml);
        ;
        return KycAuthXml;

    }*/

    public String createXmlForAuth(String deviceType, String aadhaarNo) {
       /* if (fType.equalsIgnoreCase("0")){
            deviceType = "F";
        }else{
            deviceType = "I";
        }*/
        Log.e("deviceType", "==" + deviceType);
        Log.e("aadhaarNo", "==2" + aadhaarNo);
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        Calendar localCalendar = GregorianCalendar
                .getInstance();
        localCalendar.setTime(date);
        String pidTimeStamp = String.valueOf(localCalendar
                .get(Calendar.YEAR)) + "-" +
                (String.valueOf(localCalendar.get(Calendar.MONTH) + 1).length() < 2 ? "0" + String.valueOf(localCalendar
                        .get(Calendar.MONTH) + 1) : String.valueOf(localCalendar
                        .get(Calendar.MONTH) + 1))
                + "-" +
                (String.valueOf(localCalendar.get(Calendar.DATE)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.DATE)) : String.valueOf(localCalendar.get(Calendar.DATE)))
                + "T" +
                (String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)) : String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)))
                + ":" +
                (String.valueOf(localCalendar.get(Calendar.MINUTE)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.MINUTE)) : String.valueOf(localCalendar.get(Calendar.MINUTE)))
                + ":" +
                (String.valueOf(localCalendar.get(Calendar.SECOND)).length() < 2 ? "0" + String.valueOf(localCalendar.get(Calendar.SECOND)) : String.valueOf(localCalendar.get(Calendar.SECOND)));


        Log.e("pidTimeStamp", "==" + pidTimeStamp);

        String KycAuthXml = "";
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        org.w3c.dom.Document document = documentBuilder.newDocument();

        boolean bio, pi;
        if (deviceType.equalsIgnoreCase("F")) {
            bio = true;
            pi = false;
        } else if (deviceType.equalsIgnoreCase("I")) {
            bio = true;
            pi = false;
        } else {
            bio = false;
            pi = false;
        }
        boolean otp_check = false;
        String txn = "";
        if (e_KYC) {
            txn = "UKC:" + aadhaarNo + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "");
        } else {
            txn = "AuthDemo:" + aadhaarNo + pidTimeStamp.replace("T", "").replace("-", "").replace(":", "");
        }
        Element mainElement = document.createElement("AuthBioData");
        document.appendChild(mainElement);
        Element rootElement = document.createElement("Auth");
        mainElement.appendChild(rootElement);
  /*      Element rootElement = document.createElement("Auth");
        document.appendChild(rootElement);*/
        Attr rootUid = document.createAttribute("uid");
        rootUid.setValue(aadhaarNo);
        Attr rootTid = document.createAttribute("tid");
        rootTid.setValue("registered");
        Attr rootAc = document.createAttribute("ac");
        rootAc.setValue("public");
        Attr rootSa = document.createAttribute("sa");
        rootSa.setValue("");

        Attr rootLk = document.createAttribute("lk");
        rootLk.setValue("");

        Attr rootTxn = document.createAttribute("txn");
        rootTxn.setValue(txn);
        Attr rootVer = document.createAttribute("ver");
        rootVer.setValue("2.0");

        Attr rootrc = document.createAttribute("rc");
        rootrc.setValue("Y");

        Attr metapip = document.createAttribute("mypip");
        metapip.setValue(IPaddress);
        rootElement.setAttributeNode(metapip);

        Attr rootXmlns = document.createAttribute("xmlns");
        rootXmlns.setValue("http://www.uidai.gov.in/authentication/uid-auth-request/2.0");

        rootElement.setAttributeNode(rootrc);
        rootElement.setAttributeNode(rootTid);
        rootElement.setAttributeNode(rootAc);
        rootElement.setAttributeNode(rootSa);
        rootElement.setAttributeNode(rootLk);
        rootElement.setAttributeNode(rootTxn);
        rootElement.setAttributeNode(rootVer);
        rootElement.setAttributeNode(rootXmlns);
        rootElement.setAttributeNode(rootUid);

        //		<Uses bio="y"  bt="'.$data['bt'].'" pi="n" pa="n" pfa="n" pin="n" otp="n" />

        Element usesElement = document.createElement("Uses");

        Attr usesBio = document.createAttribute("bio");
        if (bio) {
            usesBio.setValue("y");
        } else {
            usesBio.setValue("n");
        }

        Attr usesBt = document.createAttribute("bt");
        if (deviceType.equalsIgnoreCase("F")) {
            usesBt.setValue("FMR");
        } else if (deviceType.equalsIgnoreCase("I")) {
            usesBt.setValue("IIR");
        }

        Attr usesPi = document.createAttribute("pi");
        if (demographic) {
            usesPi.setValue("y");
        } else {
            usesPi.setValue("n");
        }

        Attr usesPa = document.createAttribute("pa");
        usesPa.setValue("n");
        Attr usesPfa = document.createAttribute("pfa");
        usesPfa.setValue("n");
        Attr usesPin = document.createAttribute("pin");
        usesPin.setValue("n");
        Attr usesOtp = document.createAttribute("otp");
        if (otp_check) {
            usesOtp.setValue("y");
        } else {
            usesOtp.setValue("n");
        }

        usesElement.setAttributeNode(usesBio);
//        if (deviceType.equalsIgnoreCase("F") | deviceType.equalsIgnoreCase("I")) {
        if (bio) {
            usesElement.setAttributeNode(usesBt);
        }

        usesElement.setAttributeNode(usesPi);
        usesElement.setAttributeNode(usesPa);
        usesElement.setAttributeNode(usesPfa);
        usesElement.setAttributeNode(usesPin);
        usesElement.setAttributeNode(usesOtp);

        rootElement.appendChild(usesElement);

        //		<Meta udc="AIIMSTEST" fdc="'.$data['fdc'].'" idc="'.$data['idc'].'" pip="127.0.0.1" lot="P" lov="110092" />

        Element metaElement = document.createElement("Meta");

        Attr metaUdc = document.createAttribute("udc");
        metaUdc.setValue(deviceModel + serialNumber);

        metaElement.setAttributeNode(metaUdc);

        Attr metardsId = document.createAttribute("rdsId");
        metardsId.setValue(rdsId);

        Attr metardsVer = document.createAttribute("rdsVer");
        metardsVer.setValue(rdsVer);

        Attr metadpId = document.createAttribute("dpId");
        metadpId.setValue(dpid);

        Attr metadc = document.createAttribute("dc");
        metadc.setValue(dc);

        Attr metami = document.createAttribute("mi");
        metami.setValue(deviceModel);

        Attr metamc = document.createAttribute("mc");
        metamc.setValue(mc);


        metaElement.setAttributeNode(metardsId);
        metaElement.setAttributeNode(metardsVer);
        metaElement.setAttributeNode(metadpId);
        metaElement.setAttributeNode(metadc);
        metaElement.setAttributeNode(metami);
        metaElement.setAttributeNode(metamc);

        rootElement.appendChild(metaElement);


        Element skeyElement = document.createElement("Skey");

        Attr skeyCi = document.createAttribute("ci");
        skeyCi.setValue(ci);

        skeyElement.setAttributeNode(skeyCi);
        skeyElement.appendChild(document.createTextNode(Skey));

        rootElement.appendChild(skeyElement);

        Element dataElement = document.createElement("Data");

        Attr dataType = document.createAttribute("type");
        dataType.setValue("X");
        dataElement.appendChild(document.createTextNode(Data));
        dataElement.setAttributeNode(dataType);

        rootElement.appendChild(dataElement);

        Element hmcaElement = document.createElement("Hmac");
        hmcaElement.appendChild(document.createTextNode(Hmac));

        rootElement.appendChild(hmcaElement);
        Element elementUserData = document.createElement("UserData");
        Attr imeiNo = document.createAttribute("imeiNo");
        imeiNo.setValue("867802027718791");
        //Log.e("imei no", Global.imei);
        Attr projectInfo = document.createAttribute("projectInfo");
        projectInfo.setValue("NHPS-FVS");
        Attr macAddress = document.createAttribute("macAddress");
        macAddress.setValue("10.247.47.79");
        Attr uid = document.createAttribute("uid");
        uid.setValue(Global.VALIDATORAADHAR);
        Attr userName = document.createAttribute("userName");
        userName.setValue(ApplicationGlobal.USER_NAME);
        Attr userPwd = document.createAttribute("userPass");
        userPwd.setValue(ApplicationGlobal.USER_PASSWORD);

        elementUserData.setAttributeNode(imeiNo);
        elementUserData.setAttributeNode(projectInfo);
        elementUserData.setAttributeNode(macAddress);
        elementUserData.setAttributeNode(uid);
        elementUserData.setAttributeNode(userName);
        elementUserData.setAttributeNode(userPwd);
        mainElement.appendChild(elementUserData);
        Element signatureElement = document.createElement("Signature");
        signatureElement.appendChild(document.createTextNode(""));


        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        //			OutputStream output = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(writer);
        try {
            transformer.transform(domSource, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        KycAuthXml = writer.getBuffer().toString();
        Log.e("kyc auth ", "==" + KycAuthXml);
//        showMessageDialogue(KycAuthXml);
        ;
        return KycAuthXml;

    }


    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        Toast.makeText(context, "We got the Storage Permission", Toast.LENGTH_LONG).show();
    }

    private void disableCapture() {
        // auth_demo_go.setEnabled(false);
    }

    public void enableCapture() {
        auth_demo_go.setEnabled(true);
    }

    protected void toast(final String msg) {
     /*   activity.runOnUiThread(new Thread(new Runnable() {*/
      /*      @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void run() {*/
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
   /*         }
        }));*/
    }

    public void enableInfo() {
        auth_demo_go.setEnabled(true);
    }

    public void showMessageDialogue(String messageTxt) {
        new AlertDialog.Builder(ekycActivity)
                .setCancelable(false)
                .setTitle("Message")
                .setMessage(messageTxt)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void NetwordDetect() {

        boolean WIFI = false;

        boolean MOBILE = false;

        ConnectivityManager CM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfo = CM.getAllNetworkInfo();

        for (NetworkInfo netInfo : networkInfo) {

            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))

                if (netInfo.isConnected())

                    WIFI = true;

            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))

                if (netInfo.isConnected())

                    MOBILE = true;
        }

        if (WIFI)

        {
            IPaddress = GetDeviceipWiFiData();
            Log.e("IPaddress", "-->" + IPaddress);
//            toast(IPaddress);


        }

        if (MOBILE) {

            IPaddress = GetDeviceipMobileData();
            Log.e("IPaddress", "-->" + IPaddress);
//            toast(IPaddress);

        }

    }

    public String GetDeviceipMobileData() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    System.out.println("ip1--:" + inetAddress);
                    System.out.println("ip2--:" + inetAddress.getHostAddress());

                    // for getting IPV4 format
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {

                        String ip = inetAddress.getHostAddress().toString();
                        System.out.println("ip---::" + ip);

                        // return inetAddress.getHostAddress().toString();
                        return ip;
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;

    }

    public String GetDeviceipWiFiData() {

        WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);

        @SuppressWarnings("deprecation")

        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        return ip;

    }

    private void hitToServerforFINALRequest() {
        checkNetwork = checkConnection.isConnectingToInternet();
        //    countDown.cancel();
        if (checkNetwork) {
            startTime = System.currentTimeMillis();
            HitToServer task = new HitToServer();
            task.execute();
        } else {
            AppUtility.alertWithOk(context, "Network Issue", "Please Enable the network");
        }
    }


    //	To hit server
    private class HitToServer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            Log.e("request xml", "=====" + Global.KYCIRISXML);
         /*   if (isKycEnabled) {

                res = CustomHttp.HttpPostLifeCerticiate(Global.KYC_URL, Global.KYCIRISXML, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
            } else {

                res = CustomHttp.HttpPostLifeCerticiate(AppConstant.REQUEST_FOR_OTP_AUTH, Global.KycXmlForNhps, AppConstant.AUTHORIZATION, AppConstant.AUTHORIZATIONVALUE);
            }*/
            res = CustomHttp.HttpPostLifeCerticiate(AppConstant.REQUEST_FOR_KYC_VIA_RD_SERVICES, final_XML, AppConstant.AUTHORIZATION, AppConstant.TOKEN_VALUE_AADHAAR);
            Log.e("Response", "==" + res);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);

            if (result.equalsIgnoreCase("ERROR") || result.equalsIgnoreCase("False from server") || result.equalsIgnoreCase("Connection time out Error")) {

                AppUtility.alertWithOk(context, result);
            } else if (result.equalsIgnoreCase(AppConstant.ACCESS_DENIED_ERROR)) {

                AppUtility.alertWithOk(context, "ACCESS DENIED", result);
            } else if (result.equalsIgnoreCase("")) {

                AppUtility.alertWithOk(context, "No response from Server");
            } else {
                aadhaarKycResponse = new AadhaarResponseItem().create(result);

                if (aadhaarKycResponse != null) {
                    if (aadhaarKycResponse.getResult() != null && aadhaarKycResponse.getResult().equalsIgnoreCase(AppConstant.AADHAAR_AUTH_YES)) {
                       /* Intent intent = new Intent(context, FingerprintResultActivity.class);
                        intent.putExtra("result", aadhaarKycResponse);
                        startActivity(intent);*/
                        //ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME,"AADHAAR_DATA",aadhaarKycResponse.serialize(),context);
                   /*     PersonalDetailItem personalDetailItem = new PersonalDetailItem();
                        personalDetailItem.setBenefPhoto(aadhaarKycResponse.getBase64());
                        personalDetailItem.setMobileNo(aadhaarKycResponse.getPhone());
                        personalDetailItem.setName(aadhaarKycResponse.getName());
                        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME,"AADHAAR_DATA",
                                personalDetailItem.serialize(),context);*/


                        if (personalDetailItem != null) {
                            personalDetailItem.setBenefPhoto(aadhaarKycResponse.getBase64());
                            personalDetailItem.setMobileNo(aadhaarKycResponse.getPhone());
                            personalDetailItem.setGovtIdNo(edtxt_Aadhaar.getText().toString());
                            personalDetailItem.setGovtIdType("aadhar");
                            personalDetailItem.setIsAadhar("Y");
                            personalDetailItem.setName(aadhaarKycResponse.getName());
                            personalDetailItem.setFlowStatus(AppConstant.AADHAR_STATUS);
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME, "AADHAAR_DATA", personalDetailItem.serialize(), context);

                        } else {

                            personalDetailItem = new PersonalDetailItem();
                            personalDetailItem.setBenefPhoto(aadhaarKycResponse.getBase64());
                            personalDetailItem.setGovtIdNo(edtxt_Aadhaar.getText().toString());
                            personalDetailItem.setGovtIdType("aadhar");
                            personalDetailItem.setIsAadhar("Y");
                            personalDetailItem.setMobileNo(aadhaarKycResponse.getPhone());
                            personalDetailItem.setName(aadhaarKycResponse.getName());
                            personalDetailItem.setFlowStatus(AppConstant.AADHAR_STATUS);
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_NAME, "AADHAAR_DATA", personalDetailItem.serialize(), context);
                        }
                        ekycActivity.finish();
                    } else {
                        CustomAlert.alertWithOk(context, aadhaarKycResponse.getErr());
                        return;
                    }
                } else {
                    CustomAlert.alertWithOk(context, "Unable To Connect from UIDAI Server, Please try again.");

                }
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    private void ShowKycDataNew(String JSON) {
        try {
            aadhaarKycResponse = new AadhaarResponseItem().create(JSON);
            // AppUtility.writeFileToStorage(JSON, "E-kyc_with_BIO/JSON");
            if (aadhaarKycResponse != null) {


                if (aadhaarKycResponse != null && aadhaarKycResponse.getResult() != null
                        && aadhaarKycResponse.getResult().equalsIgnoreCase("Y")) {

                    kycDetailLayoutNew.setVisibility(View.VISIBLE);
                    kycErrorLayout.setVisibility(View.GONE);

                    if (aadhaarKycResponse.getBase64() != null && !aadhaarKycResponse.getBase64().equalsIgnoreCase("")) {

                        Bitmap imageBitmap = AppUtility.convertStringToBitmap(aadhaarKycResponse.getBase64());
                        if (imageBitmap != null) {
                            kycImageView.setImageBitmap(imageBitmap);
                        }

                    } else {
                        kycImageView.setVisibility(View.GONE);
                    }
                    if (aadhaarKycResponse.getName() != null)
                        kycName.setText(aadhaarKycResponse.getName());
                    if (aadhaarKycResponse.getDob() != null)
                        kycDob.setText(aadhaarKycResponse.getDob());
                  /*  if (aadhaarKycResponse.getUidData().getPoa().getCo() != null)
                        kycCareOf.setText(aadhaarKycResponse.getUidData().getPoa().getCo());*/
                    if (aadhaarKycResponse.getEmail() != null)
                        kycEmail.setText(aadhaarKycResponse.getEmail());
                    if (aadhaarKycResponse.getGender() != null)
                        kycGender.setText(aadhaarKycResponse.getGender());
                    if (aadhaarKycResponse.getPhone() != null)
                        kycPhone.setText(aadhaarKycResponse.getPhone() + "");
                    StringBuilder addr = new StringBuilder();
                    if (aadhaarKycResponse.getCo() != null)
                        addr.append(aadhaarKycResponse.getCo());
                    if (aadhaarKycResponse.getHouse() != null && !aadhaarKycResponse.getHouse().equalsIgnoreCase(""))
                        addr.append(", " + aadhaarKycResponse.getHouse());
                    if (aadhaarKycResponse.getStreet() != null && !aadhaarKycResponse.getStreet().equalsIgnoreCase(""))
                        addr.append(", " + aadhaarKycResponse.getStreet());
                    if (aadhaarKycResponse.getLm() != null && !aadhaarKycResponse.getLm().equalsIgnoreCase(""))
                        addr.append(", " + aadhaarKycResponse.getLm());
                    if (aadhaarKycResponse.getVtc() != null && !aadhaarKycResponse.getVtc().equalsIgnoreCase(""))
                        addr.append(", " + aadhaarKycResponse.getVtc());
                   /* if (aadhaarKycResponse.getUidData().getPoa().getSubdist() != null)
                        addr.append("," + aadhaarKycResponse.getUidData().getPoa().getSubdist());*/
                    if (aadhaarKycResponse.getDist() != null)
                        addr.append(", " + aadhaarKycResponse.getDist());
                    if (aadhaarKycResponse.getState() != null)
                        addr.append(", " + aadhaarKycResponse.getState());
           /*     if (aadhaarKycResponse.getC != null)
                    addr.append(", " + aadhaarKycResponse.getUidData().getPoa().getCountry());*/

                    kycAddr.setText(addr.toString());
                    if (aadhaarKycResponse.getTs() != null) {
                        kycTs.setText(AppUtility.convetEkycDate(aadhaarKycResponse.getTs()));
                    }
                    if (aadhaarKycResponse.getTxn() != null) {
                        kycTxn.setText(aadhaarKycResponse.getTxn());

                    }
                    endTime = System.currentTimeMillis();
                    totalTime = endTime - startTime;
                    kycRespTs.setText(totalTime + " miliseconds");
                    updateKycButtonNew.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertForValidateLater(aadhaarKycResponse.getName(), seccItem);
                        }
                    });

                    cancelButtonNew.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    activity.finish();
                                }
                            }
                    );
                } else if (aadhaarKycResponse != null && aadhaarKycResponse.getResult() != null &&
                        aadhaarKycResponse.getResult().equalsIgnoreCase("N") && aadhaarKycResponse.getErr() != null &&
                        !aadhaarKycResponse.getErr().equalsIgnoreCase("")) {

                    kycErrorLayout.setVisibility(View.VISIBLE);
                    kycDetailLayoutNew.setVisibility(View.GONE);
                    String err = aadhaarKycResponse.getErr();
                    if (err.equalsIgnoreCase("K-100")) {
                        err = "Resident authentication failed-" + err;
                    } else if (err.equalsIgnoreCase("K-200")) {
                        err = "Resident data currently not available-" + err;
                    } else if (err.equalsIgnoreCase("K-540")) {
                        err = "Invalid KYC XML-" + err;
                    } else if (err.equalsIgnoreCase("K-541")) {
                        err = "Invalid e-KYC API version-" + err;
                    } else if (err.equalsIgnoreCase("K-542")) {
                        err = "Invalid resident consent-" + err;
                    } else if (err.equalsIgnoreCase("K-543")) {
                        err = "Invalid timestamp-" + err;
                    } else if (err.equalsIgnoreCase("K-544")) {
                        err = "Invalid resident auth type-" + err;
                    } else if (err.equalsIgnoreCase("K-545")) {
                        err = err + "\n" + "Resident has opted-out of this service-" + err;
                    } else if (err.equalsIgnoreCase("K-550")) {
                        err = err + "\n" + "Invalid Uses Attribute-" + err;
                    } else if (err.equalsIgnoreCase("K-551")) {
                        err = "Invalid Txn namespace-" + err;
                    } else if (err.equalsIgnoreCase("K-552")) {
                        err = "Invalid License key-" + err;
                    } else if (err.equalsIgnoreCase("K-569")) {
                        err = "Digital signature verification failed for e-KYC XML-" + err;
                    } else if (err.equalsIgnoreCase("K-570")) {
                        err = "Invalid key info in digital signature for e-KYC XML-" + err;
                    } else if (err.equalsIgnoreCase("K-600")) {
                        err = "AUA is invalid or not an authorized KUA-" + err;
                    } else if (err.equalsIgnoreCase("K-601")) {
                        err = "ASA is invalid or not an authorized KSA-" + err;
                    } else if (err.equalsIgnoreCase("K-602")) {
                        err = "KUA encryption key not available-" + err;
                    } else if (err.equalsIgnoreCase("K-603")) {
                        err = "KSA encryption key not available-" + err;
                    } else if (err.equalsIgnoreCase("K-604")) {
                        err = "KSA Signature not allowed-" + err;
                    } else if (err.equalsIgnoreCase("K-605")) {
                        err = "Neither KUA key nor KSA encryption key are available-" + err;
                    } else if (err.equalsIgnoreCase("K-955")) {
                        err = "Technical Failure-" + err;
                    } else if (err.equalsIgnoreCase("K-999")) {
                        err = "Unknown error-" + err;
                    } else if (err.equalsIgnoreCase("9903")) {
                        err = "e-KYC failed";
                    } else {
                        err = err + "\n" + " e-KYC failed";
                    }
                    endTime = System.currentTimeMillis();
                    totalTime = endTime - startTime;

                    kycErrorTextView.setText(err + "\n\n" + "Response time : " + totalTime + " miliseconds");
                    errorCancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ekycActivity.finish();
                        }
                    });
                } else {
                    kycErrorLayout.setVisibility(View.VISIBLE);
                    kycDetailLayoutNew.setVisibility(View.GONE);
                    kycErrorTextView.setText("Unknown Error" + "\n\n" + "Response time : " + totalTime + " miliseconds");
                    errorCancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ekycActivity.finish();
                        }
                    });
                }
            } else {
                kycErrorLayout.setVisibility(View.VISIBLE);
                kycDetailLayoutNew.setVisibility(View.GONE);
                kycErrorTextView.setText("Unknown Error : " + JSON + "\n\n" + "Response time : " + totalTime + " miliseconds");
                errorCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ekycActivity.finish();
                    }
                });
            }
        } catch (Exception ex) {
            kycErrorLayout.setVisibility(View.VISIBLE);
            kycDetailLayoutNew.setVisibility(View.GONE);
            kycErrorTextView.setText("Unknown Error : " + JSON + "\n\n" + "Response time : " + totalTime + " miliseconds");
            errorCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ekycActivity.finish();
                }
            });
        }
    }

    private void setupData() {
        if (check) {
            startActivity();
        }
        loginResponse = (VerifierLoginResponse.create(ProjectPrefrence.
                getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.VERIFIER_CONTENT, context)));
        selectedMemItem = SelectedMemberItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
        if (selectedMemItem != null && selectedMemItem.getSeccMemberItem() != null) {
            seccItem = selectedMemItem.getSeccMemberItem();
        }

        if (seccItem != null) {
            if (seccItem.getAadhaarCapturingMode() != null) {
                if (seccItem.getAadhaarCapturingMode().equalsIgnoreCase(AppConstant.IRIS_MODE) || seccItem.getAadhaarCapturingMode().equalsIgnoreCase("")) {
                    if (seccItem.getAadhaarAuth() != null && !seccItem.getAadhaarAuth().equalsIgnoreCase("")) {
                        card_view1.setVisibility(View.VISIBLE);


                        if (seccItem.getAadhaarNo() != null) {
                            aadharNum.setText(seccItem.getAadhaarNo());
                            edtxt_Aadhaar.setText(seccItem.getAadhaarNo());
                        }
                        if (seccItem.getAadhaarGender() != null) {
                            genderAsAadhar.setText(seccItem.getAadhaarGender());
                        }
                        if (seccItem.getNameAadhaar() != null) {
                            nameAsAadhar.setText(seccItem.getNameAadhaar());
                        }
                        if (seccItem.getAadhaarDob() != null) {
                            dobAsAadhar.setText(seccItem.getAadhaarDob());
                        }


                    }
                }
            }
        }
    }


    private void alertForValidateLater(String aadharName, SeccMemberItem item) {
        internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.aadhar_validation_msg_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();

        TextView nameAsInAadharTV = (TextView) alertView.findViewById(R.id.nameAsInAadharTV);
        TextView nameAsInSeccTV = (TextView) alertView.findViewById(R.id.nameAsInSeccTV);
        nameAsInAadharTV.setText(aadharName);
        nameAsInSeccTV.setText(item.getName());
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText(context.getResources().getString(R.string.Confirm_Submit));
        final Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seccItem.setAadhaarAuth(AppConstant.VALID_STATUS);
                qrCodeSubmitAaadhaarDetail();
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
    }

    private void qrCodeSubmitAaadhaarDetail() {

        seccItem.setAadhaarVerifiedBy(loginResponse.getAadhaarNumber());
        if (aadhaarKycResponse != null) {
            seccItem.setAadhaarCapturingMode(AppConstant.IRIS_MODE);
            seccItem.setAadhaarNo(aadhaarKycResponse.getUid());
            seccItem.setNameAadhaar(aadhaarKycResponse.getName());
            seccItem.setAadhaarGender(aadhaarKycResponse.getGender());
            seccItem.setAadhaarDob(aadhaarKycResponse.getDob());
            seccItem.setConsent(consent);
            seccItem.setAadhaarSurveyedStat(AppConstant.SURVEYED + "");
            seccItem.setAadhaarAuth(AppConstant.VALID_STATUS);
            seccItem.setAadhaarAuthDt(String.valueOf(DateTimeUtil.currentTimeMillis()));
            if (seccItem.getAadhaarStatus().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
                updateAadhaarDetail();
            }
        }


    }

    private void updateAadhaarDetail() {
        Intent theIntent = new Intent(context, WithAadhaarActivity.class);
        seccItem.setLockedSave(AppConstant.SAVE + "");
        if (seccItem != null && seccItem.getDataSource() != null && seccItem.getDataSource()
                .trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            if (selectedMemItem.getOldHeadMember() != null && selectedMemItem.getNewHeadMember() != null) {
                SeccMemberItem oldHead = selectedMemItem.getOldHeadMember();
                oldHead.setLockedSave(AppConstant.LOCKED + "");
                AppUtility.showLog(AppConstant.LOG_STATUS, "", " OLD Household name " +
                        ": " + oldHead.getName() + "" +
                        " Member Status " + oldHead.getMemStatus() + " House hold Status :" +
                        " " + oldHead.getHhStatus() + " Locked Save :" + oldHead.getLockedSave());
                SeccDatabase.updateRsbyMember(selectedMemItem.getOldHeadMember(), context);
            }
            SeccDatabase.updateRsbyMember(seccItem, context);
            SeccDatabase.updateRsbyHousehold(selectedMemItem.getHouseHoldItem(), context);
            seccItem = SeccDatabase.getSeccMemberDetail(seccItem, context);

        } else {
            if (selectedMemItem.getOldHeadMember() != null && selectedMemItem.getNewHeadMember() != null) {
                SeccMemberItem oldHead = selectedMemItem.getOldHeadMember();
                oldHead.setLockedSave(AppConstant.LOCKED + "");
                AppUtility.showLog(AppConstant.LOG_STATUS, "", " OLD Household name " +
                        ": " + oldHead.getName() + "" +
                        " Member Status " + oldHead.getMemStatus() + " House hold Status :" +
                        " " + oldHead.getHhStatus() + " Locked Save :" + oldHead.getLockedSave());
                SeccDatabase.updateSeccMember(selectedMemItem.getOldHeadMember(), context);
            }

            SeccDatabase.updateSeccMember(seccItem, context);
            SeccDatabase.updateHouseHold(selectedMemItem.getHouseHoldItem(), context);
            seccItem = SeccDatabase.getSeccMemberDetail(seccItem, context);
        }

        selectedMemItem.setSeccMemberItem(seccItem);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemItem.serialize(), context);
        startActivity(theIntent);
        ekycActivity.finish();
        ekycActivity.rightTransition();

    }

    protected Boolean activateIrisLicense() {
        try {
            SecBiometricLicenseManager mLicenseMgr;
            mLicenseMgr = SecBiometricLicenseManager.getInstance(context);
            IntentFilter filter = new IntentFilter();
            filter.addAction(SecBiometricLicenseManager.ACTION_LICENSE_STATUS);
            filter.addAction(SecBiometricLicenseManager.ACTION_LICENSE_STATUS);
            // registerReceiver(mReceiver, filter, null, null);
            String key = "7FD14956718AECD5049ABCFB54D8B72E07E05D3297F0295D6699413F2D0D0D09F3BF7CF097683529659DADC28DDCACC9BF9BA0896F4ABE91D653B55721EE1022";

            String packageName = context.getPackageName();
            mLicenseMgr.activateLicense(key, packageName);
            return true;
        } catch (Exception e) {
            Log.d("TAG", "Iris Exception : " + e.getStackTrace());
        }

        return false;
    }
}

