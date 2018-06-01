package com.nhpm.PrintCard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.CustomAsyncTask;
import com.customComponent.TaskListener;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.R;
import com.nhpm.Utility.AESencrp;
import com.nhpm.Utility.AppConstant;
import com.nhpm.Utility.AppUtility;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.zebra.android.common.card.graphics.ZebraCardGraphics;
import com.zebra.android.common.card.graphics.ZebraGraphics;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.UsbCardConnection;
import com.zebra.sdk.common.card.containers.GraphicsInfo;
import com.zebra.sdk.common.card.containers.JobStatusInfo;
import com.zebra.sdk.common.card.enumerations.CardSide;
import com.zebra.sdk.common.card.enumerations.GraphicType;
import com.zebra.sdk.common.card.enumerations.OrientationType;
import com.zebra.sdk.common.card.enumerations.PrintType;
import com.zebra.sdk.common.card.exceptions.ZebraCardException;
import com.zebra.sdk.common.card.graphics.ZebraCardImageI;
import com.zebra.sdk.common.card.graphics.enumerations.PrinterModel;
import com.zebra.sdk.common.card.graphics.enumerations.RotationType;
import com.zebra.sdk.common.card.printer.ZebraCardPrinter;
import com.zebra.sdk.common.card.printer.ZxpPrinter;
import com.zebra.sdk.settings.SettingsException;
import com.zxp3.init.ZXP3init;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by DELL on 15-03-2017.
 */

public class PrintRSBYCard extends Activity implements UsbPermissionRequestor {

