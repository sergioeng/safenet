package com.dodsoneng.falldetector.models;

/**
 * Created by sergio.eng on 10/19/17.
 */

public class EventInfo {

    static final String [] NAME = { "CHECKIN", "EMERGENCY", "FALL", "SOS", "BATTERY", "NOMOTION", "POSITION" };
    static final String [] TYPE = { "1"      , "2"        ,  "3"  , "4"  , "5"      , "6"       , "7"        };

    public static int CHECKIN   = 0;
    public static int EMERGENCY = 1;
    public static int FALL      = 2;
    public static int SOS       = 3;
    public static int BATTERY   = 4;
    public static int NOMOTION  = 5;
    public static int POSITION  = 6;
    public static int MAX_EVENTS = 7;

    public static String getName (int i) { return NAME [i];}
    public static String getType (int i) { return TYPE [i];}

    public static int CHECKIN_BUTTON = 0;
    public static int CHECKIN_BUTTON_TEXT = 1;
    public static int CHECKIN_BUTTON_TEXT_SYMPTOM = 2;


}
