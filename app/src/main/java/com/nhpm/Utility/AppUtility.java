package com.nhpm.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.display.DisplayManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customComponent.CustomAlert;
import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.ProjectPrefrence;
import com.nhpm.AadhaarUtils.CommonMethods;
import com.nhpm.AadhaarUtils.Global;
import com.nhpm.AadhaarUtils.UidaiAuthHelper;
import com.nhpm.LocalDataBase.dto.SeccDatabase;
import com.nhpm.Models.ApplicationLanguageItem;
import com.nhpm.Models.NotificationModel;
import com.nhpm.Models.response.GovernmentIdItem;
import com.nhpm.Models.response.RelationItem;
import com.nhpm.Models.response.WhoseMobileItem;
import com.nhpm.Models.response.master.StateItem;
import com.nhpm.Models.response.rsbyMembers.RSBYItem;
import com.nhpm.Models.response.rsbyMembers.RsbyCardCategoryItem;
import com.nhpm.Models.response.rsbyMembers.RsbyHouseholdItem;
import com.nhpm.Models.response.rsbyMembers.RsbyRelationItem;
import com.nhpm.Models.response.seccMembers.HouseHoldItem;
import com.nhpm.Models.response.seccMembers.SeccMemberItem;
import com.nhpm.Models.response.seccMembers.SelectedMemberItem;
import com.nhpm.Models.response.verifier.VerifierLoginResponse;
import com.nhpm.R;
import com.nhpm.activity.BlockDetailActivity;
import com.nhpm.activity.LoginActivity;
import com.nhpm.activity.MemberPreviewActivity;
import com.nhpm.activity.SearchActivityWithHouseHold;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/*import static com.aadhar.DemoAuth.getDemoAuthXml;*/

/**
 * Created by psqit on 8/5/2016.
 */
public class AppUtility {
    public static String DATABASE_NAME = "m_location_master.sqlite";
    public static String TABLE_MASTER_LOC = "location";
    public static String TABLE_MASTER_NHSDATA = "nhsdata";
    public static String TABLE_METADATA = "meta_editmember";
    public static String TABLE_METADATA_ADD = "meta_addmember";
    public static String TAG = "AppUtility";
    public static int capturingType = 0;
    public static boolean isFingerPrintDeviceAttached = false;
    public static boolean navgateFromEb = false;
    public static String defaultLang;
    public static boolean isFlashEnableQr = false;
    public static ArrayList<String> genderLabel = new ArrayList<>();
    public static ArrayList<String> statusLabel = new ArrayList<>();
    public static ArrayList<String> blockLabel = new ArrayList<>();
    public static int redirection = 0;

    private static int wrongPinCount = 0;
    private static long wrongPinSavedTime;
    private static long currentTime;
    private static TextView wrongAttempetCountValue, wrongAttempetCountText;
    private static long millisecond24 = 86400000;


    static {
        defaultLang = Locale.getDefault().getLanguage();
        genderLabel.add("Male");
        genderLabel.add("Female");


        statusLabel.add("UnMarried");
        statusLabel.add("Married");


        blockLabel.add("012");
        blockLabel.add("013");

    }


    public static long convertRsbyIssuesTimeStampDateToLong(String rsbyDate, String pattern) {
        long startDate = 0;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date date = sdf.parse(rsbyDate);

            startDate = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startDate;
    }

    public static long convertRsbyDateToLong(String rsbyDate, String pattern) {
        long startDate = 0;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date date = sdf.parse(rsbyDate);

            startDate = date.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return startDate;
    }

    public static String longToDate(String date) {
        try {
            Timestamp stamp = new Timestamp(Long.parseLong(date));
            Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
            Date daten = new Date(stamp.getTime());
            System.out.println(format.format(daten).toString());
            return format.format(daten).toString();
        } catch (Exception ex) {
            return date;
        }
    }

    public static boolean compareCardExpDate(long currentDate, long cardExpDate) {
        long cardexp = cardExpDate;
        long currnt = currentDate;
        if (currentDate < cardExpDate) {
            return true;
        } else {
            return false;
        }
    }

    public static long currentDateLong() {
        String date = null;
        DateFormat dateFormat = new SimpleDateFormat(AppConstant.RSBY_DATE_FORMAT);
        Date Date = new Date();
        date = dateFormat.format(Date);

        return convertRsbyDateToLong(date, AppConstant.RSBY_DATE_FORMAT);
    }


    public static String convertRsbyIssueTimeDateFormat(String timeSpam) {
        String newDob;
        if (timeSpam != null && !timeSpam.equalsIgnoreCase("")) {
            if (timeSpam.length() == 13) {
                newDob = "0" + timeSpam;
            } else {
                newDob = timeSpam;
            }
            StringBuilder convertedDob = new StringBuilder();
            String day = newDob.substring(0, Math.min(newDob.length(), 2));
            String month = newDob.substring(2, Math.min(newDob.length(), 4));
            String year = newDob.substring(4, Math.min(newDob.length(), 8));
            String hours = newDob.substring(8, Math.min(newDob.length(), 10));
            String minutes = newDob.substring(10, Math.min(newDob.length(), 12));
            String second = newDob.substring(12, Math.min(newDob.length(), 14));

            convertedDob.append(year + "-").append(month + "-").append(day + " ").append(hours + ":" + minutes + ":" + second);


            return convertedDob.toString();
        }
        return null;
    }

    public static String readFromfile(String fileName, Context context) {
        StringBuffer returnString = new StringBuffer();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets().open(fileName, context.MODE_WORLD_READABLE);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                returnString.append(line);

            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }

