package com.aadhar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;




public class DemoAuth extends AppCompatActivity {
    EditText etAadhar,et_name,et_dobY,et_dobM,et_dobD;
    Button btn_auth;
    Spinner spinner_gender;
    String dobFormate;
    boolean formValidate = false ;
    boolean pi=true,checkNetwork, validAadhaar,pa,pfa;
    ProgressDialog barProgressDialog;
    private CheckConnection checkConnection;
    private ShowDialogWaitForAuth dialogProcessRequest;
    private String xml,name,dob,gender;
    private long startTime,endTime,totalTime;
    private Logs log;
    private StatusLogs statusLog;
    private Context context;
    private TextWatcher inputTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (etAadhar.length() == 12) {
                try {
                    if (VerhoeffAadhar.validateVerhoeff(etAadhar.getEditableText()
                            .toString())) {
                        etAadhar.setTextColor(Color.parseColor("#0B610B"));
                        validAadhaar = true;

                    } else {
                        etAadhar.setTextColor(Color.parseColor("#ff0000"));
                        validAadhaar = false;
                    }

                } catch (Exception e) {
                }
            } else {
                etAadhar.setTextColor(Color.parseColor("#000000"));
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

    public static String getDemoAuthXml(Context context, String productionKey, String aadhaarNo, boolean pi, boolean pa, boolean pfa, String name, String dob, String gender) {
        String kycXml = "";
        byte[] publicKey = null;
        //		GetPubKeycertificateData();
        publicKey = Base64.decode(productionKey, Base64.DEFAULT);
        String imeiNo = CommonMethods.GetIMEI(context);
        Log.e("demoIMEI", imeiNo);
        UidaiAuthHelper helper = new UidaiAuthHelper(publicKey);
        // edited by saurabh

        kycXml = "";//helper.createXmlForDemoAuthCustom(aadhaarNo,pi,pa,pfa,name,dob,gender);
        Log.e("demo auth xml", "==" + kycXml);
        kycXml = helper.createCustomXmlForAuth(kycXml, "demo_auth");

        return kycXml;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demoauth);
        context =this;
        et_name=(EditText)findViewById(R.id.et_name);
        et_dobY=(EditText)findViewById(R.id.et_dobY);
        et_dobM=(EditText)findViewById(R.id.et_dobM);
        et_dobD=(EditText)findViewById(R.id.et_dobD);
        btn_auth=(Button)findViewById(R.id.btn_auth);
        spinner_gender=(Spinner)findViewById(R.id.spinner_gender);
        dialogProcessRequest = new ShowDialogWaitForAuth(
                DemoAuth.this);
        dialogProcessRequest.setCancelable(false);

        checkConnection = new CheckConnection(DemoAuth.this);
        log = new Logs(DemoAuth.this);
        statusLog = new StatusLogs(this);
        dobFormate = "YYYY-MM-DD";
        name=dob=gender="";
        pi = true;
        CommonMethods.SetApplicationContext(DemoAuth.this);
        dobFormate = "YYYY-MM-DD";
        barProgressDialog = new ProgressDialog(DemoAuth.this);
        barProgressDialog.setTitle("");
        barProgressDialog.setMessage("Please wait ...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setCancelable(false);
        barProgressDialog.show();

        checkNetwork = checkConnection.isConnectingToInternet();
        if (checkNetwork) {
            GetPubKeycertificateData publicKey1 = new GetPubKeycertificateData();
            publicKey1.execute();
            //		init();
        }else {
            ShowPromptNetwork("Network Issue", "Please Enable the network");
        }
    }

    public  String getPidBlockXml(){
            GetPubKeycertificateData publicKey1 = new GetPubKeycertificateData();
            publicKey1.execute();
            //		init();

      return null;
    }

    public boolean check_form_Validation() {


        if (pi) {
            formValidate = true;
           // CommonMethods.showErrorDialog("ok","okok");
        } else {
           formValidate = false;
            CommonMethods.showErrorDialog("Invalid Value",
                    "Please Select any for Demographic Authentication");
        }

        if (pi) {

            String pi_name = et_name.getText().toString().trim();
            Log.d("name",pi_name);
            int sp_gender = spinner_gender.getSelectedItemPosition();
            String dobY = et_dobY.getText().toString();
            String dobM = et_dobM.getText().toString();
            String dobD = et_dobD.getText().toString();

            if (dobFormate.equalsIgnoreCase("YYYY-MM-DD")) {
                if (dobY.length() == 4 && !dobM.equalsIgnoreCase("") && !dobD.equalsIgnoreCase("")) {
                    String mmf = dobM;
                    String ddf = dobD;
                    int mm = Integer.parseInt(dobM);
                    int dd = Integer.parseInt(dobD);
                    if (mm < 10 && mm != 2) {
                        mmf = "0" + mmf;
                    }
                    if (dd < 10 && dd != 2) {
                        ddf = "0" + ddf;
                    }
                    dobY = dobY + "-" + mmf + "-" + ddf;
                    Log.d("date",dobY);

                } else {
                    if (dobY.equalsIgnoreCase("") && dobM.equalsIgnoreCase("") && dobD.equalsIgnoreCase("")) {
                        formValidate = false;
                    } else {
                        CommonMethods.showErrorDialog("Invalid Value",
                                "Please check Date (YYYY-MM-DD)");
                        Log.e("else", "---->");
                       return formValidate = false;
                    }
                }
            }

            if (pi_name.equalsIgnoreCase("") && sp_gender == 0 && dob.equalsIgnoreCase("")) {
                CommonMethods.showErrorDialog("Invalid Value",
                        "Please enter any value of Personal Identification");
                return formValidate = false;
            }else
            {
                name=pi_name;
                gender = sp_gender+"";
                dob = dobY;
                Log.d("date_full",dob);
                if (spinner_gender.getSelectedItemPosition() == 1) {
                    gender = "M";
                }else if (spinner_gender.getSelectedItemPosition() == 2) {
                    gender = "F";
                }else if (spinner_gender.getSelectedItemPosition() == 3) {
                    gender = "T";
                }else {
                    gender = "";
                }
                formValidate = true;
            }


        }

        return formValidate;
    }

    private void ShowPromptNetwork(String title, String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                DemoAuth.this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.erroricon);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                init();
            }
        });
        dlgAlert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        btn_auth.setEnabled(false);

                    }
                });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    private void ShowPrompt(String title, String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                DemoAuth.this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.erroricon);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });
        dlgAlert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        btn_auth.setEnabled(false);
                    }
                });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    private void ShowPromptNetworkforAUTH(String title, String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                DemoAuth.this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.erroricon);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialogProcessRequest.dismiss();
                hitToServerforDemoAUTHRequest();

            }
        });
        dlgAlert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialogProcessRequest.dismiss();
                        dialogProcessRequest.cancel();

                    }
                });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    public void hitToServerforDemoAUTHRequest(){
        checkNetwork = checkConnection.isConnectingToInternet();
        if (checkNetwork) {
            startTime = System.currentTimeMillis();
            HitToServer task = new HitToServer();
            task.execute();
        }else{
            ShowPromptNetworkforAUTH("Network Issue", "Please Enable the network");
        }
    }

    private void ShowErrorMessage(String error) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                DemoAuth.this);
        dlgAlert.setCancelable(false);
        String message = null;

        message = error;
        dlgAlert.setMessage(message);
        dlgAlert.setTitle("Warning");
        dlgAlert.setIcon(R.drawable.erroricon);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                /*Intent intent = new Intent(DemoAuth.this,MainScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);*/
                finish();

            }
        });
        dlgAlert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

            /*}

        }*/
       /* if (pa) {
            String co = editText_Pa_co.getText().toString().trim();
            String house = editText_Pa_house.getText().toString().trim();
            String street = editText_Pa_street.getText().toString().trim();
            String landmark = editText_Pa_lm.getText().toString().trim();
            String loc = editText_Pa_loc.getText().toString().trim();
            String vill = editText_Pa_vtc.getText().toString().trim();
            String sub_dist = editText_Pa_subDist.getText().toString().trim();
            String dist = editText_Pa_dist.getText().toString().trim();
            String state = editText_Pa_state.getText().toString().trim();
            String pc = editText_Pa_pc.getText().toString().trim();
            String po = editText_Pa_po.getText().toString().trim();

            if (co.equalsIgnoreCase("") && house.equalsIgnoreCase("") && street.equalsIgnoreCase("") && landmark.equalsIgnoreCase("") && loc.equalsIgnoreCase("") &&
                    vill.equalsIgnoreCase("") && vill.equalsIgnoreCase("") && sub_dist.equalsIgnoreCase("") && dist.equalsIgnoreCase("") && state.equalsIgnoreCase("") &&
                    pc.equalsIgnoreCase("") && po.equalsIgnoreCase("")){
                formValidate = false;
                CommonMethods.showErrorDialog("Invalid Value",
                        "Please enter any value of Persoanl Address");
            }else {
                pa_co = co;
                pa_house = house;
                pa_street = street;
                pa_landmark = landmark;
                pa_loc = loc;
                pa_vill = vill;
                pa_subdist = sub_dist;
                pa_dist = dist;
                pa_state =state;
                pa_pc = pc;
                pa_po = po;
                formValidate = true;
            }

        }

        if (pfa) {
            int pos = spinner_Pfa_ms.getSelectedItemPosition();
            if (pos == 2) {
                if (editText_Pfa_mv.getText().toString().equalsIgnoreCase("")) {
                    formValidate = false;
                    CommonMethods.showErrorDialog("Invalid Value",
                            "Please enter match %(1-100) in Full Address");
                }else {
                    if (spinner_Pfa_ms.getSelectedItemPosition() != 0) {
                        pfa_ms = spinner_Pfa_ms.getSelectedItemPosition() + "";
                    }
                    pfa_mv = editText_Pfa_mv.getText().toString();
                    if (pos == 1) {

                        pfa_ms = "E";
                        pfa_mv = "100";
                    }else if (pos ==2) {
                        pfa_ms = "P";
                    }else {
                        pfa_ms = "E";
                        pfa_mv = "100";
                    }

                    if (editText_Pfa_av.getText().toString().trim().equalsIgnoreCase("")) {
                        formValidate = false;
                        CommonMethods.showErrorDialog("Invalid Value",
                                "Please enter value in Full Address");
                    }else {
                        pfa_av = editText_Pfa_av.getText().toString().trim();
                        formValidate = true;
                    }
                }
            }else{
                if (pos == 1) {
                    pfa_ms = "E";
                    pfa_mv = "100";
                }
                if (editText_Pfa_av.getText().toString().trim().equalsIgnoreCase("")) {
                    formValidate = false;
                    CommonMethods.showErrorDialog("Invalid Value",
                            "Please enter value in Full Address");
                }else {
                    pfa_av = editText_Pfa_av.getText().toString().trim();
                    formValidate = true;
                }
            }


        }
*/
            private void init() {
                btn_auth=(Button)findViewById(R.id.btn_auth);
                btn_auth.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Log.e("dob", "------------->"+et_dobY.getText().toString());
                        checkNetwork = checkConnection.isConnectingToInternet();
                        /*Global.AADHAAR_NO = Global.AUTH_AADHAAR = etAadhar.getText().toString().trim();
                        //dialogProcessRequest.show();
                        Log.e("onChecked"," pi"+pi);

                        xml = getDemoAuthXml(Global.AUTH_AADHAAR ,pi,name,dob,gender);
                        // hitToServerforDemoAUTHRequest();
                        Toast.makeText(DemoAuth.this,"valid",Toast.LENGTH_LONG).show();*/

                        if (checkNetwork) {
                            if (validAadhaar ) {
                                if (check_form_Validation() && formValidate) {

                                    Global.AADHAAR_NO = Global.AUTH_AADHAAR = etAadhar.getText().toString().trim();
                                 //   dialogProcessRequest.show();
                                    Log.e("onChecked"," pi"+pi);

                               //     xml = getDemoAuthXml(DemoAuth.this,Global.AUTH_AADHAAR ,pi,pa,pfa,name,dob,gender);
                                    //hitToServerforDemoAUTHRequest();
                                    System.out.print(xml);
                                    Toast.makeText(DemoAuth.this,"valid",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                CommonMethods.showErrorDialog("Invalid Value",
                                        "Please check Aadhaar Number");
                                Toast.makeText(DemoAuth.this,"invalid",Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });

                etAadhar=(EditText)findViewById(R.id.etAadhaar);
                etAadhar.addTextChangedListener(inputTextWatcher);
                if (!Global.AUTH_AADHAAR.equalsIgnoreCase("")) {
                    etAadhar.setText(Global.AUTH_AADHAAR);
                    etAadhar.setTextColor(Color.parseColor("#0B610B"));
                    validAadhaar  = true;
                }

                et_dobY=(EditText)findViewById(R.id.et_dobY);
                et_dobM=(EditText)findViewById(R.id.et_dobM);
                et_dobD=(EditText)findViewById(R.id.et_dobD);
                et_dobM.setFilters(new InputFilter[]{new InputFilterMinMax("1", "12")});

                et_dobD.setFilters(new InputFilter[]{new InputFilterMinMax("1", "31")});

                et_dobY.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub
                        if (et_dobY.getText().toString().length() == 4) {
                            et_dobM.requestFocus();
                        }

                    }
                });

                et_dobM.addTextChangedListener(new TextWatcher() {

			/* private static final int TOTAL_SYMBOLS = 5; // size of pattern 0000-0000-0000-0000
	        private static final int TOTAL_DIGITS = 4; // max numbers of digits in pattern: 0000 x 4
	        private static final int DIVIDER_MODULO = 3; // means divider position is every 5th symbol beginning with 1
	        private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
	        private static final char DIVIDER = '-';*/

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // noop
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // noop
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        //	            if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                        //	                s.replace(0, s.length(), buildCorrecntString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
                        //	            }
                        if (et_dobM.getText().toString().length() == 2) {
                            et_dobD.requestFocus();
                        }
                    }
                });

				/*String str =et_Pi_Dob2.getText().toString();
	        	if (et_Pi_Dob2.getText().toString().length() == 2) {
	        		et_Pi_Dob2.setText(str + "-");
	        		et_Pi_Dob2.setSelection(3);


				}*/
                    }





            /*if (dobFormate.equalsIgnoreCase("YYYY-MM-DD")) {
                if (dob.length()==4 && !et_Pi_Dob2.getText().toString().equalsIgnoreCase("") && !et_Pi_Dob3.getText().toString().equalsIgnoreCase("")) {
                    String mmf = et_Pi_Dob2.getText().toString();
                    String ddf = et_Pi_Dob3.getText().toString();
                    int mm = Integer.parseInt(et_Pi_Dob2.getText().toString());
                    int dd = Integer.parseInt(et_Pi_Dob3.getText().toString());
                    if (mm < 10 && et_Pi_Dob2.getText().toString().length() !=2) {
                        mmf = "0" + mmf;
                    }
                    if (dd <10 && et_Pi_Dob3.getText().toString().length() !=2) {
                        ddf = "0" + ddf;
                    }
                    dob = dob + "-" + mmf + "-" + ddf;

                }else{
                    if (dob.equalsIgnoreCase("") && et_Pi_Dob2.getText().toString().equalsIgnoreCase("") && et_Pi_Dob3.getText().toString().equalsIgnoreCase("")) {
                        formValidate = false;
                    }else{
                        CommonMethods.showErrorDialog("Invalid Value",
                                "Please check Date (YYYY-MM-DD)");
                        Log.e("else","---->");
                        return formValidate = false;
                    }
                }
            }else if (dobFormate.equalsIgnoreCase("YYYY")) {
                if (dob.length()==4) {

                }else{
                    if (dob.equalsIgnoreCase("")) {
                        formValidate = false;
                    }else{
                        CommonMethods.showErrorDialog("Invalid Value",
                                "Please check Date (YYYY)");
                        return formValidate = false;
                    }
                }
            }

            String age = editText_PI_age.getText().toString();
            String ph = editText_PI_mob.getText().toString();
            String mail = editText_PI_mail.getText().toString();
            if (name.equalsIgnoreCase("") && gender == 0 && dob.equalsIgnoreCase("") && age.equalsIgnoreCase("") && ph.equalsIgnoreCase("") && mail.equalsIgnoreCase("")) {
                CommonMethods.showErrorDialog("Invalid Value",
                        "Please enter any value of Personal Identification");
                return formValidate = false;
            }else {

                int pos = spinner_PI_Type.getSelectedItemPosition();
                if (pos == 2 ) {
                    if (editText_PI_Mv.getText().toString().equalsIgnoreCase("")) {
                        CommonMethods.showErrorDialog("Invalid Value",
                                "Please enter match %(1-100) in Personal Identification");
                        return formValidate = false;
                    }else {
                        if (pos ==1) {
                            pi_ms = "E";
                        }else if (pos == 2) {
                            pi_ms = "P";
                        }else {
                            pi_ms = "";
                        }
                        pi_mv = editText_PI_Mv.getText().toString();
                        pi_name = name;
                        pi_gender = gender+"";
                        pi_dob = dob;
                        pi_age = age;
                        pi_ph = ph;
                        pi_mail = mail;
                        if (spinner_PI_dobtype.getSelectedItemPosition() == 1) {

                            pi_dobt = "V";
                        }else if (spinner_PI_dobtype.getSelectedItemPosition() == 2) {
                            pi_dobt = "D";
                        }else if (spinner_PI_dobtype.getSelectedItemPosition() == 3) {
                            pi_dobt = "A";
                        }else {
                            pi_dobt = "";
                        }
                        if (spinner_PI_gender.getSelectedItemPosition() == 1) {
                            pi_gender = "M";
                        }else if (spinner_PI_gender.getSelectedItemPosition() == 2) {
                            pi_gender = "F";
                        }else if (spinner_PI_gender.getSelectedItemPosition() == 3) {
                            pi_gender = "T";
                        }else {
                            pi_gender = "";
                        }

                        formValidate = true;
                    }
                }else{



                    pi_name = name;
                    pi_gender = gender+"";
                    pi_dob = dob;
                    pi_age = age;
                    pi_ph = ph;
                    pi_mail = mail;
                    if (spinner_PI_dobtype.getSelectedItemPosition() == 1) {

                        pi_dobt = "V";
                    }else if (spinner_PI_dobtype.getSelectedItemPosition() == 2) {
                        pi_dobt = "D";
                    }else if (spinner_PI_dobtype.getSelectedItemPosition() == 3) {
                        pi_dobt = "A";
                    }else {
                        pi_dobt = "";
                    }
                    if (spinner_PI_gender.getSelectedItemPosition() == 1) {
                        pi_gender = "M";
                    }else if (spinner_PI_gender.getSelectedItemPosition() == 2) {
                        pi_gender = "F";
                    }else if (spinner_PI_gender.getSelectedItemPosition() == 3) {
                        pi_gender = "T";
                    }else {
                        pi_gender = "";
                    }


                    if (!mail.equalsIgnoreCase("")) {
                        if (validEmail) {
                            formValidate = true;
                        }else{
                            CommonMethods.showErrorDialog("Invalid Value",
                                    "Please check Registered Email");
                            return formValidate = false;
                        }
                    }else{
                        formValidate = true;
                    }
                }
*/

    public void readAuthXml(String result)
    {
        //appendLog(result);

        String rt = null;
        endTime   = System.currentTimeMillis();
        totalTime = endTime - startTime;
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc2 = dBuilder.parse(new InputSource(new StringReader(result)));

            //optional, but recommended

            // XPath to retrieve the content of the <FamilyAnnualDeductibleAmount> tag
            XPath xpath2 = XPathFactory.newInstance().newXPath();


            XPathExpression expImei = xpath2.compile("/xml/imeivalid");
            String imeiValid = (String)expImei.evaluate(doc2, XPathConstants.STRING);
            String encodedResponse;
            Document doc = null;
            XPath xpath=null;
            if (imeiValid.equalsIgnoreCase("true")) {
                XPathExpression expRes = xpath2.compile("/xml/res");
                String response = (String)expRes.evaluate(doc2, XPathConstants.STRING);

                byte[] data = Base64.decode(response, Base64.DEFAULT);
                encodedResponse = new String(data, "UTF-8");

                xpath = XPathFactory.newInstance().newXPath();

                doc = dBuilder.parse(new InputSource(new StringReader(encodedResponse)));
                XPathExpression exprt = xpath.compile("/AuthRes/@ret");
                rt = (String)exprt.evaluate(doc, XPathConstants.STRING);

                XPathExpression expcode = xpath.compile("/AuthRes/@code");
                String authcode = (String)expcode.evaluate(doc, XPathConstants.STRING);

                XPathExpression expts = xpath.compile("/AuthRes/@ts");
                String authrests = (String)expts.evaluate(doc, XPathConstants.STRING);

                Log.e("rt", "==="+rt);
                String status = "";
                if (rt.equalsIgnoreCase("Y")) {
                    status = "Demo Auth Success";
                    ShowPromptMessage(rt, "", totalTime,authcode,authrests);
                }else{
                    status = "Demo Auth failed";
                    XPathExpression expErr = xpath.compile("/AuthRes/@err");
                    String error = (String)expErr.evaluate(doc, XPathConstants.STRING);

                    XPathExpression expErrmsg = xpath.compile("/AuthRes/@msg");
                    String errormsg = (String)expErrmsg.evaluate(doc, XPathConstants.STRING);

                    int err = Integer.parseInt(error);
                    switch (err) {
                        case 100:
                            error = "?Pi? (basic) attributes of demographic data did not match-"+err;
                            break;

                        case 200:
                            error = "?Pa? (address) attributes of demographic data did not match-"+err;
                            break;

                        case 300:
                            error = "Biometric data did not match-"+err;
                            break;

                        case 310:
                            error = "Duplicate fingers used-"+err;
                            break;

                        case 311:
                            error = "Duplicate Irises used-"+err;
                            break;

                        case 312:
                            error = "FMR and FIR cannot be used in same transaction-"+err;
                            break;

                        case 313:
                            error = "Single FIR record contains more than one finger-"+err;
                            break;

                        case 314:
                            error = "Number of FMR/FIR should not exceed 10-"+err;
                            break;

                        case 315:
                            error = "Number of IIR should not exceed 2-"+err;
                            break;

                        case 400:
                            error = "Invalid OTP value-"+err;
                            break;

                        case 401:
                            error = "Invalid TKN value-"+err;
                            break;

                        case 500:
                            error = "Invalid encryption of Skey-"+err;
                            break;

                        case 501:
                            error = "Invalid certificate identifier in ?ci? attribute of ?Skey?-"+err;
                            break;

                        case 502:
                            error = "Invalid encryption of Pid-"+err;
                            break;

                        case 503:
                            error = "Invalid encryption of Hmac-"+err;
                            break;

                        case 504:
                            error = "Session key re-initiation required due to expiry or key out of sync-"+err;
                            break;

                        case 505:
                            error = "Synchronized Key usage not allowed for the AUA-"+err;
                            break;

                        case 510:
                            error = "Invalid Auth XML format-"+err;
                            break;

                        case 511:
                            error = "Invalid PID XML format-"+err;
                            break;

                        case 520:
                            error = "Invalid device-"+err;
                            break;

                        case 521:
                            error = "Invalid FDC code under Meta tag-"+err;

                        case 522:
                            error = "Invalid IDC code under Meta tag-"+err;
                            break;

                        case 530:
                            error = "Invalid authenticator code-"+err;
                            break;

                        case 540:
                            error = "Invalid Auth XML version-"+err;
                            break;

                        case 541:
                            error = "Invalid PID XML version-"+err;
                            break;

                        case 542:
                            error = "AUA not authorized for ASA. This erroror will be returned if AUA and ASA do not have linking in the portal-"+err;

                        case 543:
                            error = "Sub-AUA not associated with ?AUA?. This error will be returned if Sub-AUA specified in ?sa? attribute is not added as ?Sub-AUA? in portal-"+err;
                            break;

                        case 550:
                            error = "Invalid ?Uses? element attributes-"+err;
                            break;

                        case 551:
                            error = "Invalid ?tid? value for registered device-"+err;
                            break;

                        case 552:
                            error = "Invalid registered device key, please reset-"+err;
                            break;

                        case 553:
                            error = "Invalid registered device HOTP, please reset-"+err;

                        case 554:
                            error = "Invalid registered device encryption-"+err;
                            break;

                        case 555:
                            error = "Mandatory reset required for registered device-"+err;
                            break;

                        case 561:
                            error = "Request expired-"+err;
                            break;

                        case 562:
                            error = "Timestamp value is future time-"+err;
                            break;

                        case 563:
                            error = "Duplicate request-"+err;

                        case 564:
                            error = "HMAC Validation failed-"+err;
                            break;

                        case 565:
                            error = "AUA license has expired-"+err;
                            break;

                        case 566:
                            error = "Invalid non-decryptable license key-"+err;
                            break;

                        case 567:
                            error = "Invalid input-"+err;
                            break;

                        case 568:
                            error = "Unsupported Language-"+err;
                            break;

                        case 569:
                            error = "Digital signature verification failed-"+err;
                            break;

                        case 570:
                            error = "Invalid key info in digital signature-"+err;
                            break;

                        case 571:
                            error = "PIN Requires reset-"+err;
                            break;

                        case 572:
                            error = "Invalid biometric position-"+err;
                            break;

                        case 573:
                            error = "Pi usage not allowed as per license-"+err;
                            break;

                        case 574:
                            error = "Pa usage not allowed as per license-"+err;
                            break;

                        case 575:
                            error = "Pfa usage not allowed as per license-"+err;
                            break;

                        case 576:
                            error = "FMR usage not allowed as per license-"+err;
                            break;

                        case 577:
                            error = "FIR usage not allowed as per license-"+err;
                            break;

                        case 578:
                            error = "IIR usage not allowed as per license-"+err;
                            break;

                        case 579:
                            error = "OTP usage not allowed as per license-"+err;
                            break;

                        case 580:
                            error = "PIN usage not allowed as per license-"+err;
                            break;

                        case 581:
                            error = "Fuzzy matching usage not allowed as per license-"+err;
                            break;

                        case 582:
                            error = "Local language usage not allowed as per license-"+err;
                            break;

                        case 584:
                            error = "Invalid pincode in LOV attribute under Meta tag-"+err;
                            break;

                        case 585:
                            error = "Invalid geo-code in LOV attribute under Meta tag-"+err;
                            break;

                        case 710:
                            error = "Missing ?Pi? data as specified in ?Uses?-"+err;
                            break;

                        case 720:
                            error = "Missing ?Pa? data as specified in ?Uses?-"+err;
                            break;

                        case 721:
                            error = "Missing ?Pfa? data as specified in ?Uses?-"+err;
                            break;

                        case 730:
                            error = "Missing PIN data as specified in ?Uses?-"+err;
                            break;

                        case 740:
                            error = "Missing OTP data as specified in ?Uses?-"+err;
                            break;

                        case 800:
                            error = "Invalid biometric data-"+err;
                            break;

                        case 810:
                            error = "Missing biometric data as specified in ?Uses?-"+err;
                            break;

                        case 811:
                            error = "Missing biometric data in CIDR for the given Aadhaar number-"+err;
                            break;

                        case 812:
                            error = "Resident has not done ?Best Finger Detection?.-"+err;
                            break;

                        case 820:
                            error = "Missing or empty value for ?bt? attribute in ?Uses? element-"+err;
                            break;

                        case 821:
                            error = "Invalid value in the ?bt? attribute of ?Uses? element-"+err;
                            break;

                        case 901:
                            error = "No authentication data found in the request-"+err;
                            break;

                        case 902:
                            error = "Invalid ?dob? value in the ?Pi? element-"+err;
                            break;

                        case 910:
                            error = "Invalid ?mv? value in the ?Pi? element-"+err;
                            break;

                        case 911:
                            error = "Invalid ?mv? value in the ?Pfa? element-"+err;
                            break;

                        case 912:
                            error = "Invalid ?ms? value-"+err;
                            break;

                        case 913:
                            error = "Both ?Pa? and ?Pfa? are present in the authentication request-"+err;
                            break;

                        case 930:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 931:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 932:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 933:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 934:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 935:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 936:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 937:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 938:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 939:
                            error = "Technical error that are internal to authentication server-"+err;
                            break;

                        case 940:
                            error = "Unauthorized ASA channel-"+err;
                            break;

                        case 941:
                            error = "Unspecified ASA channel-"+err;
                            break;

                        case 980:
                            error = "Unsupported option-"+err;
                            break;

                        case 997:
                            error = "Invalid Aadhaar status-"+err;
                            break;

                        case 998:
                            error = "Invalid Aadhaar Number-"+err;
                            break;

                        case 999:
                            error = "Unknown error-"+err;
                            break;

                        case 9903:
                            error = errormsg;
                            break;

                        default:
                            break;
                    }

                    ShowPromptMessage(rt,error,totalTime,authcode,authrests);
                }
                statusLog.myMessage(authcode, totalTime+" ms",status);
            }else{
                log.myMessage(getResources().getString(R.string.invalid_device) + " Demo Auth", Global.AUTH_AADHAAR);
                ShowPromptMessage("n","Unauthorised Device",totalTime,"","");
            }

        } catch (Exception e) {
            Log.e("Exception", "=="+e);
            e.printStackTrace();
            appendLogInvalidXml("<<<<<<<<<<-------"+ result + "-------->>>>>>>>"+ "DEMO AUTH>>>>>");
            showMyMessage("Invalid response XML");
        }

    }


    // TODO Auto-generated method stub
    //		dpResult = (DatePicker) findViewById(R.id.dpResult);
               /* btnScanQR = (Button) findViewById(R.id.scan);

                ll_Pa = (LinearLayout) findViewById(R.id.layout_pa);
                ll_Pfa = (LinearLayout) findViewById(R.id.layout_pfa);
//		sv_pa = (ScrollView) findViewById(R.id.scroll);

                ll_PI = (LinearLayout) findViewById(R.id.layoutPI);

                btnAuth = (Button) findViewById(R.id.auth_demo);
                btnHome = (Button) findViewById(R.id.home);
                btnHome.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(DemoAuth.this,MainScreen.class);
                        startActivity(intent);
                        finish();
                    }
                });

                tv_exceptionView = (TextView) findViewById(R.id.exceptionView);

                btnAuth.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Log.e("dob", "------------->"+et_Pi_Dob.getText().toString());
                        checkNetwork = checkConnection.isConnectingToInternet();
                        if (checkNetwork) {
                            if (validAadhaar ) {
                                if (check_form_Validation() && formValidate) {

                                    Global.AADHAAR_NO = Global.AUTH_AADHAAR = editText_Aadhaar.getText().toString().trim();
                                    dialogProcessRequest.show();
                                    Log.e("onChecked", "--> pa"+ pa + " pfa"+pfa + " pi"+pi);

                                    xml = getDemoAuthXml(Global.AUTH_AADHAAR ,pi,pa,pfa,pi_ms,pi_mv,pi_name,pi_gender,pi_dob,pi_dobt,pi_age,pi_ph,pi_mail,
                                            pa_co,pa_house,pa_street,pa_landmark,pa_loc,pa_vill,pa_subdist,pa_dist,pa_state,pa_pc,pa_po,pfa_ms,pfa_mv,pfa_av);
                                    hitToServerforDemoAUTHRequest();
                                }
                            }else{
                                CommonMethods.showErrorDialog("Invalid Value",
                                        "Please check Aadhaar Number");
                            }
                        }

                    }
                });

                editText_Aadhaar = (EditText) findViewById(R.id.aadhaar);
                editText_Aadhaar.addTextChangedListener(inputTextWatcher);
                if (!Global.AUTH_AADHAAR.equalsIgnoreCase("")) {
                    editText_Aadhaar.setText(Global.AUTH_AADHAAR);
                    editText_Aadhaar.setTextColor(Color.parseColor("#0B610B"));
                    validAadhaar  = true;
                }

                editText_PI_Mv = (EditText) findViewById(R.id.pi_mv);
                editText_PI_Mv.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "100")});

                editText_PI_Name = (EditText) findViewById(R.id.pi_name);
                et_Pi_Dob = (EditText) findViewById(R.id.pi_dob);
                et_Pi_Dob2 = (EditText) findViewById(R.id.pi_dob2);
                et_Pi_Dob2.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "12")});
                et_Pi_Dob3 = (EditText) findViewById(R.id.pi_dob3);
                et_Pi_Dob3.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "31")});

                editText_PI_age = (EditText) findViewById(R.id.pi_age);
                editText_PI_age.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "120")});
                editText_PI_mob = (EditText) findViewById(R.id.pi_phone);
                editText_PI_mail = (EditText) findViewById(R.id.pi_email);

                editText_PI_mail .addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {

                        String email = editText_PI_mail.getText().toString().trim();

                        if (email.matches(emailPattern) && s.length() > 0)
                        {
                            //		            Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                            Log.e("--------------", "valid email address");
                            validEmail = true;
                            // or
                        }
                        else
                        {
                            validEmail = false;
                            Log.e("--------------", "Invalid email address");
                            //		             Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
                            //or
                        }
                    }
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // other stuffs
                    }
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // other stuffs
                    }
                });

                //		txtView_Pi_Dob.addTextChangedListener(dobWatcher);


                spinner_PI_Type = (Spinner) findViewById(R.id.spinner_pi_ms);
                spinner_PI_gender = (Spinner) findViewById(R.id.spinner_pi_gender);
                spinner_PI_dob_formate = (Spinner) findViewById(R.id.spinner_pi_dobformate);
                spinner_PI_dobtype = (Spinner) findViewById(R.id.spinner_pi_dobt);

                spinner_PI_Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // your code here
                        if (position == 2 ) {
                            editText_PI_Mv.setVisibility(View.VISIBLE);
                        }else{
                            editText_PI_Mv.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                        editText_PI_Mv.setVisibility(View.GONE);
                    }

                });

                spinner_PI_dob_formate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // your code here
                        if (position == 1) {
                            et_Pi_Dob.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
                            et_Pi_Dob.setText("");
                            et_Pi_Dob.setHint("YYYY");
                            et_Pi_Dob2.setText("");
                            et_Pi_Dob2.setHint("MM");
                            et_Pi_Dob3.setText("");
                            et_Pi_Dob3.setHint("DD");
                            et_Pi_Dob2.setVisibility(View.VISIBLE);
                            et_Pi_Dob3.setVisibility(View.VISIBLE);
                            dobFormate = "YYYY-MM-DD";
                        }else if (position == 2) {
                            et_Pi_Dob.setFilters(new InputFilter[] {new InputFilter.LengthFilter(4)});
                            et_Pi_Dob.setText("");
                            et_Pi_Dob2.setText("");
                            et_Pi_Dob2.setVisibility(View.INVISIBLE);
                            et_Pi_Dob3.setText("");
                            et_Pi_Dob3.setVisibility(View.INVISIBLE);
                            et_Pi_Dob.setHint("YYYY");
                            dobFormate = "YYYY";
                        }else {
                            et_Pi_Dob.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
                            dobFormate = "YYYY-MM-DD";
                            et_Pi_Dob.setText("");
                            et_Pi_Dob2.setText("");
                            et_Pi_Dob3.setText("");
                            et_Pi_Dob3.setHint("DD");
                            et_Pi_Dob2.setHint("MM");
                            et_Pi_Dob2.setVisibility(View.INVISIBLE);
                            et_Pi_Dob3.setVisibility(View.INVISIBLE);
                            et_Pi_Dob.setHint("YYYY");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                        et_Pi_Dob.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
                        dobFormate = "YYYY-MM-DD";
                        et_Pi_Dob2.setText("");
                        et_Pi_Dob3.setText("");
                        et_Pi_Dob2.setVisibility(View.VISIBLE);
                        et_Pi_Dob3.setVisibility(View.VISIBLE);
                        et_Pi_Dob.setHint("YYYY");
                        et_Pi_Dob2.setHint("MM");
                        et_Pi_Dob3.setHint("DD");
                    }

                });

                editText_Pa_co = (EditText) findViewById(R.id.pa_co);
                editText_Pa_house = (EditText) findViewById(R.id.pa_house);
                editText_Pa_street = (EditText) findViewById(R.id.pa_street);
                editText_Pa_lm = (EditText) findViewById(R.id.pa_lm);
                editText_Pa_loc = (EditText) findViewById(R.id.pa_loc);
                editText_Pa_vtc = (EditText) findViewById(R.id.pa_vtc);
                editText_Pa_subDist = (EditText) findViewById(R.id.pa_subdist);
                editText_Pa_dist = (EditText) findViewById(R.id.pa_dist);
                editText_Pa_state = (EditText) findViewById(R.id.pa_state);
                editText_Pa_pc = (EditText) findViewById(R.id.pa_pc);
                editText_Pa_po = (EditText) findViewById(R.id.pa_po);


                editText_Pfa_mv = (EditText) findViewById(R.id.pfa_mv);
                editText_Pfa_mv.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "100")});
                editText_Pfa_av = (EditText) findViewById(R.id.pfa_av);

                spinner_Pfa_ms = (Spinner) findViewById(R.id.spinner_pfa_ms);

                spinner_Pfa_ms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // TODO Auto-generated method stub
                        if (position == 1) {
                            editText_Pfa_mv.setVisibility(View.GONE);
                        }else if (position == 2) {
                            editText_Pfa_mv.setVisibility(View.VISIBLE);
                        }else {
                            editText_Pfa_mv.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                        editText_Pfa_mv.setVisibility(View.GONE);
                    }
                });

                radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

                radioButton_PA = (RadioButton) findViewById(R.id.radioPA);
                radioButton_Pfa = (RadioButton) findViewById(R.id.radiopPFA);

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // checkedId is the RadioButton selected
                        if (checkedId == R.id.radioPA) {
                            ll_Pa.setVisibility(View.VISIBLE);
                            ll_Pfa.setVisibility(View.GONE);
//					sv_pa.setVisibility(View.VISIBLE);
                            pfa = false;
                            pa = true;
                            Log.e("onChecked", "--> pa"+ pa + " pfa"+pfa);
                        }else if (checkedId == R.id.radiopPFA) {
                            pfa = true;
                            pa = false;
                            ll_Pa.setVisibility(View.GONE);
                            ll_Pfa.setVisibility(View.VISIBLE);
//					sv_pa.setVisibility(View.GONE);
                            Log.e("onChecked", "--> pa"+ pa + " pfa"+pfa);
                        }else if (checkedId == R.id.radiopClear) {
                            ll_Pa.setVisibility(View.GONE);
                            ll_Pfa.setVisibility(View.GONE);
//					sv_pa.setVisibility(View.GONE);
                            pfa = false;
                            pa = false;
                            Log.e("onChecked", "--> pa"+ pa + " pfa"+pfa);
                        }
                    }
                });

                radioGroupPI = (RadioGroup) findViewById(R.id.radioGroup1);
                radioGroupPI.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // TODO Auto-generated method stub
                        if (checkedId == R.id.radioPI) {
                            ll_PI.setVisibility(View.VISIBLE);
                            pi = true;
                        }else if (checkedId == R.id.radiopClearPI) {
                            ll_PI.setVisibility(View.GONE);
                            pi = false;
                        }

                    }
                });

                et_Pi_Dob.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub
                        if (et_Pi_Dob.getText().toString().length() == 4) {
                            et_Pi_Dob2.requestFocus();
                        }

                    }
                });

                et_Pi_Dob2.addTextChangedListener(new TextWatcher() {

			*//* private static final int TOTAL_SYMBOLS = 5; // size of pattern 0000-0000-0000-0000
            private static final int TOTAL_DIGITS = 4; // max numbers of digits in pattern: 0000 x 4
	        private static final int DIVIDER_MODULO = 3; // means divider position is every 5th symbol beginning with 1
	        private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
	        private static final char DIVIDER = '-';*//*

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // noop
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // noop
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        //	            if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                        //	                s.replace(0, s.length(), buildCorrecntString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
                        //	            }
                        if (et_Pi_Dob2.getText().toString().length() == 2) {
                            et_Pi_Dob3.requestFocus();
                        }

				*//*String str =et_Pi_Dob2.getText().toString();
	        	if (et_Pi_Dob2.getText().toString().length() == 2) {
	        		et_Pi_Dob2.setText(str + "-");
	        		et_Pi_Dob2.setSelection(3);


				}*//*
                    }

                    private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                        boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                        for (int i = 0; i < s.length(); i++) { // chech that every element is right
                            if (i > 0 && (i + 1) % dividerModulo == 0) {
                                isCorrect &= divider == s.charAt(i);
                            } else {
                                isCorrect &= Character.isDigit(s.charAt(i));
                            }
                        }
                        return isCorrect;
                    }

                    private String buildCorrecntString(char[] digits, int dividerPosition, char divider) {
                        final StringBuilder formatted = new StringBuilder();

                        for (int i = 0; i < digits.length; i++) {
                            if (digits[i] != 0) {
                                formatted.append(digits[i]);
                                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                                    formatted.append(divider);
                                }
                            }
                        }

                        return formatted.toString();
                    }

                    private char[] getDigitArray(final Editable s, final int size) {
                        char[] digits = new char[size];
                        int index = 0;
                        for (int i = 0; i < s.length() && index < size; i++) {
                            char current = s.charAt(i);
                            if (Character.isDigit(current)) {
                                digits[index] = current;
                                index++;
                            }
                        }
                        return digits;
                    }
                });

                spinner_PI_Type.setSelection(1);
                pi_ms = "E";
                spinner_Pfa_ms.setSelection(1);
                pfa_ms = "E";

                btnScanQR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tv_exceptionView.setVisibility(View.GONE);
                        tv_exceptionView.setText("");
                        editText_Pa_co.setText("");
                        editText_Pa_house.setText("");
                        editText_Aadhaar.setText("");
                        editText_Pa_dist.setText("");
                        editText_Pa_lm.setText("");
                        editText_Pa_loc.setText("");
                        editText_Pa_pc.setText("");
                        editText_Pa_po.setText("");
                        editText_Pa_state.setText("");
                        editText_Pa_street.setText("");
                        editText_Pa_subDist.setText("");
                        editText_Pa_vtc.setText("");
//				editText_PI_age.setText("");
                        editText_PI_mail.setText("");
                        editText_PI_mob.setText("");
                        editText_PI_Name.setText("");
                        et_Pi_Dob.setText("");
                        et_Pi_Dob2.setText("");
                        et_Pi_Dob3.setText("");

                        Intent theIntent=new Intent(DemoAuth.this,BarcodeCaptureActivity.class);
                        startActivityForResult(theIntent, BARCODE_READER_REQUEST_CODE);
                    }
                });*/

    public void showMyMessage(String msg){
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
                DemoAuth.this);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle("Warning");
        dlgAlert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        /*Intent intnt = new Intent(DemoAuth.this , MainScreen.class);
                        startActivity(intnt);*/
                        finish();
                    }
                });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    public void appendLogInvalidXml(String text)
    {
        //File logFile = new File("sdcard/BIO_KYC"+ctime+".txt");
        File logFile = new File("sdcard/invalid_xml.txt");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, false));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            Log.e("IOException", "appendlog***** " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void ShowPromptMessage(String ret,String err, long totaltime, String authcode, String rests) {
        final Dialog authdialog = new Dialog(DemoAuth.this);
        authdialog.show();
        authdialog.setCancelable(false);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //		dialog.getWindow().setLayout(width, height);
        authdialog.setContentView(R.layout.auth_information_box);
        ImageView imgviewName = (ImageView) authdialog.findViewById(R.id.imageViewAtdType);

        TextView authStatus =  (TextView)authdialog.findViewById(R.id.textView1);


        if(ret.equalsIgnoreCase("y")){
            imgviewName.setImageResource(R.drawable.atdsuccess);
            authStatus.setText("Authentication Success");
        }else{
            imgviewName.setImageResource(R.drawable.atdfail);
            authStatus.setText(err);
        }

        TextView respstatus =  (TextView)authdialog.findViewById(R.id.responsestatus);
        respstatus.setText("Demographic Authentication"+". Code= " + authcode);

        TextView respts =  (TextView)authdialog.findViewById(R.id.responsets);
        respts.setText("Authentication Time= " + rests);

        TextView resptime =  (TextView)authdialog.findViewById(R.id.responseTime);
        resptime.setText("Response Time - "+totaltime+" ms");



        Button btn = (Button) authdialog.findViewById(R.id.okbtn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                authdialog.dismiss();
				/*Intent intent2 = new Intent(DemoAuth.this , MainScreen.class);
				intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent2);
				finish();*/
            }
        });

    }

    private class GetPubKeycertificateData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            String result = null;
            StringBuilder total = new StringBuilder();


            try {

                InputStream is = getResources().openRawResource(
                        R.raw.uidai_auth_prod);

                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                System.out.println("4444444444444444444");

                String line = "";
                System.out.println("55555555555555");
                while ((line = r.readLine()) != null) {
                    // 	total.append();
                    //  	total.append();
                    total.append(line);

                }
                System.out.println("6666666666666");
                result = total.toString();
                Log.e("result", "==" + result);

            } catch (Exception e) {
                Log.e("GetPubKeycert", "=" + e);
                ShowPrompt("Connection Issue", "Please go back...");
                System.out.println("errrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            barProgressDialog.dismiss();
            if ((result.startsWith("-----BEGIN CERTIFICATE-----") && result.endsWith("-----END CERTIFICATE-----"))) {
                result = result.replace("-----BEGIN CERTIFICATE-----", "");
                result = result.replace("-----END CERTIFICATE-----", "");
                result = result.replace("\r\n", "");
                Global.productionPublicKey = result;
                init();
            }
            //			if (result.endsWith("=") ) {
            //				result=result.replace("\r\n", "");
            //				Global.productionPublicKey=result;
            //			}
            else {
                //				ShowPrompt("Critical Error", "Please go back...");
                GetPubKeycertificateData publicKey1 = new GetPubKeycertificateData();
                publicKey1.execute();
            }

        }
    }

    private class HitToServer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            res = CommonMethods.HttpPostLifeCerticiate(Global.KYC_URL, xml,"","");
            Log.e("Response", "==" + res);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            dialogProcessRequest.dismiss();
            if (result.equalsIgnoreCase("ERROR") || result.equalsIgnoreCase("False from server") || result.equalsIgnoreCase("Connection time out Error")) {
                log.myMessage(result + " Demo Auth", Global.AUTH_AADHAAR);
                ShowErrorMessage(result);
            } else {
                //				edtText_Aadhaar.setText("");
                readAuthXml(result);

            }
        }
    }



}



