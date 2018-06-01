package com.customComponent.utility;

/**
 * Created by Anand on 14-09-2016.
 */
public class NumberUtil {

    public static double roundOf(double value){
        double finalValue = Math.round( value * 100.0 ) / 100.0;
        return  finalValue;
    }
}
