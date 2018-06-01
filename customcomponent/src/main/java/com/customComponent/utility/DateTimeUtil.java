package com.customComponent.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Anand on 10-03-2016.
 */
public class DateTimeUtil  {

    public static long currentTimeMillis(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        long mSec = System.currentTimeMillis();//calendar.get(Calendar.MILLISECOND);
        return  mSec;

    }
    public static String currentDate(String dateFormat) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }

    public static boolean isTimeBefore(long firstTime,long secondTime){
        Date date=new Date(firstTime);
     return  date.before(new Date(secondTime));
    }
    public static String currentDateTime(String dateTimeFormat) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
        return sdf.format(cal.getTime());
    }

    public static Date currentDate() {
        Calendar cal = Calendar.getInstance(Locale.getDefault());

        return cal.getTime();
    }

    public static String dateStringFromMillis(long timeMillis,String dateFormat){
        SimpleDateFormat sdf=new SimpleDateFormat(dateFormat);
        return sdf.format(timeMillis);
    }


    public static Date convertStringToDate(String date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*public static String addTime(String time,int hour){
        Log.d("DateTime Util","Selected time : "+time);
        Calendar cal = Calendar.getInstance(); // creates calendar
        Date newDate=new Date(time);
        cal.setTime(newDate); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
        cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm a");
      return dateFormat.format(cal.getTime());
    }*/

    public static long addTime(String time,int hour) {
        long timeMillisec=-1;
        try{
            SimpleDateFormat parser = new SimpleDateFormat("hh:mm a");
            Date myDate = parser.parse(time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(myDate);
            cal.add(Calendar.HOUR_OF_DAY, hour); // this will add two hours
            myDate = cal.getTime();
            timeMillisec=myDate.getTime();

            //  finalTime = parser.format(myDate);
        }catch(Exception e){
        }
        return timeMillisec;
    }

    public static String convertTimeIntoString(long timeMilliSec){
        String stringTime=null;
        try{
            SimpleDateFormat parser = new SimpleDateFormat("hh:mm a");
            stringTime=parser.format(new Date(timeMilliSec));
        }catch(Exception e){

        }
        return stringTime;
    }

    public static String convertTimeMillisIntoStringDate(long timeMilliSec){
        String stringTime=null;
        try{
            SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
            stringTime=parser.format(new Date(timeMilliSec));
        }catch(Exception e){

        }
        return stringTime;
    }

    public static String convertTimeMillisIntoStringDateNew(long timeMilliSec) {
        String stringTime = null;
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeMilliSec);
            stringTime = formatter.format(calendar.getTime());
        } catch (Exception e) {

        }
        return stringTime;
    }





    public static Date convertTimeMillisIntoDate(long timeMilliSec){
        //Date date;
        try{
           // SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
            return new Date(timeMilliSec);
        }catch(Exception e){

        }
        return null;
    }

    public static String convertTimeMillisIntoStringDate(long timeMilliSec,String dateFormat){
        String stringTime=null;
        try{
            SimpleDateFormat parser = new SimpleDateFormat(dateFormat);
            stringTime=parser.format(new Date(timeMilliSec));
        }catch(Exception e){

        }
        return stringTime;
    }
    public static String convertTimeMillisIntoStringDateNew(String timeMilliSec, String dateFormat) {
        String stringTime = null;
        try {
            long date = Long.parseLong(timeMilliSec);
            SimpleDateFormat parser = new SimpleDateFormat(dateFormat);
            stringTime = parser.format(new Date(date));
        } catch (Exception e) {
            stringTime = timeMilliSec.replace(".0", "");
        }
        return stringTime;
    }
    public static long convertDateIntoTimeMillis(String stringDate){
        long timeMillis=-1;
        try{
            SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
            Date date=parser.parse(stringDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
          //  long time = c.getTimeInMillis();
            timeMillis=c.getTimeInMillis();
        }catch(Exception e){

        }
        return timeMillis;
    }
    public static String convertDateIntoTimeMillis(String stringDate, String format) {
        String formatedString = "";
        long  timeMillisn = -1;
        try {
            SimpleDateFormat parser = new SimpleDateFormat(format);
            Date date = parser.parse(stringDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            //  long time = c.getTimeInMillis();
            timeMillisn = c.getTimeInMillis();
            try{
                SimpleDateFormat parserN = new SimpleDateFormat("MMM dd, yyyy");
                formatedString=parserN.format(new Date(timeMillisn));
            }catch(Exception e){

            }
        } catch (Exception e) {

        }
        return formatedString;
    }

    public static long convertTimeIntoMillisec(String stringTime){
        long timeMillis=-1;
        try{
            SimpleDateFormat parser = new SimpleDateFormat("hh:mm aa");
            Date date=parser.parse(stringTime);
            timeMillis=date.getTime();
        }catch(Exception e){

        }
        return timeMillis;
    }
    public static long convertDobIntoMillisec(String stringTime){
        long timeMillis=-1;
        try{
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
            Date date=parser.parse(stringTime);
            timeMillis=date.getTime();
        }catch(Exception e){

        }
        return timeMillis;
    }


}