    public static boolean validateAge(String dateOfBirth, int expectedAge) {
        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();

        int age = 0;
        Date date = new Date(dateOfBirth);

        birthDate.setTime(date);
        if (birthDate.after(today)) {
            /*throw new IllegalArgumentException("Can't be born in the future");*/
            return false;
        }

        age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        // If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year
        if ((birthDate.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) > 3) ||
                (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH))) {
            age--;

            // If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
        } else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH)) &&
                (birthDate.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }
        return age > expectedAge;
    }

    public static boolean futurevalidateAge(String dateOfBirth) {
        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();

        int age = 0;
        Date date = new Date(dateOfBirth);

        birthDate.setTime(date);
        if (birthDate.after(today)) {
            /*throw new IllegalArgumentException("Can't be born in the future");*/
            return false;
        } else {
            return true;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            // Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    public static void alertWithOk(Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Alert");
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void alertWithOk(Context mContext, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Alert");
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static String convetEkycDate(String datee) {

        String str_date = datee;
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
            Date date = (Date) formatter.parse(datee);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            long timeMillis = c.getTimeInMillis();
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            str_date = parser.format(new Date(timeMillis));
        } catch (Exception ex) {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = originalFormat.parse(datee);
                str_date = targetFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //  Log.d("ConvertedDate",formattedDate);
        }
        return str_date;
    }

    public static String getIMEINumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String device_id = tm.getDeviceId();
       //  return "358520071002666"; //gera sir
       // return "358520071087378"; //gera sir current
        return device_id;
   // return "352356078907011"; //wahid
        //  return "358187072515557";
        // return "867802027718791"; //saurabh
        // return "358520070004861";
        // return "356145083487168"; // Lalit
        // return "358520070004861"; //manoj sir
        // return "358223077788251"; //sunil sir
    }

    public static String convertBitmapToString(Bitmap bitmap) {
        Bitmap selectedImage = bitmap;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String strBase64 = Base64.encodeToString(byteArray, 0);
        return strBase64;
    }

    public static Bitmap convertStringToBitmap(String strBase64) {
        byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        //image.setImageBitmap(decodedByte);
        return decodedByte;
    }

    public static ArrayList<String> getMaritalStatusLabel() {
        ArrayList<String> label = new ArrayList<>();
        label.add("Select");
        label.add("Never Married");
        label.add("Married");
        label.add("Widowed");
        label.add("Separation");
        label.add("Divorsed");
        return label;
    }

    public static ArrayList<WhoseMobileItem> prepareWhoseMobile() {
        ArrayList<WhoseMobileItem> spinnerList1 = new ArrayList<>();
        spinnerList1.add(new WhoseMobileItem("-1", "Select whose mobile number"));
        spinnerList1.add(new WhoseMobileItem("0", "Self"));
        spinnerList1.add(new WhoseMobileItem("1", "Family"));
        spinnerList1.add(new WhoseMobileItem("2", "Other"));
        return spinnerList1;
    }

    public static ArrayList<GovernmentIdItem> prepareGovernmentIdSpinner() {
        ArrayList<GovernmentIdItem> govtIdStatusList = new ArrayList<>();
        govtIdStatusList.add(new GovernmentIdItem(0, "Choose Your ID Card"));
        govtIdStatusList.add(new GovernmentIdItem(2, "Voter ID Card"));
        govtIdStatusList.add(new GovernmentIdItem(3, "Ration Card"));
        govtIdStatusList.add(new GovernmentIdItem(4, "NREGA job card"));
        govtIdStatusList.add(new GovernmentIdItem(5, "Driving License"));
        govtIdStatusList.add(new GovernmentIdItem(6, "Birth certificate"));
        govtIdStatusList.add(new GovernmentIdItem(7, "Other Photo ID Card"));
        govtIdStatusList.add(new GovernmentIdItem(8, "Member with No ID card"));
        govtIdStatusList.add(new GovernmentIdItem(9, "Government ID with no Photograph"));

        return govtIdStatusList;
    }
    public static ArrayList<GovernmentIdItem> prepareGovernmentIdSpinnerForNoAadhar() {
        ArrayList<GovernmentIdItem> govtIdStatusList = new ArrayList<>();
        govtIdStatusList.add(new GovernmentIdItem(0, "Choose Your ID Card"));
        govtIdStatusList.add(new GovernmentIdItem(2, "Voter ID Card"));
        govtIdStatusList.add(new GovernmentIdItem(3, "Ration Card"));
        govtIdStatusList.add(new GovernmentIdItem(4, "NREGA job card"));
        govtIdStatusList.add(new GovernmentIdItem(5, "Driving License"));
        govtIdStatusList.add(new GovernmentIdItem(6, "Birth certificate"));
        govtIdStatusList.add(new GovernmentIdItem(7, "Other Photo ID Card"));
        govtIdStatusList.add(new GovernmentIdItem(8, "Member with No ID card"));
        govtIdStatusList.add(new GovernmentIdItem(9, "Government ID with no Photograph"));
        govtIdStatusList.add(new GovernmentIdItem(10, "Aadhar ID Card"));

        return govtIdStatusList;
    }
    public static ArrayList<GovernmentIdItem> prepareGovernmentIdSpinnerList() {
        ArrayList<GovernmentIdItem> govtIdStatusList = new ArrayList<>();
        govtIdStatusList.add(new GovernmentIdItem(0, "Choose Family ID Card"));
       // govtIdStatusList.add(new GovernmentIdItem(2, "Voter ID Card"));
        govtIdStatusList.add(new GovernmentIdItem(3, "Ration Card"));
      /*  govtIdStatusList.add(new GovernmentIdItem(4, "NREGA job card"));
        govtIdStatusList.add(new GovernmentIdItem(5, "Driving License"));
        govtIdStatusList.add(new GovernmentIdItem(6, "Birth certificate"));
        govtIdStatusList.add(new GovernmentIdItem(7, "Other Photo ID Card"));
        govtIdStatusList.add(new GovernmentIdItem(8, "Member with No ID card"));
        govtIdStatusList.add(new GovernmentIdItem(9, "Government ID with no Photograph"));*/

        return govtIdStatusList;
    }

    public static ArrayList<ApplicationLanguageItem> prepareLanguageSpinner(Context context) {
        ArrayList<ApplicationLanguageItem> govtIdStatusList = new ArrayList<>();
        // ArrayList<String> spinnerList=new ArrayList<>();
        govtIdStatusList.add(new ApplicationLanguageItem("0", context.getResources().getString(R.string.appLanguage)));
        govtIdStatusList.add(new ApplicationLanguageItem("en", "English"));
        govtIdStatusList.add(new ApplicationLanguageItem("hi", "हिंदी"));
      /*  govtIdStatusList.add(new ApplicationLanguageItem("gu", "ગુજરાતી"));*/


        return govtIdStatusList;
    }

    public static ArrayList<String> getMaritalStatusCode() {
        ArrayList<String> label = new ArrayList<>();
        /*1- Never Married
        2- Married
        3- Widowed
        4- Separation
        5. Divorsed*/
        label.add("0");
        label.add("1");
        label.add("2");
        label.add("3");
        label.add("4");
        label.add("5");
        return label;
    }

    public static ArrayList<String> getGenderLabel() {
        ArrayList<String> label = new ArrayList<>();
        /*1- Male
        2- Female
        3- Transgender
       */
        label.add("Select Gender");
        label.add("Male");
        label.add("Female");
        label.add("Trans Gender");

        return label;
    }

    public static ArrayList<String> getGenderCode() {
        ArrayList<String> label = new ArrayList<>();
        /*1- Male
        2- Female
        3- Transgender
       */
        label.add("0");
        label.add("1");
        label.add("2");
        label.add("3");

        return label;
    }

   /* public static ArrayList<FamilyStatusItem>  familyStatusList(Context context) {

        ArrayList<FamilyStatusItem> familyStatusList = new ArrayList<>();
        familyStatusList.add(new FamilyStatusItem("H", "0", "Select Household Status"));
        familyStatusList.add(new FamilyStatusItem("H", "1", "Household Found"));
        familyStatusList.add(new FamilyStatusItem("H", "2", "Household not found"));
        familyStatusList.add(new FamilyStatusItem("H", "3", "No member living in the h"));
        familyStatusList.add(new FamilyStatusItem("H", "8", "Family migrated"));
        return familyStatusList;
    }

    public static ArrayList<MemberStatusItem> memberStatusList(){

    }*/

    public static void showLog(boolean logFlag, String tag, String message) {
        if (logFlag) {
            Log.d(tag, message);
        }
    }

    public static String fixEncoding(String response) {
        /*try {
            byte[] u = response.toString().getBytes(
                    "ISO-8859-1");
            response = new String(u, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }*/
        return response;
    }

    public static boolean deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null)
                for (File f : files) deleteFile(f);
        }
        return file.delete();
    }

    public static void toggleShowPassword(boolean show, EditText editText) {
        if (show)
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        else
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText.setSelection(editText.length());
    }

    public static void openReset(final SeccMemberItem item1, Button button, final Context context, final Activity activity) {
        PopupMenu popup = new PopupMenu(context, button);
        popup.getMenuInflater()
                .inflate(R.menu.menu_reset, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.reset:
                        alertForValidateLater(context.getResources().getString(R.string.alert_for_reset), item1, context, activity, AppConstant.RESET);
                        break;
                    case R.id.unlockRecord:
                        // askPinToLock(SeccMemberListActivity.EDIT,item1);
                        alertForValidateLater(context.getResources().getString(R.string.alert_for_unlock), item1, context, activity, AppConstant.UNLOCK);
                        break;
                    case R.id.preview:
                        // openPreview(item1);
                        openSyncPreview(item1, context, activity);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    public static void openResetHousehold(final SeccMemberItem item1, Button button, final Context context, final Activity activity) {
        PopupMenu popup = new PopupMenu(context, button);
        popup.getMenuInflater()
                .inflate(R.menu.menu_reset_household, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.reset:
                        alertForValidateLater(context.getResources().getString(R.string.alert_for_reset), item1, context, activity, AppConstant.RESET);
                        break;
                    case R.id.preview:
                        // openPreview(item1);
                        openSyncPreview(item1, context, activity);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    public static void openSyncPreview(SeccMemberItem item, Context context, Activity activity) {
        SelectedMemberItem selectedMemberItem = new SelectedMemberItem();
        // HouseHoldItem houseHoldItem=;//findHousehold(item);
        HouseHoldItem houseHoldItem;
        if (item != null && item.getDataSource() != null && item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            houseHoldItem = SeccDatabase.getRsbyHouseHoldByUrn(item.getRsbyUrnId(), context);
        } else {
            houseHoldItem = SeccDatabase.getHouseHoldDetailsByHhdNo(item.getHhdNo(), context);
        }

        selectedMemberItem.setHouseHoldItem(houseHoldItem);
        selectedMemberItem.setSeccMemberItem(item);
        ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
        Intent theIntent = new Intent(context, MemberPreviewActivity.class);
        activity.startActivity(theIntent);
    }

    public static void askPinToLock(final String status, final SeccMemberItem item, final Context context, final Activity activity) {
        AppUtility.softKeyBoard(activity, 1);
        final AlertDialog askForPinDailog = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.ask_pin_layout, null);
        askForPinDailog.setView(alertView);
        askForPinDailog.show();
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
                errorTV.setVisibility(View.GONE);
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


                    AppUtility.softKeyBoard(activity, 0);
                    String pin = pinET.getText().toString();
                    if (pin.toString().equalsIgnoreCase(verifierDetail.getPin())) {
                        if (status.equalsIgnoreCase(AppConstant.RESET)) {
                            SeccDatabase.resetData(item, context);
                            HouseHoldItem houseHoldItem = null;
                            if (item != null && item.getDataSource() != null && item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                                houseHoldItem = SeccDatabase.getRsbyHouseHoldByUrn(item.getRsbyUrnId(), context);

                            } else {
                                houseHoldItem = SeccDatabase.getHouseHoldList(item.getHhdNo(), context);
                            }
                            SelectedMemberItem selectedMemberItem = SelectedMemberItem.create(ProjectPrefrence.
                                    getSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                            AppConstant.SELECTED_ITEM_FOR_VERIFICATION, context));
                            selectedMemberItem.setHouseHoldItem(houseHoldItem);
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                    AppConstant.SELECTED_ITEM_FOR_VERIFICATION, selectedMemberItem.serialize(), context);
                            askForPinDailog.dismiss();
                            activity.recreate();
                        } else if (status.equalsIgnoreCase(AppConstant.UNLOCK)) {
                            SeccDatabase.editRecord(item, context);
                            askForPinDailog.dismiss();
                            // activity.recreate();
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS, 1 + "", context);
                            ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                                    AppConstant.HOUSEHOLD_TAB_STATUS, 6 + "", context);
                            Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                            context.startActivity(theIntent);

                        }
                    } else if (pin.equalsIgnoreCase("")) {
                        errorTV.setVisibility(View.VISIBLE);
                        errorTV.setText("Enter pin");
                        pinET.setText("");
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
                askForPinDailog.dismiss();
            }
        });
    }

    private static void alertForValidateLater(String msg, final SeccMemberItem item, final Context context, final Activity activity, final String action) {

        final AlertDialog internetDiaolg = new AlertDialog.Builder(context).create();
        LayoutInflater factory = LayoutInflater.from(context);
        View alertView = factory.inflate(R.layout.internet_try_again_popup, null);
        internetDiaolg.setView(alertView);
        internetDiaolg.show();
        TextView tryGainMsgTV = (TextView) alertView.findViewById(R.id.deleteMsg);
        tryGainMsgTV.setText(msg);
        Button tryAgainBT = (Button) alertView.findViewById(R.id.tryAgainBT);
        tryAgainBT.setText("Confirm");
        Button cancelBT = (Button) alertView.findViewById(R.id.cancelBT);
        tryAgainBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (action.equalsIgnoreCase(AppConstant.RESET)) {
                    askPinToLock(AppConstant.RESET, item, context, activity);
                    internetDiaolg.dismiss();
                } else if (action.equalsIgnoreCase(AppConstant.UNLOCK)) {
                    SeccDatabase.editRecord(item, context);
                    //activity.recreate();
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.DASHBOARD_TAB_STATUS, 1 + "", context);
                    ProjectPrefrence.saveSharedPrefrenceData(AppConstant.PROJECT_PREF,
                            AppConstant.HOUSEHOLD_TAB_STATUS, 6 + "", context);
                    Intent theIntent = new Intent(context, SearchActivityWithHouseHold.class);
                    context.startActivity(theIntent);
                }
            }
        });
        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internetDiaolg.dismiss();
            }
        });
    }

    public static boolean isAadhaarDuplicate(SeccMemberItem item1, Context context) {
        int duplicateCount = 0;
        if (item1.getAadhaarNo() != null && !item1.getAadhaarNo().equalsIgnoreCase("")) {
            if (item1 != null && item1.getDataSource() != null && item1.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                ArrayList<SeccMemberItem> seccMemberItems = SeccDatabase.getRsbyMemberListWithUrn(item1.getRsbyUrnId(), context);
                for (SeccMemberItem item : seccMemberItems) {
                    if (item.getRsbyMemId() != null && !item.getRsbyMemId().trim().
                            equalsIgnoreCase(item1.getRsbyMemId().trim())) {
                        if (item.getAadhaarNo() != null && item.getAadhaarNo().trim().equalsIgnoreCase(item1.getAadhaarNo().trim())) {
                            return true;
                        }
                    }
                }
            } else {
                ArrayList<SeccMemberItem> seccMemberItems = SeccDatabase.getSeccMemberList(item1.getHhdNo(), context);
                for (SeccMemberItem item : seccMemberItems) {
                    if (item.getNhpsMemId() != null && !item.getNhpsMemId().trim().equalsIgnoreCase(item1.getNhpsMemId().trim())) {
                        if (item.getAadhaarNo() != null && item.getAadhaarNo().trim().equalsIgnoreCase(item1.getAadhaarNo().trim())) {
                            return true;
                        }
                    }
                }
            }

            ArrayList<SeccMemberItem> list1 = SeccDatabase.seccMemberWithAadhaarLocked(item1.getAadhaarNo(), AppConstant.LOCKED + "", context);
            for (SeccMemberItem item2 : list1) {
                if (item2 != null && item2.getDataSource() != null && item2.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                    // if(item1!=null && item1.getDataSource()!=null && item1.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                    if (item1.getDataSource() != null && item1.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                        if (item2.getRsbyMemId() != null && !item2.getRsbyMemId().trim().equalsIgnoreCase(item1.getRsbyMemId().trim())) {
                            HouseHoldItem houseHoldItem = SeccDatabase.getRsbyHouseHoldList(item2.getRsbyUrnId(), context);
                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sending validated data..8" + item2.getName());
                            if (houseHoldItem.getLockSave() != null && houseHoldItem.getLockSave()
                                    .equalsIgnoreCase(AppConstant.LOCKED + "")) {
                                duplicateCount++;
                            }
                        }
                    } else {
                        HouseHoldItem houseHoldItem = SeccDatabase.getRsbyHouseHoldList(item2.getRsbyUrnId(), context);
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sending validated data..8" + item2.getName());
                        if (houseHoldItem.getLockSave() != null && houseHoldItem.getLockSave()
                                .equalsIgnoreCase(AppConstant.LOCKED + "")) {
                            duplicateCount++;
                        }
                    }
                    // }
                } else {
                    if (item1 != null && item1.getDataSource() != null && !item1.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
                        if (!item2.getNhpsMemId().trim().equalsIgnoreCase(item1.getNhpsMemId().trim())) {
                            HouseHoldItem houseHoldItem = SeccDatabase.getHouseHoldList(item2.getHhdNo(), context);
                            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sending validated data..8" + item2.getName());
                            if (houseHoldItem.getLockSave() != null && houseHoldItem.getLockSave()
                                    .equalsIgnoreCase(AppConstant.LOCKED + "")) {
                                duplicateCount++;
                            }
                        }
                    } else {
                        HouseHoldItem houseHoldItem = SeccDatabase.getHouseHoldList(item2.getHhdNo(), context);
                        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sending validated data..8" + item2.getName());
                        if (houseHoldItem.getLockSave() != null && houseHoldItem.getLockSave()
                                .equalsIgnoreCase(AppConstant.LOCKED + "")) {
                            duplicateCount++;
                        }
                    }
                }
            }
            if (duplicateCount > 0) {
                return true;
            }

              /*if(list1!=null && list1.size()>1)  {
                  item1.setError_code(AppConstant.AADHAAR_ALREADY_ALLOCATED);
                  item1.setError_type(AppConstant.SYNCING_ERROR);
                  item1.setError_msg(AppConstant.AADHAAR_ALREADY_ALLOCATED_MSG);
                  SeccDatabase.updateSeccMember(item1,context);
                  duplicateCount++;
              }*/
        }
        return false;
    }

    public static boolean isScreenOn(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
            boolean screenOn = false;
            for (Display display : dm.getDisplays()) {
                if (display.getState() != Display.STATE_OFF) {
                    screenOn = true;
                }
            }
            return screenOn;
        } else {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            //noinspection deprecation
            return pm.isScreenOn();
        }
    }

    public static boolean isAppIsInBackground(Context context) {
        // If the screen is off then the device has been locked
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return isScreenOn = !powerManager.isInteractive();
        } else {
            return isScreenOn = !powerManager.isScreenOn();
        }


        /*    KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if( keyguardManager.inKeyguardRestrictedInputMode()) {
            return true;
        } else {
            return false;
        }*/
       /* boolean isInBackground = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
                if (runningProcesses != null && runningProcesses.size() > 0) {
                    for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                        if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                            for (String activeProcess : processInfo.pkgList) {
                                if (activeProcess.equals(context.getPackageName())) {
                                    isInBackground = false;
                                }
                            }
                        }
                    }
                }
            } else {
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                ComponentName componentInfo = taskInfo.get(0).topActivity;
                if (componentInfo.getPackageName().equals(context.getPackageName())) {
                    isInBackground = false;
                }
            }
        } catch (Exception ex) {

        }*/

       /* return isInBackground;*/

       /* if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
            boolean screenOn = false;
            for (Display display : dm.getDisplays()) {
                if (display.getState() != Display.STATE_OFF) {
                    screenOn = true;
                }
            }
            return screenOn;
        } else {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            //noinspection deprecation
            return pm.isScreenOn();
        }*/
    }

    public static int findAge(String dob) {
        Date realDdate = DateTimeUtil.convertStringToDate(dob, AppConstant.DATE_FORMAT);
        Date compareDate = DateTimeUtil.convertStringToDate("2017-01-01", AppConstant.DATE_FORMAT);

        String yob = dob.substring(0, 4);

       /* AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Year of birth : " + yob);
        Calendar firstCal = GregorianCalendar.getInstance();
        Calendar secondCal = GregorianCalendar.getInstance();
        secondCal.add(Calendar.DAY_OF_YEAR, -firstCal.get(Calendar.DAY_OF_YEAR))*/
        ;
        int age = getDiffYears(realDdate, compareDate);
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Calculated Age : " + age);

       /* public int getYearsBetweenDates(Date first, Date second) {
            Calendar firstCal = GregorianCalendar.getInstance();
            Calendar secondCal = GregorianCalendar.getInstance();

            firstCal.setTime(first);
            secondCal.setTime(second);

            secondCal.add(Calendar.DAY_OF_YEAR, -firstCal.get(Calendar.DAY_OF_YEAR));

            return secondCal.get(Calendar.YEAR) - firstCal.get(Calendar.YEAR);
        }*/
        return 0;
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.DAY_OF_YEAR) > b.get(Calendar.DAY_OF_YEAR)) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public static SeccMemberItem clearAadhaarOrGovtDetail(SeccMemberItem item) {

        if (item.getAadhaarStatus() != null && item.getAadhaarStatus().trim().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
            item.setIdNo("");
            item.setIdType("");
            item.setGovtIdPhoto(null);
            item.setNameAsId("");
        } else if (item.getAadhaarStatus() != null && item.getAadhaarStatus().trim().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
            item.setAadhaarAuth("");
            item.setAadhaarCapturingMode("");
            item.setAadhaarAuthMode("");
            item.setAadhaarAuthDt("");
            item.setAadhaarCo("");
            item.setAadhaarDist("");
            item.setAadhaarDob("");
            item.setAadhaarGender("");
            item.setAadhaarGname("");
            item.setAadhaarHouse("");
            item.setAadhaarLm("");
            item.setAadhaarLoc("");
            item.setAadhaarNo("");
            item.setNameAadhaar("");
            item.setAadhaarPc("");
            item.setAadhaarPo("");
            item.setAadhaarState("");
            item.setAadhaarStreet("");
            item.setAadhaarSubdist("");
            item.setAadhaarVtc("");
            item.setAadhaarYob("");
        }
        return item;
    }

    public static String convertRsbyDateFormat(String dob) {
        String newDob;
        if (dob != null && !dob.equalsIgnoreCase("")) {
            if (dob.length() == 7) {
                newDob = "0" + dob;
            } else {
                newDob = dob;
            }
            AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "RSBY Date1111111 : " + dob);
            StringBuilder convertedDob = new StringBuilder();
            String day = newDob.substring(0, Math.min(newDob.length(), 2));
            String month = newDob.substring(2, Math.min(newDob.length(), 4));
            String year = newDob.substring(4, Math.min(newDob.length(), newDob.length()));
            convertedDob.append(year + "-").append(month + "-").append(day);
            //  String[] dobArray;


            return convertedDob.toString();
        }
        return null;
    }

    public static int byteSizeOf(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        } else {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }

    public static SeccMemberItem findHof(SeccMemberItem item, Context context) {
        SeccMemberItem hofItem = null;

        if (item.getDataSource() != null && item.getDataSource().trim().equalsIgnoreCase(AppConstant.RSBY_SOURCE)) {
            if (item.getHhStatus() != null && item.getHhStatus().trim().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)) {
                //   AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Relation Code1111111 : "+item.getNhpsRelationCode());
                if (item.getRsbyUrnId() != null) {
                    ArrayList<SeccMemberItem> familyList = SeccDatabase.getRsbyMemberListWithUrn(item.getRsbyUrnId().trim(), context);
                    for (SeccMemberItem item1 : familyList) {
                        if (item1.getNhpsRelationCode() != null && item1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                            //               AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Relation Code1111111 : "+item1.getNhpsRelationCode());
                            hofItem = item1;
                            break;
                        }
                    }
                    // }
                }
            }
        } else {
            // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Relation Code1111111 : "+item.getNhpsRelationCode());
            if (item.getHhStatus() != null && item.getHhStatus().trim().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)) {
                //   AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Relation Code1111111 : "+item.getNhpsRelationCode());
                ArrayList<SeccMemberItem> familyList = SeccDatabase.getSeccMemberList(item.getHhdNo().trim(), context);
                for (SeccMemberItem item1 : familyList) {
                    //     AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Relation Code1111111 : "+item1.getNhpsRelationCode());

                    // if (item1.getHhStatus() != null && item1.getHhStatus().trim().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)) {
                    if (item1.getNhpsRelationCode() != null && item1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                        //               AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Relation Code1111111 : "+item1.getNhpsRelationCode());
                        hofItem = item1;
                        break;
                    }
                    // }
                }
            }
        }
        return hofItem;
    }

    public static String findRelation(SeccMemberItem item, Context context) {
        String relationName = null;
        if (item.getNhpsRelationCode() != null && !item.getNhpsRelationCode().trim().equalsIgnoreCase("")) {
            ArrayList<RelationItem> relationList;
            if (item.getGenderid() != null && item.getGenderid().trim().equalsIgnoreCase(AppConstant.MALE_GENDER)) {
                relationList = SeccDatabase.getRelationListByGender(context, AppConstant.MALE_GENDER);
            } else if (item.getGenderid() != null && item.getGenderid().trim().equalsIgnoreCase(AppConstant.FEMALE_GENDER)) {
                relationList = SeccDatabase.getRelationListByGender(context, AppConstant.MALE_GENDER);
            } else {
                relationList = SeccDatabase.getRelationListByGender(context, null);
            }
            if (relationList != null) {
                for (RelationItem relItem : relationList) {
                    if (item.getNhpsRelationCode() != null && item.getNhpsRelationCode().trim().equalsIgnoreCase(relItem.getRelationCode().trim())) {
                        relationName = relItem.getRelationName();
                        break;
                    }
                }
            }
        }
        return relationName;
    }

    public static HouseHoldItem checkAllSyncStatus(HouseHoldItem item1, Context context) {
        int underSurvey = 0;
        int completeSurvey;
        int yetToSurvey = 0;
        boolean completeSync;
        boolean underSurveyFlag = false;
        ArrayList<SeccMemberItem> list = SeccDatabase.getSeccMemberList(item1.getHhdNo(), context);
        if (list != null) {
            for (SeccMemberItem item : list) {
                if (item.getSyncedStatus() != null && !item.getSyncedStatus().equalsIgnoreCase("")) {
                    underSurveyFlag = true;
                    break;
                }
            }

            if (underSurveyFlag) {
                for (SeccMemberItem item : list) {
                    // if(item.getHhStatus()!=null && item.getHhStatus().trim().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)) {
                    if (item.getHhStatus() != null) {
                        item1.setHhStatus(item.getHhStatus());
                    }
                    if (item.getSyncedStatus() != null && item.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
                        underSurvey++;
                    }
                    /*}else{
                        if (item.getHhStatus() != null && ite) {
                            item1.setHhStatus(item.getHhStatus());
                            item.setHhStatus(item.getHhStatus());
                            item.setLockedSave(AppConstant.LOCKED+"");
                            item=AppUtility.addAdditionalParamInSecc(item,context);
                            SeccDatabase.updateSeccMember(item,context);
                        }
                    }*/
                }
            }

            if (underSurvey == list.size()) {
                item1.setLockSave(AppConstant.LOCKED + "");
                return item1;
            } else if (underSurvey < list.size() && underSurvey > 0) {
                //under survey
                if (item1.getHhStatus() != null && item1.getHhStatus().trim().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)) {
                    item1.setLockSave(AppConstant.SAVE + "");
                } else {
                    updateSeccMember(list, item1, context);
                    item1.setLockSave(AppConstant.LOCKED + "");
                    // item1=addAdditionalParamInHousehold(item1,context);
                }
                return item1;
            }
        }

        return item1;
    }

    private static void updateSeccMember(ArrayList<SeccMemberItem> list,
                                         HouseHoldItem houseHoldItem, Context context) {
        for (SeccMemberItem item : list) {
            if (item.getSyncedStatus() != null && item.getSyncedStatus().trim().equalsIgnoreCase(AppConstant.SYNCED_STATUS)) {
            } else {
                item.setHhStatus(houseHoldItem.getHhStatus());
                item.setLockedSave(AppConstant.LOCKED + "");
                item.setMemStatus(AppConstant.NO_INFO_AVAIL);
                // item=AppUtility.addAdditionalParamInSecc(item,context);
                SeccDatabase.updateSeccMember(item, context);
            }
        }
    }

    public static String getCurrentApplicationVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        String myVersionName = "not available"; // initialize String

        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return myVersionName;
    }

    public static String aadharXmlCorrection(String XML) {
        StringBuilder validatedXml = new StringBuilder();
        String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String spliter = "<PrintLetterBarcodeData";
        String[] xmlArray;
        xmlArray = XML.split(spliter);

        validatedXml.append(xmlHeader).append(spliter).append(xmlArray[1]);
        return validatedXml.toString();
    }

   /* public static HouseHoldItem addAdditionalParamInHousehold(HouseHoldItem houseHoldItem,Context context){
      *//*  houseHoldItem.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
        houseHoldItem.setSyncDt(String.valueOf(DateTimeUtil.currentTimeMillis()));*//*
        return houseHoldItem;
    }
    public static SeccMemberItem addAdditionalParamInSecc(SeccMemberItem item,Context context){
        *//*item.setAppVersion(AppUtility.getCurrentApplicationVersion(context));
        item.setSyncDt(String.valueOf(DateTimeUtil.currentTimeMillis()));*//*
        return item;
    }*/

    public static void showSoftInput(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //if (imm.isActive()) {
            // imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            // show
            // }
        } catch (Exception ex) {

        }

    }

    public static void hideSoftInput(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //    if (imm.isActive()) {
            // hide
            imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
            //   }
        } catch (Exception ex) {

        }
    }

    public static void softKeyBoard(Activity activity, int value) {
        if (value == 0) {
            try {
  /*        *//*   *//**//*   InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                //    if (imm.isActive()) {
                // hide
                imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);*//**//*
                activity.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN*//*
                );
                //   }*/

               /* InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);*/
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            } catch (Exception ex) {

            }
        } else {
            try {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                //if (imm.isActive()) {
                // imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                // show
                // }
            } catch (Exception ex) {

            }
        }
    }

    public static void hideSoftInput(Activity activity, Button button) {
        try {
            InputMethodManager mgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(button.getWindowToken(), 0);
            //   }
        } catch (Exception ex) {

        }
    }

    public static void requestFocus(EditText editText) {
        try {
            editText.requestFocus();
        } catch (Exception ex) {

        }
    }

    public static void clearFocus(EditText editText) {
        try {
            editText.clearFocus();
        } catch (Exception ex) {

        }
    }

    ////////////
    /// Rsby ///
    ////////////
    public static RSBYItem findRsbyHof(RSBYItem item, Context context) {
        RSBYItem hofItem = null;
        // AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Relation Code1111111 : "+item.getNhpsRelationCode());
        if (item.getHhStatus() != null && item.getHhStatus().trim().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)) {
            //   AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Relation Code1111111 : "+item.getNhpsRelationCode());
            ArrayList<RSBYItem> familyList = SeccDatabase.getRsbyMemberList(item.getUrnId().trim(), context);
            for (RSBYItem item1 : familyList) {
                //     AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Relation Code1111111 : "+item1.getNhpsRelationCode());

                // if (item1.getHhStatus() != null && item1.getHhStatus().trim().equalsIgnoreCase(AppConstant.HOUSEHOLD_FOUND)) {
                if (item1.getNhpsRelationCode() != null && item1.getNhpsRelationCode().trim().equalsIgnoreCase(AppConstant.NEW_HEAD_RELATION_CODE)) {
                    //               AppUtility.showLog(AppConstant.LOG_STATUS,TAG,"Relation Code1111111 : "+item1.getNhpsRelationCode());
                    hofItem = item1;
                    break;
                }
                // }
            }
        }
        return hofItem;
    }

    public static RSBYItem clearAadhaarOrGovtDetailRSBY(RSBYItem item) {

        if (item.getAadhaarStatus() != null && item.getAadhaarStatus().trim().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
            item.setIdNo("");
            item.setIdType("");
            item.setGovtIdPhoto(null);
            item.setNameAsId("");
        } else if (item.getAadhaarStatus() != null && item.getAadhaarStatus().trim().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
            item.setAadhaarAuth("");
            item.setAadhaarCapturingMode("");
            item.setAadhaarAuthMode("");
            item.setAadhaarAuthDt("");
            item.setAadhaarCo("");
            item.setAadhaarDist("");
            item.setAadhaarDob("");
            item.setAadhaarGender("");
            item.setAadhaarGname("");
            item.setAadhaarHouse("");
            item.setAadhaarLm("");
            item.setAadhaarLoc("");
            item.setAadhaarNo("");
            item.setNameAadhaar("");
            item.setAadhaarPc("");
            item.setAadhaarPo("");
            item.setAadhaarState("");
            item.setAadhaarStreet("");
            item.setAadhaarSubdist("");
            item.setAadhaarVtc("");
            item.setAadhaarYob("");
        }
        return item;
    }

    public static boolean isAadhaarDuplicateRSBY(RSBYItem item1, Context context) {
        int duplicateCount = 0;
        if (item1.getAadhaarNo() != null && !item1.getAadhaarNo().equalsIgnoreCase("")) {
            ArrayList<RSBYItem> seccMemberItems = SeccDatabase.getRsbyMemberList(item1.getUrnId(), context);
            for (RSBYItem item : seccMemberItems) {
                if (item.getRsbyMemId() != null && !item.getRsbyMemId().trim().equalsIgnoreCase(item1.getRsbyMemId().trim())) {
                    if (item.getAadhaarNo() != null && item.getAadhaarNo().trim().equalsIgnoreCase(item1.getAadhaarNo().trim())) {
                        return true;
                    }
                }
            }

            ArrayList<RSBYItem> list1 = SeccDatabase.rsbyMemberWithAadhaarLocked(item1.getAadhaarNo(), AppConstant.LOCKED + "", context);
            for (RSBYItem item2 : list1) {
                if (!item2.getRsbyMemId().trim().equalsIgnoreCase(item1.getRsbyMemId().trim())) {
                    RsbyHouseholdItem houseHoldItem = SeccDatabase.getRsbyHouseHoldQ(item2.getUrnId(), context);
                    AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "Sending validated data..8" + item2.getName());
                    if (houseHoldItem.getLockedSave() != null && houseHoldItem.getLockedSave()
                            .equalsIgnoreCase(AppConstant.LOCKED + "")) {
                        duplicateCount++;
                    }
                }
            }
            if (duplicateCount > 0) {
                return true;
            }

              /*if(list1!=null && list1.size()>1)  {
                  item1.setError_code(AppConstant.AADHAAR_ALREADY_ALLOCATED);
                  item1.setError_type(AppConstant.SYNCING_ERROR);
                  item1.setError_msg(AppConstant.AADHAAR_ALREADY_ALLOCATED_MSG);
                  SeccDatabase.updateSeccMember(item1,context);
                  duplicateCount++;
              }*/
        }
        return false;
    }

    public static String convertRsbyDate(String dob) {
        String newDob;
        if (dob.length() == 7) {
            newDob = "0" + dob;
        } else {
            newDob = dob;
        }
        AppUtility.showLog(AppConstant.LOG_STATUS, TAG, "RSBY Date1111111 : " + dob);
        StringBuilder convertedDob = new StringBuilder();
        String day = newDob.substring(0, Math.min(newDob.length(), 2));
        String month = newDob.substring(2, Math.min(newDob.length(), 4));
        String year = newDob.substring(4, Math.min(newDob.length(), newDob.length()));
        convertedDob.append(day + "-").append(month + "-").append(year);
        //  String[] dobArray;


        return convertedDob.toString();

    }

    public static boolean isValidCard(Context context, String str_CardCategory) {
        boolean isValidCard = false;
        if (str_CardCategory != null && !str_CardCategory.equalsIgnoreCase("")) {
            ArrayList<RsbyCardCategoryItem> list = SeccDatabase.getAllCategoryMaster(context);
            for (RsbyCardCategoryItem item : list) {
                if (item.getCategoryCode().trim().equalsIgnoreCase(str_CardCategory.trim())) {
                    isValidCard = true;
                    break;
                }
            }
        }
        return isValidCard;
    }

    public static String getCardCategoryName(Context context, String str_CardCategory) {
        String isValidCard = "";
        if (str_CardCategory != null && !str_CardCategory.equalsIgnoreCase("")) {
            ArrayList<RsbyCardCategoryItem> list = SeccDatabase.getAllCategoryMaster(context);
            for (RsbyCardCategoryItem item : list) {
                if (item.getCategoryCode().trim().equalsIgnoreCase(str_CardCategory.trim())) {
                    isValidCard = item.getCatName();
                    break;
                }
            }
        }
        return isValidCard;
    }

    public static String getRsbyMemberRelationName(Context context, int relationCode) {
        String relationName = String.valueOf(relationCode);

        if (relationName != null && !relationName.equalsIgnoreCase("")) {
            ArrayList<RsbyRelationItem> list = SeccDatabase.getRsbyMemberRelationCode(context);
            for (RsbyRelationItem item : list) {
                if (item.getRelCode().trim().equalsIgnoreCase(relationName.trim())) {
                    relationName = item.getRelName();
                    break;
                }
            }
        }
        return relationName;
    }

    public static String formatUrn(String urnNo) {
        String str = "";
        if (urnNo != null && urnNo.length() == 17) {
            str = urnNo.substring(0, 4) + " ";
            str = str + urnNo.substring(4, 8) + " ";
            str = str + urnNo.substring(8, 12) + " ";
            str = str + urnNo.substring(12, 16) + " ";
            str = str + urnNo.substring(16);
        }
        return str;
    }

    public static boolean validateEidTimeStamp(String timeStamp) {
        String format = "1234/12345/12345 dd/mm/yyyy hh:mm:ss";
        /*timeStamp = format;*/
        int month = 0;
        int day = 0;
        int sec = 0;
        int min = 0;
        int hour = 0;

        if (timeStamp != null && timeStamp.length() == 36) {
            month = Integer.parseInt(timeStamp.substring(20, 22));

            day = Integer.parseInt(timeStamp.substring(17, 19));

            sec = Integer.parseInt(timeStamp.substring(34, 36));

            min = Integer.parseInt(timeStamp.substring(31, 33));

            hour = Integer.parseInt(timeStamp.substring(28, 30));


            System.out.print(month + "/" + day + "/" + sec + "/" + min + "/" + hour);
        }

        if (month > 12 || day > 31 || sec > 59 || min > 59 || hour > 23) {
            return false;
        }

        return true;
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());

        return strDate;
    }

    public static String getNotificationData(Context context) {
        String result = null;
        StringBuilder messages = new StringBuilder();
        ArrayList<NotificationModel> notificationList = new ArrayList<>();
        notificationList = SeccDatabase.getAllAppNotification(context);
        StateItem selectedStateItem = StateItem.create(ProjectPrefrence.getSharedPrefrenceData(AppConstant.PROJECT_PREF, AppConstant.SELECTED_STATE, context));
        long time = System.currentTimeMillis();
        if (selectedStateItem != null) {
            if (notificationList != null && notificationList.size() > 0) {
                for (NotificationModel item : notificationList) {
                    if (compareCardExpDate(time, Long.parseLong(item.getDateExpire())) && compareCardExpDate(Long.parseLong(item.getStartDate()), time)) {
                        if (item.getTargetState() != null && item.getTargetState().equalsIgnoreCase("00")) {
                            messages.append("<font color=\"#ff0000\"><b>" + "* " + item.getDescription() + "</b></font>").append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                        }
                        if (item.getTargetState() != null) {
                            if (selectedStateItem != null && selectedStateItem.getStateCode() != null) {
                                if (selectedStateItem.getStateCode().equalsIgnoreCase(item.getTargetState())) {
                                    messages.append("<font color=\"#006024\"><b>" + "* " + item.getDescription() + "</b></font>").append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                                }
                            }
                        }

                    }
                }
            /*result = "<html><body style=\"background-color:#E6E6FA\"><FONT color='#5c0913' FACE='courier'><marquee behavior='scroll' direction='left' scrollamount=5>"
                    + messages.toString() + "</marquee></FONT></body></html>";*/
                result = "<html><body style=\"background-color:#eaeaea\"><FONT color='#FF0000' FACE='courier' SIZE='5sp'><marquee behavior='scroll' direction='left' scrollamount=5>"
                        + messages.toString() + "</marquee></FONT></body></html>";

            } else {
                result = null;
            }
        }


        return result;
    }

    public static String generatePIDblockXml(Context context, String productionKey, String name, String dob, String gender, String aadhar ) {
        String imeiNo = CommonMethods.GetIMEI(context);
        Global.DEVICE_IMEI_NO = imeiNo;
        Global.productionPublicKey = productionKey;
        String xml = getDemoAuthXmlNew(aadhar, true, false, false, name, dob, gender, Global.CONSENT);
        return xml;
    }

    public static String getDemoAuthXmlNew(String aadhaarNo, boolean pi, boolean pa, boolean pfa, String name, String dob, String gender,String consent) {
        String kycXml = "";
        byte[] publicKey = null;
        //  GetPubKeycertificateData();
        publicKey = Base64.decode(Global.productionPublicKey, Base64.DEFAULT);


        UidaiAuthHelper helper = new UidaiAuthHelper(publicKey);
        kycXml = helper.createXmlForDemoAuthNew(aadhaarNo, pi, pa, pfa, name, dob, gender,consent);
        Log.e("demo auth xml", "==" + kycXml);
        // kycXml = helper.createCustomXmlForAuth(kycXml, "demo_auth");

        return kycXml;
    }

    public static String getAadhaarOtpRequestXml(String aadhaarNo) {
        String kycXml = "";
        byte[] publicKey = null;
        //  GetPubKeycertificateData();
        publicKey = Base64.decode(Global.productionPublicKey, Base64.DEFAULT);


        UidaiAuthHelper helper = new UidaiAuthHelper(publicKey);
        kycXml = helper.createXmlForRequestOtpAuth(aadhaarNo);
        // kycXml = helper.createCustomXmlForAuth(kycXml, "demo_auth");
        kycXml = kycXml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        Log.e("demo auth xml", "==" + kycXml);

        return kycXml;
    }

    public static String formatAadharAuth(String timeStamp) {
        String format = timeStamp;
        /*timeStamp = format;*/

        if (timeStamp.length() > 4) {
            String year = timeStamp.substring(0, 4);
            String month = timeStamp.substring(4, 6);
            String day = timeStamp.substring(6, 8);
            format = year + "-" + month + "-" + day;
            return format;
        } else {
            return format;
        }

    }


    public static String getDemoAuthOTPXmlNew(String aadhaarNo, String otp) {
        String kycXml = "";
        byte[] publicKey = null;
        //  GetPubKeycertificateData();
        publicKey = Base64.decode(Global.productionPublicKey, Base64.DEFAULT);


        UidaiAuthHelper helper = new UidaiAuthHelper(publicKey);
        kycXml = helper.createXmlForOtpAuth(aadhaarNo, true, otp);
        Log.e("demo auth xml", "==" + kycXml);
        // kycXml = helper.createCustomXmlForAuth(kycXml, "demo_auth");

        return kycXml;
    }

    public static String formatQrCodeAadharDob(String timeStamp) {
        String format = timeStamp;
        /*timeStamp = format;*/
        timeStamp = timeStamp.replace("-","");
        if (timeStamp.length() > 4) {
            String year = timeStamp.substring(4, 8);
            String month = timeStamp.substring(2, 4);
            String day = timeStamp.substring(0, 2);
            format = year + "-" + month + "-" + day;
            return format;
        } else {
            return format;
        }

    }

    public static String DuplicateCharRemover(String input) {


        Pattern pattern = Pattern.compile("(\\W)\\1{1,}");  // \W FOR NON DIGIT / WORDS, \w FOR DIGIT AND WORDS.
        return pattern.matcher(input).replaceAll("$1"); // REPLACE WITH MULTIPLE OCCURANCE WITH THE SINGLE OCCURANCE
    }

    //check first two are char not specail charcter.
    public static boolean isCheckFirstTwoChar(String name) {

        if (name.length() <= 2) {
            return !name.matches("[a-zA-Z0-9.? ]*");
        } else {
            return true;
        }


    }

    public static boolean DuplicateCharRemoverbool(String input) {
        Pattern pattern = Pattern.compile("(\\W)\\1{1,}");
        Matcher match = pattern.matcher(input);

        return match.find();
        //        return pattern.matcher(input).replaceAll("$1");

    }

    public String colorString(String text) {
        final SpannableStringBuilder sb = new SpannableStringBuilder(text);

// Span to set text color to some RGB value
        //      final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(158, 158, 158));

// Span to make text bold
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

// Set the text color for first 4 characters
        //   sb.setSpan(fcs, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

// make them also bold
        sb.setSpan(bss, 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb.toString();
    }

    private void closeKeyPad(Button button) {
    }


    public static void writeFileToStorage(String text, String loc) {
        //File logFile = new File("sdcard/BIO_KYC"+ctime+".txt");
        File logFile = new File("sdcard/" + loc + ".txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, false));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("IOException", "appendlog***** " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static long convertDateIntoTimeMillis(String stringDate) {
        long timeMillis = -1;
        try {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = parser.parse(stringDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            //  long time = c.getTimeInMillis();
            timeMillis = c.getTimeInMillis();
        } catch (Exception e) {
            try {
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                Date date = parser.parse(stringDate);
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                //  long time = c.getTimeInMillis();
                timeMillis = c.getTimeInMillis();
            } catch (Exception ec) {

            }
        }
        return timeMillis;
    }

    public static String generateOtpRequestXml(Context context, String Json) {
        String request = "";
        try {
            JSONObject json = new JSONObject(Json);
            request = XML.toString(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return request;
    }


    public static SeccMemberItem clearMemberBasicDetail(SeccMemberItem item) {

        if (item.getAadhaarStatus() != null && item.getAadhaarStatus().trim().equalsIgnoreCase(AppConstant.GOVT_ID_STAT)) {
            item.setIdNo("");
            item.setIdType("");
            item.setGovtIdPhoto(null);
            item.setNameAsId("");
        } else if (item.getAadhaarStatus() != null && item.getAadhaarStatus().trim().equalsIgnoreCase(AppConstant.AADHAAR_STAT)) {
            item.setAadhaarAuth("");
            item.setAadhaarCapturingMode("");
            item.setAadhaarAuthMode("");
            item.setAadhaarAuthDt("");
            item.setAadhaarCo("");
            item.setAadhaarDist("");
            item.setAadhaarDob("");
            item.setAadhaarGender("");
            item.setAadhaarGname("");
            item.setAadhaarHouse("");
            item.setAadhaarLm("");
            item.setAadhaarLoc("");
            item.setAadhaarNo("");
            item.setNameAadhaar("");
            item.setAadhaarPc("");
            item.setAadhaarPo("");
            item.setAadhaarState("");
            item.setAadhaarStreet("");
            item.setAadhaarSubdist("");
            item.setAadhaarVtc("");
            item.setAadhaarYob("");
        }
        item.setMemberPhoto("");
        item.setMobileAuth("");
        item.setMobileAuthDt("");
        item.setMobileNo("");
        item.setNomineeDetailSurveyedStat("");
        item.setNomineeGaurdianName("");
        item.setNomineeRelationName("");
        item.setNameNominee("");
        item.setRelationNomineeCode("");


        return item;
    }


    public static void navigateToHome(final Context context, Activity activity){
       final ImageView settings = (ImageView) activity.findViewById(R.id.settings);
       settings.setVisibility(View.VISIBLE);
       RelativeLayout menuLayout = (RelativeLayout) activity.findViewById(R.id.menuLayout);
        menuLayout.setVisibility(View.VISIBLE);
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_nav_dashboard, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.dashboard:
                                Intent intent = new Intent(context, BlockDetailActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                break;


                        }
                        return true;
                    }
                });
                popup.show();

            }
        });
    }

    public static void navigateToHomeWithZoom(final Context context, Activity activity,View view){
        final ImageView settings = (ImageView) view.findViewById(R.id.settings);
        RelativeLayout menuLayout = (RelativeLayout) view.findViewById(R.id.menuLayout);
        menuLayout.setVisibility(View.VISIBLE);
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_nav_dashboard, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.dashboard:
                                Intent intent = new Intent(context, BlockDetailActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                break;


                        }
                        return true;
                    }
                });
                popup.show();

            }
        });
    }
}