    private static final String LOG_TAG = PrintCardMainActivity.class.getName();
    private boolean firstTimeThrough = false;
    private boolean receivedUsbPermissionResponse = false;
    private boolean usbPermissionGranted = false;
    private List<UsbDevice> discoveredPrinterUsbList = new ArrayList<UsbDevice>();
    private String logString;
    private String nameString= "Family of: ";
    public byte [] frontdata ;
    public byte [] backdata ;
    private ImageView back;
    public final static int QRcodeWidth = 500 ;
    private ImageView  imageView_logo;
    private Bitmap decodedBitmap;
    private Bitmap qrCodedecodedBitmap;
    private ImageView imageView_photo ;
    private ImageView  imageView_qrcode;
    private ImageView  imageView_preview;
    private ImageView imageView_back;
    private ZXP3init zxp3;
    private String benificiaryImageBase64;
    private String benificiaryQrCodeBase64;
    private String benificairyName;
    private String benificiaryFatherName;
    private String benificiaryGender;
    private String benificiaryDOB;
    private String memberID;
    private String benificiaryNhpsId;
    private Context context;
    private RelativeLayout backLayout;
    private EditText benificairyNameET;
    private EditText benificiaryFatherNameET;
    private EditText benificiaryGenderET;
    private EditText benificiarydobET;
    private EditText benificiaryNhpsidET;
    private RSBYItem rsbyMemberItem;
    private byte[] decodedImageString;
    private byte[] decodedQrString;
    private ArrayList<RSBYItem> rsbyMemberList;
    private ArrayList<RSBYItem> rsbyMemberListWithoutHead;
    private  String qrCodeData;
    private boolean memberFound=false;
    private boolean houseHoldEligible = true;
    private String formattedDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_print_preview);
        context = this;

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
        formattedDate = df.format(c.getTime());
        rsbyMemberItem = (RSBYItem) getIntent().getSerializableExtra(AppConstant.sendingPrintData);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageView_photo = (ImageView)this.findViewById(R.id.imageView_photo);
        imageView_qrcode = (ImageView)this.findViewById(R.id.imageView_qrcode);
        imageView_preview = (ImageView)this.findViewById(R.id.imageView_preview);
        imageView_back =  (ImageView)this.findViewById(R.id.imageView_back);
        // textView_status = (TextView)findViewById(R.id.textView_status);
        benificairyNameET = (EditText) findViewById(R.id.textname);
        benificiaryFatherNameET = (EditText) findViewById(R.id.textfathername);
        benificiaryGenderET = (EditText) findViewById(R.id.textViewgender);
        benificiarydobET = (EditText) findViewById(R.id.editTextdob);
        benificiaryNhpsidET = (EditText)findViewById(R.id.editText_nhpsid);
        if(rsbyMemberItem!=null) {
            setUpData();
        }



        zxp3 = new ZXP3init();

        findViewById(R.id.printButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printtask();
            }
        });
        firstTimeThrough = (savedInstanceState == null);


    }

    private void setUpData(){
        rsbyMemberList = SeccDatabase.getRsbyMemberList(rsbyMemberItem.getHhdNo(),context);
        try {
            memberID = rsbyMemberItem.getRsbyMemId();
            if(rsbyMemberItem.getMemberPhoto1()!=null && !rsbyMemberItem.getMemberPhoto1().equalsIgnoreCase("") && rsbyMemberItem.getGender()!=null &&!rsbyMemberItem.getGender().equalsIgnoreCase("") && rsbyMemberItem.getName()!=null && !rsbyMemberItem.getName().equalsIgnoreCase("") && rsbyMemberItem.getDob()!=null & !rsbyMemberItem.getDob().equalsIgnoreCase("")) {
                benificairyNameET.setText(rsbyMemberItem.getName());
                benificairyName = benificairyNameET.getText().toString();
                // benificiaryFatherNameET.setText(rsbyMemberItem.getFathername());
                // benificiaryFatherName = benificiaryFatherNameET.getText().toString();
                benificiarydobET.setText(AppUtility.convertRsbyDate(rsbyMemberItem.getDob()));
                benificiaryDOB = benificiarydobET.getText().toString();
                if (rsbyMemberItem.getGender().equalsIgnoreCase("1")) {
                    benificiaryGenderET.setText("Male");
                } else if (rsbyMemberItem.getGender().equalsIgnoreCase("2")) {
                    benificiaryGenderET.setText("Female");
                } else if (rsbyMemberItem.getGender().equalsIgnoreCase("3")) {
                    benificiaryGenderET.setText("Other");
                }
                benificiaryGender = benificiaryGenderET.getText().toString();
                benificiaryNhpsidET.setText(rsbyMemberItem.getNhpsId());
                benificiaryNhpsId = benificiaryNhpsidET.getText().toString();
                benificiaryImageBase64 = rsbyMemberItem.getMemberPhoto1();
                houseHoldEligible = true;
                prePairJson();
/*if(!memberFound) {*/
                setUpPreview();
/*}*/
            }else{
                findAligableMember();
            }

        }catch (Exception e){

        }
       /* String qrCodeData ="{\n" +
                "\t\"benificaryName\": \""+benificairyName+"\",\n" +
                "\t\"benificaryFatherName\": \""+benificiaryFatherName+"\",\n" +
                "\t\"benificaryDOB\": \""+benificiaryDOB+"\",\n" +
                "\t\"benificaryGender\": \""+benificiaryGender+"\",\n" +
                "\t\"benificaryNHPSID\": \""+benificiaryNhpsId+"\"\n" +
                "}";*/
     /*   prePairJson();
*//*if(!memberFound) {*//*
    setUpPreview();
*//*}*/
    }

    private void printtask() {
        benificiaryNhpsId = benificiaryNhpsidET.getText().toString();
        final Context context = this.getApplicationContext();
        new Thread(new Runnable() {
            @Override
            public void run() {

                ZebraGraphics graphics = null;
                UsbCardConnection usbCardConnection = null;
                ZxpPrinter zxpPrn = null;
                runOnUiThread(clearLog);
                runOnUiThread(disableButton);
                try {
                    logString = "Opening connection to printer";
                    runOnUiThread(updateLog);
                    if (discoveredPrinterUsbList.isEmpty()) {
                        discoveredPrinterUsbList = UsbHelper.discoverUsbDevices(getApplicationContext());
                    }
                    if (!discoveredPrinterUsbList.isEmpty()) {
                        UsbDevice usbDevice = discoveredPrinterUsbList.get(0);
                        UsbHelper.selectUsbPrinter(PrintRSBYCard.this, usbDevice);
                        UsbManager usbManager = UsbHelper.getUsbManager(getApplicationContext());
                        usbCardConnection = new UsbCardConnection(usbManager, usbDevice);
                        while (true) {
                            if (usbManager.hasPermission(usbDevice) || (receivedUsbPermissionResponse && usbPermissionGranted)) {
                                zxpPrn = zxp3.initprinter(usbCardConnection);
                                break;
                            } else if (receivedUsbPermissionResponse && !usbPermissionGranted) {
                                throw new ConnectionException("USB permission denied");
                            }
                        }
                    }
                    if(zxpPrn==null)
                    {
                        logString = "Error Initializing Printer";
                        runOnUiThread(updateLog);
                    }
                    // Front Color
                    graphics = new ZebraCardGraphics(zxpPrn);
                    graphics.setPrinterModel(PrinterModel.ZXPSeries3);
                    graphics.initialize(context, 0, 0, OrientationType.Landscape, PrintType.Color, Color.WHITE);
                    Bitmap frontpre = BitmapFactory.decodeResource(context.getResources(), R.drawable.front);

                    ByteArrayOutputStream streampre = new ByteArrayOutputStream();
                    frontpre.compress(Bitmap.CompressFormat.PNG, 100, streampre);

                    byte[] imageData = streampre.toByteArray();


                    if (imageData != null)
                        graphics.drawImage(imageData, 0, 0, 0, 0, RotationType.RotateNoneFlipNone);



                    int offsety = 200;



                    int maxlen = 30;


                    if(benificairyName.length()<maxlen)
                    {
                        /*graphics.drawText( "Name: "+ nameui , 240, offsety, 7, Color.BLACK);*/
                        graphics.drawText( nameString+ benificairyName , 240, offsety, 7, Color.BLACK);

                        offsety = offsety + 70;
                    }
                    else
                    {

                        String firstline = benificairyName.substring(0,benificairyName.lastIndexOf(" "));

                        graphics.drawText( nameString+ firstline , 240, offsety, 7, Color.BLACK);

                        offsety = offsety + 30;

                        String secondline =  benificairyName.substring(benificairyName.lastIndexOf(" ")+1, benificairyName.length() );


                        graphics.drawText( "             "+ secondline, 240, offsety, 7, Color.BLACK);


                        offsety = offsety + 70;
                    }





                    // if(benificiaryFatherName.length()<maxlen)
                    // {
                    graphics.drawText( "DOB: "+  benificiaryDOB, 240, offsety, 7, Color.BLACK);

                    offsety = offsety + 70;
                    //}
                   /* else
                    {

                        String firstline = benificiaryFatherName.substring(0,benificiaryFatherName.lastIndexOf(" "));

                        graphics.drawText( "Father Name: "+ firstline , 240, offsety, 7, Color.BLACK);

                        offsety = offsety + 40;

                        String secondline =  benificiaryFatherName.substring(benificiaryFatherName.lastIndexOf(" ")+1, benificiaryFatherName.length() );


                        graphics.drawText( "                          "+ secondline, 240, offsety, 7, Color.BLACK);


                        offsety = offsety + 50;
                    }*/


                    graphics.drawText( "Valid upto: "+  "", 240, offsety, 7, Color.BLACK);

                    offsety = offsety + 70;

                    graphics.drawText( "Printed on: "+  formattedDate, 240, offsety, 7, Color.BLACK);


                    graphics.drawText( "NHPS ID:"+  benificiaryNhpsId, 260, 560, 10, Color.BLACK);

                    //String fullstr = 	receiveddt.substring(receiveddt.indexOf("somee.com")+ 11, receiveddt.indexOf("</string>"));

                    //  String [] alldata= fullstr.split("\\&amp;");


                    //  byte[] byteArray = Base64.decode(alldata[4], Base64.DEFAULT);

// unlock when image is not null
                    graphics.drawImage(decodedImageString, 30 , 200, 200, 250, RotationType.RotateNoneFlipNone);
                    graphics.drawImage(decodedQrString,  780 , 250, 220, 220, RotationType.RotateNoneFlipNone);


                    logString = "Rasterizing front of card";
                    runOnUiThread(updateLog);
                    ZebraCardImageI frontColor = graphics.createImage();



                    graphics.clear();


                    // Front K

                    offsety = 200;

                    graphics.initialize(context, 0, 0, OrientationType.Landscape, PrintType.MonoK, Color.WHITE);


                    if(benificairyName.length()<maxlen)
                    {
                        graphics.drawText( nameString+ benificairyName , 240, offsety, 7, Color.BLACK);
                        offsety = offsety + 70;
                    }
                    else
                    {

                        String firstline = benificairyName.substring(0,benificairyName.lastIndexOf(" "));

                        graphics.drawText( nameString+ firstline , 240, offsety, 7, Color.BLACK);

                        offsety = offsety + 30;

                        String secondline =  benificairyName.substring(benificairyName.lastIndexOf(" ")+1, benificairyName.length() );


                        graphics.drawText( "             "+ secondline, 240, offsety, 7, Color.BLACK);


                        offsety = offsety + 70;
                    }


                /*    if(benificiaryFatherName.length()<maxlen)
                    {*/
                    graphics.drawText( "DOB: "+ benificiaryDOB , 240, offsety, 7, Color.BLACK);
                    offsety = offsety + 70;
                  /*  }
                    else
                    {

                        String firstline = benificiaryFatherName.substring(0,benificiaryFatherName.lastIndexOf(" "));

                        graphics.drawText( "Father Name: "+ firstline , 240, offsety, 7, Color.BLACK);

                        offsety = offsety + 40;

                        String secondline =  benificiaryFatherName.substring(benificiaryFatherName.lastIndexOf(" ")+1, benificiaryFatherName.length() );


                        graphics.drawText( "                          "+ secondline, 240, offsety, 7, Color.BLACK);


                        offsety = offsety + 50;
                    }*/

                    graphics.drawText( "Valid upto: "+  "", 240, offsety, 7, Color.BLACK);

                    offsety = offsety + 70;

                    graphics.drawText( "Printed on: "+ formattedDate, 240, offsety, 7, Color.BLACK);


                    graphics.drawText( "NHPS ID:"+  benificiaryNhpsId, 260, 560, 10, Color.BLACK);

                    ZebraCardImageI frontK = graphics.createImage();
                    graphics.clear();

                    // Back of card (K)

/*

                    graphics.initialize(context, 0, 0, OrientationType.Landscape, PrintType.MonoK, Color.WHITE);


                    Bitmap backpre = BitmapFactory.decodeResource(context.getResources(), R.drawable.back);
                    ByteArrayOutputStream streampreback = new ByteArrayOutputStream();
                    backpre.compress(Bitmap.CompressFormat.PNG, 100, streampreback);
                    byte[] imageDataback = streampreback.toByteArray();


                    if (imageDataback != null)
                        graphics.drawImage(imageDataback, 0, 0, 0, 0, RotationType.RotateNoneFlipNone);


                    //fullstr = 	receiveddt.substring(receiveddt.indexOf("somee.com")+ 11, receiveddt.indexOf("</string>"));

                    //alldata= fullstr.split("\\&amp;");

                    // unlock when qrcode string is not null
                   // decodedQrString = Base64.decode(benificiaryQrCodeBase64, Base64.DEFAULT);


                    graphics.drawImage(decodedQrString, topixelint(25) , topixelint(7), topixelint(40), topixelint(40), RotationType.RotateNoneFlipNone);

                    logString = "Rasterizing back of card";
                    runOnUiThread(updateLog);
                    ZebraCardImageI backK = graphics.createImage();
                    graphics.clear();

                    /// save front color in file

                    byte []  frontcol = frontColor.getImageData();

                    File photo=new File(Environment.getExternalStorageDirectory(), "photo.jpg");

                    if (photo.exists()) {
                        photo.delete();
                    }

                    try {
                        FileOutputStream fos=new FileOutputStream(photo.getPath());
                        fos.write(frontcol);
                        fos.close();
                    }
                    catch (java.io.IOException e) {
                        Log.e("PictureDemo", "Exception in photoCallback", e);
                    }

                    // save front k in file

                    byte []  frontk = frontK.getImageData();

                    File photok=new File(Environment.getExternalStorageDirectory(), "photok.jpg");

                    if (photok.exists()) {
                        photok.delete();
                    }

                    try {
                        FileOutputStream fos=new FileOutputStream(photok.getPath());
                        fos.write(frontk);
                        fos.close();
                    }
                    catch (java.io.IOException e) {
                        Log.e("PictureDemo", "Exception in photoCallback", e);
                    }


                    // get back data
                    byte []  backcol = backK.getImageData();

                    File photoback=new File(Environment.getExternalStorageDirectory(), "photoback.jpg");

                    if (photoback.exists()) {
                        photoback.delete();
                    }

                    try {
                        FileOutputStream fos=new FileOutputStream(photoback.getPath());
                        fos.write(backcol);
                        fos.close();
                    }
                    catch (java.io.IOException e) {
                        Log.e("PictureDemo", "Exception in photoCallback", e);
                    }

*/
// Preview

                  /*  frontdata = frontcol;

                    backdata = backcol;*/
                    runOnUiThread(previewcard);

                    // printCard(zxpPrn, frontColor, frontK, backK);
                    printCard(zxpPrn, frontColor, frontK);
                } catch (Exception e) {
                    logString = e.getLocalizedMessage();
                    runOnUiThread(updateLog);
                    Log.e(LOG_TAG, logString, e);
                } finally {
                    if (graphics != null) {
                        graphics.clear();
                        graphics.close();
                    }

                    try {
                        if (zxpPrn != null) {
                            zxpPrn.getConnection().close();
                            zxpPrn.destroy();
                        }
                    } catch (ZebraCardException e) {
                        logString = e.getLocalizedMessage();
                        runOnUiThread(updateLog);
                        Log.e(LOG_TAG, logString, e);
                    } catch (ConnectionException e) {
                        logString = e.getLocalizedMessage();
                        runOnUiThread(updateLog);
                        Log.e(LOG_TAG, logString, e);
                    }

                    try {
                        if (usbCardConnection != null) {
                            usbCardConnection.close();
                        }
                    } catch (ConnectionException e) {
                        Log.e(LOG_TAG, logString, e);
                    }
                    runOnUiThread(enableButton);
                }
            }
        }).start();
    }

    private Bitmap mergebmp(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);


        return bmOverlay;
    }

    private int topixelint(float inmm)
    {
        return zxp3.topixelint(inmm);

    }


   /* public void getdata(final View v)
    {
        runOnUiThread(cleardata);

        new RequestTask().execute("http://kashyap-sstpl.somee.com/service1.asmx/GetHOFdata?nhpsid=" + editText_nhpsid.getText().toString());

    }*/




    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();


                    //receiveddt  = responseString;
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..

            runOnUiThread(updatedata);
        }
    }


    /*private void printCard(ZxpPrinter device, ZebraCardImageI frontColor, ZebraCardImageI frontK,ZebraCardImageI backK) throws ZebraCardException, ConnectionException, SettingsException {*/
    private void printCard(ZxpPrinter device, ZebraCardImageI frontColor, ZebraCardImageI frontK) throws ZebraCardException, ConnectionException, SettingsException {
        List<GraphicsInfo> grList;

        grList = new ArrayList<GraphicsInfo>();
        GraphicsInfo grInfo = new GraphicsInfo();

        // Front Color
        grInfo.side = CardSide.Front;
        grInfo.printType = PrintType.Color;
        grInfo.graphicType = GraphicType.BMP;
        grInfo.graphicData = frontColor;
        grList.add(grInfo);

        // Front MonoK
        grInfo = new GraphicsInfo();
        grInfo.side = CardSide.Front;
        grInfo.printType = PrintType.MonoK;
        grInfo.graphicType = GraphicType.BMP;
        grInfo.graphicData = frontK;
        grList.add(grInfo);

        // Front O
        grInfo = new GraphicsInfo();
        grInfo.side = CardSide.Front;
        grInfo.printType = PrintType.Overlay;
        grInfo.graphicType = GraphicType.NA;
        grInfo.graphicData = null;
        grList.add(grInfo);

//         Back MonoK
// comment to stop back side printing

     /*   grInfo = new GraphicsInfo();
        grInfo.side = CardSide.Back;
        grInfo.printType = PrintType.MonoK;
        grInfo.graphicType = GraphicType.BMP;
        grInfo.graphicData = backK;
        grList.add(grInfo);*/

        //        Back Overlay
//     grInfo = new GraphicsInfo();
//     grInfo.side = CardSide.Back;
//     grInfo.printType = PrintType.Overlay;
//     grInfo.graphicType = GraphicType.NA;
//     grInfo.graphicData = null;
//     grList.add(grInfo);


        logString = "Printing card";
        runOnUiThread(updateLog);
        int actionID = device.print(1, grList);
        if (pollJobStatus(device, actionID)) {
            logString = "Job completed successfully";
            runOnUiThread(updateLog);
            Log.i(LOG_TAG, logString);
        } else {
            logString = "Job did not complete successfully";
            runOnUiThread(updateLog);
            Log.i(LOG_TAG, logString);
        }
    }

    public Bitmap encodeAsBitmap(String str) throws WriterException {

        final int WHITE = 0xFFFFFFFF;
        final int BLACK = 0xFF000000;
        final int WIDTH = 400;
        final int HEIGHT = 400;

        BitMatrix result;
        try
        {

            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);

        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private boolean pollJobStatus(ZebraCardPrinter device, int actionID) throws ZebraCardException {
        boolean success;

        // Poll job status
        JobStatusInfo jStatus;

        do {
            jStatus = device.getJobStatus(actionID);
            Log.i(LOG_TAG, String.format("Job %d, Status:%s, Card Position:%s, ReadyForNextJob:%s, Mag Status:%s, Contact Status:%s, Contactless Status:%s, " + "Error Code:%d, Alarm Code:%d", actionID,
                    jStatus.printStatus, jStatus.cardPosition, jStatus.readyForNextJob, jStatus.magneticEncoding, jStatus.contactSmartCard, jStatus.contactlessSmartCard, jStatus.errorInfo.value, jStatus.alarmInfo.value));

            if (jStatus.contactSmartCard.contains("station")) {
                success = true;
                break;
            } else if (jStatus.contactlessSmartCard.contains("station")) {
                success = true;
                break;
            } else if (jStatus.printStatus.contains("done_ok")) {
                success = true;
                break;
            } else if (jStatus.printStatus.contains("alarm_handling")) {
                device.cancel(actionID);
                success = false;
                break;
            } else if (jStatus.printStatus.contains("error") || jStatus.printStatus.contains("cancelled")) {
                success = false;
                break;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logString = e.getLocalizedMessage();
                runOnUiThread(updateLog);
                Log.e(LOG_TAG, logString, e);
            }

        } while (true);

        return success;
    }

    @Override
    protected void onPause() {
        unregisterReceiver(usbReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        myOnNewIntent(getIntent());
        registerReceiver(usbReceiver, filter);
    }

    @Override
    public void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        Log.i(LOG_TAG, "onNewIntent intent=" + getIntent().toString());

        myOnNewIntent(intent);
    }

    private void myOnNewIntent(@NonNull Intent intent) {
        Log.i(LOG_TAG, "myOnNewIntent");

        if (intentNotLaunchedFromHistory(intent)) {
            String action = intent.getAction() == null ? "" : intent.getAction();
            intent.setAction("");

            if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            } else if (action.equalsIgnoreCase(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            } else if (firstTimeThrough) { //Standard case of opening app w/o USB or NFC event...check for existing USB connections
                discoveredPrinterUsbList = UsbHelper.discoverUsbDevices(this);
                firstTimeThrough = false;
            }
        }
    }

    private boolean intentNotLaunchedFromHistory(@NonNull Intent intent) {
        return ((intent.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == 0);
    }

    private IntentFilter filter = new IntentFilter(UsbHelper.ACTION_USB_PERMISSION);

    public final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, @NonNull Intent intent) {
            Log.i(LOG_TAG, "USB BroadcastReceiver onReceive");
            String action = intent.getAction();
            if (UsbHelper.ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        usbPermissionGranted = true;
                        receivedUsbPermissionResponse = true;
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
        Log.i(LOG_TAG, "requestUsbPermission");

        PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(UsbHelper.ACTION_USB_PERMISSION), 0);
        UsbManager usbManager = UsbHelper.getUsbManager(this);
        usbManager.requestPermission(usbDevice, permissionIntent);
    }

    public boolean isNumeric(String str) {
        return str.matches("^[0-9]+$");
    }

    public String addChecksum(String str) {
        int sumOfOdd = 0;
        int sumOfEven = 0;
        for (int i = 1; i < 12; i+=2) {
            sumOfOdd += Integer.parseInt(str.substring(i, i+1));
        }
        for (int i = 0; i < 11; i+=2) {
            sumOfEven += Integer.parseInt(str.substring(i, i+1));
        }
        int checksum = (sumOfOdd * 3) + sumOfEven;
        checksum = 10 - (checksum % 10);

        str = str.concat(Integer.toString(checksum));
        return str;
    }

    private static byte[] baosToByteArray(ByteArrayOutputStream baos) throws Exception {
        byte[] byteArray = null;
        byteArray = baos.toByteArray();
        return byteArray;
    }

    private ByteArrayOutputStream readImageToBAOS(String fileName) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download");
                if (dir.exists()) {
                    String filePath = dir + "/" + fileName;

                    InputStream inputStream = new FileInputStream(new File(filePath));

                    byte[] b = new byte[1024 * 8];
                    int bytesRead = 0;

                    while ((bytesRead = inputStream.read(b)) != -1) {
                        bos.write(b, 0, bytesRead);
                    }

                    inputStream.close();
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getLocalizedMessage(), e);
        }
        return bos;
    }

    final Runnable updateLog = new Runnable() {
        @Override
        public void run() {
            ((TextView)findViewById(R.id.textView3)).append(String.format("\n%s", logString));
        }
    };

    final Runnable previewcard = new Runnable() {
        @Override
        public void run()
        {

            Bitmap bMap = BitmapFactory.decodeByteArray(frontdata, 0, frontdata.length);

            // merge

            Bitmap merged = mergebmp(bMap, imageView_qrcode.getDrawingCache());


            ImageView image = (ImageView)findViewById(R.id.imageView_preview);

            imageView_preview.setVisibility(View.VISIBLE);
            image.setImageBitmap(bMap);

         /*   Bitmap bMapbk = BitmapFactory.decodeByteArray(backdata, 0, backdata.length);

            ImageView imagebk = (ImageView)findViewById(R.id.imageView_back);

            imagebk.setVisibility(View.VISIBLE);

            imagebk.setImageBitmap(bMapbk);*/

        }
    };

    final Runnable clearLog = new Runnable() {
        @Override
        public void run() {
            ((TextView)findViewById(R.id.textView3)).setText("Log:");
        }
    };


    final Runnable cleardata = new Runnable() {
        @Override
        public void run() {


            //  textView_status.setVisibility(View.VISIBLE);
            // display data
            EditText  textname  = (EditText) findViewById(R.id.textname);
            EditText  textfathername  = (EditText) findViewById(R.id.textfathername);
            EditText  textViewgender  = (EditText) findViewById(R.id.textViewgender);
            EditText  editTextdob  = (EditText) findViewById(R.id.editTextdob);



            textname.setText("");
            textfathername.setText("");
            textViewgender.setText("");

            editTextdob.setText("");


            imageView_photo.setVisibility(View.INVISIBLE);
            imageView_photo.setImageBitmap(null);

            imageView_qrcode.setVisibility(View.INVISIBLE);
            imageView_qrcode.setImageBitmap(null);

            imageView_preview.setVisibility(View.INVISIBLE);
            imageView_preview.setImageBitmap(null);


            imageView_back.setVisibility(View.INVISIBLE);
            imageView_back.setImageBitmap(null);


            ((TextView)findViewById(R.id.textView3)).setText("Log:");


        }


    };



    final Runnable updatedata = new Runnable() {
        @Override
        public void run() {

      /*   //   textView_status.setVisibility(View.INVISIBLE);


       //     String fullstr = 	receiveddt.substring(receiveddt.indexOf("somee.com")+ 11, receiveddt.indexOf("</string>"));

        //    if(fullstr.contains("NOTFOUND")==false)
          //  {
                // display data
                EditText  textname  = (EditText) findViewById(R.id.textname);
                EditText  textfathername  = (EditText) findViewById(R.id.textfathername);
                EditText  textViewgender  = (EditText) findViewById(R.id.textViewgender);
                EditText  editTextdob  = (EditText) findViewById(R.id.editTextdob);


                String [] alldata= fullstr.split("\\&amp;");

                //   textname.setText("PANDUBHAI");

                textname.setText(alldata[0]);

                textfathername.setText(alldata[1]);


                if(alldata[2].compareToIgnoreCase("1")==0)
                    textViewgender.setText("MALE");
                else if (alldata[2].compareToIgnoreCase("2")==0)
                    textViewgender.setText("FEMALE");


                editTextdob.setText(alldata[3]);*/

            byte[] decodedString = Base64.decode(benificiaryImageBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


            imageView_photo.setVisibility(View.VISIBLE);

            imageView_photo.setImageBitmap(decodedByte);


            byte[] decodedStringqr = Base64.decode(benificiaryQrCodeBase64, Base64.DEFAULT);
            Bitmap decodedByteqr = BitmapFactory.decodeByteArray(decodedStringqr, 0, decodedStringqr.length);


            imageView_qrcode.setVisibility(View.VISIBLE);

            imageView_qrcode.setImageBitmap(decodedByteqr);

          /*  }
            else
            {
                Toast.makeText(getApplicationContext(),fullstr , Toast.LENGTH_LONG).show();

            }*/
        }
    };

    final Runnable disableButton = new Runnable() {
        @Override
        public void run() {
            //((Button) findViewById(R.id.printButton)).setEnabled(false);
        }
    };

    final Runnable enableButton = new Runnable() {
        @Override
        public void run() {
            ((Button) findViewById(R.id.printButton)).setEnabled(true);
        }
    };

    private Bitmap TextToQrCode(String Value) {
        BitMatrix bitMatrix = null;
        try {
            try {
                bitMatrix = new MultiFormatWriter().encode(
                        Value,
                        BarcodeFormat.DATA_MATRIX.QR_CODE,
                        QRcodeWidth, QRcodeWidth, null
                );
            } catch (WriterException e) {
                e.printStackTrace();
            }

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    private byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return byteArray;
    }


    private void setUpPreview(){
        TaskListener taskListener=new TaskListener() {
            @Override
            public void execute() {
                if(benificiaryImageBase64!=null) {
                    decodedImageString = Base64.decode(benificiaryImageBase64, Base64.DEFAULT);
                    decodedBitmap= BitmapFactory.decodeByteArray(decodedImageString, 0, decodedImageString.length);

                }


                try {
                    //qrCodedecodedBitmap = TextToQrCode(AESencrp.encrypt(qrCodeData));
                    qrCodedecodedBitmap = TextToQrCode(qrCodeData);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                decodedQrString = bitmapToByte(qrCodedecodedBitmap);
                ZebraGraphics graphics = null;
                UsbCardConnection usbCardConnection = null;
                ZxpPrinter zxpPrn = null;
                try {
                    graphics = new ZebraCardGraphics(zxpPrn);
                } catch (ZebraCardException e) {
                    e.printStackTrace();
                }
                graphics.setPrinterModel(PrinterModel.ZXPSeries3);
                graphics.initialize(context, 0, 0, OrientationType.Landscape, PrintType.Color, Color.WHITE);
                Bitmap frontpre = BitmapFactory.decodeResource(context.getResources(), R.drawable.front);

                ByteArrayOutputStream streampre = new ByteArrayOutputStream();
                frontpre.compress(Bitmap.CompressFormat.PNG, 100, streampre);

           /*     byte[] imageData = streampre.toByteArray();


                if (imageData != null)
                    try {
                        graphics.drawImage(imageData, 0, 0, 0, 0, RotationType.RotateNoneFlipNone);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                int offsety = 200;



                int maxlen = 30;


                if(benificairyName.length()<maxlen)
                {
                        *//*graphics.drawText( "Name: "+ nameui , 240, offsety, 7, Color.BLACK);*//*
                    graphics.drawText( nameString+ benificairyName , 240, offsety, 7, Color.BLACK);

                    offsety = offsety + 70;
                }
                else
                {

                    String firstline = benificairyName.substring(0,benificairyName.lastIndexOf(" "));

                    graphics.drawText( nameString+ firstline , 240, offsety, 7, Color.BLACK);

                    offsety = offsety + 30;

                    String secondline =  benificairyName.substring(benificairyName.lastIndexOf(" ")+1, benificairyName.length() );


                    graphics.drawText( "             "+ secondline, 240, offsety, 7, Color.BLACK);


                    offsety = offsety + 70;
                }





                if(benificiaryFatherName.length()<maxlen)
                {
                    graphics.drawText( "Father Name: "+ benificiaryFatherName , 240, offsety, 7, Color.BLACK);

                    offsety = offsety + 70;
                }
                else
                {

                    String firstline = benificiaryFatherName.substring(0,benificiaryFatherName.lastIndexOf(" "));

                    graphics.drawText( "Father Name: "+ firstline , 240, offsety, 7, Color.BLACK);

                    offsety = offsety + 40;

                    String secondline =  benificiaryFatherName.substring(benificiaryFatherName.lastIndexOf(" ")+1, benificiaryFatherName.length() );


                    graphics.drawText( "                          "+ secondline, 240, offsety, 7, Color.BLACK);


                    offsety = offsety + 50;
                }


                graphics.drawText( "Gender: "+  benificiaryGender, 240, offsety, 7, Color.BLACK);

                offsety = offsety + 70;

                graphics.drawText( "DOB: "+  benificiaryDOB, 240, offsety, 7, Color.BLACK);


                graphics.drawText( "NHPS ID:"+  benificiaryNhpsId, 260, 560, 10, Color.BLACK);

                //String fullstr = 	receiveddt.substring(receiveddt.indexOf("somee.com")+ 11, receiveddt.indexOf("</string>"));

                //  String [] alldata= fullstr.split("\\&amp;");


                //  byte[] byteArray = Base64.decode(alldata[4], Base64.DEFAULT);

// unlock when image is not null
                try {
                    graphics.drawImage(decodedImageString, 30 , 200, 200, 250, RotationType.RotateNoneFlipNone);
                    //(leftmargin,,h,w)
                    graphics.drawImage(decodedQrString,  780 , 250, 220, 220, RotationType.RotateNoneFlipNone);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                logString = "Rasterizing front of card";
                runOnUiThread(updateLog);
                ZebraCardImageI frontColor = null;
                try {
                    frontColor = graphics.createImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                graphics.clear();


                // Front K

                offsety = 200;

                graphics.initialize(context, 0, 0, OrientationType.Landscape, PrintType.MonoK, Color.WHITE);


                if(benificairyName.length()<maxlen)
                {
                    graphics.drawText( nameString+ benificairyName , 240, offsety, 7, Color.BLACK);
                    offsety = offsety + 70;
                }
                else
                {

                    String firstline = benificairyName.substring(0,benificairyName.lastIndexOf(" "));

                    graphics.drawText( nameString+ firstline , 240, offsety, 7, Color.BLACK);

                    offsety = offsety + 30;

                    String secondline =  benificairyName.substring(benificairyName.lastIndexOf(" ")+1, benificairyName.length() );


                    graphics.drawText( "             "+ secondline, 240, offsety, 7, Color.BLACK);


                    offsety = offsety + 70;
                }


                if(benificiaryFatherName.length()<maxlen)
                {
                    graphics.drawText( "Father Name: "+ benificiaryFatherName , 240, offsety, 7, Color.BLACK);
                    offsety = offsety + 70;
                }
                else
                {

                    String firstline = benificiaryFatherName.substring(0,benificiaryFatherName.lastIndexOf(" "));

                    graphics.drawText( "Father Name: "+ firstline , 240, offsety, 7, Color.BLACK);

                    offsety = offsety + 40;

                    String secondline =  benificiaryFatherName.substring(benificiaryFatherName.lastIndexOf(" ")+1, benificiaryFatherName.length() );


                    graphics.drawText( "                          "+ secondline, 240, offsety, 7, Color.BLACK);


                    offsety = offsety + 50;
                }

                graphics.drawText( "Gender: "+  benificiaryGender, 240, offsety, 7, Color.BLACK);

                offsety = offsety + 70;

                graphics.drawText( "DOB: "+ benificiaryDOB, 240, offsety, 7, Color.BLACK);


                graphics.drawText( "NHPS ID:"+  benificiaryNhpsId, 260, 560, 10, Color.BLACK);*/

                byte[] imageData = streampre.toByteArray();


                if (imageData != null)
                    try {
                        graphics.drawImage(imageData, 0, 0, 0, 0, RotationType.RotateNoneFlipNone);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                int offsety = 200;



                int maxlen = 30;


                if(benificairyName.length()<maxlen)
                {
                        /*graphics.drawText( "Name: "+ nameui , 240, offsety, 7, Color.BLACK);*/
                    graphics.drawText( nameString+ benificairyName , 240, offsety, 7, Color.BLACK);

                    offsety = offsety + 70;
                }
                else
                {

                    String firstline = benificairyName.substring(0,benificairyName.lastIndexOf(" "));

                    graphics.drawText( nameString+ firstline , 240, offsety, 7, Color.BLACK);

                    offsety = offsety + 30;

                    String secondline =  benificairyName.substring(benificairyName.lastIndexOf(" ")+1, benificairyName.length() );


                    graphics.drawText( "             "+ secondline, 240, offsety, 7, Color.BLACK);


                    offsety = offsety + 70;
                }





                // if(benificiaryFatherName.length()<maxlen)
                // {
                graphics.drawText( "DOB: "+  benificiaryDOB, 240, offsety, 7, Color.BLACK);

                offsety = offsety + 70;
                //}
                   /* else
                    {

                        String firstline = benificiaryFatherName.substring(0,benificiaryFatherName.lastIndexOf(" "));

                        graphics.drawText( "Father Name: "+ firstline , 240, offsety, 7, Color.BLACK);

                        offsety = offsety + 40;

                        String secondline =  benificiaryFatherName.substring(benificiaryFatherName.lastIndexOf(" ")+1, benificiaryFatherName.length() );


                        graphics.drawText( "                          "+ secondline, 240, offsety, 7, Color.BLACK);


                        offsety = offsety + 50;
                    }*/


                graphics.drawText( "Valid upto: "+  "2019", 240, offsety, 7, Color.BLACK);

                offsety = offsety + 70;

                graphics.drawText( "Printed on: "+  formattedDate, 240, offsety, 7, Color.BLACK);


                graphics.drawText( "NHPS ID:"+  benificiaryNhpsId, 260, 560, 10, Color.BLACK);

                //String fullstr = 	receiveddt.substring(receiveddt.indexOf("somee.com")+ 11, receiveddt.indexOf("</string>"));

                //  String [] alldata= fullstr.split("\\&amp;");


                //  byte[] byteArray = Base64.decode(alldata[4], Base64.DEFAULT);

// unlock when image is not null
                try {
                    graphics.drawImage(decodedImageString, 30 , 200, 200, 250, RotationType.RotateNoneFlipNone);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    graphics.drawImage(decodedQrString,  780 , 250, 220, 220, RotationType.RotateNoneFlipNone);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                logString = "Rasterizing front of card";
                runOnUiThread(updateLog);
                ZebraCardImageI frontColor = null;
                try {
                    frontColor = graphics.createImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                graphics.clear();


                // Front K

                offsety = 200;

                graphics.initialize(context, 0, 0, OrientationType.Landscape, PrintType.MonoK, Color.WHITE);


                if(benificairyName.length()<maxlen)
                {
                    graphics.drawText( nameString+ benificairyName , 240, offsety, 7, Color.BLACK);
                    offsety = offsety + 70;
                }
                else
                {

                    String firstline = benificairyName.substring(0,benificairyName.lastIndexOf(" "));

                    graphics.drawText( nameString+ firstline , 240, offsety, 7, Color.BLACK);

                    offsety = offsety + 30;

                    String secondline =  benificairyName.substring(benificairyName.lastIndexOf(" ")+1, benificairyName.length() );


                    graphics.drawText( "             "+ secondline, 240, offsety, 7, Color.BLACK);


                    offsety = offsety + 70;
                }


                /*    if(benificiaryFatherName.length()<maxlen)
                    {*/
                graphics.drawText( "DOB: "+ benificiaryDOB , 240, offsety, 7, Color.BLACK);
                offsety = offsety + 70;
                  /*  }
                    else
                    {

                        String firstline = benificiaryFatherName.substring(0,benificiaryFatherName.lastIndexOf(" "));

                        graphics.drawText( "Father Name: "+ firstline , 240, offsety, 7, Color.BLACK);

                        offsety = offsety + 40;

                        String secondline =  benificiaryFatherName.substring(benificiaryFatherName.lastIndexOf(" ")+1, benificiaryFatherName.length() );


                        graphics.drawText( "                          "+ secondline, 240, offsety, 7, Color.BLACK);


                        offsety = offsety + 50;
                    }*/

                graphics.drawText( "Valid upto: "+  "", 240, offsety, 7, Color.BLACK);

                offsety = offsety + 70;

                graphics.drawText( "Printed on: "+ formattedDate, 240, offsety, 7, Color.BLACK);


                graphics.drawText( "NHPS ID:"+  benificiaryNhpsId, 260, 560, 10, Color.BLACK);


                ZebraCardImageI frontK = null;
                try {
                    frontK = graphics.createImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                graphics.clear();

                // Back of card (K)


                graphics.initialize(context, 0, 0, OrientationType.Landscape, PrintType.MonoK, Color.WHITE);


                Bitmap backpre = BitmapFactory.decodeResource(context.getResources(), R.drawable.back);
                ByteArrayOutputStream streampreback = new ByteArrayOutputStream();
                backpre.compress(Bitmap.CompressFormat.PNG, 100, streampreback);
                byte[] imageDataback = streampreback.toByteArray();


                if (imageDataback != null)
                    try {
                        graphics.drawImage(imageDataback, 0, 0, 0, 0, RotationType.RotateNoneFlipNone);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                //fullstr = 	receiveddt.substring(receiveddt.indexOf("somee.com")+ 11, receiveddt.indexOf("</string>"));

                //alldata= fullstr.split("\\&amp;");

                // unlock when qrcode string is not null
                // decodedQrString = Base64.decode(benificiaryQrCodeBase64, Base64.DEFAULT);


                try {
                    graphics.drawImage(decodedQrString, topixelint(25) , topixelint(7), topixelint(40), topixelint(40), RotationType.RotateNoneFlipNone);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                logString = "Rasterizing back of card";
                runOnUiThread(updateLog);
                ZebraCardImageI backK = null;
                try {
                    backK = graphics.createImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                graphics.clear();

                /// save front color in file

                byte []  frontcol = frontColor.getImageData();

                File photo=new File(Environment.getExternalStorageDirectory(), "photo.jpg");

                if (photo.exists()) {
                    photo.delete();
                }

                try {
                    FileOutputStream fos=new FileOutputStream(photo.getPath());
                    fos.write(frontcol);
                    fos.close();
                }
                catch (IOException e) {
                    Log.e("PictureDemo", "Exception in photoCallback", e);
                }

                // save front k in file

                byte []  frontk = frontK.getImageData();

                File photok=new File(Environment.getExternalStorageDirectory(), "photok.jpg");

                if (photok.exists()) {
                    photok.delete();
                }

                try {
                    FileOutputStream fos=new FileOutputStream(photok.getPath());
                    fos.write(frontk);
                    fos.close();
                }
                catch (IOException e) {
                    Log.e("PictureDemo", "Exception in photoCallback", e);
                }


                // get back data
                byte []  backcol = backK.getImageData();

                File photoback=new File(Environment.getExternalStorageDirectory(), "photoback.jpg");

                if (photoback.exists()) {
                    photoback.delete();
                }

                try {
                    FileOutputStream fos=new FileOutputStream(photoback.getPath());
                    fos.write(backcol);
                    fos.close();
                }
                catch (IOException e) {
                    Log.e("PictureDemo", "Exception in photoCallback", e);
                }

// Preview

                frontdata = frontcol;

                backdata = backcol;
                runOnUiThread(previewcard);
            }

            @Override
            public void updateUI() {
                imageView_photo.setImageBitmap(decodedBitmap);
                imageView_qrcode.setImageBitmap(qrCodedecodedBitmap);
            }
        };

        CustomAsyncTask asyncTask=new CustomAsyncTask(taskListener,"Please wait..",context);
        asyncTask.execute();
    }
    public  void alertWithOkNoMemberFound(Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Alert");
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public  void alertWithOk(Context mContext, String msg, final RSBYItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Alert");
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do
                        memberID = item.getNhpsMemId();
                        benificairyNameET.setText(item.getName());
                        benificairyName = benificairyNameET.getText().toString();
/*
                        benificiaryFatherNameET.setText(item.getFathername());
                        benificiaryFatherName = benificiaryFatherNameET.getText().toString();*/

                        benificiarydobET.setText(AppUtility.convertRsbyDate(item.getDob()));
                        benificiaryDOB = benificiarydobET.getText().toString();
                        if(item.getGender().equalsIgnoreCase("1")){
                            benificiaryGenderET.setText("Male");
                        }else if(item.getGender().equalsIgnoreCase("2")){
                            benificiaryGenderET.setText("Female");
                        }else if(item.getGender().equalsIgnoreCase("3")){
                            benificiaryGenderET.setText("Other");
                        }
                        benificiaryGender = benificiaryGenderET.getText().toString();
                        benificiaryNhpsidET.setText(item.getNhpsId());
                        benificiaryNhpsId = benificiaryNhpsidET.getText().toString();
                        benificiaryImageBase64 = item.getMemberPhoto1();
                        prePairJson();
/*if(!memberFound) {*/
                        setUpPreview();
/*}*/
                        //  setUpPreview();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void findAligableMember(){

        for(RSBYItem item1 : rsbyMemberList){
            if(item1.getName().equalsIgnoreCase(rsbyMemberItem.getName())){

            }else{

                if (item1.getName() != null && !item1.getName().equalsIgnoreCase("")) {
                    if (item1.getMemberPhoto1() != null && !item1.getMemberPhoto1().equalsIgnoreCase("")) {
                       // if (item1.getFathername() != null && !item1.getFathername().equalsIgnoreCase("")) {
                            if (item1.getDob() != null && !item1.getDob().equalsIgnoreCase("")) {
                                if (item1.getGender() != null && !item1.getGender().equalsIgnoreCase("")) {
                                    // member contain all detail required for print
                                    if(!memberFound) {
                                        alertWithOk(context, item1.getName() + " contain all detail required for print",item1);
                                    }
                                    memberFound = true;
                                    houseHoldEligible= true;
                                    break;

                                } else {
                                    houseHoldEligible = false;
                                    //   AppUtility.alertWithOk(context, "Member gender not available");
                                }
                            } else {
                                //   AppUtility.alertWithOk(context, "Member date of birth not available");
                                houseHoldEligible = false;
                            }
                  /*      } else {
                            //    AppUtility.alertWithOk(context, "Member father name not available");
                            houseHoldEligible = false;
                        }*/
                    } else {
                        //   AppUtility.alertWithOk(context, "Member photo not available");
                        houseHoldEligible = false;
                    }
                } else {
                    //   AppUtility.alertWithOk(context, "Member name not available");
                    houseHoldEligible = false;

                }

            }

        }
        if(!houseHoldEligible){
            alertWithOkNoMemberFound(context, "No household member is eligiable to print card");
        }

    }

    private void prePairJson(){
        if(memberFound){
            nameString = "Family of: ";
        }
        StringBuilder prePairedJson = new StringBuilder();
        prePairedJson.append("{\n"+ "\t\"NHPSID\": \""+benificiaryNhpsId+"\",\n" +
                "\t\"MemberID\": \""+memberID+"\",\n");
        prePairedJson.append("\t\"members\":{\n");

        if(rsbyMemberItem.getMemberPhoto1()!=null && !rsbyMemberItem.getMemberPhoto1().equalsIgnoreCase("")){
            int count =1;
            for(RSBYItem item : rsbyMemberList){
                if(item.getName().equalsIgnoreCase(rsbyMemberItem.getName())){

                }else{
                    if(count==1) {
                        prePairedJson.append("\t\"member" + count + "\":" + "\"" + item.getName() + "\"\n");
                        prePairedJson.append(",\n\t\"memberId" + count + "\":" + "\"" + item.getNhpsMemId() + "\"\n");
                    }else{
                        prePairedJson.append(",\n\t\"member" + count + "\":" + "\"" + item.getName() + "\"\n");
                        prePairedJson.append(",\n\t\"memberId" + count + "\":" + "\"" + item.getNhpsMemId() + "\"\n");
                    }
                    count++;
                }
            }
            prePairedJson.append("}\t\n}");
        }else{
            int count =1;
            for(RSBYItem item1 : rsbyMemberList){
                if(item1.getName().equalsIgnoreCase(rsbyMemberItem.getName())){

                }else{
                    if(count==1) {
                        prePairedJson.append("\t\"member" + count + "\":" + "\"" + item1.getName() + "\"\n");
                    }else{
                        prePairedJson.append(",\n\t\"member" + count + "\":" + "\"" + item1.getName() + "\"\n");
                    }
                    count++;
                }
            }
            prePairedJson.append("}\n}");
        }
        qrCodeData = prePairedJson.toString();
    }
}