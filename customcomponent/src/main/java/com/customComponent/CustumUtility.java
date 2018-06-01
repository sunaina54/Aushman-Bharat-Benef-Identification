package com.customComponent;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.provider.Telephony.Mms.Part.FILENAME;

public class CustumUtility {


    public static String dateFormatter(Calendar cal, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                format);
        return dateFormat.format(cal.getTime());
    }

    public static boolean isConnectingToInternet(Context mContext) {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public static long getCurrentTimeInMillisecond() {
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }

    public static DatePickerDialog setDOB1(Context mContext, final TextView textView, String dateFormat) {
        Calendar newCalendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                textView.setText(dateFormatter.format(newDate.getTime()));

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        return datePickerDialog;
    }


    public static TimePickerDialog setTime(Context mContext, final TextView textView) {

        Calendar newCalendar = Calendar.getInstance();

        Log.i("", "Calander instance : " + newCalendar);
        //final SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.US);
        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //textView.setText(hourOfDay+":"+minute);
                updateTime(textView, hourOfDay, minute);
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), false);

        return timePickerDialog;
    }

    public static void updateTime(TextView output, int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        output.setText(aTime);
       /* if (textView1 != null) {
            output.setText(aTime);
            textView1.setText(timeSet);
        } else {
            output.setText(aTime + " " + timeSet);
        }
*/
    }
    public static TimePickerDialog setTime(Context mContext, final TextView textView, final TextView textView1) {

        Calendar newCalendar = Calendar.getInstance();

        Log.i("", "Calander instance : " + newCalendar);
        //final SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.US);
        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //textView.setText(hourOfDay+":"+minute);
                updateTime(textView, hourOfDay, minute, textView1);
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), false);

        return timePickerDialog;
    }

    public static void updateTime(TextView output, int hours, int mins, TextView textView1) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").toString();

        if (textView1 != null) {
            output.setText(aTime);
            textView1.setText(timeSet);
        } else {
            output.setText(aTime + " " + timeSet);
        }

    }


    public static void setTimeInterval(Context mContext, final TextView textView) {

        NumberPicker myNumberPicker = new NumberPicker(mContext);
        myNumberPicker.setMaxValue(1000);
        myNumberPicker.setMinValue(0);

        NumberPicker.OnValueChangeListener myValChangedListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                textView.setText(newVal + "");
            }
        };

        myNumberPicker.setOnValueChangedListener(myValChangedListener);

        new AlertDialog.Builder(mContext).setView(myNumberPicker).show();

    }
    public static String readFromfile(String fileName, Context context) {

        StringBuffer returnString = new StringBuffer();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets().open(fileName, Context.MODE_WORLD_READABLE);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                returnString.append(line);
                returnString.append("\n");

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

    public static void writeLogFile(String fileName,String content) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

           // String content = "This is the content to write into file\n";

            fw = new FileWriter(FILENAME);
            bw = new BufferedWriter(fw);
            bw.write(content);

            System.out.println("Done");

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
    }

}
