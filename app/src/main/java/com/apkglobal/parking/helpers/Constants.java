package com.apkglobal.parking.helpers;

import java.text.SimpleDateFormat;

public class Constants {
	

	public static String IPAddress = "http://searchkero.com/parking";
	public static String IPAddressForFreePArkingLots = "http://searchkero.com";
	
	
	public static double millisecondsIntoHours = 3600000.0;
	public static double doubleDefaultValue = 0.0;
	public static long thrityMinInMilliSeconds = 1800000;// milliseconds to 30 min
	public static long tenMinutesInMilliseconds = 600000;
	
	public static String TAG_SUCCESS = "success";
	public static String hmFreePLlatitude = "latitude";
	public static String hmFreePLlongitude = "longitude";
	public static String hmFreePLAddress = "address";
	public static String hmFreePLNoOfSpots = "nooflots";
	public static String hmFreePLMiles = "miles";
	public static String hmFreePLTime = "time";
	public static String hmFreePLUser = "user";
	
	public static String GOOGLEDIRECTIONSURL = "http://maps.googleapis.com/maps/api/directions/xml?";
	public final static String MODE_DRIVING = "driving";
	public final static String MYPREFERENCES = "MYPREFERENCES";
	public final static String UNAME = "UNAME";
	public final static String UPDATEDBY = "Updated by";
	public final static String AT= "at";
	public final static String VACANTSPOTS = "Vacant spot(s) =";
		
	
	
	 public final static  SimpleDateFormat dtf = new SimpleDateFormat("MM-dd-yyyy HH:mm");

}
