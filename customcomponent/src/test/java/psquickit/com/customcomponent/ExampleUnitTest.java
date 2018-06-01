package psquickit.com.customcomponent;

import com.customComponent.utility.DateTimeUtil;
import com.customComponent.utility.EmailValidateUtil;

import org.junit.Test;

import java.lang.System;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
       // assertEquals(4, 2 + 2);
     //   isNextDate("12-03-2016");
        //System.out.print("Email is "+EmailValidateUtil.isGmail("wahidali@gmail.com"));
        //DateTimeUtil.convertTimeIntoMillisec("05:29 AM");
        long timeMillis= DateTimeUtil.convertTimeIntoMillisec("5:14 AM");
        System.out.print("Time millis " +timeMillis);
        System.out.print("Time String is " + DateTimeUtil.convertTimeIntoString(timeMillis));

        //  System.out.print("Email valid is "+EmailValidateUtil.isValidEmail("wahidali@gmail.com"));

       /* System.out.println("Added Date : "+addTime("05:15 PM"));
        System.out.println("Converted Time : " + convertTimeIntoString(addTime("05:15 PM")));*/
       /* if(DateTimeUtil.convertTimeIntoMillisec("06:15 PM")>DateTimeUtil.convertTimeIntoMillisec("05:15 PM") ) {
            System.out.println("Time is less than ");
        }else{
            System.out.println("Time is greater than ");
        }*/
        //System.out.println("Converted Time 11111: " + isTimeBefore("11:15 AM","05:15 PM"));
    }




   /* public static String addTime(String time) {
        String finalTime=null;
        try{
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm a");
        Date myDate = parser.parse(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.add(Calendar.HOUR_OF_DAY, 10); // this will add two hours
        myDate = cal.getTime();
        finalTime = parser.format(myDate);
    }catch(Exception e){
    }

        return finalTime;
    }*/

    // 0-lesser with given time
    // 1-greater with given time

    public static boolean isTimeBefore(String firstTime,String secondTime){

        Date date=new Date( convertTimeIntoMillisec(firstTime));
        return  date.before(new Date( convertTimeIntoMillisec(secondTime)));
    }

    public static int timeDifference(long firstTime,long secondTime){
        if(firstTime>secondTime){
            return 1;
        }

        return 0;
    }
    public static long addTime(String time) {
        long timeMillisec=-1;
        try{
            SimpleDateFormat parser = new SimpleDateFormat("hh:mm a");
            Date myDate = parser.parse(time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(myDate);
            cal.add(Calendar.HOUR_OF_DAY, -1); // this will add two hours
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

    public static long convertTimeIntoMillisec(String stringTime){
        long timeMillis=-1;
        try{
            SimpleDateFormat parser = new SimpleDateFormat("HH:mm a");
            Date date=parser.parse(stringTime);
            timeMillis=date.getTime();
        }catch(Exception e){

        }
        return timeMillis;
    }

    private boolean isNextDate(String date){
     //Date currentDate=   DateTimeUtil.currentDate();

        Calendar selectedDate=Calendar.getInstance(Locale.getDefault());
      //  selectedDate.setTime(DateTimeUtil.convertStringToDate(date));
        Calendar currentDate=Calendar.getInstance(Locale.getDefault());
        currentDate.setTime(DateTimeUtil.currentDate());
        if(selectedDate.after(currentDate)){
            System.out.print("selected date is after current date");
            return true;
        }
        return  false;

    }
}